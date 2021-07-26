package net.choco.customitems.item;

import lombok.Getter;
import net.choco.customitems.Main;
import net.choco.customitems.utility.ChatUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Item {

    @Getter
    private String itemName;

    @Getter
    private String firstLoreLine = null;

    @Getter
    private ItemStack item;

    @Getter
    private int customModel;

    @Getter
    private boolean unbreakable;

    public Item(String itemName, ItemHandler itemHandler) {
        FileConfiguration tempConfig = Main.getItemsConfig();

        this.itemName = itemName;
        this.customModel = tempConfig.getInt(itemName + ".customModel");
        this.unbreakable = tempConfig.getBoolean(itemName + ".unbreakable");

        item = new ItemStack(Material.valueOf(tempConfig.getString(itemName + ".material")));
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatUtils.color(tempConfig.getString(itemName+".name")));
        List<String> lore = new ArrayList<>();

        for(String line : tempConfig.getStringList(itemName+".lore")) {
            if(firstLoreLine == null)
                firstLoreLine = ChatUtils.color(line);

            lore.add(ChatUtils.color(line));
        }

        im.setLore(lore);
        im.setCustomModelData(customModel);
        im.setUnbreakable(unbreakable);

        for(String ench : tempConfig.getStringList(itemName+".enchants")) {
            String[] data = ench.split(":");

            String enchName = data[0].toUpperCase();
            int lvl = Integer.parseInt(data[1]);

            Enchantment enchantment = Enchantment.getByName(enchName);
            if(enchantment == null) {
                Main.log(Main.LOG_LEVEL.ERROR, "[CustomItems] Couldn't find enchantment "+data[0]);
                continue;
            }
            im.addEnchant(enchantment, lvl, true);
        }
        item.setItemMeta(im);
    }
}
