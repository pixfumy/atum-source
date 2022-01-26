package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Main;
import me.voidxwalker.autoreset.screen.AutoResetOptionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
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
        if (Main.isRunning) {
            if (Main.loopPrevent) {
                Main.loopPrevent = false;
            } else {
                minecraft.openScreen(new CreateWorldScreen(this));
            }
        } else {
            resetButton = this.addButton(new ButtonWidget(this.width / 2 - 124, this.height / 4 + 48, 20, 20, "", (buttonWidget) -> {
                if (hasShiftDown()) {
                    minecraft.openScreen(new AutoResetOptionScreen(this));
                } else {
                    Main.isRunning = true;
                    this.minecraft.openScreen(this);
                }
            }));
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void goldBootsOverlay(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        getDifficulty();
        this.minecraft.getTextureManager().bindTexture(BUTTON_IMAGE);
        blit(this.width / 2 - 124+2, this.height / 4 + 48+2, 0.0F, 0.0F, 16, 16, 16, 16);
        if (resetButton.isHovered() && hasShiftDown()) {
            drawCenteredString(minecraft.textRenderer, difficulty.asString(), this.width / 2 - 124+11, this.height / 4 + 48-15, 16777215);
        }
    }

    private void getDifficulty() {
        if(Main.isHardcore) {
            difficulty = Main.getTranslation("menu.autoreset.hardcore-on","Hardcore: ON");
        }
        else {
            difficulty = Main.getTranslation("menu.autoreset.hardcore-off","Hardcore: OFF");
        }

    }
}
