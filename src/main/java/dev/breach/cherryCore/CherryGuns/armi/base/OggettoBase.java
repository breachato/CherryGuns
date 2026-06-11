package dev.breach.cherryCore.CherryGuns.armi.base;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class OggettoBase {

    private final UUID uuid = UUID.randomUUID();
    private final String nome;
    private final Identificatore identificatore;

    private String nomeVisualizzato;
    private List<String> lorePersonalizzata;

    private String suonoEquipaggia;
    private float volumeSuonoEquipaggia = 1.0f;
    private String suonoColpo;
    private float volumeSuonoColpo = 1.0f;

    private int stackMassimo = 1;

    protected OggettoBase(String nome, Identificatore identificatore, String nomeVisualizzato, List<String> lorePersonalizzata) {
        this.nome = nome;
        this.identificatore = identificatore;
        this.nomeVisualizzato = nomeVisualizzato;
        this.lorePersonalizzata = lorePersonalizzata != null ? new ArrayList<>(lorePersonalizzata) : new ArrayList<>();
    }

    public final UUID getUuid() {
        return uuid;
    }

    public final String getNome() {
        return nome;
    }

    public final Identificatore getIdentificatore() {
        return identificatore;
    }

    public String getNomeVisualizzato() {
        return nomeVisualizzato;
    }

    public void setNomeVisualizzato(String nomeVisualizzato) {
        this.nomeVisualizzato = nomeVisualizzato;
    }

    public List<String> getLorePersonalizzata() {
        return new ArrayList<>(lorePersonalizzata);
    }

    public void setLorePersonalizzata(List<String> lore) {
        this.lorePersonalizzata = lore != null ? new ArrayList<>(lore) : new ArrayList<>();
    }

    public String getSuonoEquipaggia() {
        return suonoEquipaggia;
    }

    public void setSuonoEquipaggia(String suono) {
        this.suonoEquipaggia = suono;
    }

    public float getVolumeSuonoEquipaggia() {
        return volumeSuonoEquipaggia;
    }

    public void setVolumeSuonoEquipaggia(float volume) {
        this.volumeSuonoEquipaggia = volume;
    }

    public String getSuonoColpo() {
        return suonoColpo;
    }

    public void setSuonoColpo(String suono) {
        this.suonoColpo = suono;
    }

    public float getVolumeSuonoColpo() {
        return volumeSuonoColpo;
    }

    public void setVolumeSuonoColpo(float volume) {
        this.volumeSuonoColpo = volume;
    }

    public int getStackMassimo() {
        return stackMassimo;
    }

    public void setStackMassimo(int stackMassimo) {
        this.stackMassimo = Math.max(1, stackMassimo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OggettoBase)) return false;
        return uuid.equals(((OggettoBase) o).uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + nome + " | " + identificatore + "}";
    }
}