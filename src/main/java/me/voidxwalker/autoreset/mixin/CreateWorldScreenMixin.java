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

    @Shadow private Difficulty currentDifficulty;

    @Shadow @Final public MoreOptionsDialog moreOptionsDialog;

    @Inject(method = "init", at = @At("TAIL"))
    private void createDesiredWorld(CallbackInfo info) {
        if (Atum.isRunning) {
            Difficulty difficulty;
            switch (Atum.difficulty) {
                case 0 -> difficulty = Difficulty.PEACEFUL;
                case 1 -> difficulty = Difficulty.EASY;
                case 2 -> difficulty = Difficulty.NORMAL;
                case 3 -> difficulty = Difficulty.HARD;
                case 4 -> {
                    difficulty = Difficulty.HARD;
                    hardcore = true;
                }
                default -> {
                    Atum.log(Level.WARN, "Invalid difficulty");
                    difficulty = Difficulty.EASY;
                }
            }
            if(Atum.seed==null|| Atum.seed.isEmpty()){
                Atum.rsgAttempts++;
            }
            else {
                Atum.ssgAttempts++;
            }
            Atum.saveProperties();
            currentDifficulty = difficulty;
            levelNameField.setText((Atum.seed==null|| Atum.seed.isEmpty())?"Random Speedrun #" + Atum.rsgAttempts:"Set Speedrun #" + Atum.ssgAttempts);
            ((IMoreOptionsDialog)moreOptionsDialog).setGeneratorType(GeneratorTypeAccessor.getVALUES().get(Atum.generatorType));
            ((IMoreOptionsDialog)moreOptionsDialog).setGenerateStructure(Atum.structures);
            ((IMoreOptionsDialog)moreOptionsDialog).setGenerateBonusChest(Atum.bonusChest);
            createLevel();
        }
    }
}
