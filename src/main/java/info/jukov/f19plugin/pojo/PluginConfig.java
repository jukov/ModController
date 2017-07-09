package info.jukov.f19plugin.pojo;

import java.util.List;

/**
 * User: jukov
 * Date: 08.07.2017
 * Time: 22:07
 */
public class PluginConfig {

	public enum Mode {
		WHITELIST,
		BLACKLIST;

		public static Mode get(final int index) {
			if (index >= 0 && index < Mode.values().length) {
				return Mode.values()[index];
			}
			throw new IllegalStateException();
		}
	}

	private final Mode mode;
	private final List<ModData> modList;
	private final String kickMessage;

	public PluginConfig(final Mode mode, final List<ModData> modList, final String kickMessage) {
		this.mode = mode;
		this.modList = modList;
		this.kickMessage = kickMessage;
	}

	public Mode getMode() {
		return mode;
	}

	public List<ModData> getModList() {
		return modList;
	}

	public String getKickMessage() {
		return kickMessage;
	}

	@Override
	public String toString() {
		return "PluginConfig{" +
				"mode=" + mode +
				", modList=" + modList +
				", kickMessage='" + kickMessage + '\'' +
				'}';
	}
}
