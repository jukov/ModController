package info.jukov.f19plugin;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import info.jukov.f19plugin.pojo.IPluginConfig;
import info.jukov.f19plugin.pojo.PluginConfig;

/**
 * User: jukov
 * Date: 20.07.2017
 * Time: 23:20
 */
public class CmdDelegate {

	private static final Text PARENT_DESCRIPTION = Text.of("Mod controller configuration.");

	private static final String PARENT_PERMISSION = "modcontroller";
	private static final String ADD_PERMISSION = "modcontroller.add";
	private static final String REMOVE_PERMISSION = "modcontroller.remove";
	private static final String RELOAD_PERMISSION = "modcontroller.reload";
	private static final String MODE_PERMISSION = "modcontroller.mode";
	private static final String LIST_PERMISSION = "modcontroller.list";

	public static final String MOD_CONTROLLER = "modcontroller";

	private static final String ADD_COMMAND = "add";
	private static final String REMOVE_COMMAND = "remove";
	private static final String LIST_COMMAND = "list";
	private static final String RELOAD_COMMAND = "reload";
	private static final String MODE_COMMAND = "mode";

	private static final String[] ADD_ALIASES = {ADD_COMMAND, "a"};
	private static final String[] REMOVE_ALIASES = {REMOVE_COMMAND, "rm"};
	private static final String[] LIST_ALIASES = {LIST_COMMAND, "l"};
	private static final String[] RELOAD_ALIASES = {RELOAD_COMMAND, "r"};
	private static final String[] MODE_ALIASES = {MODE_COMMAND, "m"};

	private static final HashMap<String, String> CMD_DESC = new HashMap<>();

	private static final HashMap<String, String> CMD_USAGE = new HashMap<>();

	static {
		CMD_DESC.put(ADD_COMMAND, "Add mod to list.");
		CMD_DESC.put(REMOVE_COMMAND, "Remove mod from list.");
		CMD_DESC.put(LIST_COMMAND, "Print list of added mods.");
		CMD_DESC.put(RELOAD_COMMAND, "Reload mods from config file.");
		CMD_DESC.put(MODE_COMMAND, "Change plugin mode. Available modes: banlist, whitelist.");

		CMD_USAGE.put(ADD_COMMAND, "/" + MOD_CONTROLLER + " <" + ADD_COMMAND + "> <modid>...");
		CMD_USAGE.put(REMOVE_COMMAND, "/" + MOD_CONTROLLER + " <" + REMOVE_COMMAND + "> <modid>...");
		CMD_USAGE.put(LIST_COMMAND, "/" + MOD_CONTROLLER + " <" + LIST_COMMAND + ">");
		CMD_USAGE.put(RELOAD_COMMAND, "/" + MOD_CONTROLLER + " <" + RELOAD_COMMAND + ">");
		CMD_USAGE.put(MODE_COMMAND, "/" + MOD_CONTROLLER + " <" + MODE_COMMAND + "> <banlist|whitelist>");
	}

	private Logger logger;

	public static void register(final Object plugin, final Logger logger, final IPluginConfig pluginConfig) {

		final CommandCallable addCallable = CommandSpec.builder()
				.permission(ADD_PERMISSION)
				.description(Text.of(CMD_USAGE.get(ADD_COMMAND)))
				.extendedDescription(Text.of(CMD_DESC.get(ADD_COMMAND)))
				.arguments(GenericArguments.remainingJoinedStrings(Text.of(ADD_COMMAND)))
				.executor(new AddCommandExecutor(logger, pluginConfig))
				.build();

		final CommandCallable removeCallable = CommandSpec.builder()
				.permission(REMOVE_PERMISSION)
				.description(Text.of(CMD_USAGE.get(REMOVE_COMMAND)))
				.extendedDescription(Text.of(CMD_DESC.get(REMOVE_COMMAND)))
				.arguments(GenericArguments.remainingJoinedStrings(Text.of(REMOVE_COMMAND)))
				.executor(new RemoveCommandExecutor(logger, pluginConfig)).build();

		final CommandCallable listCallable = CommandSpec.builder()
				.permission(LIST_PERMISSION)
				.description(Text.of(CMD_USAGE.get(LIST_COMMAND)))
				.extendedDescription(Text.of(CMD_DESC.get(LIST_COMMAND)))
				.arguments(GenericArguments.none())
				.executor(new ListCommandExecutor(logger, pluginConfig))
				.build();

		final CommandCallable reloadCallable = CommandSpec.builder()
				.permission(REMOVE_PERMISSION)
				.description(Text.of(CMD_USAGE.get(RELOAD_COMMAND)))
				.extendedDescription(Text.of(CMD_DESC.get(RELOAD_COMMAND)))
				.arguments(GenericArguments.none())
				.executor(new ReloadCommandExecutor(logger, pluginConfig))
				.build();

		final CommandCallable modeCallable = CommandSpec.builder()
				.permission(MODE_PERMISSION)
				.description(Text.of(CMD_USAGE.get(MODE_COMMAND)))
				.extendedDescription(Text.of(CMD_DESC.get(MODE_COMMAND)))
				.arguments(GenericArguments.enumValue(Text.of(MODE_COMMAND), PluginConfig.Mode.class))
				.executor(new ModeCommandExecutor(logger, pluginConfig))
				.build();

		final CommandCallable parentCallable = CommandSpec.builder()
				.permission(PARENT_PERMISSION)
				.description(PARENT_DESCRIPTION)
				.extendedDescription(getHelp())
				.child(addCallable, ADD_ALIASES)
				.child(removeCallable, REMOVE_ALIASES)
				.child(listCallable, LIST_ALIASES)
				.child(reloadCallable, RELOAD_ALIASES)
				.child(modeCallable, MODE_ALIASES)
				.build();

		Sponge.getCommandManager().register(plugin, parentCallable, MOD_CONTROLLER);
	}

