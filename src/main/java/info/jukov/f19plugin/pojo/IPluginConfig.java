package info.jukov.f19plugin.pojo;

import java.util.Collection;
import java.util.Collections;

/**
 * User: jukov
 * Date: 22.07.2017
 * Time: 15:33
 */
public interface IPluginConfig {

	enum Mode {
		WHITELIST,
		BANLIST
	}

	void setMode(final Mode mode);

	Mode getMode();

	void addMod(final String modId);

	void addMods(final Collection<String> modIds);

	void removeMod(final String modId);

	void removeMods(final Collection<String> modIds);

	Collection<String> getMods();

	void reload();
}
