package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.world.gen.GeneratorOptions;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.OptionalLong;

@Mixin(MoreOptionsDialog.class)
public abstract class MoreOptionsDialogMixin {


    @ModifyArg(method = "getGeneratorOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/GeneratorOptions;withHardcore(ZLjava/util/OptionalLong;)Lnet/minecraft/world/gen/GeneratorOptions;",ordinal = 0),index = 1)
    public OptionalLong setSeed(OptionalLong originalSeed){
        if(!Atum.isRunning){
            return originalSeed;
        }
        String string = Atum.seed==null?"": Atum.seed;
        OptionalLong optionalLong= GeneratorOptions.parseSeed(string);


        Atum.log(Level.INFO,"Resetting "+((Atum.seed ==null|| Atum.seed.isEmpty()?"a random seed":"the set seed: "+"\""+optionalLong.getAsLong()+"\"")));
        return optionalLong;
    }
}
