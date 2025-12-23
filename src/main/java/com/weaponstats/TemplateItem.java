package com.weaponstats;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class TemplateItem {
    
    private final Plugin plugin;
    private final NamespacedKey templateItemKey;
    
    public TemplateItem(Plugin plugin) {
        this.plugin = plugin;
        this.templateItemKey = new NamespacedKey(plugin, "template_item_type");
    }
    
    /**
     * Создает шаблон для отслеживания убийств
     */
    public ItemStack createKillsTemplate() {
        ItemStack template = new ItemStack(Material.PAPER);
        ItemMeta meta = template.getItemMeta();
        if (meta == null) {
            return template;
        }
        
        meta.setDisplayName("§6§lШаблон: Отслеживание убийств");
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Используйте этот шаблон на оружии,");
        lore.add("§7чтобы начать отслеживать убийства игроков");
        lore.add("");
        lore.add("§eПКМ по оружию для применения");
        
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(templateItemKey, PersistentDataType.STRING, "kills");
        
        template.setItemMeta(meta);
        return template;
    }
    
    /**
     * Создает шаблон для отслеживания урона
     */
    public ItemStack createDamageTemplate() {
        ItemStack template = new ItemStack(Material.PAPER);
        ItemMeta meta = template.getItemMeta();
        if (meta == null) {
            return template;
        }
        
        meta.setDisplayName("§6§lШаблон: Отслеживание урона");
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Используйте этот шаблон на оружии,");
        lore.add("§7чтобы начать отслеживать нанесенный урон");
        lore.add("");
        lore.add("§eПКМ по оружию для применения");
        
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(templateItemKey, PersistentDataType.STRING, "damage");
        
        template.setItemMeta(meta);
        return template;
    }
    
    /**
     * Проверяет, является ли предмет шаблоном убийств
     */
    public boolean isKillsTemplate(ItemStack item) {
        if (item == null || item.getType() != Material.PAPER) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String type = container.get(templateItemKey, PersistentDataType.STRING);
        return "kills".equals(type);
    }
    
    /**
     * Проверяет, является ли предмет шаблоном урона
     */
    public boolean isDamageTemplate(ItemStack item) {
        if (item == null || item.getType() != Material.PAPER) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String type = container.get(templateItemKey, PersistentDataType.STRING);
        return "damage".equals(type);
    }
    
    /**
     * Проверяет, является ли предмет любым шаблоном
     */
    public boolean isTemplate(ItemStack item) {
        return isKillsTemplate(item) || isDamageTemplate(item);
    }
}
