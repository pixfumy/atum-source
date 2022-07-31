package me.voidxwalker.autoreset.mixin.hotkey;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "prepareStartRegion",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/world/ServerWorld;getSpawnPos()Lnet/minecraft/util/math/BlockPos;"))
    public void trackWorldGenStart(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.WORLD_GEN;
    }
    @Inject(method="runServer",at=@At(value="INVOKE",target="Lnet/minecraft/server/MinecraftServer;setupServer()Z",shift = At.Shift.AFTER), cancellable = true)
    public void worldpreview_kill2(CallbackInfo ci){
        Atum.hotkeyState= Atum.HotkeyState.POST_WORLDGEN;
    }
}
