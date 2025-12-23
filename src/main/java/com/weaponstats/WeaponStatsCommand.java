package com.weaponstats;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeaponStatsCommand implements CommandExecutor {
    
    private final WeaponStatsPlugin plugin;
    private final TemplateManager templateManager;
    private final TemplateItem templateItem;
    
    public WeaponStatsCommand(WeaponStatsPlugin plugin, TemplateManager templateManager, TemplateItem templateItem) {
        this.plugin = plugin;
        this.templateManager = templateManager;
        this.templateItem = templateItem;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭта команда доступна только игрокам!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "kills":
            case "kill":
                if (!player.hasPermission("weaponstats.command.kills")) {
                    player.sendMessage("§cУ вас нет прав на использование этой команды!");
                    return true;
                }
                ItemStack killsTemplate = templateItem.createKillsTemplate();
                player.getInventory().addItem(killsTemplate);
                player.sendMessage("§aВы получили шаблон отслеживания убийств!");
                player.sendMessage("§7Используйте ПКМ по оружию для применения.");
                break;
                
            case "damage":
            case "dmg":
                if (!player.hasPermission("weaponstats.command.damage")) {
                    player.sendMessage("§cУ вас нет прав на использование этой команды!");
                    return true;
                }
                ItemStack damageTemplate = templateItem.createDamageTemplate();
                player.getInventory().addItem(damageTemplate);
                player.sendMessage("§aВы получили шаблон отслеживания урона!");
                player.sendMessage("§7Используйте ПКМ по оружию для применения.");
                break;
                
            default:
                sendHelp(player);
                break;
        }
        
        return true;
    }
    
    private void sendHelp(Player player) {
        player.sendMessage("§6§l=== WeaponStats Plugin ===");
        if (player.hasPermission("weaponstats.command.kills")) {
            player.sendMessage("§e/weaponstats kills §7- Получить шаблон отслеживания убийств");
        }
        if (player.hasPermission("weaponstats.command.damage")) {
            player.sendMessage("§e/weaponstats damage §7- Получить шаблон отслеживания урона");
        }
        player.sendMessage("§7Используйте ПКМ по оружию с шаблоном в руке для применения.");
    }
}
