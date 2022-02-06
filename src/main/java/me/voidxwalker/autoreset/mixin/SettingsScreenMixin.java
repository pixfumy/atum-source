package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.realms.RealmsBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SettingsScreen.class)
public abstract class SettingsScreenMixin extends Screen {
    @Shadow protected abstract void buttonClicked(ButtonWidget button);


    @Inject(method ="init",at = @At("TAIL"))
    public void addAutoResetButton(CallbackInfo ci){
        if(Atum.isRunning){
            this.buttons.add(new ButtonWidget(696969,0, this.height - 20, 100, 20,  "Stop Resets & Quit"));
        }
    }
    @Inject(at = @At("RETURN"), method = "buttonClicked")
    private void buttonCheck(ButtonWidget button, CallbackInfo info) {
        if (button.id == 696969) {
            Atum.isRunning=false;
            boolean bl = this.client.isIntegratedServerRunning();
            button.active = false;
            this.client.world.disconnect();
            this.client.connect((ClientWorld)null);
            if (bl) {
                this.client.openScreen(new TitleScreen());
            } else {
                this.client.openScreen(new MultiplayerScreen(new TitleScreen()));
            }
        }
    }
}
