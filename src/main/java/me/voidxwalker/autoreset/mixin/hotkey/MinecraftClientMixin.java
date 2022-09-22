package me.voidxwalker.autoreset.mixin.hotkey;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow public abstract void disconnect(Screen screen);

    @Shadow @Nullable public ClientWorld world;

    @Shadow @Nullable public Screen currentScreen;

    @Shadow public abstract boolean isInSingleplayer();

    @Shadow public abstract boolean isConnectedToRealms();

    @Shadow public abstract void disconnect();

    @Shadow @Final private Window window;

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Shadow @Final public Keyboard keyboard;

    @Inject(method = "startIntegratedServer",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/ServerNetworkIo;bindLocal()Ljava/net/SocketAddress;",shift = At.Shift.BEFORE))
    public void atum_trackPostWorldGen(CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.POST_WORLDGEN;
    }
    @Inject(method = "startIntegratedServer",at = @At(value = "HEAD"))
    public void atum_trackPreWorldGen( CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.PRE_WORLDGEN;
    }
    @Inject(method = "tick",at = @At("HEAD"))
    public void atum_tick(CallbackInfo ci){
        if(Atum.hotkeyPressed){
            if(Atum.hotkeyState==Atum.HotkeyState.INSIDE_WORLD){
                Screen s = new GameMenuScreen(true);
                s.init( (MinecraftClient) (Object)this, this.window.getScaledWidth(), this.window.getScaledHeight());
                ButtonWidget b=null;
                for (Element e: s.children() ) {
                    if(e instanceof ButtonWidget){
                        if( ((ButtonWidget)e).getMessage().equals(Text.translatable("menu.quitWorld"))){
                            if(b==null){
                                b =(ButtonWidget)e;
                            }
                        }
                    }
                }
                Atum.resetKey.setPressed(false);
                Atum.hotkeyPressed=false;
                Atum.isRunning = true;
                if(b==null){
                    boolean bl = this.isInSingleplayer();
                        boolean bl2 = this.isConnectedToRealms();
                        this.world.disconnect();
                        if (bl) {
                            this.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
                        } else {
                            this.disconnect();
                        }
                    TitleScreen titleScreen = new TitleScreen();
                    if (bl) {
                        this.setScreen(titleScreen);
                    } else if (bl2) {
                        this.setScreen(new RealmsMainScreen(titleScreen));
                    } else {
                        this.setScreen(new MultiplayerScreen(titleScreen));
                    }
                    }
                    else {
                        b.onPress();
                    }
            }
            else if(Atum.hotkeyState==Atum.HotkeyState.OUTSIDE_WORLD){
                Atum.resetKey.setPressed(false);
                Atum.hotkeyPressed=false;
                Atum.isRunning=true;
                MinecraftClient.getInstance().setScreen(new TitleScreen());
            }
        }
    }
    @Inject(method = "startIntegratedServer",at=@At(value = "INVOKE",shift = At.Shift.AFTER,target = "Lnet/minecraft/server/integrated/IntegratedServer;isLoading()Z"),cancellable = true)
    public void atum_tickDuringWorldGen( CallbackInfo ci){
        if(Atum.hotkeyPressed&&Atum.hotkeyState==Atum.HotkeyState.WORLD_GEN){
            if(currentScreen instanceof LevelLoadingScreen){
                ButtonWidget b=null;
                keyboard.onKey(this.window.getHandle(),256,1,1,0);
                if(!currentScreen.children().isEmpty()){
                    for (Element e: currentScreen.children() ) {
                        if(e instanceof ButtonWidget){
                            if( ((ButtonWidget)e).getMessage().equals(Text.translatable("menu.returnToMenu"))){
                                if(b==null){
                                    b =(ButtonWidget)e;
                                }
                            }
                        }
                    }

                    if(b!=null){
                        Atum.resetKey.setPressed(false);
                        Atum.hotkeyPressed=false;
                        b.onPress();
                    }
                }
            }
        }
    }
}
