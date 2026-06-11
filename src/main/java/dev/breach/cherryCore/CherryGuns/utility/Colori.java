package dev.breach.cherryCore.CherryGuns.utility;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Colori {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private Colori() {}

    public static String traduci(String testo) {
        if (testo == null) return "";
        Matcher matcher = HEX_PATTERN.matcher(testo);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.valueOf("#" + hex).toString());
        }
        matcher.appendTail(buffer);
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    public static List<String> traduci(Collection<String> righe) {
        if (righe == null) return new ArrayList<>();
        List<String> risultato = new ArrayList<>(righe.size());
        for (String r : righe) risultato.add(traduci(r));
        return risultato;
    }

    public static String rimuovi(String testo) {
        return testo == null ? "" : ChatColor.stripColor(testo);
    }
}