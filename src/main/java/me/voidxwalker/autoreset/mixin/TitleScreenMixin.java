package me.voidxwalker.autoreset.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.voidxwalker.autoreset.Atum;
import me.voidxwalker.autoreset.screen.AutoResetOptionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    private ButtonWidget resetButton;
    private Text difficulty;
    private static final Identifier BUTTON_IMAGE = new Identifier("textures/item/golden_boots.png");

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo info) {
        if (Atum.isRunning) {
            CreateWorldScreen.create(client,this);
        } else {
            resetButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 124, this.height / 4 + 48, 20, 20,Text.of(""), (buttonWidget) -> {
                if (hasShiftDown()) {
                    client.setScreen(new AutoResetOptionScreen(this));
                } else {
                    Atum.isRunning = true;
                    this.client.setScreen(this);
                }
            }));
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void goldBootsOverlay(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        getDifficulty();
        RenderSystem.setShaderTexture(0, BUTTON_IMAGE);
        drawTexture(matrices, this.width / 2 - 124+2, this.height / 4 + 48+2, 0.0F, 0.0F, 16, 16, 16, 16);
        if (resetButton.isHovered() && hasShiftDown()) {
            drawCenteredText(matrices, textRenderer, difficulty, this.width / 2 - 124+11, this.height / 4 + 48-15, 16777215);
        }
    }

    private void getDifficulty() {
        switch (Atum.difficulty) {
            case 0 :
                difficulty = Atum.getTranslation("menu.peaceful", "Peaceful");
                break;
            case 1 :
                difficulty = Atum.getTranslation("menu.easy", "Easy");
                break;
            case 2 :
                difficulty = Atum.getTranslation("menu.normal", "Normal");
                break;
            case 3 :
                difficulty = Atum.getTranslation("menu.hard", "Hard");
                break;
            case 4 :
                difficulty = Atum.getTranslation("menu.hardcore", "Hardcore");
                break;
        }
    }
}
