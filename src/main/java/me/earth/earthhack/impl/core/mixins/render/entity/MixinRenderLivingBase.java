package me.earth.earthhack.impl.core.mixins.render.entity;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.impl.event.events.render.ModelRenderEvent;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.player.spectate.Spectate;
import me.earth.earthhack.impl.modules.render.chams.Chams;
import me.earth.earthhack.impl.modules.render.chams.mode.ChamsMode;
import me.earth.earthhack.impl.modules.render.esp.ESP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderLivingBase.class})
public abstract class MixinRenderLivingBase {
    private static final ModuleCache<Spectate> SPECTATE = Caches.getModule(Spectate.class);
    private static final ModuleCache<Chams> CHAMS = Caches.getModule(Chams.class);
    private static final ModuleCache<ESP> ESP_MODULE = Caches.getModule(ESP.class);
    private float prevRenderYawOffset;
    private float renderYawOffset;
    private float prevRotationYawHead;
    private float rotationYawHead;
    private float prevRotationPitch;
    private float rotationPitch;

    @Inject(method={"doRender"}, at={@At(value="HEAD")})
    private void doRenderHookHead(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (entity instanceof EntityPlayerSP || SPECTATE.isEnabled() && entity.equals(((Spectate)SPECTATE.get()).getFake())) {
            this.prevRenderYawOffset = entity.prevRenderYawOffset;
            this.renderYawOffset = entity.renderYawOffset;
            this.prevRotationYawHead = entity.prevRotationYawHead;
            this.rotationYawHead = entity.rotationYawHead;
            this.prevRotationPitch = entity.prevRotationPitch;
            this.rotationPitch = entity.rotationPitch;
            entity.prevRenderYawOffset = Managers.ROTATION.getPrevRenderYawOffset();
            entity.renderYawOffset = Managers.ROTATION.getRenderYawOffset();
            entity.prevRotationYawHead = Managers.ROTATION.getPrevRotationYawHead();
            entity.rotationYawHead = Managers.ROTATION.getRotationYawHead();
            entity.prevRotationPitch = Managers.ROTATION.getPrevPitch();
            entity.rotationPitch = Managers.ROTATION.getRenderPitch();
        }
    }

    @Inject(method={"doRender"}, at={@At(value="RETURN")})
    private void doRenderHookReturn(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (entity instanceof EntityPlayerSP || SPECTATE.isEnabled() && entity.equals(((Spectate)SPECTATE.get()).getFake())) {
            entity.prevRenderYawOffset = this.prevRenderYawOffset;
            entity.renderYawOffset = this.renderYawOffset;
            entity.prevRotationYawHead = this.prevRotationYawHead;
            entity.rotationYawHead = this.rotationYawHead;
            entity.prevRotationPitch = this.prevRotationPitch;
            entity.rotationPitch = this.rotationPitch;
        }
    }

    @Inject(method={"renderLayers"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderLayersHook(CallbackInfo info) {
        if (ESP.isRendering) {
            info.cancel();
        }
    }

    @Inject(method={"renderName"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderNameHook(EntityLivingBase entity, double x, double y, double z, CallbackInfo info) {
        if (ESP.isRendering) {
            info.cancel();
        }
    }

    @Redirect(method={"setBrightness"}, at=@At(value="FIELD", target="Lnet/minecraft/entity/EntityLivingBase;hurtTime:I"))
    public int hurtTimeHook(EntityLivingBase base) {
        if (!ESP_MODULE.returnIfPresent(ESP::shouldHurt, false).booleanValue()) {
            return 0;
        }
        return base.hurtTime;
    }

    @Inject(method={"doRender"}, at={@At(value="HEAD")})
    public void doRender_Pre(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (CHAMS.returnIfPresent(c -> c.isValid(entity, ChamsMode.Normal), false).booleanValue()) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }

    @Inject(method={"doRender"}, at={@At(value="RETURN")})
    public void doRender_Post(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (CHAMS.returnIfPresent(c -> c.isValid(entity, ChamsMode.Normal), false).booleanValue()) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0f, 1100000.0f);
        }
    }

    @Redirect(method={"renderModel"}, at=@At(value="INVOKE", target="net/minecraft/client/model/ModelBase.render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderHook(ModelBase model, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        RenderLivingBase renderLiving = (RenderLivingBase)RenderLivingBase.class.cast(this);
        EntityLivingBase entity = (EntityLivingBase)entityIn;
        ModelRenderEvent.Pre event = new ModelRenderEvent.Pre(renderLiving, entity, model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Bus.EVENT_BUS.post(event);
        if (!event.isCancelled()) {
            model.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        Bus.EVENT_BUS.post(new ModelRenderEvent.Post(renderLiving, entity, model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale));
    }
}
