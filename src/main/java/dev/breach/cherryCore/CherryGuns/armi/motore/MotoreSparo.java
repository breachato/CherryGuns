package dev.breach.cherryCore.CherryGuns.armi.motore;

import dev.breach.cherryCore.CherryGuns.api.eventi.ArmaColpoEntitaEvent;
import dev.breach.cherryCore.CherryGuns.api.eventi.ArmaSparoEvent;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Armatura;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Taser;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class MotoreSparo {

    private static final Map<UUID, Long> ULTIMI_SPARI = new HashMap<>();

    private MotoreSparo() {}

    public static boolean puoSparare(Arma arma, Player player) {
        if (arma == null || player == null) return false;
        UUID uuid = player.getUniqueId();
        Long ultimo = ULTIMI_SPARI.get(uuid);
        long ora = System.currentTimeMillis();
        long ritardoMs = (long) (arma.getRitardoTraColpi() * 1000);
        return ultimo == null || (ora - ultimo) >= ritardoMs;
    }

    public static void registraSparo(Player player) {
        if (player != null) ULTIMI_SPARI.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public static boolean spara(Arma arma, Player player, ItemStack stack) {
        if (arma == null || player == null || stack == null) return false;
        if (!puoSparare(arma, player)) return false;

        if (player.getGameMode() != GameMode.CREATIVE && !arma.haMunizioniInfinite()) {
            int munizioni = GestoreMunizioni.leggiMunizioniNelloStack(stack);
            if (munizioni <= 0) return false;
        }

        ArmaSparoEvent eventoSparo = new ArmaSparoEvent(player, arma);
        Bukkit.getPluginManager().callEvent(eventoSparo);
        if (eventoSparo.isCancelled()) return false;

        if (player.getGameMode() != GameMode.CREATIVE && !arma.haMunizioniInfinite()) {
            int attuale = GestoreMunizioni.leggiMunizioniNelloStack(stack);
            GestoreMunizioni.scriviMunizioniNelloStack(stack, attuale - 1);
        }

        registraSparo(player);

        if (arma.getSuonoSparo() != null) {
            player.getWorld().playSound(player.getLocation(), arma.getSuonoSparo(), (float) arma.getVolume(), 1.0f);
        }

        if (arma.getKnockbackSparo() != 0) {
            Vector v = player.getVelocity();
            v.add(player.getLocation().getDirection().normalize().multiply(-arma.getKnockbackSparo()));
            player.setVelocity(v);
        }

        for (int i = 0; i < arma.getProiettiliPerColpo(); i++) {
            eseguiRayTrace(arma, player);
        }

        return true;
    }

    private static void eseguiRayTrace(Arma arma, Player player) {
        Location origine = player.getEyeLocation();
        Vector direzione = origine.getDirection();
        double sway = calcolaSwayEffettiva(arma, player);

        if (sway > 0) {
            direzione = applicaSway(direzione, sway);
        }

        RayTraceResult risultato = player.getWorld().rayTrace(
                origine,
                direzione,
                arma.getDistanzaMassima(),
                org.bukkit.FluidCollisionMode.NEVER,
                true,
                0.1,
                entita -> entita != player && entita instanceof LivingEntity
        );

        if (risultato == null) return;

        Entity colpito = risultato.getHitEntity();
        if (!(colpito instanceof LivingEntity)) return;

        LivingEntity bersaglio = (LivingEntity) colpito;
        Location puntoImpatto = risultato.getHitPosition().toLocation(player.getWorld());

        ParteCorpo parteCorpo = RilevatoreParteCorpo.rileva(bersaglio, puntoImpatto);
        double dannoBase = arma.getDanno();
        double dannoFinale = calcolaDanno(arma, bersaglio, parteCorpo, dannoBase);

        ArmaColpoEntitaEvent eventoColpo = new ArmaColpoEntitaEvent(
                player, arma, bersaglio, parteCorpo, dannoFinale, puntoImpatto
        );
        Bukkit.getPluginManager().callEvent(eventoColpo);
        if (eventoColpo.isCancelled()) return;

        bersaglio.damage(eventoColpo.getDanno(), player);

        if (arma instanceof Taser && bersaglio instanceof Player) {
            applicaEffettiTaser((Taser) arma, (Player) bersaglio);
        }
    }

    private static double calcolaDanno(Arma arma, LivingEntity bersaglio, ParteCorpo parteCorpo, double dannoBase) {
        double danno = dannoBase;

        if (parteCorpo == ParteCorpo.TESTA) {
            if (bersaglio instanceof Player) {
                Player p = (Player) bersaglio;
                ItemStack elmetto = p.getInventory().getHelmet();
                if (elmetto != null) {
                    Object item = dev.breach.cherryCore.CherryGuns.armi.base.Registro.get().getQualsiasi(elmetto);
                    if (item instanceof Armatura) {
                        Armatura armatura = (Armatura) item;
                        if (armatura.annullaHeadshot()) {
                            return dannoBase;
                        }
                        danno -= armatura.getProtezione() * 0.5;
                    }
                }
            }
            danno *= arma.getMoltiplicatoreHeadshot();
        } else if (parteCorpo == ParteCorpo.GAMBE) {
            danno *= 0.7;
        } else if (parteCorpo == ParteCorpo.BRACCIA) {
            danno *= 0.8;
        }

        return Math.max(0.5, danno);
    }

    private static double calcolaSwayEffettiva(Arma arma, Player player) {
        double sway = arma.getSway();
        if (sway <= 0) return 0;

        if (arma.isModificatoreSwayMovimento() && player.getVelocity().lengthSquared() > 0.01) {
            sway *= arma.getMoltiplicatoreSway();
        }
        if (arma.isModificatoreSwayShift() && player.isSneaking()) {
            sway *= 0.5;
        }
        if (arma.isModificatoreSwayCorsa() && player.isSprinting()) {
            sway *= arma.getMoltiplicatoreSway();
        }
        if (!MotoreMira.staMirando(player)) {
            sway *= arma.getMoltiplicatoreSwayNonZoom();
        }
        return sway;
    }

    private static Vector applicaSway(Vector direzione, double sway) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        double offsetX = (rng.nextDouble() - 0.5) * sway;
        double offsetY = (rng.nextDouble() - 0.5) * sway;
        double offsetZ = (rng.nextDouble() - 0.5) * sway;
        return direzione.clone().add(new Vector(offsetX, offsetY, offsetZ)).normalize();
    }

    private static void applicaEffettiTaser(Taser taser, Player vittima) {
        int durataTick = taser.getDurataStunSecondi() * 20;
        vittima.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, durataTick, taser.getForzaLentezza(), false, true));
        if (taser.applicaCecita()) {
            vittima.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, durataTick, 0, false, true));
        }
        vittima.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, durataTick, 4, false, true));
    }
}