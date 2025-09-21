package net.iicosahedra.chronoshift.item.watch;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Optional;

public class WatchHelper {
    public static Optional<ItemStack> getWatchFromSlot(Player player) {
        return CuriosApi.getCuriosInventory(player).flatMap(inv ->
                inv.getStacksHandler("watch").flatMap(slotInv -> {
                    for (int i = 0; i < slotInv.getSlots(); i++) {
                        ItemStack s = slotInv.getStacks().getStackInSlot(i);
                        if (!s.isEmpty()) return Optional.of(s);
                    }
                    return Optional.empty();
                })
        );
    }

    public static Optional<ItemStack> getWatchFromSlot(Player player, Item timeWatchItem) {
        return getWatchFromSlot(player).filter(s -> s.is(timeWatchItem));
    }
}
