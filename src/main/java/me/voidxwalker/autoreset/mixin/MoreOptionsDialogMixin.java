package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Atum;
import me.voidxwalker.autoreset.IMoreOptionsDialog;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.WorldPreset;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.OptionalLong;

@Mixin(MoreOptionsDialog.class)
public abstract class MoreOptionsDialogMixin implements IMoreOptionsDialog {


    @Shadow private GeneratorOptionsHolder generatorOptionsHolder;

    @Redirect(method = "getGeneratorOptionsHolder(Z)Lnet/minecraft/client/world/GeneratorOptionsHolder;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/GeneratorOptions;parseSeed(Ljava/lang/String;)Ljava/util/OptionalLong;",ordinal = 0))
    public OptionalLong setSeed(String string){
        if(!Atum.isRunning){
            return GeneratorOptions.parseSeed(string);
        }
        OptionalLong optionalLong = GeneratorOptions.parseSeed(Atum.seed);
        Atum.log(Level.INFO,"Resetting "+((Atum.seed ==null|| Atum.seed.isEmpty()?"a random seed":"the set seed: "+"\""+optionalLong.getAsLong()+"\"")));
        return optionalLong;
    }
    public void setGeneratorType(WorldPreset preset){
        this.generatorOptionsHolder.apply(generatorOptions -> preset.createGeneratorOptions((GeneratorOptions)generatorOptions));

    }
    public void setGenerateStructure(boolean generate){
        if(generate!=generatorOptionsHolder.generatorOptions().shouldGenerateStructures()){
            generatorOptionsHolder.apply(GeneratorOptions::toggleGenerateStructures);
        }
    }
    public void setGenerateBonusChest(boolean generate){
        if(generate!=generatorOptionsHolder.generatorOptions().hasBonusChest()){
            generatorOptionsHolder.apply(GeneratorOptions::toggleBonusChest);
        }
    }
}
