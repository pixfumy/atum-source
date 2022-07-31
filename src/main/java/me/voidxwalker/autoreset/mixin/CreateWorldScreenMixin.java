package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;


@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {
    @Shadow private TextFieldWidget levelNameField;
    @Shadow protected abstract void createLevel();


    @Shadow public CompoundTag generatorOptionsTag;

    @Shadow private int generatorType;

    @Shadow private boolean structures;

    @Shadow private boolean bonusChest;

    @Shadow private boolean field_3178;

    @Inject(method = "init", at = @At("TAIL"))
    private void createDesiredWorld(CallbackInfo info) {
        if (Atum.isRunning) {
            if(Atum.difficulty==-1){
                this.field_3178=true;
            }
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
        if(Atum.seed==null|| Atum.seed.isEmpty()){
            Atum.rsgAttempts++;
        }
        else {
            Atum.ssgAttempts++;
        }
        setGeneratorType(Atum.generatorType);
        setGenerateStructure(Atum.structures);
        setGenerateBonusChest(Atum.bonusChest);
        Atum.saveProperties();
        levelNameField.setText((Atum.seed==null|| Atum.seed.isEmpty())?"Random Speedrun #" + Atum.rsgAttempts:"Set Speedrun #" + Atum.ssgAttempts);
        return ""+l;
    }
    public void setGeneratorType(int generatorType){
        this.generatorOptionsTag = new CompoundTag();
        this.generatorType=generatorType;
    }
    public void setGenerateStructure(boolean generate){
        this.structures=generate;
    }
    public void setGenerateBonusChest(boolean generate){
        this.bonusChest=generate;
    }
    @Inject(method = "createLevel",at = @At("HEAD"))
    public void atum_trackResetting(CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.RESETTING;
    }
}
