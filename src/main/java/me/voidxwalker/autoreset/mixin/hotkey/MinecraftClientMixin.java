package me.voidxwalker.autoreset.mixin.hotkey;

import com.mojang.datafixers.util.Function4;
import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow public abstract void disconnect(Screen screen);

    @Shadow @Nullable public ClientWorld world;

    @Shadow @Nullable public Screen currentScreen;

    @Shadow public abstract void openScreen(@Nullable Screen screen);

    @Shadow public abstract boolean isInSingleplayer();

    @Shadow public abstract boolean isConnectedToRealms();

    @Shadow public abstract void disconnect();

    @Shadow @Final private Window window;

    @Inject(method = "startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/RegistryTracker$Modifiable;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/ServerNetworkIo;bindLocal()Ljava/net/SocketAddress;",shift = At.Shift.BEFORE))
    public void atum_trackPostWorldGen(CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.POST_WORLDGEN;
    }
    @Inject(method = "startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/RegistryTracker$Modifiable;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V",at = @At(value = "HEAD"))
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
                        if( ((ButtonWidget)e).getMessage().equals(new TranslatableText("menu.quitWorld"))){
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
                            this.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
                        } else {
                            this.disconnect();
                        }
                        if (bl) {
                            this.openScreen(new TitleScreen());
                        } else if (bl2) {
                            RealmsBridge realmsBridge = new RealmsBridge();
                            realmsBridge.switchToRealms(new TitleScreen());
                        } else {
                            this.openScreen(new MultiplayerScreen(new TitleScreen()));
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
                MinecraftClient.getInstance().openScreen(new TitleScreen());
            }
        }
    }
    @Inject(method = "startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/RegistryTracker$Modifiable;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V",at=@At(value = "INVOKE",shift = At.Shift.AFTER,target = "Lnet/minecraft/server/integrated/IntegratedServer;isLoading()Z"),cancellable = true)
    public void atum_tickDuringWorldGen( CallbackInfo ci){
        if(Atum.hotkeyPressed&&Atum.hotkeyState==Atum.HotkeyState.WORLD_GEN){
            if(currentScreen instanceof LevelLoadingScreen){
                ButtonWidget b=null;
                if(!currentScreen.children().isEmpty()){
                    for (Element e: currentScreen.children() ) {
                        if(e instanceof ButtonWidget){
                            if( ((ButtonWidget)e).getMessage().equals(new TranslatableText("menu.returnToMenu"))){
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