package emanondev.itemtag.scheduler;

import emanondev.itemtag.ItemTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/** Player callbacks follow the player when they move between Folia regions. */
public final class ItemTagTasks {
    private ItemTagTasks() {
    }

    public static void playerLater(Plugin plugin, Player player, Runnable action, long ticks) {
        if (!plugin.isEnabled()) return;
        player.getScheduler().runDelayed(plugin, task -> {
            if (plugin.isEnabled() && player.isOnline()) action.run();
        }, null, Math.max(1L, ticks));
    }

    public static boolean consoleCommand(String command) {
        ItemTag plugin = ItemTag.get();
        if (!plugin.isEnabled()) return false;
        if (Bukkit.isGlobalTickThread()) {
            return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        Bukkit.getGlobalRegionScheduler().execute(plugin, () -> {
            if (plugin.isEnabled()) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
        return true;
    }
}
