package fr.frozenia.shop.inventorys;

import java.util.Arrays;
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

public class InventoryVendor implements Listener
{

	private static Shop instance;
	private static DefaultConfiguration configuration;
	private PlayerManager playerManager = new PlayerManager();
	private HashMap<Integer, Integer> item_id = new HashMap<>();
	
	public InventoryVendor(Shop main)
	{
		instance = main;
		configuration = new DefaultConfiguration(main, "InventoryVendor.yml");
		ConfigurationSection section = configuration.getConfigurationSection("Items");
		for (String key : section.getKeys(false))
		{
			int ID = section.getInt(key + ".ID");
			item_id.put(section.getInt(key + ".slot"), ID);
		}
	}
	
	public static void openInventory(Player player, ItemStack item)
	{	
		Inventory inventory = Bukkit.createInventory(null, configuration.getInt("size"), configuration.getString("title"));
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r" + meta.getDisplayName());
		meta.setLore(Arrays.asList(new String[] { "lores" }));
		item.setItemMeta(meta);
		inventory.setItem(configuration.getInt("itemSelect.slot"), item);
		
		ConfigurationSection section = configuration.getConfigurationSection("Items");
		for (String key : section.getKeys(false))
		{
			ItemStack itemOther = new ItemStack(Material.valueOf(section.getString(key + ".material")));
			itemOther.setDurability((short) section.getInt(key + ".nbt"));
			ItemMeta itemMeta = itemOther.getItemMeta();
			itemMeta.setDisplayName("§r" + section.getString(key + ".name").replaceAll("&", "§"));
			itemOther.setItemMeta(itemMeta);
			
			inventory.setItem(section.getInt(key + ".slot"), itemOther);
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
			
			if (item_id.get(event.getSlot()) == null) return;
			
			if (item_id.get(event.getSlot()) == 0)
			{
				
			}
			
			if (item_id.get(event.getSlot()) == 1)
			{
				
			}
			
			if (item_id.get(event.getSlot()) == 2)
			{
				InventoryMenu.openInventory(player, Shop.getPlayerManager().getPlayerManager(player).getMenu(), 0);
				return;
			}
		}
	}
}
