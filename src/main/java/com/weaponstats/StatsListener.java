package com.weaponstats;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class StatsListener implements Listener {
    
    private final Plugin plugin;
    private final TemplateManager templateManager;
    private final TemplateItem templateItem;
    
    public StatsListener(Plugin plugin, TemplateManager templateManager, TemplateItem templateItem) {
        this.plugin = plugin;
        this.templateManager = templateManager;
        this.templateItem = templateItem;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        // Проверяем, что убита сущность является игроком
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        // Проверяем, был ли убит игроком
        if (event.getEntity().getKiller() == null) {
            return;
        }
        
        Player killer = event.getEntity().getKiller();
        ItemStack weapon = killer.getInventory().getItemInMainHand();
        
        // Проверяем, есть ли на оружии шаблон отслеживания убийств
        if (templateManager.hasKillsTemplate(weapon)) {
            templateManager.incrementKills(weapon);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Проверяем, что урон нанесен игроком
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        Player attacker = (Player) event.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        
        // Проверяем, есть ли на оружии шаблон отслеживания урона
        if (templateManager.hasDamageTemplate(weapon)) {
            double damage = event.getFinalDamage();
            templateManager.addDamage(weapon, damage);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Проверяем, что это клик правой кнопкой мыши по блоку или воздуху
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // Проверяем пермишн на использование шаблонов
        if (!player.hasPermission("weaponstats.use.template")) {
            return;
        }
        
        ItemStack template = player.getInventory().getItemInMainHand();
        
        // Проверяем, что в руке шаблон
        if (!templateItem.isTemplate(template)) {
            return;
        }
        
        // Получаем оружие в другой руке или ищем в инвентаре
        ItemStack weapon = player.getInventory().getItemInOffHand();
        
        // Если в другой руке нет оружия, проверяем слоты инвентаря
        if (weapon == null || weapon.getType().isAir()) {
            // Ищем оружие в инвентаре (мечи, топоры, луки и т.д.)
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && !item.getType().isAir()) {
                    Material type = item.getType();
                    if (type.name().contains("SWORD") || type.name().contains("AXE") || 
                        type.name().contains("BOW") || type.name().contains("CROSSBOW") ||
                        type.name().contains("TRIDENT") || type.name().contains("HOE")) {
                        weapon = item;
                        break;
                    }
                }
            }
        }
        
        // Если оружие не найдено, сообщаем игроку
        if (weapon == null || weapon.getType().isAir()) {
            player.sendMessage("§cВозьмите оружие в руку или положите в инвентарь!");
            return;
        }
        
        // Применяем соответствующий шаблон
        boolean applied = false;
        if (templateItem.isKillsTemplate(template)) {
            applied = templateManager.applyKillsTemplate(weapon);
            if (applied) {
                player.sendMessage("§aШаблон отслеживания убийств применен к оружию!");
                // Уменьшаем количество шаблонов на 1
                if (template.getAmount() > 1) {
                    template.setAmount(template.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
            }
        } else if (templateItem.isDamageTemplate(template)) {
            applied = templateManager.applyDamageTemplate(weapon);
            if (applied) {
                player.sendMessage("§aШаблон отслеживания урона применен к оружию!");
                // Уменьшаем количество шаблонов на 1
                if (template.getAmount() > 1) {
                    template.setAmount(template.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
            }
        }
        
        if (applied) {
            event.setCancelled(true);
        }
    }
}
