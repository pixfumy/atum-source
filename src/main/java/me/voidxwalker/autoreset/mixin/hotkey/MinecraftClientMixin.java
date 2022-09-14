package me.voidxwalker.autoreset.mixin.hotkey;

import com.google.common.collect.HashBiMap;
import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientWorld world;

    @Shadow @Nullable public Screen currentScreen;

    @Shadow public abstract void openScreen(@Nullable Screen screen);

    @Shadow public abstract boolean isInSingleplayer();

    @Shadow public abstract boolean isConnectedToRealms();



    @Shadow public abstract void connect(@Nullable ClientWorld world);


    @Shadow public abstract boolean isIntegratedServerRunning();

    @Shadow public abstract void connect(@Nullable ClientWorld world, String loadingMessage);

    @Shadow public int width;

    @Shadow public int height;

    @Inject(method = "startGame",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/ServerNetworkIo;bindLocal()Ljava/net/SocketAddress;",shift = At.Shift.BEFORE))
    public void atum_trackPostWorldGen(CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.POST_WORLDGEN;
    }
    @Inject(method = "startGame",at = @At(value = "HEAD"))
    public void atum_trackPreWorldGen( CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.PRE_WORLDGEN;
    }
    @Inject(method = "tick",at = @At("HEAD"),cancellable = true)
    public void atum_tick(CallbackInfo ci){
        if(Atum.hotkeyPressed){
            if(Atum.hotkeyState==Atum.HotkeyState.INSIDE_WORLD){
                Screen s = new GameMenuScreen();
                Window window = new Window((MinecraftClient) (Object)this,this.width,height);
                int i = window.getWidth();
                int j = window.getHeight();
                s.init( (MinecraftClient) (Object)this, i, j);
                ButtonWidget b=null;
                for (ButtonWidget e: ((ScreenAccessor)(s)).getButtons()  ) {
                    if(e != null){
                        if( ((ButtonWidget)e).message.equals(new TranslatableText("menu.quitWorld").asString())){
                            if(b==null){
                                b =(ButtonWidget)e;
                            }
                        }
                    }
                }
                KeyBinding.setKeyPressed( Atum.resetKey.getCode(),false);
                Atum.hotkeyPressed=false;
                Atum.isRunning = true;
                if(b==null){
                    boolean bl = MinecraftClient.getInstance().isIntegratedServerRunning();
                    MinecraftClient.getInstance().world.disconnect();
                    MinecraftClient.getInstance().connect((ClientWorld)null);
                    if (bl) {
                        MinecraftClient.getInstance().openScreen(new TitleScreen());
                    }else {
                        MinecraftClient.getInstance().openScreen(new MultiplayerScreen(new TitleScreen()));
                    }
                    ci.cancel();
                }
                else {
                    b.mouseReleased(0,0);
                }
            }
            else if(Atum.hotkeyState==Atum.HotkeyState.OUTSIDE_WORLD){
                System.out.println(1);
                KeyBinding.setKeyPressed ( Atum.resetKey.getCode(),false);
                Atum.hotkeyPressed=false;
                Atum.isRunning=true;
                MinecraftClient.getInstance().openScreen(new TitleScreen());
            }
        }
    }
    @Inject(method = "startGame",at=@At(value = "INVOKE",shift = At.Shift.AFTER,target = "Lnet/minecraft/server/integrated/IntegratedServer;isLoading()Z"),cancellable = true)
    public void atum_tickDuringWorldGen( CallbackInfo ci){
        if(Atum.hotkeyPressed&&Atum.hotkeyState==Atum.HotkeyState.WORLD_GEN){
            if(currentScreen instanceof ProgressScreen){
                ButtonWidget b=null;
                if(!((ScreenAccessor)(currentScreen)).getButtons().isEmpty()){
                    for (ButtonWidget e: ((ScreenAccessor)(currentScreen)).getButtons() ) {
                        if( ((ButtonWidget)e).message.equals(new TranslatableText("menu.returnToMenu").asString())){
                            if(b==null){
                                b =(ButtonWidget)e;
                            }
                        }
                    }
                    if(b!=null){
                        KeyBinding.setKeyPressed( Atum.resetKey.getCode(),false);
                        Atum.hotkeyPressed=false;
                        b.mouseReleased(0,0);
                    }
                }
            }
        }
    }
}
