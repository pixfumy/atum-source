package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Main;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Text title) {
        super(title);
    }
    @Inject(method ="init",at = @At("TAIL"))
    public void addAutoResetButton(CallbackInfo ci){

        if(Main.isRunning){
            this.addDrawableChild(new ButtonWidget(0, this.height - 20, 100, 20, Main.getTranslation("menu.stop_resets","Stop Resets & Quit"), (buttonWidget) -> {

                Main.isRunning = false;
                this.client.world.disconnect();
                this.client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
                this.client.setScreen(new TitleScreen());
            }));
        }

    }

}
