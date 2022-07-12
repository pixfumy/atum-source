package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Atum;
import me.voidxwalker.autoreset.IMoreOptionsDialog;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.world.Difficulty;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {
    @Shadow public boolean hardcore;
    @Shadow private TextFieldWidget levelNameField;
    @Shadow protected abstract void createLevel();

    @Shadow private Difficulty field_24289;
    @Shadow private Difficulty field_24290;

    @Shadow @Final public MoreOptionsDialog moreOptionsDialog;

    @Inject(method = "init", at = @At("TAIL"))
    private void atum_createDesiredWorld(CallbackInfo info) {
        if (Atum.isRunning) {
            Difficulty difficulty;
            switch (Atum.difficulty) {
                case 0 :
                    difficulty = Difficulty.PEACEFUL;
                    break;
                case 1 :
                    difficulty = Difficulty.EASY;
                    break;
                case 2 :
                    difficulty = Difficulty.NORMAL;
                    break;
                case 3 :
                    difficulty = Difficulty.HARD;
                    break;
                case -1 :
                    difficulty = Difficulty.HARD;
                    hardcore = true;
                    break;
                default :
                    Atum.log(Level.WARN, "Invalid difficulty");
                    difficulty = Difficulty.EASY;
                    break;
            }
            field_24289=difficulty;
            field_24290=difficulty;
            if(Atum.seed==null|| Atum.seed.isEmpty()){
                Atum.rsgAttempts++;
            }
            else {
                Atum.ssgAttempts++;
            }

            Atum.saveProperties();
            levelNameField.setText((Atum.seed==null|| Atum.seed.isEmpty())?"Random Speedrun #" + Atum.rsgAttempts:"Set Speedrun #" + Atum.ssgAttempts);
            ((IMoreOptionsDialog)moreOptionsDialog).setGeneratorType(GeneratorTypeAccessor.getVALUES().get(Atum.generatorType));
            ((IMoreOptionsDialog)moreOptionsDialog).setGenerateStructure(Atum.structures);
            ((IMoreOptionsDialog)moreOptionsDialog).setGenerateBonusChest(Atum.bonusChest);
            createLevel();
        }
    }
    @Inject(method = "createLevel",at = @At("HEAD"))
    public void atum_trackResetting(CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.RESETTING;
    }
}
