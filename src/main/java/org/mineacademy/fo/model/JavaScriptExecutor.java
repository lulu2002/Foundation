package org.mineacademy.fo.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.plugin.SimplePlugin;

import lombok.NonNull;

/**
 * An engine that compiles and executes code on the fly.
 * <p>
 * The code is based off JavaScript with new Java methods, see:
 * https://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/
 */
public final class JavaScriptExecutor {

	/**
	 * The engine singleton
	 */
	private static final ScriptEngine engine;

	/**
	 * Cache scripts for 1 second per player for highest performance
	 * <p>
	 * Player -> Map of scripts and their results
	 */
	private static final Map<UUID, Map<String, Object>> resultCache = ExpiringMap.builder().expiration(1, TimeUnit.SECONDS).build();

	// Load the engine
	static {
		Thread.currentThread().setContextClassLoader(SimplePlugin.class.getClassLoader());

		final ScriptEngineManager engineManager = new ScriptEngineManager(null);
		engine = engineManager.getEngineByName("Nashorn");

		if (engine == null)
			Common.logFramed(true,
					"JavaScript placeholders will not function!",
					"",
					"Your Java version/distribution lacks",
					"the Nashorn library for JavaScript",
					"placeholders. Ensure you have Oracle",
					"Java 8.");
	}

	/**
	 * Compiles and executes the given JavaScript code
	 *
	 * @param javascript
	 * @return
	 */
	public static Object run(String javascript) {
		return run(javascript, null, null);
	}

	/**
	 * Runs the given JavaScript code for the player,
	 * making the "player" variable in the code usable
	 *
	 * @param javascript
	 * @param sender
	 * @return
	 */
	public static Object run(String javascript, CommandSender sender) {
		return run(javascript, sender, null);
	}

	/**
	 * Compiles and executes the Javascript code for the player ("player" variable is put into the JS code)
	 * as well as the bukkit event (use "event" variable there)
	 *
	 * @param javascript
	 * @param sender
	 * @param event
	 * @return
	 */
	public static Object run(@NonNull String javascript, CommandSender sender, Event event) {

		// Cache for highest performance
		Map<String, Object> cached = sender instanceof Player ? resultCache.get(((Player) sender).getUniqueId()) : null;

		if (cached != null) {
			final Object result = cached.get(javascript);

			if (result != null)
				return result;
		}

		try {
			engine.getBindings(ScriptContext.ENGINE_SCOPE).clear();

			if (sender != null)
				engine.put("player", sender);

			if (event != null)
				engine.put("event", event);

			final Object result = engine.eval(javascript);

			if (sender instanceof Player) {
				if (cached == null)
					cached = new HashMap<>();

				cached.put(javascript, result);
				resultCache.put(((Player) sender).getUniqueId(), cached);
			}

			return result;

		} catch (final ScriptException ex) {
			Common.error(ex,
					"Script executing failed!",
					"Script: " + javascript,
					"%error");

			return null;
		}
	}

	/**
	 * Executes the Javascript code with the given variables - you have to handle the error yourself
	 *
	 * @param javascript
	 * @param replacements
	 * @return
	 * @throws ScriptException
	 */
	public static Object run(String javascript, Map<String, Object> replacements) throws ScriptException {
		engine.getBindings(ScriptContext.ENGINE_SCOPE).clear();

		if (replacements != null)
			for (final Map.Entry<String, Object> replacement : replacements.entrySet())
				engine.put(replacement.getKey(), replacement.getValue());

		return engine.eval(javascript);
	}
}