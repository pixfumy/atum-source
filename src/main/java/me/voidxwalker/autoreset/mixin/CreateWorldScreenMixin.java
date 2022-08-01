package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Atum;
import me.voidxwalker.autoreset.IMoreOptionsDialog;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.gen.WorldPresets;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;


@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen {
    @Shadow public boolean hardcore;
    @Shadow private TextFieldWidget levelNameField;

    protected CreateWorldScreenMixin(Text text) {
        super(text);
    }

    @Shadow protected abstract void createLevel();

    @Shadow private Difficulty currentDifficulty;

    @Shadow @Final public MoreOptionsDialog moreOptionsDialog;

    @Inject(method = "init", at = @At("TAIL"))
    private void createDesiredWorld(CallbackInfo info) {
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
                case 4 :
                    difficulty = Difficulty.HARD;
                    hardcore = true;
                    break;
                default :
                    Atum.log(Level.WARN, "Invalid difficulty");
                    difficulty = Difficulty.EASY;
                    break;

            }
            if(Atum.seed==null|| Atum.seed.isEmpty()){
                Atum.rsgAttempts++;
            }
            else {
                Atum.ssgAttempts++;
            }
            try {
                Atum.saveProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentDifficulty = difficulty;
            levelNameField.setText((Atum.seed==null|| Atum.seed.isEmpty())?"Random Speedrun #" + Atum.rsgAttempts:"Set Speedrun #" + Atum.ssgAttempts);
            switch (Atum.generatorType){
                case 0:
                    ((IMoreOptionsDialog)moreOptionsDialog).setGeneratorType(DynamicRegistryManager.createAndLoad().get(Registry.WORLD_PRESET_KEY).entryOf(WorldPresets.DEFAULT).comp_349());
                    break;
                case 1:

                    ((IMoreOptionsDialog)moreOptionsDialog).setGeneratorType(DynamicRegistryManager.createAndLoad().get(Registry.WORLD_PRESET_KEY).entryOf(WorldPresets.FLAT).comp_349());
                    break;
                case 2:
                    ((IMoreOptionsDialog)moreOptionsDialog).setGeneratorType(DynamicRegistryManager.createAndLoad().get(Registry.WORLD_PRESET_KEY).entryOf(WorldPresets.LARGE_BIOMES).comp_349());
                    break;
                case 3:
                    ((IMoreOptionsDialog)moreOptionsDialog).setGeneratorType(DynamicRegistryManager.createAndLoad().get(Registry.WORLD_PRESET_KEY).entryOf(WorldPresets.AMPLIFIED).comp_349());
                    break;
                case 4:
                    ((IMoreOptionsDialog)moreOptionsDialog).setGeneratorType(DynamicRegistryManager.createAndLoad().get(Registry.WORLD_PRESET_KEY).entryOf(WorldPresets.SINGLE_BIOME_SURFACE).comp_349());

                    break;
            }

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
