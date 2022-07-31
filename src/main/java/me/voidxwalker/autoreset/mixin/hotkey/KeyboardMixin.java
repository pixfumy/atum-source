package me.voidxwalker.autoreset.mixin.hotkey;

import com.google.common.collect.HashBiMap;
import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.Keyboard;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey",at = @At("HEAD"))
    public void atum_onKey(long window, int key, int scancode, int i, int j, CallbackInfo ci){
        if(Atum.hasClicked) {
            if(Atum.resetKey.matchesKey(key,scancode)&&!Atum.hotkeyHeld){
                Atum.hotkeyHeld=true;
                KeyBinding.setKeyPressed( HashBiMap.create( KeyBindingAccessor.getKeysByCode()).inverse().get(Atum.resetKey),true);
                Atum.hotkeyPressed=true;
            }
            else if(Atum.hotkeyHeld){
                Atum.hotkeyHeld=false;
            }
        }
    }
}
