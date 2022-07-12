package me.voidxwalker.autoreset.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.voidxwalker.autoreset.Atum;
import me.voidxwalker.autoreset.screen.AutoResetOptionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    private ButtonWidget resetButton;
    private static final Identifier BUTTON_IMAGE = new Identifier("textures/item/golden_boots.png");

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method="init", at=@At("TAIL"))
    private void init(CallbackInfo info) {
        this.resetButton = addDrawableChild(new ButtonWidget(this.width / 2 - 124, this.height / 4 + 48, 20, 20, new LiteralText(""), buttonWidget -> {
            if (hasShiftDown()) {
                this.client.setScreen(new AutoResetOptionScreen(this));
            } else {
                Atum.isRunning = true;
            }
        }));
    }

    @Inject(method="render", at=@At("HEAD"), cancellable = true)
    private void reset(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Atum.isRunning) {
            this.client.setScreen(CreateWorldScreen.create(this));
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void goldBootsOverlay(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        RenderSystem.setShaderTexture(0, BUTTON_IMAGE);
        drawTexture(matrices, this.width / 2 - 124+2, this.height / 4 + 48+2, 0.0F, 0.0F, 16, 16, 16, 16);
        if (resetButton.isHovered() && hasShiftDown()) {
            drawCenteredText(matrices, textRenderer, getDifficultyText(), this.width / 2 - 124+11, this.height / 4 + 48-15, 16777215);
        }
    }

    Text getDifficultyText(){
        if(Atum.difficulty==-1){
            return new TranslatableText("selectWorld.gameMode.hardcore");
        }
        return Difficulty.byOrdinal(Atum.difficulty).getTranslatableName();
    }
}
