package me.voidxwalker.autoreset.mixin;

import com.google.common.collect.HashBiMap;
import me.voidxwalker.autoreset.Atum;
import me.voidxwalker.autoreset.mixin.hotkey.KeyBindingAccessor;
import me.voidxwalker.autoreset.screen.AutoResetOptionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.KeyBinding;
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

    @Inject(method = "<init>()V",at = @At("TAIL"))
    public void resetHotkey(CallbackInfo ci){
        KeyBinding.setKeyPressed( HashBiMap.create( KeyBindingAccessor.getKeysByCode()).inverse().get(Atum.resetKey),true);

        Atum.hotkeyPressed=false;
    }
    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo info) {
        if (Atum.isRunning) {
            if (Atum.loopPrevent) {
                Atum.loopPrevent = false;
            } else {
                minecraft.openScreen(new CreateWorldScreen(this));
            }
        } else {
            resetButton = this.addButton(new ButtonWidget(this.width / 2 - 124, this.height / 4 + 48, 20, 20, "", (buttonWidget) -> {
                if (hasShiftDown()) {
                    minecraft.openScreen(new AutoResetOptionScreen(this));
                } else {
                    Atum.isRunning = true;
                    this.minecraft.openScreen(this);
                }
            }));
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void goldBootsOverlay(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.minecraft.getTextureManager().bindTexture(BUTTON_IMAGE);
        blit(this.width / 2 - 124+2, this.height / 4 + 48+2, 0.0F, 0.0F, 16, 16, 16, 16);
        if (resetButton.isHovered() && hasShiftDown()) {
            drawCenteredString(minecraft.textRenderer, getDifficultyText().asString(), this.width / 2 - 124+11, this.height / 4 + 48-15, 16777215);
        }
    }

    Text getDifficultyText(){
        if(Atum.difficulty==-1){
            return new TranslatableText("selectWorld.gameMode.hardcore");
        }
        return Difficulty.byOrdinal(Atum.difficulty).getTranslatableName();
    }
}
