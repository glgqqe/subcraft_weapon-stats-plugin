package com.weaponstats;

import org.bukkit.plugin.java.JavaPlugin;

public class WeaponStatsPlugin extends JavaPlugin {
    
    private TemplateManager templateManager;
    private TemplateItem templateItem;
    private StatsListener statsListener;
    private CraftingManager craftingManager;
    
    @Override
    public void onEnable() {
        // Инициализация менеджера шаблонов
        this.templateManager = new TemplateManager(this);
        
        // Инициализация предметов шаблонов
        this.templateItem = new TemplateItem(this);
        
        // Инициализация менеджера крафта
        this.craftingManager = new CraftingManager(this, templateItem);
        craftingManager.registerRecipes();
        
        // Инициализация слушателя событий
        this.statsListener = new StatsListener(this, templateManager, templateItem);
        getServer().getPluginManager().registerEvents(statsListener, this);
        
        // Регистрация команд
        getCommand("weaponstats").setExecutor(new WeaponStatsCommand(this, templateManager, templateItem));
        
        getLogger().info("WeaponStatsPlugin включен!");
    }
    
    @Override
    public void onDisable() {
        // Удаляем рецепты при отключении
        if (craftingManager != null) {
            craftingManager.unregisterRecipes();
        }
        
        getLogger().info("WeaponStatsPlugin выключен!");
    }
    
    public TemplateManager getTemplateManager() {
        return templateManager;
    }
    
    public TemplateItem getTemplateItem() {
        return templateItem;
    }
}
