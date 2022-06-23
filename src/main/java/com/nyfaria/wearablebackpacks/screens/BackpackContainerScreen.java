package com.nyfaria.wearablebackpacks.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Matrix4f;
import com.nyfaria.wearablebackpacks.WearableBackpacks;
import com.nyfaria.wearablebackpacks.backpack.BackpackContainer;
import com.nyfaria.wearablebackpacks.util.Dimension;
import com.nyfaria.wearablebackpacks.util.Rectangle;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class BackpackContainerScreen extends ContainerScreen<BackpackContainer> {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(WearableBackpacks.MODID, "textures/gui/backpack_container.png");


    public BackpackContainerScreen(BackpackContainer handler, PlayerInventory player, ITextComponent title) {
        super(handler, player, title);

        Dimension dimension = handler.getDimension();
        this.imageWidth = dimension.getWidth();
        this.imageHeight = dimension.getHeight();
        this.titleLabelY = 7;
        this.inventoryLabelX = handler.getPlayerInvSlotPosition(dimension, 0, 0).x;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    private static void blitdQuad(Matrix4f matrices, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuilder();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.vertex(matrices, (float) x0, (float) y1, (float) z).uv(u0, v1).endVertex();
        bufferBuilder.vertex(matrices, (float) x1, (float) y1, (float) z).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrices, (float) x1, (float) y0, (float) z).uv(u1, v0).endVertex();
        bufferBuilder.vertex(matrices, (float) x0, (float) y0, (float) z).uv(u0, v0).endVertex();
        bufferBuilder.end();
        WorldVertexBufferUploader.end(bufferBuilder);
    }

    @Override
    protected void renderBg(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        renderBackgroundTexture(matrices, new Rectangle(x, y, imageWidth, imageHeight), delta, 0xFFFFFFFF);
        Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("textures/gui/container/hopper.png"));
        for (Slot slot : getMenu().slots) {
            this.blit(matrices, x + slot.x - 1, y + slot.y - 1, 43, 19, 18, 18);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.renderTooltip(matrices, mouseX, mouseY);
    }

    public void renderBackgroundTexture(MatrixStack matrices, Rectangle bounds, float delta, int color) {
        float alpha = ((color >> 24) & 0xFF) / 255f;
        float red = ((color >> 16) & 0xFF) / 255f;
        float green = ((color >> 8) & 0xFF) / 255f;
        float blue = (color & 0xFF) / 255f;
        RenderSystem.clearColor(red, green, blue, alpha);
        Minecraft.getInstance().getTextureManager().bind(GUI_TEXTURE);
        int x = bounds.x, y = bounds.y, width = bounds.width, height = bounds.height;
        int xTextureOffset = 0;
        int yTextureOffset = 66;

        // 9 Patch Texture

        // Four Corners
        this.blit(matrices, x, y, 106 + xTextureOffset, 124 + yTextureOffset, 8, 8);
        this.blit(matrices, x + width - 8, y, 248 + xTextureOffset, 124 + yTextureOffset, 8, 8);
        this.blit(matrices, x, y + height - 8, 106 + xTextureOffset, 182 + yTextureOffset, 8, 8);
        this.blit(matrices, x + width - 8, y + height - 8, 248 + xTextureOffset, 182 + yTextureOffset, 8, 8);

        Matrix4f matrix = matrices.last().pose();
        // Sides
        blitdQuad(matrix, x + 8, x + width - 8, y, y + 8, getBlitOffset(), (114 + xTextureOffset) / 256f, (248 + xTextureOffset) / 256f, (124 + yTextureOffset) / 256f, (132 + yTextureOffset) / 256f);
        blitdQuad(matrix, x + 8, x + width - 8, y + height - 8, y + height, getBlitOffset(), (114 + xTextureOffset) / 256f, (248 + xTextureOffset) / 256f, (182 + yTextureOffset) / 256f, (190 + yTextureOffset) / 256f);
        blitdQuad(matrix, x, x + 8, y + 8, y + height - 8, getBlitOffset(), (106 + xTextureOffset) / 256f, (114 + xTextureOffset) / 256f, (132 + yTextureOffset) / 256f, (182 + yTextureOffset) / 256f);
        blitdQuad(matrix, x + width - 8, x + width, y + 8, y + height - 8, getBlitOffset(), (248 + xTextureOffset) / 256f, (256 + xTextureOffset) / 256f, (132 + yTextureOffset) / 256f, (182 + yTextureOffset) / 256f);

        // Center
        blitdQuad(matrix, x + 8, x + width - 8, y + 8, y + height - 8, getBlitOffset(), (114 + xTextureOffset) / 256f, (248 + xTextureOffset) / 256f, (132 + yTextureOffset) / 256f, (182 + yTextureOffset) / 256f);
    }
}