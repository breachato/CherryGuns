package dev.breach.cherryCore.CherryGuns.log;

import dev.breach.cherryCore.CherryGuns.armi.motore.ParteCorpo;
import dev.breach.cherryCore.CherryGuns.configurazione.Configurazione;
import dev.breach.cherryCore.CherryGuns.hook.HookLuckPerms;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class GestoreLog {

    private static GestoreLog istanza;

    private final JavaPlugin plugin;
    private final TrackerColpi tracker;

    private GestoreLog(JavaPlugin plugin) {
        this.plugin = plugin;
        long reset = Configurazione.get() != null ? Configurazione.get().getResetTrackerSecondi() : 5L;
        this.tracker = new TrackerColpi(reset);
    }

    public static void inizializza(JavaPlugin plugin) {
        if (istanza == null) istanza = new GestoreLog(plugin);
    }

    public static GestoreLog get() {
        return istanza;
    }

    public TrackerColpi getTracker() {
        return tracker;
    }

    public void inviaLogSparo(DatiSparo dati) {
        if (dati == null) return;
        tracker.registra(dati);

        String prefisso = dati.eTaser()
                ? ChatColor.GOLD + "[TASER] "
                : ChatColor.GREEN + "[SPARO] ";

        String verbo = dati.eTaser() ? "ha taserato" : "ha sparato a";

        String messaggio = prefisso + ChatColor.WHITE + dati.getTiratoreNome() + ChatColor.GRAY + " " + verbo + " "
                + ChatColor.WHITE + dati.getBersaglioNome();

        if (!dati.eTaser()) {
            messaggio += ChatColor.GRAY + " con " + ChatColor.AQUA + dati.getArmaNomeVisualizzato();
        }
        messaggio += ChatColor.GRAY + ".";

        TextComponent componente = costruisciComponenteCliccabile(messaggio, costruisciHoverSparo(dati), dati.getBersaglioUuid());
        broadcastAStaff(componente);
    }

    public void inviaLogMorteConArma(DatiSparo dati) {
        if (dati == null) return;

        String messaggio = ChatColor.DARK_RED + "[MORTE] " + ChatColor.WHITE + dati.getTiratoreNome()
                + ChatColor.GRAY + " ha ucciso " + ChatColor.WHITE + dati.getBersaglioNome()
                + ChatColor.GRAY + " con " + ChatColor.AQUA + dati.getArmaNomeVisualizzato()
                + ChatColor.GRAY + ".";

        TextComponent componente = costruisciComponenteCliccabile(messaggio, costruisciHoverMorte(dati), dati.getBersaglioUuid());
        broadcastAStaff(componente);
        tracker.rimuovi(dati.getBersaglioUuid());
    }

    public void inviaLogMorteGenerica(UUID vittimaUuid, String vittimaNome) {
        String messaggio = ChatColor.DARK_RED + "[MORTE] " + ChatColor.GRAY + "Il giocatore "
                + ChatColor.WHITE + vittimaNome + ChatColor.GRAY + " è morto.";

        TextComponent componente = new TextComponent(messaggio);
        broadcastAStaff(componente);
        tracker.rimuovi(vittimaUuid);
    }

    private Text costruisciHoverSparo(DatiSparo dati) {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.LIGHT_PURPLE).append(ChatColor.BOLD).append("INFORMAZIONI SPARO").append("\n");
        sb.append("\n");
        sb.append(ChatColor.GRAY).append("Arma: ").append(ChatColor.WHITE).append(dati.getArmaNomeVisualizzato()).append("\n");
        sb.append(ChatColor.GRAY).append("Parte colpita: ").append(ChatColor.WHITE).append(formatParteCorpo(dati.getParteCorpo())).append("\n");
        sb.append(ChatColor.GRAY).append("Danno: ").append(ChatColor.WHITE).append(arrotonda(dati.getDanno())).append("\n");
        sb.append("\n");
        sb.append(ChatColor.GRAY).append("Posizione ").append(dati.getTiratoreNome()).append(": ").append(ChatColor.WHITE).append(formatLoc(dati.getPosizioneTiratore())).append("\n");
        sb.append(ChatColor.GRAY).append("Posizione ").append(dati.getBersaglioNome()).append(": ").append(ChatColor.WHITE).append(formatLoc(dati.getPosizioneBersaglio())).append("\n");
        sb.append("\n");
        sb.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("CLICCA PER TELETRASPORTARTI");
        return new Text(sb.toString());
    }

    private Text costruisciHoverMorte(DatiSparo dati) {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append("INFORMAZIONI MORTE").append("\n");
        sb.append("\n");
        sb.append(ChatColor.GRAY).append("Ucciso da: ").append(ChatColor.WHITE).append(dati.getTiratoreNome()).append("\n");
        sb.append(ChatColor.GRAY).append("Arma: ").append(ChatColor.WHITE).append(dati.getArmaNomeVisualizzato()).append("\n");
        sb.append(ChatColor.GRAY).append("Colpo finale: ").append(ChatColor.WHITE).append(formatParteCorpo(dati.getParteCorpo())).append("\n");
        sb.append("\n");
        sb.append(ChatColor.GRAY).append("Posizione ").append(dati.getTiratoreNome()).append(": ").append(ChatColor.WHITE).append(formatLoc(dati.getPosizioneTiratore())).append("\n");
        sb.append(ChatColor.GRAY).append("Posizione ").append(dati.getBersaglioNome()).append(": ").append(ChatColor.WHITE).append(formatLoc(dati.getPosizioneBersaglio())).append("\n");
        sb.append("\n");
        sb.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("CLICCA PER TELETRASPORTARTI");
        return new Text(sb.toString());
    }

    private TextComponent costruisciComponenteCliccabile(String testo, Text hoverText, UUID bersaglio) {
        TextComponent comp = new TextComponent(testo);
        comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cherryguns_tp " + bersaglio));
        return comp;
    }

    private void broadcastAStaff(TextComponent componente) {
        String permesso = Configurazione.get() != null
                ? Configurazione.get().getPermessoLog()
                : "cherryguns.logs";

        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission(permesso)) {
                staff.spigot().sendMessage(componente);
            }
        }
    }

    public void teletrasportaAStaff(Player staff, UUID bersaglioUuid) {
        if (staff == null || bersaglioUuid == null) return;

        OfflinePlayer offline = Bukkit.getOfflinePlayer(bersaglioUuid);
        if (offline.isOnline() && offline.getPlayer() != null) {
            Player target = offline.getPlayer();
            garantisciVanishPoiTp(staff, target);
            return;
        }

        DatiSparo dati = tracker.getUltimoSparoVerso(bersaglioUuid);
        if (dati != null && dati.getPosizioneBersaglio() != null) {
            if (HookLuckPerms.get() != null && HookLuckPerms.get().eDisponibile()) {
                HookLuckPerms.get().garantisciVanish(staff);
            }
            staff.teleport(dati.getPosizioneBersaglio());
        } else {
            staff.sendMessage(ChatColor.RED + "Impossibile teletrasportarti: posizione non più disponibile.");
        }
    }

    private void garantisciVanishPoiTp(Player staff, Player target) {
        if (HookLuckPerms.get() != null && HookLuckPerms.get().eDisponibile()) {
            HookLuckPerms.get().garantisciVanish(staff);
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tp " + staff.getName() + " " + target.getName());
        }, 5L);
    }

    private String formatLoc(Location loc) {
        if (loc == null) return "?, ?, ?";
        return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }

    private String formatParteCorpo(ParteCorpo parte) {
        return parte != null ? parte.getNomeVisualizzato() : "Corpo";
    }

    private String arrotonda(double valore) {
        return String.format("%.1f", valore);
    }
}