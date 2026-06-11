package dev.breach.cherryCore.CherryGuns.interfaccia;

import com.cryptomorin.xseries.XMaterial;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.List;

public final class CategoriaArma {

    public enum Filtro {
        TUTTE,
        ARMI,
        MUNIZIONI,
        ARMATURE,
        GRANATE
    }

    private final String id;
    private final String nome;
    private final Material materialeIcona;
    private final String nomeIcona;
    private final List<String> loreIcona;
    private final Filtro filtro;

    public CategoriaArma(String id, ConfigurationSection config) {
        this.id = id;
        this.nome = Colori.traduci(config.getString("nome", "&dCategoria"));

        ConfigurationSection iconaSection = config.getConfigurationSection("icona");
        if (iconaSection != null) {
            String tipoStr = iconaSection.getString("materiale", "CHEST");
            this.materialeIcona = XMaterial.matchXMaterial(tipoStr).orElse(XMaterial.CHEST).get();
            this.nomeIcona = Colori.traduci(iconaSection.getString("nome", this.nome));
            List<String> rawLore = iconaSection.getStringList("lore");
            this.loreIcona = rawLore.isEmpty() ? Collections.emptyList() : Colori.traduci(rawLore);
        } else {
            this.materialeIcona = XMaterial.CHEST.get();
            this.nomeIcona = this.nome;
            this.loreIcona = Collections.emptyList();
        }

        String filtroStr = config.getString("filtro", "TUTTE").toUpperCase();
        Filtro filtroParsed;
        try {
            filtroParsed = Filtro.valueOf(filtroStr);
        } catch (IllegalArgumentException e) {
            filtroParsed = Filtro.TUTTE;
        }
        this.filtro = filtroParsed;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public Material getMaterialeIcona() { return materialeIcona; }
    public String getNomeIcona() { return nomeIcona; }
    public List<String> getLoreIcona() { return loreIcona; }
    public Filtro getFiltro() { return filtro; }
}