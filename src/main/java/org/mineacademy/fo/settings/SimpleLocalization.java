package org.mineacademy.fo.settings;

import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.plugin.SimplePlugin;

/**
 * A simple implementation of a basic localization file.
 * We create the localization/messages_LOCALEPREFIX.yml file
 * automatically and fill it with values from your localization/messages_LOCALEPREFIX.yml
 * file placed within in your plugins jar file.
 */
@SuppressWarnings("unused")
public abstract class SimpleLocalization extends YamlStaticConfig {

	/**
	 * A flag indicating that this class has been loaded
	 *
	 * You can place this class to {@link SimplePlugin#getSettingsClasses()} to make
	 * it load automatically
	 */
	private static boolean localizationClassCalled;

	// --------------------------------------------------------------------
	// Loading
	// --------------------------------------------------------------------

	/**
	 * Create and load the localization/messages_LOCALEPREFIX.yml file.
	 *
	 * See {@link SimpleSettings#LOCALE_PREFIX} for the locale prefix.
	 *
	 * The locale file is extracted from your plugins jar to the localization/ folder
	 * if it does not exists, or updated if it is out of date.
	 */
	@Override
	protected final void load() throws Exception {
		createLocalizationFile(SimpleSettings.LOCALE_PREFIX);
	}

	// --------------------------------------------------------------------
	// Version
	// --------------------------------------------------------------------

	/**
	 * The configuration version number, found in the "Version" key in the file.,
	 */
	protected static Integer VERSION;

	/**
	 * Set and update the config version automatically, however the {@link #VERSION} will
	 * contain the older version used in the file on the disk so you can use
	 * it for comparing in the init() methods
	 *
	 * Please call this as a super method when overloading this!
	 */
	@Override
	protected void beforeLoad() {
		// Load version first so we can use it later
		pathPrefix(null);

		if ((VERSION = getInteger("Version")) != getConfigVersion())
			set("Version", getConfigVersion());
	}

	/**
	 * Return the very latest config version
	 *
	 * Any changes here must also be made to the "Version" key in your settings file.
	 *
	 * @return
	 */
	protected abstract int getConfigVersion();

	// --------------------------------------------------------------------
	// Shared values
	// --------------------------------------------------------------------

	// NB: Those keys are optional - you do not have to write them into your messages_X.yml files
	// but if you do, we will use your values instead of the default ones!

	/**
	 * Locale keys related to your plugin commands
	 */
	public static class Commands {

		/**
		 * The message at "No_Console" key shown when console is denied executing a command.
		 */
		public static String NO_CONSOLE = "&c不好意思，只有玩家才能執行此指令。";

		/**
		 * The message shown when there is a fatal error running this command
		 */
		public static String COOLDOWN_WAIT = "&c請稍等 {duration} 秒後再使用這個指令。";

		/**
		 * The message shown when the player tries a command but inputs an
		 * invalid first argument parameter. We suggest he types /{label} ? for help so make
		 * sure you implement some help there as well.
		 *
		 */
		public static String INVALID_ARGUMENT = "&c無法辨識的指令參數，請輸入 &6/{label} ? &c來獲得指令參數列表。";

		/**
		 * The message shown when the player tries a command but inputs an
		 * invalid second argument parameter. We so suggest he types /{label} {0} for help
		 *
		 */
		public static String INVALID_SUB_ARGUMENT = "&c無法辨識的指令參數，請輸入 '/{label} {0}' 來獲得參數使用方次。";

		/**
		 * The message shown on the same occasion as {@link #INVALID_ARGUMENT} however
		 * this is shows when the command overrides {@link SimpleCommand#getMultilineUsageMessage()} ()}
		 *
		 */
		public static String INVALID_ARGUMENT_MULTILINE = "&c無法辨識的指令參數，以下為協助資訊:";

		/**
		 * The description label
		 */
		public static String LABEL_DESCRIPTION = "&c說明: {description}";

		/**
		 * The multiline usages label, see {@link SimpleCommand#getMultilineUsageMessage()}
		 */
		public static String LABEL_USAGES = "&c使用方式:";

		/**
		 * The usage label
		 */
		public static String LABEL_USAGE = "&c使用方式:";

		/**
		 * The message at "Reload_Success" key shown when the plugin has been reloaded successfully.
		 */
		public static String RELOAD_SUCCESS = "&6{plugin_name} {plugin_version} 已重新載入。";

		/**
		 * The message at "Reload_Fail" key shown when the plugin has failed to reload.
		 */
		public static String RELOAD_FAIL = "&4哇糟糕，&c插件在載入過程中發生了錯誤，請查看後台獲取詳細資訊，錯誤類型為: {error}";

		/**
		 * The message shown when there is a fatal error running this command
		 */
		public static Replacer ERROR = Replacer.of("&4&l哇糟糕，&c指令在運作時發生了錯誤，請查看後台獲取詳細錯誤訊息。");

