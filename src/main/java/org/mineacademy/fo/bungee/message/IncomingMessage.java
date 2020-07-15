package org.mineacademy.fo.bungee.message;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.bungee.BungeeAction;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.plugin.SimplePlugin;

/**
 * Represents an incoming plugin message.
 * <p>
 * NB: This uses the standardized Foundation model where the first
 * string is the server name and the second string is the
 * {@link BungeeAction} by its name *read automatically*.
 */
public final class IncomingMessage extends Message {

	/**
	 * The raw byte array to read from
	 */
	@Getter
	private final byte[] data;

	/**
	 * The input we use to read our data array
	 */
	private final ByteArrayDataInput input;

	/**
	 * Create a new incoming message from the given array
	 * <p>
	 * NB: This uses the standardized Foundation model where the first
	 * string is the server name and the second string is the
	 * {@link BungeeAction} by its name *read automatically*.
	 *
	 * @param data
	 */
	public IncomingMessage(byte[] data) {
		this.data = data;
		this.input = ByteStreams.newDataInput(data);

		// -----------------------------------------------------------------
		// We are automatically reading the first two strings assuming the
		// first is the senders server name and the second is the action
		// -----------------------------------------------------------------

		// Read server name
		setServerName(input.readUTF());

		// Read action
		setAction(input.readUTF());
	}

	/**
	 * Read a string from the data
	 *
	 * @return
	 */
	public String readString() {
		moveHead(String.class);

		return input.readUTF();
	}

	/**
	 * Read a boolean from the data
	 *
	 * @return
	 */
	public boolean readBoolean() {
		moveHead(Boolean.class);

		return input.readBoolean();
	}

	/**
	 * Read a byte from the data
	 *
	 * @return
	 */
	public byte readByte() {
		moveHead(Byte.class);

		return input.readByte();
	}

	/**
	 * Read a double from the data
	 *
	 * @return
	 */
	public double readDouble() {
		moveHead(Double.class);

		return input.readDouble();
	}

	/**
	 * Read a float from the data
	 *
	 * @return
	 */
	public float readFloat() {
		moveHead(Float.class);

		return input.readFloat();
	}

	/**
	 * Read an integer from the data
	 *
	 * @return
	 */
	public int writeInt() {
		moveHead(Integer.class);

		return input.readInt();
	}

	/**
	 * Read a long from the data
	 *
	 * @return
	 */
	public long readLong() {
		moveHead(Long.class);

		return input.readLong();
	}

	/**
	 * Read a short from the data
	 *
	 * @return
	 */
	public short readShort() {
		moveHead(Short.class);

		return input.readShort();
	}

	/**
	 * Forwards this message to a player
	 *
	 * @param connection
	 */
	public void forward(Player player) {
		player.sendPluginMessage(SimplePlugin.getInstance(), getChannel(), data);

		Debugger.debug("bungee", "Forwarding data on " + getChannel() + " channel from " + getAction() + " as " + player.getName() + " player to BungeeCord.");
	}
}