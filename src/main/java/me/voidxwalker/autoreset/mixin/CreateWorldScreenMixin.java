package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.world.level.LevelGeneratorOptions;
import net.minecraft.world.level.LevelGeneratorType;
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
public abstract class CreateWorldScreenMixin {
    @Shadow public boolean hardcore;
    @Shadow private TextFieldWidget levelNameField;
    @Shadow protected abstract void createLevel();


    @Shadow private int generatorType;

    @Shadow public LevelGeneratorOptions generatorOptions;

    @Shadow protected abstract LevelGeneratorType getLevelGeneratorType();

    @Shadow private boolean structures;

    @Shadow private boolean bonusChest;

    @Inject(method = "init", at = @At("TAIL"))
    private void createDesiredWorld(CallbackInfo info) {
        if (Atum.isRunning) {
            if(Atum.difficulty==-1){
                hardcore=true;
            }
            if(Atum.seed==null|| Atum.seed.isEmpty()){
                Atum.rsgAttempts++;
            }
            else {
                Atum.ssgAttempts++;
            }
            Atum.saveProperties();
            levelNameField.setText((Atum.seed==null|| Atum.seed.isEmpty())?"Random Speedrun #" + Atum.rsgAttempts:"Set Speedrun #" + Atum.ssgAttempts);
            setGeneratorType(Atum.generatorType);
            setGenerateStructure(Atum.structures);
            setGenerateBonusChest(Atum.bonusChest);
            Atum.loopPrevent=true;
            createLevel();
        }
    }
    @Redirect(method = "createLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;getText()Ljava/lang/String;",ordinal = 0))
    private String injected(TextFieldWidget instance) {
        if(!Atum.isRunning){
            return instance.getText();
        }
        long l = (new Random()).nextLong();
        String string = Atum.seed==null?"": Atum.seed;
        if (!StringUtils.isEmpty(string)) {
            try {
                long m = Long.parseLong(string);
                if (m != 0L) {
                    l = m;
                }
            } catch (NumberFormatException var6) {
                l = string.hashCode();
            }
        }

        Atum.log(Level.INFO,(Atum.seed==null|| Atum.seed.isEmpty()?"Resetting a random seed":"Resetting the set seed"+"\""+l+"\""));
        return ""+l;
    }
    public void setGeneratorType(int generatorType){
        this.generatorType=generatorType;
        this.generatorOptions = this.getLevelGeneratorType().getDefaultOptions();
    }
    public void setGenerateStructure(boolean generate){
        this.structures=generate;
    }
    public void setGenerateBonusChest(boolean generate){
        this.bonusChest=generate;
    }
}
