package dev.breach.cherryCore.CherryGuns.armi.tipi;

import dev.breach.cherryCore.CherryGuns.armi.base.Comportamento;
import dev.breach.cherryCore.CherryGuns.armi.base.Identificatore;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import dev.breach.cherryCore.CherryGuns.armi.motore.GestoreClick;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Arma extends OggettoBase implements Comportamento {

    private Munizione munizione;
    private TipoArma tipo = TipoArma.PISTOLA;

    private boolean haIronSights = true;
    private int livelloZoom = 0;
    private boolean visioneNotturna = false;

    private float danno = 1.0f;
    private int caricatoreMassimo = 12;
    private double moltiplicatoreHeadshot = 2.0;

    private double sway = 0.1;
    private double moltiplicatoreSway = 2.0;
    private double moltiplicatoreSwayNonZoom = 1.0;
    private boolean modificatoreSwayMovimento = true;
    private boolean modificatoreSwayShift = true;
    private boolean modificatoreSwayCorsa = true;

    private boolean automatica = false;
    private int rateoFuoco = 1;
    private int proiettiliPerColpo = 1;
    private double ritardoTraColpi = 0.25;
    private double tempoRicarica = 1.5;
    private int distanzaMassima = 150;

    private boolean primaria = true;
    private boolean munizioniInfinite = false;

    private double rinculo = 1.0;
    private double knockbackSparo = 0;
    private int lentezzaEquipaggia = 0;
    private double volume = 4.0;

    private String suonoSparo = "entity.generic.explode";
    private String suonoRicarica = "block.iron_door.close";
    private String suonoCarica = "block.iron_door.open";

    private Particle particella = null;
    private double particellaR = 1.0;
    private double particellaG = 1.0;
    private double particellaB = 1.0;
    private int particellaData = 0;
    private Material particellaMateriale = Material.COAL_BLOCK;

    private boolean fumoCanna = false;
    private int livelloLuce = 20;

    private String messaggioMorte = "%vittima% è stato ucciso da %killer% con %arma%";

    public Arma(String nome, Identificatore identificatore) {
        super(nome, identificatore, nome, null);
    }

    public Arma(String nome, Identificatore identificatore, String nomeVisualizzato) {
        super(nome, identificatore, nomeVisualizzato, null);
    }

    public Munizione getMunizione() { return munizione; }
    public void setMunizione(Munizione munizione) { this.munizione = munizione; }

    public TipoArma getTipo() { return tipo; }
    public void setTipo(TipoArma tipo) { this.tipo = tipo; }

    public boolean haIronSights() { return haIronSights; }
    public void setHaIronSights(boolean v) { this.haIronSights = v; }

    public int getLivelloZoom() { return livelloZoom; }
    public void setLivelloZoom(int v) { this.livelloZoom = v; }

    public boolean haVisioneNotturna() { return visioneNotturna; }
    public void setVisioneNotturna(boolean v) { this.visioneNotturna = v; }

    public float getDanno() { return danno; }
    public void setDanno(float danno) { this.danno = danno; }

    public int getCaricatoreMassimo() { return caricatoreMassimo; }
    public void setCaricatoreMassimo(int v) { this.caricatoreMassimo = v; }

    public double getMoltiplicatoreHeadshot() { return moltiplicatoreHeadshot; }
    public void setMoltiplicatoreHeadshot(double v) { this.moltiplicatoreHeadshot = v; }

    public double getSway() { return sway; }
    public void setSway(double sway) { this.sway = sway; }

    public double getMoltiplicatoreSway() { return moltiplicatoreSway; }
    public void setMoltiplicatoreSway(double v) { this.moltiplicatoreSway = v; }

    public double getMoltiplicatoreSwayNonZoom() { return moltiplicatoreSwayNonZoom; }
    public void setMoltiplicatoreSwayNonZoom(double v) { this.moltiplicatoreSwayNonZoom = v; }

    public boolean isModificatoreSwayMovimento() { return modificatoreSwayMovimento; }
    public void setModificatoreSwayMovimento(boolean v) { this.modificatoreSwayMovimento = v; }

    public boolean isModificatoreSwayShift() { return modificatoreSwayShift; }
    public void setModificatoreSwayShift(boolean v) { this.modificatoreSwayShift = v; }

    public boolean isModificatoreSwayCorsa() { return modificatoreSwayCorsa; }
    public void setModificatoreSwayCorsa(boolean v) { this.modificatoreSwayCorsa = v; }

    public boolean eAutomatica() { return automatica; }
    public void setAutomatica(boolean v) { this.automatica = v; }

    public int getRateoFuoco() { return rateoFuoco; }
    public void setRateoFuoco(int v) { this.rateoFuoco = v; }

    public int getProiettiliPerColpo() { return proiettiliPerColpo; }
    public void setProiettiliPerColpo(int v) { this.proiettiliPerColpo = v; }

    public double getRitardoTraColpi() { return ritardoTraColpi; }
    public void setRitardoTraColpi(double v) { this.ritardoTraColpi = v; }

    public double getTempoRicarica() { return tempoRicarica; }
    public void setTempoRicarica(double v) { this.tempoRicarica = v; }

    public int getDistanzaMassima() { return distanzaMassima; }
    public void setDistanzaMassima(int v) { this.distanzaMassima = v; }

    public boolean ePrimaria() { return primaria; }
    public void setPrimaria(boolean v) { this.primaria = v; }

    public boolean haMunizioniInfinite() { return munizioniInfinite || munizione == null; }
    public void setMunizioniInfinite(boolean v) { this.munizioniInfinite = v; }

    public double getRinculo() { return rinculo; }
    public void setRinculo(double v) { this.rinculo = v; }

    public double getKnockbackSparo() { return knockbackSparo; }
    public void setKnockbackSparo(double v) { this.knockbackSparo = v; }

    public int getLentezzaEquipaggia() { return lentezzaEquipaggia; }
    public void setLentezzaEquipaggia(int v) { this.lentezzaEquipaggia = v; }

    public double getVolume() { return volume; }
    public void setVolume(double v) { this.volume = v; }

    public String getSuonoSparo() { return suonoSparo; }
    public void setSuonoSparo(String v) { this.suonoSparo = v; }

    public String getSuonoRicarica() { return suonoRicarica; }
    public void setSuonoRicarica(String v) { this.suonoRicarica = v; }

    public String getSuonoCarica() { return suonoCarica; }
    public void setSuonoCarica(String v) { this.suonoCarica = v; }

    public Particle getParticella() { return particella; }
    public double getParticellaR() { return particellaR; }
    public double getParticellaG() { return particellaG; }
    public double getParticellaB() { return particellaB; }
    public int getParticellaData() { return particellaData; }
    public Material getParticellaMateriale() { return particellaMateriale; }

    public void setParticella(Particle p, double r, double g, double b, Material m, int data) {
        this.particella = p;
        this.particellaR = r;
        this.particellaG = g;
        this.particellaB = b;
        this.particellaMateriale = m;
        this.particellaData = data;
    }

    public boolean usaFumoCanna() { return fumoCanna; }
    public void setFumoCanna(boolean v) { this.fumoCanna = v; }

    public int getLivelloLuce() { return livelloLuce; }
    public void setLivelloLuce(int v) { this.livelloLuce = v; }

    public String getMessaggioMorte() { return messaggioMorte; }
    public void setMessaggioMorte(String v) { this.messaggioMorte = v; }

    @Override
    public boolean alClickSinistro(Player player, ItemStack stack) {
        return GestoreClick.gestisciClick(this, player, stack, true);
    }

    @Override
    public boolean alClickDestro(Player player, ItemStack stack) {
        return GestoreClick.gestisciClick(this, player, stack, false);
    }

    @Override
    public boolean alloShift(Player player, ItemStack stack, boolean accovacciato) {
        return false;
    }

    @Override
    public boolean alCambioVerso(Player player, ItemStack stack) {
        return GestoreClick.gestisciCambioVerso(this, player, stack);
    }

    @Override
    public boolean alCambioDa(Player player, ItemStack stack) {
        return GestoreClick.gestisciCambioDa(this, player, stack);
    }
}