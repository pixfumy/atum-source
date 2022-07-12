package me.voidxwalker.autoreset.mixin.hotkey;

import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
    @Accessor("CATEGORY_ORDER_MAP") static Map<String, Integer> invokeGetCategoryMap() {
        throw new AssertionError();
    }
}