	private static Text getHelp() {
		final Text.Builder textBuilder = Text.builder();
		textBuilder.append(Text.of("Available commands:\n"));
		CMD_USAGE.forEach((cmd, desc) -> textBuilder.append(Text.of(desc + "\n")));
		return textBuilder.build();
	}

	private static final class AddCommandExecutor implements CommandExecutor {

		private final Logger logger;
		private IPluginConfig pluginConfig;

		private AddCommandExecutor(final Logger logger, final IPluginConfig pluginConfig) {
			this.logger = logger;
			this.pluginConfig = pluginConfig;
		}

		@Override
		public CommandResult execute(final CommandSource src, final CommandContext args) throws CommandException {

			final String rawModIds = args.<String> getOne(ADD_COMMAND).orElse("");

			final Collection<String> modIds = Arrays.asList(rawModIds.split(" "));

			pluginConfig.addMods(modIds);
			src.sendMessage(Text.of("Plugins added."));

			return CommandResult.success();
		}
	}

	private static final class RemoveCommandExecutor implements CommandExecutor {

		private final Logger logger;
		private IPluginConfig pluginConfig;

		private RemoveCommandExecutor(final Logger logger, final IPluginConfig pluginConfig) {
			this.logger = logger;
			this.pluginConfig = pluginConfig;
		}

		@Override
		public CommandResult execute(final CommandSource src, final CommandContext args) throws CommandException {

			final String rawModIds = args.<String> getOne(REMOVE_COMMAND).orElse("");

			final Collection<String> modIds = Arrays.asList(rawModIds.split(" "));

			pluginConfig.removeMods(modIds);
			src.sendMessage(Text.of("Plugins removed."));

			return CommandResult.success();
		}
	}

	private static final class ListCommandExecutor implements CommandExecutor {

		private final Logger logger;
		private IPluginConfig pluginConfig;

		private ListCommandExecutor(final Logger logger, final IPluginConfig pluginConfig) {
			this.logger = logger;
			this.pluginConfig = pluginConfig;
		}

		@Override
		public CommandResult execute(final CommandSource src, final CommandContext args) throws CommandException {

			src.sendMessage(Text.of("Mods in list: " + pluginConfig.getMods() + "."));

			return CommandResult.success();
		}
	}

	private static final class ReloadCommandExecutor implements CommandExecutor {

		private final Logger logger;
		private IPluginConfig pluginConfig;

		private ReloadCommandExecutor(final Logger logger, final IPluginConfig pluginConfig) {
			this.logger = logger;
			this.pluginConfig = pluginConfig;
		}

		@Override
		public CommandResult execute(final CommandSource src, final CommandContext args) throws CommandException {
			pluginConfig.reload();

			src.sendMessage(Text.of("Config reloaded from drive."));

			return CommandResult.success();
		}
	}

	private static final class ModeCommandExecutor implements CommandExecutor {

		private final Logger logger;
		private IPluginConfig pluginConfig;

		private ModeCommandExecutor(final Logger logger, final IPluginConfig pluginConfig) {
			this.logger = logger;
			this.pluginConfig = pluginConfig;
		}

		@Override
		public CommandResult execute(final CommandSource src, final CommandContext args) throws CommandException {

			final Optional<IPluginConfig.Mode> optionalMode = args.getOne(MODE_COMMAND);

			pluginConfig.setMode(optionalMode.orElse(IPluginConfig.Mode.WHITELIST));

			src.sendMessage(Text.of("Mode changed to " + pluginConfig.getMode() + "."));

			return CommandResult.success();
		}
	}
}
