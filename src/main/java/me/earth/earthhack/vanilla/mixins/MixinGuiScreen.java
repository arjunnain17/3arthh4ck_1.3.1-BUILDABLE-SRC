package me.earth.earthhack.vanilla.mixins;

import java.util.List;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.impl.core.mixins.gui.IGuiContainer;
import me.earth.earthhack.impl.event.events.render.ToolTipEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiScreen.class})
public abstract class MixinGuiScreen {
    @Shadow
    public Minecraft field_146297_k;
    private int toolTipX;
    private int toolTipY;

    @Inject(method={"drawHoveringText(Ljava/util/List;II)V"}, at={@At(value="HEAD")})
    private void drawHoveringTextHookHead(List<String> textLines, int x, int y, CallbackInfo ci) {
        this.toolTipX = -1;
        this.toolTipY = -1;
    }

    @Redirect(method={"drawHoveringText(Ljava/util/List;II)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawHoveringTextHook(FontRenderer fontRenderer, String text, float x, float y, int color) {
        if (this.toolTipY == -1) {
            this.toolTipY = (int)(y + 1.0f);
            this.toolTipX = (int)x;
        }
        return fontRenderer.drawStringWithShadow(text, x, y, color);
    }

    @Inject(method={"drawHoveringText(Ljava/util/List;II)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;enableLighting()V", shift=At.Shift.BEFORE)})
    private void drawHoveringTextHook(List<String> textLines, int x, int y, CallbackInfo ci) {
        if (this.toolTipY == -1 || !(this instanceof IGuiContainer)) {
            return;
        }
        Slot slot = ((IGuiContainer)((Object)this)).getHoveredSlot();
        if (slot != null) {
            Bus.EVENT_BUS.post(new ToolTipEvent.Post(slot.getStack(), this.toolTipX, this.toolTipY));
        }
    }
}
