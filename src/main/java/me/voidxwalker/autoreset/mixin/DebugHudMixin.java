package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Atum;
import me.voidxwalker.autoreset.Pingable;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin implements Pingable {
    @Inject(
            method = {"getRightText"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private void atum_getRightText(CallbackInfoReturnable<List<String>> info) {
        if(Atum.isRunning){
            List<String> returnValue = info.getReturnValue();
            returnValue.add("Resetting "+(Atum.seed==null|| Atum.seed.isEmpty()?"a random seed":("the seed: \""+ Atum.seed+"\"")));
            if(Atum.generatorType!=0){
                returnValue.add("GenType:" + LevelGeneratorType.TYPES[ Atum.generatorType].getTranslationKey());
            }
            if(!Atum.structures){
                returnValue.add("NoStructures");
            }
            if(Atum.bonusChest){
                returnValue.add("BonusChest");
            }
        }
    }
    @Override
    public boolean ping() {
        return true;
    }
}
