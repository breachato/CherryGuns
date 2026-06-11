package dev.breach.cherryCore.CherryGuns.hook;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class HookLuckPerms {

    private static HookLuckPerms istanza;
    private final JavaPlugin plugin;
    private LuckPerms api;
    private boolean disponibile = false;

    private HookLuckPerms(JavaPlugin plugin) {
        this.plugin = plugin;
        inizializza();
    }

    public static void registra(JavaPlugin plugin) {
        if (istanza == null) istanza = new HookLuckPerms(plugin);
    }

    public static HookLuckPerms get() {
        return istanza;
    }

    private void inizializza() {
        if (!Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            plugin.getLogger().warning("LuckPerms non trovato. Hook disabilitato.");
            return;
        }
        try {
            api = LuckPermsProvider.get();
            disponibile = true;
            plugin.getLogger().info("Hook LuckPerms registrato correttamente.");
        } catch (Exception e) {
            plugin.getLogger().warning("Impossibile registrare hook LuckPerms: " + e.getMessage());
        }
    }

    public boolean eDisponibile() {
        return disponibile && api != null;
    }

    public boolean haPermesso(Player player, String permesso) {
        if (!eDisponibile() || player == null || permesso == null) return false;
        User user = api.getUserManager().getUser(player.getUniqueId());
        if (user == null) return false;
        return user.getCachedData().getPermissionData().checkPermission(permesso).asBoolean();
    }

    public void garantisciVanish(Player staff) {
        if (!eDisponibile() || staff == null) return;
        if (haPermesso(staff, "vanish.on")) return;

        UUID uuid = staff.getUniqueId();
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> future = userManager.loadUser(uuid);

        future.thenAcceptAsync(user -> {
            if (user == null) return;
            Node nodo = Node.builder("vanish.on").build();
            user.data().add(nodo);
            userManager.saveUser(user);
        });
    }
}