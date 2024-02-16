package fr.frozenia.shop.inventorys;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.frozenia.shop.Shop;
import fr.frozenia.shop.configuration.DefaultConfiguration;
import fr.frozenia.shop.managers.PlayerManager;

public class InventoryRoot implements Listener
{

	private static DefaultConfiguration configuration;
	public static PlayerManager playerManager = new PlayerManager();
	private HashMap<Integer, String> menu = new HashMap<>();
	
	public InventoryRoot(Shop main)
	{
		configuration = new DefaultConfiguration(main, "InventoryRoot.yml");
		ConfigurationSection section = configuration.getConfigurationSection("Menus");
		for (String key : section.getKeys(false))
		{
			menu.put(section.getInt(key + ".slot"), key);
		}
	}

	public static void openInventory(Player player)
	{
		Inventory inventory = Bukkit.createInventory(null, configuration.getInt("size"), configuration.getString("title"));
		
		ConfigurationSection section = configuration.getConfigurationSection("Menus");
		for (String key : section.getKeys(false))
		{
			ItemStack item = new ItemStack(Material.valueOf(section.getString(key + ".material")));
			item.setDurability((short) section.getInt(key + ".nbt"));
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§r" + section.getString(key + ".name").replaceAll("&", "§"));
			if (section.contains(key + ".lores")) itemMeta.setLore(section.getStringList(key + ".lores"));
			item.setItemMeta(itemMeta);
			
			inventory.setItem(section.getInt(key + ".slot"), item);
		}
		player.openInventory(inventory);
	}

	@EventHandler
	public void onInteractInventory(InventoryClickEvent event)
	{
		ItemStack item = event.getCurrentItem();
		
		if (event.getView().getTitle().equals(configuration.getString("title")))
		{
			event.setCancelled(true);
			if (item == null || event.getInventory() == null) return;
			
			Player player = (Player) event.getWhoClicked();
			
			if (menu.get(event.getSlot()) == null) return;
			
			if (!playerManager.contains(player))
			{
				playerManager.addPlayer(player);
				playerManager.addMenu(menu.get(event.getSlot()));
			}
			InventoryMenu.openInventory(player, menu.get(event.getSlot()), 0);
		}
	}
}
