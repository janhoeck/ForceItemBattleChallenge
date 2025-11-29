package de.janhck.forceitembattlechallenge.gui.builder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.ChatPaginator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ItemStackBuilder {

    private String descriptionHeadline;
    private String[] description;
    private String leftClickDescription;
    private String rightClickDescription;
    private final ItemStack itemStack;

    public ItemStackBuilder(Material material) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        this.itemStack = itemStack;
    }

    public ItemStackBuilder withDisplayName(String displayName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§8» §7" + displayName);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder withDescriptionHeadline(String descriptionHeadline) {
        this.descriptionHeadline = "§3" + descriptionHeadline;
        return this;
    }

    public ItemStackBuilder withDescription(ChatPaginator.ChatPage page) {
        return withDescription(page.getLines());
    }

    public ItemStackBuilder withLeftClickDescription(String leftClickDescription) {
        this.leftClickDescription = leftClickDescription;
        return this;
    }

    public ItemStackBuilder withRightClickDescription(String rightClickDescription) {
        this.rightClickDescription = rightClickDescription;
        return this;
    }

    public ItemStackBuilder withDescription(String[] description) {
        this.description = description;
        return this;
    }

    public ItemStackBuilder withAmount(int amount) {
        if(amount > 64) {
            itemStack.setAmount(64);
        } else if(amount <= 0) {
            itemStack.setAmount(1);
        } else {
            itemStack.setAmount(amount);
        }
        return this;
    }

    public ItemStackBuilder asSkull(String id) {
        ItemMeta meta = itemStack.getItemMeta();
        SkullMeta skullMeta = (SkullMeta) meta;

        PlayerProfile profile = Bukkit.createPlayerProfile("MrBob");
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL("http://textures.minecraft.net/texture/" + id));
        } catch (MalformedURLException ignored) {}
        profile.setTextures(textures);

        skullMeta.setOwnerProfile(profile);
        itemStack.setItemMeta(skullMeta);
        return this;
    }

    public ItemStack build() {
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> lore = new ArrayList<>();
        if(descriptionHeadline != null) {
            lore.add(descriptionHeadline);
        }

        if(description != null) {
            // Add an empty whitespace before the description.
            lore.add("");
            lore.addAll(Stream.of(description).toList());
        }

        if(leftClickDescription != null || rightClickDescription != null) {
            lore.add("");
            if(leftClickDescription != null) {
                lore.add("§b§lLinks Klick: §r§7" + leftClickDescription);
            }

            if(rightClickDescription != null) {
                lore.add("§b§lRechts Klick: §r§7" + rightClickDescription);
            }
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
