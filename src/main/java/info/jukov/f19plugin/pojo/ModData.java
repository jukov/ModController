package info.jukov.f19plugin.pojo;

/**
 * User: jukov
 * Date: 08.07.2017
 * Time: 22:01
 */
public class ModData {

	private final String name;
	private final String version;

	public ModData(final String name, final String version) {
		this.name = name;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return "ModData{" +
				"name='" + name + '\'' +
				", version='" + version + '\'' +
				'}';
	}
}
