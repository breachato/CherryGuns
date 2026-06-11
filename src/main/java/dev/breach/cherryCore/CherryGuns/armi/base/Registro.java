package dev.breach.cherryCore.CherryGuns.armi.base;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Registro {

    private static final Registro ISTANZA = new Registro();

    private final Map<Identificatore, OggettoBase> armi = new ConcurrentHashMap<>();
    private final Map<Identificatore, OggettoBase> munizioni = new ConcurrentHashMap<>();
    private final Map<Identificatore, OggettoBase> armature = new ConcurrentHashMap<>();
    private final Map<Identificatore, OggettoBase> granate = new LinkedHashMap<>();

    private Registro() {}

    public static Registro get() {
        return ISTANZA;
    }

    public void svuota() {
        armi.clear();
        munizioni.clear();
        armature.clear();
        granate.clear();
    }

    public void registraArma(OggettoBase arma) {
        armi.put(arma.getIdentificatore(), arma);
    }

    public void registraMunizione(OggettoBase munizione) {
        munizioni.put(munizione.getIdentificatore(), munizione);
    }

    public void registraArmatura(OggettoBase armatura) {
        armature.put(armatura.getIdentificatore(), armatura);
    }

    public void registraGranata(OggettoBase granata) {
        granate.put(granata.getIdentificatore(), granata);
    }

    public OggettoBase getArma(Identificatore id) {
        return armi.get(id);
    }

    public OggettoBase getMunizione(Identificatore id) {
        return munizioni.get(id);
    }

    public OggettoBase getArmatura(Identificatore id) {
        return armature.get(id);
    }

    public OggettoBase getGranata(Identificatore id) {
        return granate.get(id);
    }

    public OggettoBase getQualsiasi(Identificatore id) {
        OggettoBase b = armi.get(id);
        if (b != null) return b;
        b = munizioni.get(id);
        if (b != null) return b;
        b = armature.get(id);
        if (b != null) return b;
        return granate.get(id);
    }

    public OggettoBase getQualsiasi(ItemStack stack) {
        Identificatore id = Identificatore.da(stack);
        return id != null ? getQualsiasi(id) : null;
    }

    public OggettoBase getPerNome(String nome) {
        if (nome == null) return null;
        for (OggettoBase b : armi.values()) if (b.getNome().equalsIgnoreCase(nome)) return b;
        for (OggettoBase b : munizioni.values()) if (b.getNome().equalsIgnoreCase(nome)) return b;
        for (OggettoBase b : armature.values()) if (b.getNome().equalsIgnoreCase(nome)) return b;
        for (OggettoBase b : granate.values()) if (b.getNome().equalsIgnoreCase(nome)) return b;
        return null;
    }

    public boolean eArma(ItemStack stack) {
        Identificatore id = Identificatore.da(stack);
        return id != null && armi.containsKey(id);
    }

    public boolean eMunizione(ItemStack stack) {
        Identificatore id = Identificatore.da(stack);
        return id != null && munizioni.containsKey(id);
    }

    public boolean eArmatura(ItemStack stack) {
        Identificatore id = Identificatore.da(stack);
        return id != null && armature.containsKey(id);
    }

    public boolean eGranata(ItemStack stack) {
        Identificatore id = Identificatore.da(stack);
        return id != null && granate.containsKey(id);
    }

    public boolean eQualsiasi(ItemStack stack) {
        return getQualsiasi(stack) != null;
    }

    public Collection<OggettoBase> tutteLeArmi() {
        return new ArrayList<>(armi.values());
    }

    public Collection<OggettoBase> tutteLeMunizioni() {
        return new ArrayList<>(munizioni.values());
    }

    public Collection<OggettoBase> tutteLeArmature() {
        return new ArrayList<>(armature.values());
    }

    public Collection<OggettoBase> tutteLeGranate() {
        return new ArrayList<>(granate.values());
    }

    public List<OggettoBase> tutto() {
        List<OggettoBase> tutti = new ArrayList<>();
        tutti.addAll(armi.values());
        tutti.addAll(munizioni.values());
        tutti.addAll(armature.values());
        tutti.addAll(granate.values());
        return tutti;
    }

    public int numeroArmi() {
        return armi.size();
    }

    public int numeroMunizioni() {
        return munizioni.size();
    }

    public int numeroArmature() {
        return armature.size();
    }

    public int numeroGranate() {
        return granate.size();
    }
}