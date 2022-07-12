package me.voidxwalker.autoreset.mixin;

import net.minecraft.client.world.GeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(GeneratorType.class)
public interface GeneratorTypeAccessor {
    @Accessor static List<GeneratorType> getVALUES(){
        throw  new IllegalArgumentException();
    }
}
