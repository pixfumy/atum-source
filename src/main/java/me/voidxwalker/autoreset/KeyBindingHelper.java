package me.voidxwalker.autoreset;

import com.google.common.collect.Lists;
import me.voidxwalker.autoreset.mixin.hotkey.KeyBindingAccessor;
import net.minecraft.client.options.KeyBinding;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class KeyBindingHelper {
    private static final List<KeyBinding> moddedKeyBindings = Lists.newArrayList();

    private static Set<String> getCategoryMap() {
        return KeyBindingAccessor.invokeGetCategoryMap();
    }

    private static boolean hasCategory(String categoryTranslationKey) {
        return getCategoryMap().contains(categoryTranslationKey);
    }

    public static void addCategory(String categoryTranslationKey) {
        Set<String> map = getCategoryMap();

        if (map.contains(categoryTranslationKey)) {
            return;
        }


        map.add(categoryTranslationKey);
    }

    public static KeyBinding registerKeyBinding(KeyBinding binding) {
        if (!hasCategory(binding.getCategory())) {
            addCategory(binding.getCategory());
        }

        moddedKeyBindings.add(binding);
        return binding;
    }

    public static KeyBinding[] process(KeyBinding[] keysAll) {
        List<KeyBinding> newKeysAll = Lists.newArrayList(keysAll);
        newKeysAll.removeAll(moddedKeyBindings);
        newKeysAll.addAll(moddedKeyBindings);
        return newKeysAll.toArray(new KeyBinding[0]);
    }
}