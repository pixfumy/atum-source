package me.voidxwalker.autoreset.mixin.hotkey;

import com.google.common.collect.HashBiMap;
import me.voidxwalker.autoreset.Atum;
import net.minecraft.class_4110;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_4110.class)
public class KeyboardMixin {
    @Inject(method = "method_18182",at = @At("HEAD"))
    public void atum_onKey(long window, int key, int scancode, int i, int j, CallbackInfo ci){
        if(Atum.resetKey.method_18166(key,scancode)&&!Atum.hotkeyHeld){
            Atum.hotkeyHeld=true;

            KeyBinding.method_18168( HashBiMap.create( KeyBindingAccessor.getKeysByCode()).inverse().get(Atum.resetKey),true);
            Atum.hotkeyPressed=true;

        }
        else {
            Atum.hotkeyHeld=false;
        }
    }
}
