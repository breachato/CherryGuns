package dev.breach.cherryCore.CherryGuns.armi.accesso;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class GestoreAccessi {

    private static GestoreAccessi istanza;

    private final JavaPlugin plugin;
    private final File cartellaBase;
    private final Map<String, Set<UUID>> whitelistCache = new ConcurrentHashMap<>();
    private final Map<String, Set<UUID>> blacklistCache = new ConcurrentHashMap<>();

    private GestoreAccessi(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cartellaBase = new File(plugin.getDataFolder(), "accessi");
        if (!cartellaBase.exists()) cartellaBase.mkdirs();
        caricaTutto();
    }

    public static void inizializza(JavaPlugin plugin) {
        if (istanza == null) istanza = new GestoreAccessi(plugin);
    }

    public static GestoreAccessi get() {
        return istanza;
    }

    public void ricarica() {
        whitelistCache.clear();
        blacklistCache.clear();
        caricaTutto();
    }

    private void caricaTutto() {
        File[] cartelle = cartellaBase.listFiles(File::isDirectory);
        if (cartelle == null) return;

        for (File c : cartelle) {
            String nomeArma = c.getName().toLowerCase();
            caricaLista(nomeArma, true);
            caricaLista(nomeArma, false);
        }
    }

    private void caricaLista(String nomeArma, boolean whitelist) {
        File file = new File(new File(cartellaBase, nomeArma), whitelist ? "whitelist.yml" : "blacklist.yml");
        Set<UUID> set = new HashSet<>();

        if (file.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            List<String> raw = cfg.getStringList("giocatori");
            for (String s : raw) {
                try {
                    set.add(UUID.fromString(s));
                } catch (IllegalArgumentException ignored) {}
            }
        }

        if (whitelist) whitelistCache.put(nomeArma, set);
        else blacklistCache.put(nomeArma, set);
    }

    private void salvaLista(String nomeArma, boolean whitelist) {
        String nome = nomeArma.toLowerCase();
        File cartellaArma = new File(cartellaBase, nome);
        if (!cartellaArma.exists()) cartellaArma.mkdirs();

        File file = new File(cartellaArma, whitelist ? "whitelist.yml" : "blacklist.yml");
        FileConfiguration cfg = new YamlConfiguration();
        Set<UUID> set = whitelist ? whitelistCache.get(nome) : blacklistCache.get(nome);
        if (set == null) set = new HashSet<>();

        cfg.set("giocatori", set.stream().map(UUID::toString).collect(Collectors.toList()));

        try {
            cfg.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Errore salvataggio " + file.getName() + " per " + nome + ": " + e.getMessage());
        }
    }

    public boolean puoUsare(Player player, String nomeArma) {
        if (player == null || nomeArma == null) return true;
        if (player.hasPermission("cherryguns.armi.bypass")) return true;

        UUID uuid = player.getUniqueId();
        String nome = nomeArma.toLowerCase();

        Set<UUID> blacklist = blacklistCache.get(nome);
        if (blacklist != null && blacklist.contains(uuid)) return false;

        Set<UUID> whitelist = whitelistCache.get(nome);
        if (whitelist != null && !whitelist.isEmpty()) return whitelist.contains(uuid);

        return true;
    }

    public boolean aggiungiAWhitelist(UUID uuid, String nomeArma) {
        if (uuid == null || nomeArma == null) return false;
        String nome = nomeArma.toLowerCase();
        whitelistCache.computeIfAbsent(nome, k -> new HashSet<>());
        boolean aggiunto = whitelistCache.get(nome).add(uuid);
        if (aggiunto) salvaLista(nome, true);
        return aggiunto;
    }

    public boolean rimuoviDaWhitelist(UUID uuid, String nomeArma) {
        if (uuid == null || nomeArma == null) return false;
        String nome = nomeArma.toLowerCase();
        Set<UUID> set = whitelistCache.get(nome);
        if (set == null) return false;
        boolean rimosso = set.remove(uuid);
        if (rimosso) salvaLista(nome, true);
        return rimosso;
    }

    public boolean aggiungiABlacklist(UUID uuid, String nomeArma) {
        if (uuid == null || nomeArma == null) return false;
        String nome = nomeArma.toLowerCase();
        blacklistCache.computeIfAbsent(nome, k -> new HashSet<>());
        boolean aggiunto = blacklistCache.get(nome).add(uuid);
        if (aggiunto) salvaLista(nome, false);
        return aggiunto;
    }

    public boolean rimuoviDaBlacklist(UUID uuid, String nomeArma) {
        if (uuid == null || nomeArma == null) return false;
        String nome = nomeArma.toLowerCase();
        Set<UUID> set = blacklistCache.get(nome);
        if (set == null) return false;
        boolean rimosso = set.remove(uuid);
        if (rimosso) salvaLista(nome, false);
        return rimosso;
    }

    public Set<UUID> getWhitelist(String nomeArma) {
        if (nomeArma == null) return new HashSet<>();
        return new HashSet<>(whitelistCache.getOrDefault(nomeArma.toLowerCase(), new HashSet<>()));
    }

    public Set<UUID> getBlacklist(String nomeArma) {
        if (nomeArma == null) return new HashSet<>();
        return new HashSet<>(blacklistCache.getOrDefault(nomeArma.toLowerCase(), new HashSet<>()));
    }

    public void eliminaPerArma(String nomeArma) {
        if (nomeArma == null) return;
        String nome = nomeArma.toLowerCase();
        whitelistCache.remove(nome);
        blacklistCache.remove(nome);
        File cartella = new File(cartellaBase, nome);
        if (cartella.exists()) {
            File[] files = cartella.listFiles();
            if (files != null) for (File f : files) f.delete();
            cartella.delete();
        }
    }
}