package me.voidxwalker.autoreset.mixin.hotkey;

import com.google.common.collect.HashBiMap;
import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class KeyboardMixin {
    long atum_lastHeld=0;
    @Inject(method = "handleKeyInput",at = @At("HEAD"))
    public void atum_onKey(CallbackInfo ci){
        int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
        if(System.currentTimeMillis()- atum_lastHeld>1000){
            Atum.hotkeyHeld=false;
        }
        if(Atum.resetKey.getCode()==i&&Keyboard.getEventKeyState()&&!Atum.hotkeyHeld){
            Atum.hotkeyHeld=true;
            atum_lastHeld=System.currentTimeMillis();
            KeyBinding.setKeyPressed( Atum.resetKey.getCode(),true);
            Atum.hotkeyPressed=true;

        }
        else {
            Atum.hotkeyHeld=false;
        }
    }
}
