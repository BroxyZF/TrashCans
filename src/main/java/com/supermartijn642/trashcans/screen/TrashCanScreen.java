package com.supermartijn642.trashcans.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.supermartijn642.trashcans.TrashCanTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created 7/11/2020 by SuperMartijn642
 */
public abstract class TrashCanScreen<T extends TrashCanContainer> extends ContainerScreen<T> {

    public TrashCanScreen(T container, String title){
        super(container, container.player.inventory, new TranslationTextComponent(title));
        this.xSize = container.width;
        this.ySize = container.height;
    }

    @Override
    protected void func_231160_c_(){
        super.func_231160_c_();

        TrashCanTile tile = this.container.getTileOrClose();
        if(tile != null)
            this.addButtons(tile);
    }

    protected abstract void addButtons(TrashCanTile tile);

    @Override
    public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        this.func_230446_a_(matrixStack);
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);

        TrashCanTile tile = this.container.getTileOrClose();
        if(tile != null)
            this.drawToolTips(matrixStack, tile, mouseX, mouseY);
    }

    protected abstract void drawToolTips(MatrixStack matrixStack, TrashCanTile tile, int mouseX, int mouseY);

    @Override
    public void func_231023_e_(){
        TrashCanTile tile = this.container.getTileOrClose();
        if(tile == null)
            return;

        super.func_231023_e_();
        this.tick(tile);
    }

    protected abstract void tick(TrashCanTile tile);

    protected abstract String getBackground();

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY){
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("trashcans", "textures/" + this.getBackground()));
        this.func_238474_b_(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.drawCenteredString(matrixStack, this.field_230704_d_, this.xSize / 2f, 6);
        this.drawString(matrixStack, this.playerInventory.getDisplayName(), 21, this.ySize - 94);

        TrashCanTile tile = this.container.getTileOrClose();
        if(tile != null)
            this.drawText(matrixStack, tile);
    }

    protected abstract void drawText(MatrixStack matrixStack, TrashCanTile tile);

    public void drawCenteredString(MatrixStack matrixStack, ITextComponent text, float x, float y){
        this.field_230712_o_.func_238422_b_(matrixStack, text, this.guiLeft + x - this.field_230712_o_.func_238414_a_(text) / 2f, this.guiTop + y, 4210752);
    }

    public void drawString(MatrixStack matrixStack, ITextComponent text, float x, float y){
        this.field_230712_o_.func_238422_b_(matrixStack, text, this.guiLeft + x, this.guiTop + y, 4210752);
    }

    public void renderToolTip(MatrixStack matrixStack, boolean translate, String string, int x, int y){
        super.func_238652_a_(matrixStack, translate ? new TranslationTextComponent(string) : new StringTextComponent(string), x, y);
    }

    @Override
    protected void func_230451_b_(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_){
        // stop default text drawing
    }
}