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

public class TemplateManager {
    
    private final Plugin plugin;
    private final NamespacedKey templateKey;
    private final NamespacedKey killsKey;
    private final NamespacedKey damageKey;
    
    public TemplateManager(Plugin plugin) {
        this.plugin = plugin;
        this.templateKey = new NamespacedKey(plugin, "weapon_template");
        this.killsKey = new NamespacedKey(plugin, "kills");
        this.damageKey = new NamespacedKey(plugin, "damage");
    }
    
    /**
     * Применяет шаблон отслеживания убийств к предмету
     */
    public boolean applyKillsTemplate(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(templateKey, PersistentDataType.STRING, "kills");
        container.set(killsKey, PersistentDataType.INTEGER, 0);
        
        // Обновляем описание предмета
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        
        // Удаляем старые строки статистики
        lore.removeIf(line -> line.contains("Убийств:") || line.contains("Урон:"));
        
        lore.add("§7Убийств: §c0");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        return true;
    }
    
    /**
     * Применяет шаблон отслеживания урона к предмету
     */
    public boolean applyDamageTemplate(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(templateKey, PersistentDataType.STRING, "damage");
        container.set(damageKey, PersistentDataType.DOUBLE, 0.0);
        
        // Обновляем описание предмета
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        
        // Удаляем старые строки статистики
        lore.removeIf(line -> line.contains("Убийств:") || line.contains("Урон:"));
        
        lore.add("§7Урон: §c0.0");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        return true;
    }
    
    /**
     * Проверяет, имеет ли предмет шаблон отслеживания убийств
     */
    public boolean hasKillsTemplate(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String template = container.get(templateKey, PersistentDataType.STRING);
        return template != null && "kills".equals(template);
    }
    
    /**
     * Проверяет, имеет ли предмет шаблон отслеживания урона
     */
    public boolean hasDamageTemplate(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String template = container.get(templateKey, PersistentDataType.STRING);
        return template != null && "damage".equals(template);
    }
    
    /**
     * Увеличивает счетчик убийств
     */
    public void incrementKills(ItemStack item) {
        if (!hasKillsTemplate(item)) {
            return;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int kills = container.getOrDefault(killsKey, PersistentDataType.INTEGER, 0);
        kills++;
        container.set(killsKey, PersistentDataType.INTEGER, kills);
        
        // Обновляем описание
        updateKillsLore(meta, kills);
        item.setItemMeta(meta);
    }
    
    /**
     * Добавляет урон к счетчику
     */
    public void addDamage(ItemStack item, double damage) {
        if (!hasDamageTemplate(item)) {
            return;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        double totalDamage = container.getOrDefault(damageKey, PersistentDataType.DOUBLE, 0.0);
        totalDamage += damage;
        container.set(damageKey, PersistentDataType.DOUBLE, totalDamage);
        
        // Обновляем описание
        updateDamageLore(meta, totalDamage);
        item.setItemMeta(meta);
    }
    
    private void updateKillsLore(ItemMeta meta, int kills) {
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        
        // Удаляем старые строки статистики
        lore.removeIf(line -> line.contains("Убийств:"));
        
        lore.add("§7Убийств: §c" + kills);
        
        meta.setLore(lore);
    }
    
    private void updateDamageLore(ItemMeta meta, double damage) {
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        
        // Удаляем старые строки статистики
        lore.removeIf(line -> line.contains("Урон:"));
        
        lore.add("§7Урон: §c" + String.format("%.1f", damage));
        
        meta.setLore(lore);
    }
    
    public NamespacedKey getKillsKey() {
        return killsKey;
    }
    
    public NamespacedKey getDamageKey() {
        return damageKey;
    }
}
