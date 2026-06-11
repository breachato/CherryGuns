package dev.breach.cherryCore.CherryGuns.armi.motore;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public final class RilevatoreParteCorpo {

    private RilevatoreParteCorpo() {}

    public static ParteCorpo rileva(LivingEntity bersaglio, Location puntoImpatto) {
        if (bersaglio == null || puntoImpatto == null) return ParteCorpo.CORPO;

        BoundingBox box = bersaglio.getBoundingBox();
        double altezzaEntita = box.getHeight();
        double yRelativa = puntoImpatto.getY() - box.getMinY();
        double percentuale = yRelativa / altezzaEntita;

        if (bersaglio instanceof Player) {
            if (percentuale >= 0.85) return ParteCorpo.TESTA;
            if (percentuale >= 0.45) return ParteCorpo.CORPO;
            if (percentuale >= 0.30) return determinaCorpoOBraccia(bersaglio, puntoImpatto);
            return ParteCorpo.GAMBE;
        }

        if (percentuale >= 0.80) return ParteCorpo.TESTA;
        if (percentuale >= 0.35) return ParteCorpo.CORPO;
        return ParteCorpo.GAMBE;
    }

    private static ParteCorpo determinaCorpoOBraccia(LivingEntity bersaglio, Location puntoImpatto) {
        Location centro = bersaglio.getLocation();
        Vector direzione = bersaglio.getEyeLocation().getDirection().setY(0).normalize();
        Vector lato = new Vector(-direzione.getZ(), 0, direzione.getX());

        Vector offset = puntoImpatto.toVector().subtract(centro.toVector());
        double distanzaLaterale = Math.abs(offset.dot(lato));

        BoundingBox box = bersaglio.getBoundingBox();
        double larghezza = box.getWidthX();

        if (distanzaLaterale > larghezza * 0.30) return ParteCorpo.BRACCIA;
        return ParteCorpo.CORPO;
    }

    public static boolean eHeadshot(LivingEntity bersaglio, Location puntoImpatto) {
        return rileva(bersaglio, puntoImpatto) == ParteCorpo.TESTA;
    }
}