package me.voidxwalker.autoreset.mixin.hotkey;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.class_4070;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "method_20317",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/world/ServerWorld;method_3585()Lnet/minecraft/util/math/BlockPos;"))
    public void trackWorldGenStart(class_4070 arg, CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.WORLD_GEN;
    }
}
