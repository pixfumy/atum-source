package me.voidxwalker.autoreset.mixin.hotkey;

import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
    @Accessor("categoryOrderMap") static Map<String, Integer> invokeGetCategoryMap() {
        throw new AssertionError();
    }
    @Accessor static  Map<InputUtil.KeyCode, KeyBinding> getKeysByCode() {
        throw new AssertionError();
    }
}
