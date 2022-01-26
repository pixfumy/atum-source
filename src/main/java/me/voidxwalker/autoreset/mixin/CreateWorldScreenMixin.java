package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Main;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.world.Difficulty;
import org.apache.logging.log4j.Level;
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

    @Inject(method = "init", at = @At("TAIL"))
    private void createDesiredWorld(CallbackInfo info) {
        if (Main.isRunning) {
            Difficulty difficulty;
            switch (Main.difficulty) {
                case 0 -> difficulty = Difficulty.PEACEFUL;
                case 1 -> difficulty = Difficulty.EASY;
                case 2 -> difficulty = Difficulty.NORMAL;
                case 3 -> difficulty = Difficulty.HARD;
                case 4 -> {
                    difficulty = Difficulty.HARD;
                    hardcore = true;
                }
                default -> {
                    Main.log(Level.WARN, "Invalid difficulty");
                    difficulty = Difficulty.EASY;
                }
            }
            currentDifficulty = difficulty;
            levelNameField.setText((Main.seed==null||Main.seed.isEmpty()?"Random":"Set")+"Speedrun #" + Main.getNextAttempt());

            createLevel();
        }
    }
}
