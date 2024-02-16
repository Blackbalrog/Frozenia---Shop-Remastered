package fr.frozenia.shop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.frozenia.shop.commands.CommandShop;
import fr.frozenia.shop.configuration.DefaultConfiguration;
import fr.frozenia.shop.inventorys.InventoryMenu;
import fr.frozenia.shop.inventorys.InventoryRoot;
import fr.frozenia.shop.inventorys.InventoryVendor;
import fr.frozenia.shop.managers.PlayerManager;

public class Shop extends JavaPlugin
{
	public static String prefix;
	
	@Override
	public void onEnable()
	{
		DefaultConfiguration configuration = new DefaultConfiguration(this, "Config.yml");
		prefix = configuration.getString("prefix").replaceAll("&", "§");
		
		onCommands();
		onListeners();
		
		Bukkit.getServer().getConsoleSender().sendMessage(prefix + "§aAllumer");
		
	}
	
	@Override
	public void onDisable()
	{
		Bukkit.getServer().getConsoleSender().sendMessage(prefix + "§cEteint");
	}
	
	private void onCommands()
	{
		this.getCommand("shop").setExecutor(new CommandShop());
	}
	
	private void onListeners()
	{
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new InventoryRoot(this), this);
		pluginManager.registerEvents(new InventoryMenu(this), this);
		pluginManager.registerEvents(new InventoryVendor(this), this);
	}
	
	public static PlayerManager getPlayerManager()
	{
		return InventoryRoot.playerManager;
	}
}
