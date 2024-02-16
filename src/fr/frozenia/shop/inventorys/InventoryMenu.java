package fr.frozenia.shop.inventorys;

import java.util.HashMap;
import java.util.Set;

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

public class InventoryMenu implements Listener
{
	
	private static Shop instance;
	private static DefaultConfiguration configuration;
	private static HashMap<Integer, String> map_key = new HashMap<>();
	private static int PAGE  = 0;
	private static int PAGES = 0;

	public InventoryMenu(Shop main)
	{
		instance = main;
	}

	public static void openInventory(Player player, String menu, int page)
	{
		PAGE = page;
		configuration = new DefaultConfiguration(instance, "Menus/" + menu + ".yml");
		Inventory inventory = Bukkit.createInventory(null, configuration.getInt("size"), configuration.getString("title"));
		
		ConfigurationSection section = configuration.getConfigurationSection("page_" + page);
		for (String key : section.getKeys(false))
		{
			map_key.put(configuration.getInt(key + ".slot"), key);
			
			ItemStack item = new ItemStack(Material.valueOf(section.getString(key + ".material")));
			item.setDurability((short) section.getInt(key + ".nbt"));
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§r" + section.getString(key + ".name").replaceAll("&", "§"));
			item.setItemMeta(itemMeta);
			inventory.setItem(section.getInt(key + ".slot"), item);
		}
		
		Set<String> keys = section.getKeys(false);
		PAGES = keys.size();
		
		ConfigurationSection sectionPages = configuration.getConfigurationSection("pages");
		for (String key : sectionPages.getKeys(false))
		{
			ItemStack item = new ItemStack(Material.valueOf(sectionPages.getString(key + ".material")));
			item.setDurability((short) sectionPages.getInt(key + ".nbt"));
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§rPage " + page);
			item.setItemMeta(itemMeta);
			inventory.setItem(sectionPages.getInt(key + ".slot"), item);
			//Next
			page = page + 1;
		}
		
		player.openInventory(inventory);
	}

	@EventHandler
	public void onInteractInventory(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();

		if (event.getView().getTitle() == null) return;
		if (event.getView().getTitle().equals(configuration.getString("title")))
		{
			event.setCancelled(true);
			if (event.getInventory() == null || item == null || item.getType() == Material.AIR) return;
			
			ConfigurationSection sectionPages = configuration.getConfigurationSection("pages");
			for (String key : sectionPages.getKeys(false))
			{
				if (event.getSlot() == sectionPages.getInt(key + ".slot"))
				{
					if (key.equals("after"))
					{
						if (PAGE == 0) return;
						PAGE = PAGE -1;
					}
					if (key.equals("before"))
					{
						if (PAGE == PAGES) return;
						PAGE = PAGE +1;
					}
					openInventory(player, Shop.getPlayerManager().getPlayerManager(player).getMenu(), PAGE);
				}
			}
			if (map_key.get(event.getSlot()) == null) return;
			
			InventoryVendor.openInventory(player, item);
		}
	}
}
