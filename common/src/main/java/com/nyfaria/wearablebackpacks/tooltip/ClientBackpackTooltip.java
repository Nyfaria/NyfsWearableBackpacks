package com.nyfaria.wearablebackpacks.tooltip;

import com.nyfaria.wearablebackpacks.Constants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientBackpackTooltip implements ClientTooltipComponent {
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Constants.MODID, "textures/gui/tooltip.png");

    private final NonNullList<ItemStack> items;
    private final int weight;

    public ClientBackpackTooltip(BackpackTooltip toolTip) {
        this.items = toolTip.getItems();
        this.weight = toolTip.getWeight();
    }

    public int getHeight() {
        return this.gridSizeY() * 20 + 2 + 4;
    }

    public int getWidth(Font p_169901_) {
        return this.gridSizeX() * 18 + 2;
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        boolean flag = this.weight >= 64;
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int i1 = 0; i1 < i; ++i1) {
                int j1 = pX + i1 * 18 + 1;
                int k1 = pY + l * 20 + 1;
                this.renderSlot(j1, k1, k++, flag, pGuiGraphics, pFont);
            }
        }

        this.drawBorder(pX, pY, i, j, pGuiGraphics);
    }

    private void renderSlot(int pX, int pY, int pItemIndex, boolean pIsBundleFull, GuiGraphics pGuiGraphics, Font pFont) {
        if (pItemIndex >= this.items.size()) {
            this.blit(pGuiGraphics, pX, pY, pIsBundleFull ? Texture.BLOCKED_SLOT : Texture.SLOT);
        } else {
            ItemStack itemstack = this.items.get(pItemIndex);
            this.blit(pGuiGraphics, pX, pY, Texture.SLOT);
            pGuiGraphics.renderItem(itemstack, pX + 1, pY + 1, pItemIndex);
            pGuiGraphics.renderItemDecorations(pFont, itemstack, pX + 1, pY + 1);
            if (pItemIndex == 0) {
                AbstractContainerScreen.renderSlotHighlight(pGuiGraphics, pX + 1, pY + 1, 0);
            }

        }
    }

    private void drawBorder(int pX, int pY, int pSlotWidth, int pSlotHeight, GuiGraphics pGuiGraphics) {
        this.blit(pGuiGraphics, pX, pY, Texture.BORDER_CORNER_TOP);
        this.blit(pGuiGraphics, pX + pSlotWidth * 18 + 1, pY, Texture.BORDER_CORNER_TOP);

        for(int i = 0; i < pSlotWidth; ++i) {
            this.blit(pGuiGraphics, pX + 1 + i * 18, pY, Texture.BORDER_HORIZONTAL_TOP);
            this.blit(pGuiGraphics, pX + 1 + i * 18, pY + pSlotHeight * 20, Texture.BORDER_HORIZONTAL_BOTTOM);
        }

        for(int j = 0; j < pSlotHeight; ++j) {
            this.blit(pGuiGraphics, pX, pY + j * 20 + 1, Texture.BORDER_VERTICAL);
            this.blit(pGuiGraphics, pX + pSlotWidth * 18 + 1, pY + j * 20 + 1, Texture.BORDER_VERTICAL);
        }

        this.blit(pGuiGraphics, pX, pY + pSlotHeight * 20, Texture.BORDER_CORNER_BOTTOM);
        this.blit(pGuiGraphics, pX + pSlotWidth * 18 + 1, pY + pSlotHeight * 20, Texture.BORDER_CORNER_BOTTOM);
    }

    private void blit(GuiGraphics pGuiGraphics, int pX, int pY, Texture pTexture) {
        pGuiGraphics.blit(TEXTURE_LOCATION, pX, pY, 0, (float)pTexture.x, (float)pTexture.y, pTexture.w, pTexture.h, 128, 128);
    }

    private int gridSizeX() {
        return 9;
    }

    private int gridSizeY() {
        return 3;
    }

    @OnlyIn(Dist.CLIENT)
    enum Texture {
        SLOT(0, 0, 18, 20),
        BLOCKED_SLOT(0, 40, 18, 20),
        BORDER_VERTICAL(0, 18, 1, 20),
        BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
        BORDER_CORNER_TOP(0, 20, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);

        public final int x;
        public final int y;
        public final int w;
        public final int h;

        Texture(int p_169928_, int p_169929_, int p_169930_, int p_169931_) {
            this.x = p_169928_;
            this.y = p_169929_;
            this.w = p_169930_;
            this.h = p_169931_;
        }
    }
}