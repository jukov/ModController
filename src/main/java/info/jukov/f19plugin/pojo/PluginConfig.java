package info.jukov.f19plugin.pojo;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * User: jukov
 * Date: 08.07.2017
 * Time: 22:07
 */
public class PluginConfig implements IPluginConfig {

	private Mode mode;

	private final SortedSet<String> modSet;

	public PluginConfig(final Mode mode, final Collection<String> modSet) {
		this.mode = mode;

		this.modSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		this.modSet.addAll(modSet);
	}

	@Override
	public Mode getMode() {
		return mode;
	}

	@Override
	public void setMode(final Mode mode) {
		this.mode = mode;
	}

	@Override
	public void addMod(final String modId) {
		modSet.add(modId);
	}

	@Override
	public void addMods(final Collection<String> modIds) {
		modSet.addAll(modIds);
	}

	@Override
	public void removeMod(final String modId) {
		modSet.remove(modId);
	}

	@Override
	public void removeMods(final Collection<String> modIds) {
		modSet.removeAll(modIds);
	}

	@Override
	public Collection<String> getMods() {
		return modSet;
	}

	@Override
	public String toString() {
		return "PluginConfig{" +
				"mode=" + mode +
				", modSet=" + modSet +
				'}';
	}

	@Override
	public void reload() {
		//Do nothing in POJO
	}
}
