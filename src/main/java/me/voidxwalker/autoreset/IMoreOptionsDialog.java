package me.voidxwalker.autoreset;

import net.minecraft.client.world.GeneratorType;

public interface IMoreOptionsDialog {
    void setGeneratorType(GeneratorType g);
    void setGenerateStructure(boolean generate);
    void setGenerateBonusChest(boolean generate);
}
