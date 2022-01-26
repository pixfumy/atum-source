package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Main;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;


@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen{
    @Shadow private TextFieldWidget levelNameField;


    @Shadow private boolean creatingLevel;

    @Shadow private String gamemodeName;

    @Shadow private boolean structures;

    @Shadow private boolean hardcore;

    @Shadow private int generatorType;

    @Shadow public String generatorOptions;

    @Shadow private boolean bonusChest;

    @Shadow private boolean tweakedCheats;

    @Shadow private String saveDirectoryName;

    @Inject(method = "init", at = @At("TAIL"))
    private void createDesiredWorld(CallbackInfo info) {
        if (Main.isRunning) {
            if(Main.isHardcore){
                hardcore=true;
            }
            levelNameField.setText((Main.seed==null||Main.seed.isEmpty()?"Random":"Set")+"Speedrun #" + Main.getNextAttempt());
            createLevel();
        }
    }
    private void createLevel (){
        this.client.openScreen((Screen)null);
        if (this.creatingLevel) {
            return;
        }
        this.creatingLevel = true;
        long l = (new Random()).nextLong();
        String string = Main.seed;
        if (!StringUtils.isEmpty(string)) {
            try {
                long m = Long.parseLong(string);
                if (m != 0L) {
                    l = m;
                }
            } catch (NumberFormatException var7) {
                l = (long)string.hashCode();
            }
        }

        LevelInfo.GameMode gameMode = LevelInfo.GameMode.byName(this.gamemodeName);
        LevelInfo levelInfo = new LevelInfo(l, gameMode, this.structures, this.hardcore, LevelGeneratorType.TYPES[this.generatorType]);
        levelInfo.setGeneratorOptions(this.generatorOptions);
        if (this.bonusChest && !this.hardcore) {
            levelInfo.setBonusChest();
        }

        if (this.tweakedCheats && !this.hardcore) {
            levelInfo.enableCommands();
        }
        this.client.startGame((Main.seed==null||Main.seed.isEmpty()?"Random":"Set")+"Speedrun #" + Main.getNextAttempt(), (Main.seed==null||Main.seed.isEmpty()?"Random":"Set")+"Speedrun #" + Main.getNextAttempt(), levelInfo);


        Main.log(Level.INFO,(Main.seed==null||Main.seed.isEmpty()?"Resetting a random seed":"Resetting the set seed"+"\""+l+"\""));

    }
}
