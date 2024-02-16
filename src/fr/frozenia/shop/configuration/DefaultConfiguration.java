package fr.frozenia.shop.configuration;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultConfiguration extends YamlConfiguration
{
	private File fileConfiguration;
	private JavaPlugin instance;
	
	private String file_name;
	
	public DefaultConfiguration(JavaPlugin main, String file_name)
	{
		this.instance = main;
		this.file_name = file_name;
		this.fileConfiguration = new File(this.instance.getDataFolder(), file_name);
		
		if (!new File(this.instance.getDataFolder(), file_name).exists())
		{
			saveDefault();
		}
		
		reload();
	}
	
	public void reload()
	{
		try
		{
			super.load(this.fileConfiguration);
		}
		catch (Exception exeption)
		{
			exeption.printStackTrace();
		}
	}
	
	public void saveDefault()
	{
		this.instance.saveResource(file_name, false);
	}
}
