package dev.breach.cherryCore.CherryGuns.configurazione;

import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CostruttoreItem {

    private final ItemStack stack;
    private final ItemMeta meta;

    private CostruttoreItem(Material materiale) {
        this.stack = new ItemStack(materiale);
        this.meta = this.stack.getItemMeta();
    }

    private CostruttoreItem(ItemStack stack) {
        this.stack = stack;
        this.meta = stack.getItemMeta();
    }

    public static CostruttoreItem di(Material materiale) {
        return new CostruttoreItem(materiale);
    }

    public static CostruttoreItem di(ItemStack stack) {
        return new CostruttoreItem(stack);
    }

    public CostruttoreItem quantita(int quantita) {
        stack.setAmount(Math.max(1, quantita));
        return this;
    }

    public CostruttoreItem nome(String nome) {
        if (meta != null && nome != null) {
            meta.setDisplayName(Colori.traduci(nome));
        }
        return this;
    }

    public CostruttoreItem lore(List<String> lore) {
        if (meta != null && lore != null) {
            List<String> tradotte = new ArrayList<>(lore.size());
            for (String riga : lore) {
                tradotte.add(riga == null ? "" : Colori.traduci(riga));
            }
            meta.setLore(tradotte);
        }
        return this;
    }

    public CostruttoreItem lore(String... righe) {
        return lore(Arrays.asList(righe));
    }

    public CostruttoreItem customModelData(int data) {
        if (meta != null && data > 0) {
            meta.setCustomModelData(data);
        }
        return this;
    }

    public CostruttoreItem irrompibile(boolean irrompibile) {
        if (meta != null) {
            meta.setUnbreakable(irrompibile);
            if (irrompibile) {
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }
        }
        return this;
    }

    public CostruttoreItem nascondiAttributi() {
        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        }
        return this;
    }

    public CostruttoreItem aggiungiFlag(ItemFlag... flags) {
        if (meta != null && flags != null) {
            meta.addItemFlags(flags);
        }
        return this;
    }

    public ItemStack costruisci() {
        if (meta != null) {
            stack.setItemMeta(meta);
        }
        return stack;
    }
}