package org.mineacademy.fo.bungee.message;

import com.google.common.primitives.Primitives;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.bungee.BungeeAction;
import org.mineacademy.fo.plugin.SimplePlugin;

/**
 * Represents a in/out message with a given action and server name
 * and a safety check for writing/reading the data
 * based on the action's content.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class Message {

	/**
	 * The server name
	 */
	@Getter
	private String serverName;

	/**
	 * The action
	 */
	private BungeeAction action;

	/**
	 * The current position of writing the data based on the
	 * {@link BungeeAction#getContent()}
	 */
	private int actionHead = 0;

	/**
	 * Set the server name for this message, reason it is here:
	 * cannot read in the constructor in {@link OutgoingMessage}
	 *
	 * @param serverName
	 */
	protected final void setServerName(String serverName) {
		Valid.checkBoolean(this.serverName == null, "ServerName already set");
		Valid.checkNotNull(serverName, "Server name cannot be null!");

		this.serverName = serverName;
	}

	/**
	 * Set the action head for this message, reason it is here:
	 * static access in {@link OutgoingMessage}
	 *
	 * @param action
	 */
	protected final void setAction(String actionName) {
		final BungeeAction action = BungeeAction.getByName(actionName);

		Valid.checkNotNull(action, "Unknown action named: " + actionName + ". Available: " + Common.joinToString(SimplePlugin.getInstance().getBungeeCord().getActions()));
		setAction(action);
	}

	/**
	 * Set the action head for this message, reason it is here:
	 * static access in {@link OutgoingMessage}
	 *
	 * @param action
	 */
	protected final void setAction(BungeeAction action) {
		Valid.checkBoolean(this.action == null, "Action already set");

		this.action = action;
	}

	/**
	 * Return the bungee action
	 *
	 * @param <T>
	 * @return
	 */
	public <T extends BungeeAction> T getAction() {
		return (T) action;
	}

	/**
	 * Ensures we are reading in the correct order as the given {@link BungeeAction}
	 * specifies in its {@link BungeeAction#getContent()} getter.
	 * <p>
	 * This also ensures we are reading the correct data type (both primitives and wrappers
	 * are supported).
	 *
	 * @param typeOf
	 */
	protected final void moveHead(Class<?> typeOf) {
		Valid.checkNotNull(serverName, "Server name not set!");
		Valid.checkNotNull(action, "Action not set!");

		final Class<?>[] content = action.getContent();
		Valid.checkBoolean(actionHead < content.length, "Head out of bounds! Max data size for " + action.name() + " is " + content.length);
		Valid.checkBoolean(Primitives.wrap(content[actionHead]) == typeOf, "Unexpected data type " + typeOf + ", expected " + content[actionHead] + " for " + action.name());

		actionHead++;
	}

	/**
	 * Return the bungee channel, always returns
	 * {@link SimplePlugin#getBungee()#getChannel()}
	 *
	 * @return
	 */
	public final String getChannel() {
		return SimplePlugin.getInstance().getBungeeCord().getChannel();
	}
}
