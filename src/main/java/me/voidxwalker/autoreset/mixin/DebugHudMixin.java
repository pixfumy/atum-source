package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Atum;
import me.voidxwalker.autoreset.Pingable;
import net.minecraft.client.gui.hud.DebugHud;
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
    private void getRightText(CallbackInfoReturnable<List<String>> info) {
        List<String> returnValue = info.getReturnValue();
        returnValue.add("Resetting "+(Atum.seed==null|| Atum.seed.isEmpty()?" a random seed":(" the seed: \""+ Atum.seed+"\"")));
    }
    @Override
    public boolean ping() {
        return true;
    }
}
