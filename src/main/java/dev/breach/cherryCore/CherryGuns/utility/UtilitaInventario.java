package dev.breach.cherryCore.CherryGuns.utility;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

public final class UtilitaInventario {

    private UtilitaInventario() {}

    public static void dai(HumanEntity entity, ItemStack stack) {
        if (entity == null || stack == null) return;
        if (entity.getInventory().firstEmpty() != -1) {
            entity.getInventory().addItem(stack);
        } else {
            entity.getWorld().dropItem(entity.getLocation(), stack);
        }
    }

    public static boolean haSpazio(HumanEntity entity) {
        return entity != null && entity.getInventory().firstEmpty() != -1;
    }

    public static int contaItem(HumanEntity entity, Material materiale) {
        if (entity == null || materiale == null) return 0;
        int totale = 0;
        for (ItemStack s : entity.getInventory().getContents()) {
            if (s != null && s.getType() == materiale) totale += s.getAmount();
        }
        return totale;
    }
}