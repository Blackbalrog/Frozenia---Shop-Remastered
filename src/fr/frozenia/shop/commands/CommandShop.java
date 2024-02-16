package fr.frozenia.shop.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.frozenia.shop.Shop;
import fr.frozenia.shop.inventorys.InventoryRoot;

public class CommandShop implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		
		if (!(sender instanceof Player))
		{
			Bukkit.getServer().getConsoleSender().sendMessage(Shop.prefix + "§cSeul un joueur peut éffectuer cette commande");
			return false;
		}
		
		Player player = (Player) sender;
		
		if (command.getName().equalsIgnoreCase("shop"))
		{
			InventoryRoot.openInventory(player);
		}
		return false;
	}
	
}
