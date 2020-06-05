package com.irtimaled.bbor.client.renderers;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Renderer {
    static Renderer startLines() {
        return new Renderer(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
    }

    static Renderer startCircle() {
        return new Renderer(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
    }

    static Renderer startQuads() {
        return new Renderer(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    }

    static Renderer startPoints() {
        return new Renderer(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
    }

    public static Renderer startTextured() {
        return new Renderer(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    }

    private final Tessellator tessellator;
    private final BufferBuilder bufferBuilder;

    private int red;
    private int green;
    private int blue;
    private int alpha;

    private Renderer(int glMode, VertexFormat vertexFormat) {
        tessellator = Tessellator.getInstance();
        bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(glMode, vertexFormat);
    }

    public Renderer setColor(Color color) {
        return setColor(color.getRed(), color.getGreen(), color.getBlue())
                .setAlpha(color.getAlpha());
    }

    public Renderer setColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        return this;
    }

    public Renderer setAlpha(int alpha) {
        this.alpha = alpha;
        return this;
    }

    Renderer addPoint(OffsetPoint point) {
        return addPoint(point.getX(), point.getY(), point.getZ());
    }

    public Renderer addPoints(OffsetPoint[] points) {
        Renderer renderer = this;
        for (OffsetPoint point : points) {
            renderer = renderer.addPoint(point);
        }
        return renderer;
    }

    Renderer addPoint(double x, double y, double z) {
        pos(x, y, z);
        color();
        end();
        return this;
    }

    public Renderer addPoint(double x, double y, double z, double u, double v) {
        pos(x, y, z);
        tex(u, v);
        color();
        end();
        return this;
    }

    public void render() {
        tessellator.draw();
    }

    private void pos(double x, double y, double z) {
        bufferBuilder.pos(x, y, z);
    }

    private void tex(double u, double v) {
        bufferBuilder.tex((float) u, (float) v);
    }

    private void color() {
        bufferBuilder.color(red, green, blue, alpha);
    }

    private void end() {
        bufferBuilder.endVertex();
    }
}
