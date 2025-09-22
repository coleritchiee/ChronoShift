package net.iicosahedra.chronoshift.trait;

import net.iicosahedra.chronoshift.item.watch.WatchHelper;
import net.iicosahedra.chronoshift.item.watch.WatchState;
import net.iicosahedra.chronoshift.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class TraitHelper {
    public static ServerPlayer findGreatestWatchHolder(Level level, BlockPos pos, double range) {
        if (!(level instanceof ServerLevel sl)) return null;

        Vec3 c = Vec3.atCenterOf(pos);
        AABB box = new AABB(c, c).inflate(range);

        List<ServerPlayer> candidates = sl.getEntitiesOfClass(ServerPlayer.class, box, p ->
                WatchHelper.getWatchFromSlot(p, Registration.TIME_WATCH.value())
                        .map(s -> s.get(Registration.WATCH_STATE.value()))
                        .map(ws -> ws.tier() >= 0)
                        .orElse(false)
        );
        if (candidates.isEmpty()) return null;

        return candidates.stream()
                .max(Comparator
                        .<ServerPlayer>comparingInt(TraitHelper::watchTierOf)
                        .thenComparingDouble(p -> -p.distanceToSqr(c.x, c.y, c.z))
                )
                .orElse(null);
    }

    private static int watchTierOf(ServerPlayer p) {
        return WatchHelper.getWatchFromSlot(p, Registration.TIME_WATCH.value())
                .map(s -> s.get(Registration.WATCH_STATE.value()))
                .map(WatchState::tier)
                .orElse(-1);
    }
}
