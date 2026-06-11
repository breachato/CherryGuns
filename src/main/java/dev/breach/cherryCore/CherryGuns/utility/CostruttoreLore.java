package dev.breach.cherryCore.CherryGuns.utility;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CostruttoreLore {

    private final List<String> righe = new ArrayList<>();

    private CostruttoreLore() {}

    public static CostruttoreLore crea() {
        return new CostruttoreLore();
    }

    public CostruttoreLore riga(String testo) {
        righe.add(Colori.traduci(testo));
        return this;
    }

    public CostruttoreLore righe(String... testi) {
        if (testi != null) for (String t : testi) riga(t);
        return this;
    }

    public CostruttoreLore righe(Collection<String> testi) {
        if (testi != null) for (String t : testi) riga(t);
        return this;
    }

    public CostruttoreLore vuota() {
        righe.add("");
        return this;
    }

    public CostruttoreLore separatore() {
        righe.add(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                              ");
        return this;
    }

    public CostruttoreLore chiaveValore(String chiave, Object valore) {
        righe.add(ChatColor.GRAY + chiave + ": " + ChatColor.WHITE + valore);
        return this;
    }

    public CostruttoreLore chiaveValore(String chiave, Object valore, ChatColor coloreValore) {
        righe.add(ChatColor.GRAY + chiave + ": " + coloreValore + valore);
        return this;
    }

    public CostruttoreLore se(boolean condizione, String testo) {
        if (condizione) riga(testo);
        return this;
    }

    public List<String> costruisci() {
        return new ArrayList<>(righe);
    }

    public int dimensione() {
        return righe.size();
    }
}