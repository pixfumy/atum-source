package me.voidxwalker.autoreset.mixin;

import com.google.gson.JsonElement;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import me.voidxwalker.autoreset.Atum;
import net.minecraft.class_4372;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.GameMode;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;


@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen{
    @Shadow private TextFieldWidget levelNameField;


    @Shadow private boolean creatingLevel;


    @Shadow private boolean structures;

    @Shadow private boolean hardcore;

    @Shadow private int generatorType;


    @Shadow private boolean bonusChest;

    @Shadow private boolean tweakedCheats;

    @Shadow private String saveDirectoryName;

    @Shadow private String gamemodeName;

    @Shadow public NbtCompound field_20472;

    @Inject(method = "init", at = @At("TAIL"))
    private void createDesiredWorld(CallbackInfo info) {
        if (Atum.isRunning) {
            if(Atum.difficulty==-1){
                hardcore=true;
            }

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
        String string = Atum.seed;
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
        if(Atum.seed==null|| Atum.seed.isEmpty()){
            Atum.rsgAttempts++;
        }
        else {
            Atum.ssgAttempts++;
        }
        this.field_20472 = new NbtCompound();
        LevelInfo levelInfo = new LevelInfo(l, GameMode.setGameModeWithString(this.gamemodeName), Atum.structures, this.hardcore, LevelGeneratorType.TYPES[Atum.generatorType]);
        levelInfo.method_16395((JsonElement) Dynamic.convert(class_4372.field_21487, JsonOps.INSTANCE, this.field_20472));
        if (Atum.bonusChest && !this.hardcore) {
            levelInfo.setBonusChest();
        }
        if (this.tweakedCheats && !this.hardcore) {
            levelInfo.enableCommands();
        }
        Atum.saveProperties();
        Atum.log(Level.INFO,(Atum.seed==null|| Atum.seed.isEmpty()?"Resetting a random seed":"Resetting the set seed"+"\""+l+"\""));
        levelNameField.setText((Atum.seed==null|| Atum.seed.isEmpty())?"Random Speedrun #" + Atum.rsgAttempts:"Set Speedrun #" + Atum.ssgAttempts);
        this.client.startGame(this.saveDirectoryName, levelNameField.getText().trim(), levelInfo);



    }
}
