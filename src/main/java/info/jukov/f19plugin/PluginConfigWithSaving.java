package info.jukov.f19plugin;

import java.util.Collection;

import info.jukov.f19plugin.pojo.IPluginConfig;

/**
 * User: jukov
 * Date: 22.07.2017
 * Time: 15:39
 */
public class PluginConfigWithSaving implements IPluginConfig {

	private final ConfigStorageDelegate configStorageDelegate;

	private IPluginConfig pluginConfig;

	public PluginConfigWithSaving(final ConfigStorageDelegate configStorageDelegate) {
		this.configStorageDelegate = configStorageDelegate;

		this.pluginConfig = configStorageDelegate.getConfig();
	}

	@Override
	public void setMode(final Mode mode) {
		pluginConfig.setMode(mode);

		configStorageDelegate.saveConfig(pluginConfig);
	}

	@Override
	public Mode getMode() {
		return pluginConfig.getMode();
	}

	@Override
	public void addMod(final String modId) {
		pluginConfig.addMod(modId);

		configStorageDelegate.saveConfig(pluginConfig);
	}

	@Override
	public void addMods(final Collection<String> modIds) {
		pluginConfig.addMods(modIds);

		configStorageDelegate.saveConfig(pluginConfig);
	}

	@Override
	public void removeMod(final String modId) {
		pluginConfig.removeMod(modId);

		configStorageDelegate.saveConfig(pluginConfig);
	}

	@Override
	public void removeMods(final Collection<String> modIds) {
		pluginConfig.removeMods(modIds);

		configStorageDelegate.saveConfig(pluginConfig);
	}

	@Override
	public Collection<String> getMods() {
		return pluginConfig.getMods();
	}

	@Override
	public void reload() {
		pluginConfig = configStorageDelegate.getConfig();
	}
}
