package dev.breach.cherryCore.CherryGuns.armi.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public final class Identificatore {

    private final Material materiale;
    private final int customModelData;

    private Identificatore(Material materiale, int customModelData) {
        this.materiale = materiale;
        this.customModelData = customModelData;
    }

    public static Identificatore di(Material materiale, int customModelData) {
        return new Identificatore(materiale, customModelData);
    }

    public static Identificatore crossbow(int customModelData) {
        return new Identificatore(Material.CROSSBOW, customModelData);
    }

    public static Identificatore da(ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) return null;
        ItemMeta meta = stack.getItemMeta();
        int cmd = (meta != null && meta.hasCustomModelData()) ? meta.getCustomModelData() : 0;
        return new Identificatore(stack.getType(), cmd);
    }

    public Material getMateriale() {
        return materiale;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public boolean corrisponde(ItemStack stack) {
        if (stack == null) return false;
        Identificatore altro = da(stack);
        return this.equals(altro);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Identificatore)) return false;
        Identificatore that = (Identificatore) o;
        return customModelData == that.customModelData && materiale == that.materiale;
    }

    @Override
    public int hashCode() {
        return Objects.hash(materiale, customModelData);
    }

    @Override
    public String toString() {
        return materiale.name() + ":" + customModelData;
    }
}