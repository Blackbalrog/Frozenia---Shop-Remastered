package fr.frozenia.shop.managers;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Player;

public class PlayerManager
{
	private HashMap<Player, PlayerManager> manager = new HashMap<>();
	private Player player;
	private String inventoryTitle;
	
	public void addPlayer(Player player)
	{
		if (!this.manager.containsKey(player)) this.manager.put(player, this);
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public void addMenu(String inventoryTitle)
	{
		this.inventoryTitle = inventoryTitle;
	}
	
	public String getMenu()
	{
		return this.inventoryTitle;
	}
	
	public boolean contains(Player player)
	{
		return this.manager.containsKey(player) ? true : false;
	}
	
	public void clear()
	{
		this.manager.remove(this.player);
	}
	
	public Set<Entry<Player, PlayerManager>> getMap()
	{
		return this.manager.entrySet();
	}
	
	public PlayerManager getPlayerManager(Player player)
	{
		return this.manager.get(player);
	}
}
