package dev.breach.cherryCore.CherryGuns.armi.accesso;

import dev.breach.cherryCore.CherryGuns.log.GestoreLog;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class ComandoTeletrasporto implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Solo i player possono usare questo comando.");
            return true;
        }
        if (!sender.hasPermission("cherryguns.logs")) {
            sender.sendMessage(ChatColor.RED + "Non hai il permesso.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Uso interno: UUID mancante.");
            return true;
        }

        UUID target;
        try {
            target = UUID.fromString(args[0]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "UUID non valido.");
            return true;
        }

        Player staff = (Player) sender;
        if (GestoreLog.get() != null) {
            GestoreLog.get().teletrasportaAStaff(staff, target);
        }
        return true;
    }
}