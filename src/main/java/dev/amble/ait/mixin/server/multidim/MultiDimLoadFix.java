package dev.amble.ait.mixin.server.multidim;

import dev.amble.lib.data.CachedDirectedGlobalPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import dev.amble.ait.core.tardis.ServerTardis;
import dev.amble.ait.core.tardis.manager.ServerTardisManager;
import dev.amble.ait.core.world.TardisServerWorld;

import java.util.Optional;

@Mixin(PlayerManager.class)
public class MultiDimLoadFix {

    @Redirect(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getWorld(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/server/world/ServerWorld;"))
    public @Nullable ServerWorld getWorld(MinecraftServer instance, RegistryKey<World> key) {
        ServerWorld result = instance.getWorld(key);

        if (result != null)
            return result;

        return ait$loadTardisFromWorld(instance, key);
    }

    public ServerWorld ait$loadTardisFromWorld(MinecraftServer server, RegistryKey<World> key) {
        if (!TardisServerWorld.isTardisDimension(key))
            return null;

        Optional<ServerTardis> maybeTardis = ServerTardisManager.getInstance()
                .loadTardis(server, TardisServerWorld.getTardisId(key)).left();

        if (maybeTardis.isEmpty())
            return null;

        ServerTardis tardis = maybeTardis.get();
        CachedDirectedGlobalPos pos = tardis.travel().position();

        ServerWorld targetWorld = this.ait$loadTardisFromWorld(
                server, pos.getDimension());

        if (targetWorld != null)
            pos.world(targetWorld);

        return tardis.world();
    }
}
