package com.weaponstats;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CraftingManager {
    
    private final Plugin plugin;
    private final TemplateItem templateItem;
    private final List<NamespacedKey> registeredRecipes;
    
    public CraftingManager(Plugin plugin, TemplateItem templateItem) {
        this.plugin = plugin;
        this.templateItem = templateItem;
        this.registeredRecipes = new ArrayList<>();
    }
    
    /**
     * Регистрирует рецепты крафта
     */
    public void registerRecipes() {
        // Рецепт шаблона урона: книга в центре, blaze powder по осям, diamond по диагоналям
        // D B D
        // B K B
        // D B D
        ItemStack damageTemplate = templateItem.createDamageTemplate();
        NamespacedKey damageRecipeKey = new NamespacedKey(plugin, "damage_template_recipe");
        
        // Удаляем старый рецепт, если существует
        plugin.getServer().removeRecipe(damageRecipeKey);
        
        ShapedRecipe damageRecipe = new ShapedRecipe(damageRecipeKey, damageTemplate);
        damageRecipe.shape("DBD", "BKB", "DBD");
        damageRecipe.setIngredient('D', Material.DIAMOND);
        damageRecipe.setIngredient('B', Material.BLAZE_POWDER);
        damageRecipe.setIngredient('K', Material.BOOK);
        
        plugin.getServer().addRecipe(damageRecipe);
        registeredRecipes.add(damageRecipeKey);
        
        // Рецепт шаблона убийств: книга в центре, emerald по осям, diamond по диагоналям
        // D E D
        // E K E
        // D E D
        ItemStack killsTemplate = templateItem.createKillsTemplate();
        NamespacedKey killsRecipeKey = new NamespacedKey(plugin, "kills_template_recipe");
        
        // Удаляем старый рецепт, если существует
        plugin.getServer().removeRecipe(killsRecipeKey);
        
        ShapedRecipe killsRecipe = new ShapedRecipe(killsRecipeKey, killsTemplate);
        killsRecipe.shape("DED", "EKE", "DED");
        killsRecipe.setIngredient('D', Material.DIAMOND);
        killsRecipe.setIngredient('E', Material.EMERALD);
        killsRecipe.setIngredient('K', Material.BOOK);
        
        plugin.getServer().addRecipe(killsRecipe);
        registeredRecipes.add(killsRecipeKey);
    }
    
    /**
     * Удаляет все зарегистрированные рецепты
     */
    public void unregisterRecipes() {
        for (NamespacedKey key : registeredRecipes) {
            plugin.getServer().removeRecipe(key);
        }
        registeredRecipes.clear();
    }
}
