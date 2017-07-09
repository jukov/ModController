package info.jukov.f19plugin;

import com.google.common.reflect.TypeToken;

import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import info.jukov.f19plugin.pojo.ModData;
import info.jukov.f19plugin.pojo.PluginConfig;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

/**
 * User: jukov
 * Date: 08.07.2017
 * Time: 21:47
 */
public class ConfigManager {

	private static final String NODE_MODE = "mode";
	private static final String NODE_MOD_LIST = "mod_list";
	private static final String NODE_KICK_MESSAGE = "kick_message";

	private static final String KICK_MESSAGE_DEFAULT = "These mods not allowed: %s";

	private final Logger logger;

	private final Path defaultConfigPath;
	private final ConfigurationLoader<CommentedConfigurationNode> configurationLoader;

	public ConfigManager(final Logger logger, final Path defaultConfigPath) {
		this.logger = logger;
		this.defaultConfigPath = defaultConfigPath;

		configurationLoader = HoconConfigurationLoader.builder().setPath(defaultConfigPath).build();
	}

	public PluginConfig getConfig() {

		final ConfigurationNode rootNode;

		if (!Files.exists(defaultConfigPath)) {
			rootNode = getDefaultConfig();
			saveNode(rootNode);
			logger.info("exists");
		} else {
			try {
				logger.info("not exists");
				rootNode = configurationLoader.load();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}

		return makePluginConfig(rootNode);
	}

	public void saveNode(final ConfigurationNode configurationNode) {
		try {
			configurationLoader.save(configurationNode);
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private ConfigurationNode getDefaultConfig() {
		ConfigurationNode rootNode = configurationLoader.createEmptyNode(ConfigurationOptions.defaults());

		rootNode.getNode(NODE_MODE).setValue(PluginConfig.Mode.WHITELIST.ordinal());
		rootNode.getNode(NODE_MOD_LIST).setValue(new ArrayList<ModData>());
		rootNode.getNode(NODE_KICK_MESSAGE).setValue(KICK_MESSAGE_DEFAULT);

		return rootNode;
	}

	private static PluginConfig makePluginConfig(final ConfigurationNode rootNode) {
		try {
			return new PluginConfig(
					PluginConfig.Mode.get(rootNode.getNode(NODE_MODE).getInt()),
					rootNode.getNode(NODE_MOD_LIST).getList(TypeToken.of(ModData.class)),
					rootNode.getNode(NODE_KICK_MESSAGE).getString());
		} catch (final ObjectMappingException e) {
			throw new IllegalStateException(e);
		}
	}
}
