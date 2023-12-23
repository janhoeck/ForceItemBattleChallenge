package de.janhck.forceitembattlechallenge.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;

public class ItemUtil {

    public static ItemStack createSkull(String id, String title) {
        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = skullItem.getItemMeta();
        SkullMeta skullMeta = (SkullMeta) meta;

        PlayerProfile profile = Bukkit.createPlayerProfile("Mr Bob");
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL("http://textures.minecraft.net/texture/" + id));
        } catch (MalformedURLException ignored) {}
        profile.setTextures(textures);

        skullMeta.setDisplayName(title);
        skullMeta.setOwnerProfile(profile);

        skullItem.setItemMeta(skullMeta);
        return skullItem;
    }

    public static ItemStack createGUIPlaceholderItem() {
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
