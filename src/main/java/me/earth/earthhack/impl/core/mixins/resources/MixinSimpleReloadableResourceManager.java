package me.earth.earthhack.impl.core.mixins.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.managers.client.PluginManager;
import me.earth.earthhack.impl.managers.client.PluginResourceManager;
import me.earth.earthhack.impl.managers.client.resource.PluginResourceLocation;
import me.earth.earthhack.impl.managers.client.resource.PluginResourceSupplier;
import me.earth.earthhack.impl.managers.client.resource.ResourceException;
import me.earth.earthhack.impl.managers.client.resource.ResourceSupplier;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SimpleReloadableResourceManager.class})
public abstract class MixinSimpleReloadableResourceManager {
    @Shadow
    @Final
    private MetadataSerializer field_110547_c;
    @Shadow
    @Final
    private Set<String> field_135057_d;

    @Redirect(method={"getAllResources"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/resources/IResourceManager;getAllResources(Lnet/minecraft/util/ResourceLocation;)Ljava/util/List;"))
    private List<IResource> getAllResourcesHook(IResourceManager iResourceManager, ResourceLocation location) throws IOException {
        List list = iResourceManager.getAllResources(location);
        list.addAll(PluginResourceManager.getInstance().getPluginResources(location));
        return list;
    }

    @Inject(method={"getResource"}, at={@At(value="HEAD")}, cancellable=true)
    private void getResourceHook(ResourceLocation location, CallbackInfoReturnable<IResource> cir) {
        ResourceSupplier supplier;
        if (!(location instanceof PluginResourceLocation) && location.getResourceDomain().equals("earthhack")) {
            location = new PluginResourceLocation(location.getResourceDomain() + ":" + location.getResourcePath(), "earthhack");
        }
        if (location instanceof PluginResourceLocation) {
            PluginResourceLocation loc = (PluginResourceLocation)location;
            ClassLoader classLoader = PluginManager.getInstance().getPluginClassLoader();
            if (classLoader == null) {
                throw new IllegalStateException("PluginClassLoader was null!");
            }
            supplier = new PluginResourceSupplier(loc, this.field_110547_c, classLoader);
        } else {
            supplier = PluginResourceManager.getInstance().getSingleResource(location);
        }
        if (supplier != null) {
            Earthhack.getLogger().info("Custom Resource detected: " + location);
            try {
                IResource resource = supplier.get();
                cir.setReturnValue(resource);
            }
            catch (ResourceException e) {
                e.printStackTrace();
            }
        }
    }

    @Inject(method={"getResourceDomains"}, at={@At(value="HEAD")}, cancellable=true)
    private void getResourceDomainsHook(CallbackInfoReturnable<Set<String>> cir) {
        Set<String> domains = this.field_135057_d;
        domains.add("earthhack");
    }
}
