package emanondev.itemtag.compability;

import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.actions.ActionsUtility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Immutable held-item data for callers outside the player's region. */
public final class HandSnapshotCache {
    private static final Map<UUID, HandSnapshot> snapshots = new ConcurrentHashMap<>();
    private static final HandSnapshot EMPTY = new HandSnapshot(null, 0);

    private HandSnapshotCache() {
    }

    public record HandSnapshot(String cooldownId, int uses) {
    }

    public static HandSnapshot update(Player player, ItemStack item) {
        HandSnapshot snapshot = EMPTY;
        if (!ItemUtils.isAirOrNull(item)) {
            TagItem tags = ItemTag.getTagItem(item);
            snapshot = new HandSnapshot(ActionsUtility.getCooldownId(tags), ActionsUtility.getUses(tags));
        }
        snapshots.put(player.getUniqueId(), snapshot);
        return snapshot;
    }

    public static HandSnapshot get(Player player) {
        return snapshots.getOrDefault(player.getUniqueId(), EMPTY);
    }

    public static void remove(Player player) {
        snapshots.remove(player.getUniqueId());
    }

    public static void clear() {
        snapshots.clear();
    }
}
