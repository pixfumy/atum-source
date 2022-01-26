package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Main;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.OptionalLong;

@Mixin(MoreOptionsDialog.class)
public abstract class MoreOptionsDialogMixin {
    @Shadow private static OptionalLong tryParseLong(String string) {
        return OptionalLong.empty();
    }

    @ModifyArg(method = "getGeneratorOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/GeneratorOptions;withHardcore(ZLjava/util/OptionalLong;)Lnet/minecraft/world/gen/GeneratorOptions;",ordinal = 0),index = 1)
    public OptionalLong setSeed(OptionalLong originalSeed){
        if(!Main.isRunning){
            return originalSeed;
        }
        String string = Main.seed==null?"":Main.seed;
        OptionalLong optionalLong;
        if (StringUtils.isEmpty(string)) {
            optionalLong = OptionalLong.empty();
        } else {
            OptionalLong optionalLong2 = tryParseLong(string);
            if (optionalLong2.isPresent() && optionalLong2.getAsLong() != 0L) {
                optionalLong = optionalLong2;
            } else {
                optionalLong = OptionalLong.of(string.hashCode());
            }
        }
        Main.log(Level.INFO,"Resetting "+((Main.seed ==null||Main.seed.isEmpty()?"a random seed":"the set seed: "+"\""+optionalLong.getAsLong()+"\"")));
        return optionalLong;
    }
}
