package org.mineacademy.fo.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.SimpleLocalization;
import org.mineacademy.fo.settings.SimpleLocalization.Commands;

/**
 * A simple predefined command for quickly reloading the plugin
 * using /{label} reload|rl
 */
public final class ReloadCommand extends SimpleSubCommand {

	public ReloadCommand() {
		super("reload|rl");

		setDescription(Commands.RELOAD_DESCRIPTION);
	}

	@Override
	protected void onCommand() {
		try {
			tell(Commands.RELOAD_STARTED);

			// Syntax check YML files before loading
			boolean syntaxParsed = true;

			final List<File> yamlFiles = new ArrayList<>();

			collectYamlFiles(SimplePlugin.getData(), yamlFiles);

			for (final File file : yamlFiles) {
				try {
					FileUtil.loadConfigurationStrict(file);

				} catch (final Throwable t) {
					t.printStackTrace();

					syntaxParsed = false;
				}
			}

			if (!syntaxParsed) {
				tell(SimpleLocalization.Commands.RELOAD_FILE_LOAD_ERROR);

				return;
			}

			SimplePlugin.getInstance().reload();
			tell(SimpleLocalization.Commands.RELOAD_SUCCESS);

		} catch (final Throwable t) {
			tell(SimpleLocalization.Commands.RELOAD_FAIL.replace("{error}", t.getMessage() != null ? t.getMessage() : "unknown"));

			t.printStackTrace();
		}
	}

	/*
	 * Get a list of all files ending with "yml" in the given directory
	 * and its subdirectories
	 */
	private List<File> collectYamlFiles(File directory, List<File> list) {

		for (final File file : directory.listFiles()) {
			if (file.getName().endsWith("yml"))
				list.add(file);

			if (file.isDirectory())
				collectYamlFiles(file, list);
		}

		return list;
	}

	/**
	 * @see org.mineacademy.fo.command.SimpleCommand#tabComplete()
	 */
	@Override
	protected List<String> tabComplete() {
		return NO_COMPLETE;
	}
}