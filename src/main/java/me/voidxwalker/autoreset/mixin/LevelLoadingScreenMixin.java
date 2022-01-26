package me.voidxwalker.autoreset.mixin;

import me.voidxwalker.autoreset.Main;
import me.voidxwalker.autoreset.Pingable;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin extends Screen implements Pingable {
    protected LevelLoadingScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void modifyString(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if(Main.isRunning&&Main.seed!=null&&!Main.seed.isEmpty()){
            String string =Main.seed;
            drawCenteredString(matrices, this.textRenderer, string, this.width / 2, this.height / 2 - 9 / 2 - 50, 16777215);
        }

    }
    @Override
    public boolean ping() {
        return true;
    }
}
