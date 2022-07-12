package me.voidxwalker.autoreset.mixin.hotkey;

import net.minecraft.class_4107;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
    @Accessor("field_15867") static Map<String, Integer> invokeGetCategoryMap() {
        throw new AssertionError();
    }
    @Accessor("field_19923") static  Map<class_4107.class_4108, KeyBinding> getKeysByCode() {
        throw new AssertionError();
    }
}
