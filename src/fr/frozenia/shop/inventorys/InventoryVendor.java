package fr.frozenia.shop.inventorys;

import java.util.Arrays;

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
	private static int numberItem = 1;
	private static ItemStack ITEM;
	
	public InventoryVendor(Shop main)
	{
		instance = main;
		configuration = new DefaultConfiguration(main, "InventoryVendor.yml");
	}
	
	public static void openInventory(Player player, ItemStack item)
	{
		Inventory inventory = Bukkit.createInventory(null, configuration.getInt("size"), configuration.getString("title"));
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r" + meta.getDisplayName());
		meta.setLore(Arrays.asList(new String[] { "", "§8Nombre: §6" + numberItem, "", "§8prix §6Achat", "§8prix §6Vente" }));
		item.setItemMeta(meta);
		inventory.setItem(configuration.getInt("itemSelect.slot"), item);
	
		ITEM = item;
		
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
			
			ConfigurationSection section = configuration.getConfigurationSection("Items");
			for (String key : section.getKeys(false))
			{
				if (event.getSlot() == section.getInt(key + ".slot"))
				{
					switch (key)
					{
						case "itemClear":
							numberItem = 1;
							break;
						case "itemVendor":
							break;
						case "itemBack":
							InventoryMenu.openInventory(player, Shop.getPlayerManager().getPlayerManager(player).getMenu(), 0);
							break;
					}
					int nombre = section.getInt(key + ".number");
					if (key.startsWith("buy"))
					{
						if (numberItem >= 2304) return;
						numberItem = numberItem + nombre;
						if (numberItem >= 2304) numberItem = 2304;
					}
					if (key.startsWith("sell"))
					{
						if (numberItem <= 0) return;
						numberItem = numberItem - nombre;
						if (numberItem <= 0) numberItem = 1;
					}
					System.out.println(nombre);
				}
			}
			ItemMeta itemMeta = ITEM.getItemMeta();
			itemMeta.setLore(Arrays.asList(new String[] { "", "§8Nombre: §6" + numberItem, "", "§8prix §6Achat", "§8prix §6Vente" }));
			ITEM.setItemMeta(itemMeta);
			event.getView().setItem(configuration.getInt("itemSelect.slot"), ITEM);
		}
	}
}
