package me.voidxwalker.autoreset.mixin.hotkey;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "prepareWorlds",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;getTimeMillis()J",ordinal = 0))
    public void trackWorldGenStart(CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.WORLD_GEN;
    }
}
