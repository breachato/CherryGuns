package dev.breach.cherryCore.CherryGuns.armi.accesso;

import dev.breach.cherryCore.CherryGuns.api.CherryAPI;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import dev.breach.cherryCore.CherryGuns.armi.base.Registro;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import dev.breach.cherryCore.CherryGuns.configurazione.Configurazione;
import dev.breach.cherryCore.CherryGuns.interfaccia.GestoreInterfaccia;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class ComandoArmi implements CommandExecutor, TabCompleter {

    private static final List<String> SOTTOCOMANDI = Arrays.asList(
            "gui", "prendi", "aggiungi", "rimuovi", "cerca", "whitelist", "blacklist", "reload"
    );
    private static final List<String> SOTTO_LISTE = Arrays.asList("aggiungi", "rimuovi", "lista");

    private final JavaPlugin plugin;

    public ComandoArmi(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            inviaHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "gui": return handleGui(sender);
            case "prendi": return handlePrendi(sender, args);
            case "aggiungi": return handleAggiungi(sender, args);
            case "rimuovi": return handleRimuovi(sender, args);
            case "cerca": return handleCerca(sender, args);
            case "whitelist": return handleAccesso(sender, args, true);
            case "blacklist": return handleAccesso(sender, args, false);
            case "reload": return handleReload(sender);
            default:
                inviaHelp(sender);
                return true;
        }
    }

    private String prefisso() {
        return Configurazione.get() != null
                ? Colori.traduci(Configurazione.get().getPrefisso()) + " "
                : ChatColor.LIGHT_PURPLE + "[CherryGuns] ";
    }

    private boolean handleGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Solo i player possono aprire la GUI.");
            return true;
        }
        if (!sender.hasPermission("cherryguns.armi.gui")) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Non hai il permesso.");
            return true;
        }
        if (GestoreInterfaccia.get() != null) {
            GestoreInterfaccia.get().apriMenuArmi((Player) sender);
        }
        return true;
    }

    private boolean handlePrendi(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cherryguns.armi.prendi")) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Non hai il permesso.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(prefisso() + ChatColor.YELLOW + "Uso: /armi prendi <arma> [player]");
            return true;
        }

        OggettoBase oggetto = Registro.get().getPerNome(args[1]);
        if (oggetto == null) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Arma \"" + args[1] + "\" non trovata.");
            return true;
        }

        Player destinatario;
        if (args.length >= 3) {
            destinatario = Bukkit.getPlayer(args[2]);
        } else if (sender instanceof Player) {
            destinatario = (Player) sender;
        } else {
            sender.sendMessage(prefisso() + ChatColor.RED + "Specifica un player dalla console.");
            return true;
        }

        if (destinatario == null || !destinatario.isOnline()) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Player non online.");
            return true;
        }

        CherryAPI.dai(destinatario, oggetto);
        sender.sendMessage(prefisso() + ChatColor.GREEN + "Hai dato " + ChatColor.WHITE + oggetto.getNomeVisualizzato()
                + ChatColor.GREEN + " a " + ChatColor.WHITE + destinatario.getName());

        if (sender != destinatario) {
            destinatario.sendMessage(prefisso() + ChatColor.GREEN + "Hai ricevuto: " + ChatColor.WHITE + oggetto.getNomeVisualizzato());
        }
        return true;
    }

    private boolean handleAggiungi(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cherryguns.armi.aggiungi")) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Non hai il permesso.");
            return true;
        }
        if (args.length < 7) {
            sender.sendMessage(prefisso() + ChatColor.YELLOW + "Uso: /armi aggiungi <nome> <displayname> <materiale> <munizione> <danno> <caricatore>");
            sender.sendMessage(ChatColor.GRAY + "Esempio: /armi aggiungi beretta &cBeretta&fPX4 CROSSBOW 9mm 3 12");
            return true;
        }

        String nome = args[1];
        String displayName = args[2];
        String materiale = args[3];
        String munizione = args[4];

        int danno, caricatore;
        try {
            danno = Integer.parseInt(args[5]);
            caricatore = Integer.parseInt(args[6]);
        } catch (NumberFormatException e) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Danno e caricatore devono essere numeri.");
            return true;
        }

        CreatoreArmi.Esito esito = CreatoreArmi.creaArma(plugin, nome, displayName, materiale, munizione, danno, caricatore);
        switch (esito) {
            case SUCCESSO:
                sender.sendMessage(prefisso() + ChatColor.GREEN + "Arma \"" + nome + "\" creata con successo.");
                sender.sendMessage(ChatColor.GRAY + "File: plugins/CherryGuns/armi_data/" + nome + ".yml");
                break;
            case GIA_ESISTENTE:
                sender.sendMessage(prefisso() + ChatColor.RED + "Esiste già un'arma con quel nome.");
                break;
            case MATERIALE_INVALIDO:
                sender.sendMessage(prefisso() + ChatColor.RED + "Materiale \"" + materiale + "\" non valido.");
                break;
            case MUNIZIONE_INVALIDA:
                sender.sendMessage(prefisso() + ChatColor.RED + "Munizione \"" + munizione + "\" non trovata.");
                break;
            default:
                sender.sendMessage(prefisso() + ChatColor.RED + "Errore durante la creazione dell'arma.");
                break;
        }
        return true;
    }

    private boolean handleRimuovi(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cherryguns.armi.rimuovi")) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Non hai il permesso.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(prefisso() + ChatColor.YELLOW + "Uso: /armi rimuovi <arma>");
            return true;
        }

        String nome = args[1];
        if (Registro.get().getPerNome(nome) == null) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Arma \"" + nome + "\" non trovata.");
            return true;
        }

        if (CreatoreArmi.eliminaArma(plugin, nome)) {
            sender.sendMessage(prefisso() + ChatColor.GREEN + "Arma \"" + nome + "\" eliminata. Usa /armi reload.");
        } else {
            sender.sendMessage(prefisso() + ChatColor.RED + "Impossibile eliminare l'arma.");
        }
        return true;
    }

    private boolean handleCerca(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cherryguns.armi.cerca")) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Non hai il permesso.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(prefisso() + ChatColor.YELLOW + "Uso: /armi cerca <nome|parziale>");
            return true;
        }

        String query = args[1].toLowerCase();
        List<OggettoBase> risultati = new ArrayList<>();
        for (OggettoBase o : Registro.get().tutteLeArmi()) {
            if (o.getNome().toLowerCase().contains(query)
                    || ChatColor.stripColor(o.getNomeVisualizzato()).toLowerCase().contains(query)) {
                risultati.add(o);
            }
        }

        if (risultati.isEmpty()) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Nessuna arma trovata per \"" + args[1] + "\".");
            return true;
        }

        sender.sendMessage(prefisso() + ChatColor.GREEN + "Risultati (" + risultati.size() + "):");
        for (OggettoBase o : risultati) {
            sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + o.getNome()
                    + ChatColor.GRAY + " (" + o.getNomeVisualizzato() + ChatColor.GRAY + ") "
                    + ChatColor.DARK_GRAY + "[" + o.getIdentificatore().getMateriale().name()
                    + ":" + o.getIdentificatore().getCustomModelData() + "]");
        }
        return true;
    }

    private boolean handleAccesso(CommandSender sender, String[] args, boolean whitelist) {
        String permesso = whitelist ? "cherryguns.armi.whitelist" : "cherryguns.armi.blacklist";
        String etichetta = whitelist ? "Whitelist" : "Blacklist";

        if (!sender.hasPermission(permesso)) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Non hai il permesso.");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(prefisso() + ChatColor.YELLOW + "Uso: /armi " + (whitelist ? "whitelist" : "blacklist") + " <aggiungi|rimuovi|lista> <arma> [player]");
            return true;
        }

        String azione = args[1].toLowerCase();
        String nomeArma = args[2];

        if (Registro.get().getPerNome(nomeArma) == null) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Arma \"" + nomeArma + "\" non trovata.");
            return true;
        }

        GestoreAccessi manager = GestoreAccessi.get();

        switch (azione) {
            case "aggiungi": {
                if (args.length < 4) {
                    sender.sendMessage(prefisso() + ChatColor.YELLOW + "Specifica il player.");
                    return true;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[3]);
                if (target.getUniqueId() == null) {
                    sender.sendMessage(prefisso() + ChatColor.RED + "Player non trovato.");
                    return true;
                }
                boolean aggiunto = whitelist
                        ? manager.aggiungiAWhitelist(target.getUniqueId(), nomeArma)
                        : manager.aggiungiABlacklist(target.getUniqueId(), nomeArma);
                if (aggiunto) {
                    sender.sendMessage(prefisso() + ChatColor.GREEN + args[3] + " aggiunto alla " + etichetta + " di " + nomeArma);
                } else {
                    sender.sendMessage(prefisso() + ChatColor.YELLOW + args[3] + " era già nella " + etichetta + ".");
                }
                return true;
            }
            case "rimuovi": {
                if (args.length < 4) {
                    sender.sendMessage(prefisso() + ChatColor.YELLOW + "Specifica il player.");
                    return true;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[3]);
                if (target.getUniqueId() == null) {
                    sender.sendMessage(prefisso() + ChatColor.RED + "Player non trovato.");
                    return true;
                }
                boolean rimosso = whitelist
                        ? manager.rimuoviDaWhitelist(target.getUniqueId(), nomeArma)
                        : manager.rimuoviDaBlacklist(target.getUniqueId(), nomeArma);
                if (rimosso) {
                    sender.sendMessage(prefisso() + ChatColor.GREEN + args[3] + " rimosso dalla " + etichetta + " di " + nomeArma);
                } else {
                    sender.sendMessage(prefisso() + ChatColor.YELLOW + args[3] + " non era nella " + etichetta + ".");
                }
                return true;
            }
            case "lista": {
                Set<UUID> set = whitelist ? manager.getWhitelist(nomeArma) : manager.getBlacklist(nomeArma);
                if (set.isEmpty()) {
                    sender.sendMessage(prefisso() + ChatColor.YELLOW + etichetta + " di " + nomeArma + " è vuota.");
                    return true;
                }
                sender.sendMessage(prefisso() + ChatColor.GREEN + etichetta + " di " + nomeArma + " (" + set.size() + "):");
                for (UUID uuid : set) {
                    OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
                    String nome = op.getName() != null ? op.getName() : uuid.toString();
                    sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + nome);
                }
                return true;
            }
            default:
                sender.sendMessage(prefisso() + ChatColor.RED + "Sotto-comando non valido. Usa: aggiungi, rimuovi, lista");
                return true;
        }
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("cherryguns.reload")) {
            sender.sendMessage(prefisso() + ChatColor.RED + "Non hai il permesso.");
            return true;
        }

        if (Configurazione.get() != null) Configurazione.get().ricarica();
        if (GestoreAccessi.get() != null) GestoreAccessi.get().ricarica();

        sender.sendMessage(prefisso() + ChatColor.GREEN + "Configurazione ricaricata.");
        return true;
    }

    private void inviaHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(prefisso() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Comandi disponibili:");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "/armi gui " + ChatColor.GRAY + "- Apre la GUI delle armi");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "/armi prendi <arma> [player] " + ChatColor.GRAY + "- Dà un'arma");

        if (sender.hasPermission("cherryguns.armi.aggiungi"))
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "/armi aggiungi <nome> <displayname> <materiale> <munizione> <danno> <car>");
        if (sender.hasPermission("cherryguns.armi.rimuovi"))
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "/armi rimuovi <arma> " + ChatColor.GRAY + "- Elimina arma");
        if (sender.hasPermission("cherryguns.armi.cerca"))
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "/armi cerca <nome> " + ChatColor.GRAY + "- Cerca arma");
        if (sender.hasPermission("cherryguns.armi.whitelist"))
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "/armi whitelist <aggiungi|rimuovi|lista> <arma> [player]");
        if (sender.hasPermission("cherryguns.armi.blacklist"))
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "/armi blacklist <aggiungi|rimuovi|lista> <arma> [player]");
        if (sender.hasPermission("cherryguns.reload"))
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "/armi reload " + ChatColor.GRAY + "- Ricarica config");

        sender.sendMessage("");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return SOTTOCOMANDI.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        String sub = args[0].toLowerCase();

        if (args.length == 2) {
            switch (sub) {
                case "prendi":
                case "rimuovi":
                case "cerca":
                    return nomiArmi(args[1]);
                case "whitelist":
                case "blacklist":
                    return SOTTO_LISTE.stream().filter(s -> s.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
            }
        }

        if (args.length == 3) {
            switch (sub) {
                case "prendi": return nomiOnline(args[2]);
                case "whitelist":
                case "blacklist": return nomiArmi(args[2]);
            }
        }

        if (args.length == 4 && (sub.equals("whitelist") || sub.equals("blacklist"))) {
            if (args[1].equalsIgnoreCase("aggiungi") || args[1].equalsIgnoreCase("rimuovi"))
                return nomiOnline(args[3]);
        }

        if (sub.equals("aggiungi")) {
            if (args.length == 4) {
                return Arrays.stream(Material.values())
                        .map(Material::name)
                        .filter(s -> s.toLowerCase().startsWith(args[3].toLowerCase()))
                        .limit(30)
                        .collect(Collectors.toList());
            }
            if (args.length == 5) {
                return Registro.get().tutteLeMunizioni().stream()
                        .map(OggettoBase::getNome)
                        .filter(s -> s.toLowerCase().startsWith(args[4].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        return new ArrayList<>();
    }

    private List<String> nomiArmi(String prefix) {
        return Registro.get().tutteLeArmi().stream()
                .map(OggettoBase::getNome)
                .filter(n -> n.toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<String> nomiOnline(String prefix) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(n -> n.toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
}