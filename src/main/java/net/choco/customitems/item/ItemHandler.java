package net.choco.customitems.item;

import lombok.Getter;
import net.choco.customitems.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Set;

public class ItemHandler {

    @Getter
    private HashMap<String, Item> itemHashMap = new HashMap<>();

    public ItemHandler() {
        FileConfiguration tempConfig = Main.getItemsConfig();
        for(String weapon : tempConfig.getKeys(false)) {
            itemHashMap.put(weapon, new Item(weapon, this));
            Main.log(Main.LOG_LEVEL.INFO, "Loaded "+weapon+
                    " custom weapons from weapons.yml storage file!");
        }
    }

    public Item getItem(String name) {
        return itemHashMap.get(name);
    }

    public Set<String> getItems() {
        return itemHashMap.keySet();
    }

    public Item matchItem(ItemStack item) {
        if(!item.hasItemMeta())
            return null;

        ItemMeta im = item.getItemMeta();
        if(!im.hasLore())
            return null;

        for(Item i : itemHashMap.values()) {
            for(String line : im.getLore()) {
                if(line.equalsIgnoreCase(i.getFirstLoreLine()))
                    return i;
            }
        }
        return null;
    }
}
