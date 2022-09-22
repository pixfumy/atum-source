package me.voidxwalker.autoreset;

import net.minecraft.world.gen.WorldPreset;

public interface IMoreOptionsDialog {
    void setGeneratorType(WorldPreset g);
    void setGenerateStructure(boolean generate);
    void setGenerateBonusChest(boolean generate);
}
