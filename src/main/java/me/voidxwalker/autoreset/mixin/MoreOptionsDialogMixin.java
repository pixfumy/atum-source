package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.world.gen.GeneratorOptions;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.OptionalLong;

@Mixin(GeneratorOptions.class)
public abstract class MoreOptionsDialogMixin {


    @ModifyVariable(method = "withHardcore", at = @At(value = "HEAD",ordinal = 0))
    public OptionalLong setSeed(OptionalLong value){
        if(!Atum.isRunning){
            return value;
        }
        OptionalLong optionalLong = GeneratorOptions.parseSeed(Atum.seed);
        Atum.log(Level.INFO,"Resetting "+((Atum.seed ==null|| Atum.seed.isEmpty()?"a random seed":"the set seed: "+"\""+optionalLong.getAsLong()+"\"")));
        return optionalLong;
    }
}