		/**
		 * Load the values -- this method is called automatically by reflection in the {@link YamlStaticConfig} class!
		 */
		private static void init() {
			pathPrefix("Commands");

			if (isSetDefault("No_Console"))
				NO_CONSOLE = getString("No_Console");

			if (isSetDefault("Cooldown_Wait"))
				COOLDOWN_WAIT = getString("Cooldown_Wait");

			if (isSetDefault("Invalid_Argument"))
				INVALID_ARGUMENT = getString("Invalid_Argument");

			if (isSetDefault("Invalid_Sub_Argument"))
				INVALID_SUB_ARGUMENT = getString("Invalid_Sub_Argument");

			if (isSetDefault("Invalid_Argument_Multiline"))
				INVALID_ARGUMENT_MULTILINE = getString("Invalid_Argument_Multiline");

			if (isSetDefault("Label_Description"))
				LABEL_DESCRIPTION = getString("Label_Description");

			if (isSetDefault("Label_Usage"))
				LABEL_USAGE = getString("Label_Usage");

			if (isSetDefault("Label_Usages"))
				LABEL_USAGES = getString("Label_Usages");

			if (isSetDefault("Reload_Success"))
				RELOAD_SUCCESS = getString("Reload_Success");

			if (isSetDefault("Reload_Fail"))
				RELOAD_FAIL = getString("Reload_Fail");

			if (isSetDefault("Error"))
				ERROR = getReplacer("Error");
		}
	}

	/**
	 * Key related to players
	 */
	public static class Player {

		/**
		 * Message shown when the player is not online on this server
		 */
		public static String NOT_ONLINE = "&c名為 {player} &c的玩家並不在線。";

		/**
		 * Load the values -- this method is called automatically by reflection in the {@link YamlStaticConfig} class!
		 */
		private static void init() {
			pathPrefix("Player");

			if (isSetDefault("Not_Online"))
				NOT_ONLINE = getString("Not_Online");
		}
	}

	/**
	 * Key related to the GUI system
	 */
	public static class Menu {

		/**
		 * Message shown when the player is not online on this server
		 */
		public static String ITEM_DELETED = "&2{item} 已刪除完成。";

		/**
		 * Load the values -- this method is called automatically by reflection in the {@link YamlStaticConfig} class!
		 */
		private static void init() {
			pathPrefix("Menu");

			if (isSetDefault("Item_Deleted"))
				ITEM_DELETED = getString("Item_Deleted");
		}
	}

	/**
	 * Keys related to updating the plugin
	 */
	public static class Update {

		/**
		 * The message if a new version is found but not downloaded
		 */
		public static String AVAILABLE = "&3{plugin_name}&2 已有新版本可供下載。\n"
				+ "&2您目前使用的版本: &f{current}&2; 新版本: &f{new}\n"
				+ "&2下載網址: &7https://www.spigotmc.org/resources/{resource_id}/.";

		/**
		 * The message if a new version is found and downloaded
		 */
		public static String DOWNLOADED = "&3{plugin_name}&2 的版本已從 {current} 更新至 {new}。\n"
				+ "&2請重新啟動伺服器讓新版本得以套用。"
				+ "&2同時，若想得知詳細更新內容，歡迎前往 &7https://www.spigotmc.org/resources/{resource_id} &2獲得相關資訊。\n";

		/**
		 * Load the values -- this method is called automatically by reflection in the {@link YamlStaticConfig} class!
		 */
		private static void init() {
			// Upgrade from old path
			if (isSetAbsolute("Update_Available")) {
				pathPrefix(null);

				move("Update_Available", "Update.Available");
			}

			pathPrefix("Update");

			if (isSetDefault("Available"))
				AVAILABLE = getString("Available");

			if (isSetDefault("Downloaded"))
				DOWNLOADED = getString("Downloaded");
		}
	}

	/**
	 * The message for player if they lack a permission.
	 */
	public static String NO_PERMISSION = "&c抱歉，你沒有權限使用這項功能 ({permission})。";

	/**
	 * The server prefix. Example: you have to use it manually if you are sending messages
	 * from the console to players
	 */
	public static String SERVER_PREFIX = "[伺服器]";

	/**
	 * The console localized name. Example: Console
	 */
	public static String CONSOLE_NAME = "後台";

	/**
	 * The message when a section is missing from data.db file (typically we use
	 * this file to store serialized values such as arenas from minigame plugins).
	 */
	public static String DATA_MISSING = "&c{name} lacks database information! Please only create {type} in-game! Skipping..";

	/**
	 * The message when the console attempts to start a server conversation which is prevented.
	 */
	public static String CONVERSATION_REQUIRES_PLAYER = "不好意思，只有玩家才能使用對話功能。";

	/**
	 * Load the values -- this method is called automatically by reflection in the {@link YamlStaticConfig} class!
	 */
	private static void init() {
		pathPrefix(null);
		Valid.checkBoolean(!localizationClassCalled, "語言文件已經載入完成！");

		if (isSetDefault("No_Permission"))
			NO_PERMISSION = getString("No_Permission");

		if (isSetDefault("Server_Prefix"))
			SERVER_PREFIX = getString("Server_Prefix");

		if (isSetDefault("Console_Name"))
			CONSOLE_NAME = getString("Console_Name");

		if (isSetDefault("Data_Missing"))
			DATA_MISSING = getString("Data_Missing");

		if (isSetDefault("Conversation_Requires_Player"))
			CONVERSATION_REQUIRES_PLAYER = getString("Conversation_Requires_Player");

		localizationClassCalled = true;
	}

	/**
	 * Was this class loaded?
	 *
	 * @return
	 */
	public static final Boolean isLocalizationCalled() {
		return localizationClassCalled;
	}

	/**
	 * Reset the flag indicating that the class has been loaded,
	 * used in reloading.
	 */
	public static final void resetLocalizationCall() {
		localizationClassCalled = false;
	}
}
