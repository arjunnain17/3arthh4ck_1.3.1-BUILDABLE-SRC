package org.newdawn.slick.geom;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;

final class ShapeRenderer$2
implements ShapeRenderer.PointCallback {
    final float val$scaleX;
    final float val$scaleY;
    final Image val$image;

    ShapeRenderer$2(float f, float f2, Image image) {
        this.val$scaleX = f;
        this.val$scaleY = f2;
        this.val$image = image;
    }

    @Override
    public float[] preRenderPoint(Shape shape, float x, float y) {
        float tx = x * this.val$scaleX;
        float ty = y * this.val$scaleY;
        tx = this.val$image.getTextureOffsetX() + this.val$image.getTextureWidth() * tx;
        ty = this.val$image.getTextureOffsetY() + this.val$image.getTextureHeight() * ty;
        GL.glTexCoord2f(tx, ty);
        return null;
    }
}
