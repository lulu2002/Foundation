package org.mineacademy.fo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * A very simple way of sending interactive chat messages
 */
public final class SimpleComponent {

	/**
	 * The past components having different hover/click events
	 */
	private final List<TextComponent> pastComponents = new ArrayList<>();

	/**
	 * The current component that is being modified
	 */
	private TextComponent currentComponent;

	/**
	 * Create a new empty component
	 */
	public SimpleComponent() {
		this("");
	}

	/**
	 * Create a new interactive chat component
	 *
	 * @param text
	 */
	private SimpleComponent(String... text) {
		this.currentComponent = new TextComponent(String.join("\n", Common.colorize(text)));
	}

	// --------------------------------------------------------------------
	// Events
	// --------------------------------------------------------------------

	/**
	 * Add a show text hover event for the {@link #currentComponent}
	 *
	 * @param texts
	 * @return
	 */
	public SimpleComponent onHover(Collection<String> texts) {
		return onHover(texts.toArray(new String[texts.size()]));
	}

	/**
	 * Add a show text hover event for the {@link #currentComponent}
	 *
	 * @param text
	 * @return
	 */
	public SimpleComponent onHover(String... text) {
		return onHover(HoverEvent.Action.SHOW_TEXT, String.join("\n", text));
	}

	/**
	 * Shows the item on hover if it is not air.
	 *
	 * NB: Some colors from lore may get lost as a result of Minecraft/Spigot bug.
	 *
	 * @param item
	 * @return
	 */
	public SimpleComponent onHover(ItemStack item) {
		return CompMaterial.isAir(item.getType()) ? onHover("Air") : onHover(HoverEvent.Action.SHOW_ITEM, Remain.toJson(item));
	}

	/**
	 * Add a hover event for the {@link #currentComponent}
	 *
	 * @param action
	 * @param text
	 * @return
	 */
	public SimpleComponent onHover(HoverEvent.Action action, String text) {
		currentComponent.setHoverEvent(new HoverEvent(action, TextComponent.fromLegacyText(Common.colorize(text))));

		return this;
	}

	/**
	 * Add a run command event for the {@link #currentComponent}
	 *
	 * @param text
	 * @return
	 */
	public SimpleComponent onClickRunCmd(String text) {
		return onClick(Action.RUN_COMMAND, text);
	}

	/**
	 * Add a suggest command event for the {@link #currentComponent}
	 *
	 * @param text
	 * @return
	 */
	public SimpleComponent onClickSuggestCmd(String text) {
		return onClick(Action.SUGGEST_COMMAND, text);
	}

	/**
	 * Open the given URL for the {@link #currentComponent}
	 *
	 * @param url
	 * @return
	 */
	public SimpleComponent onClickOpenUrl(String url) {
		return onClick(Action.OPEN_URL, url);
	}

	/**
	 * Add a command event for the {@link #currentComponent}
	 *
	 * @param action
	 * @param text
	 * @return
	 */
	public SimpleComponent onClick(Action action, String text) {
		currentComponent.setClickEvent(new ClickEvent(action, Common.colorize(text)));

		return this;
	}

	// --------------------------------------------------------------------
	// Building
	// --------------------------------------------------------------------

	/**
	 * Create another component. The current is put in a list of past components
	 * so next time you use onClick or onHover, you will be added the event to the new one
	 * specified here
	 *
	 * @param text
	 * @return
	 */
	public SimpleComponent append(String text) {
		pastComponents.add(currentComponent);
		currentComponent = new TextComponent(Common.colorize(text));

		return this;
	}

	/**
	 * Form a single {@link TextComponent} out of all components created
	 *
	 * @return
	 */
	public TextComponent build() {
		final TextComponent mainComponent = new TextComponent("");

		for (final TextComponent pastComponent : pastComponents)
			mainComponent.addExtra(pastComponent);

		mainComponent.addExtra(currentComponent);

		return mainComponent;
	}

	/**
	 * Return the plain colorized message combining all components into one
	 * without click/hover events
	 *
	 * @return
	 */
	public String getPlainMessage() {
		return build().toLegacyText();
	}

	// --------------------------------------------------------------------
	// Sending
	// --------------------------------------------------------------------

	/**
	 * Attempts to send the complete {@link SimpleComponent} to the given
	 * command senders. If they are players, we send them interactive elements.
	 *
	 * If they are console, they receive a plain text message.
	 *
	 * @param <T>
	 * @param senders
	 */
	public <T extends CommandSender> void send(Iterable<T> senders) {
		final TextComponent mainComponent = build();

		for (final CommandSender sender : senders)
			Remain.sendComponent(sender, mainComponent);
	}

	/**
	 * Attempts to send the complete {@link SimpleComponent} to the given
	 * command senders. If they are players, we send them interactive elements.
	 *
	 * If they are console, they receive a plain text message.
	 *
	 * @param senders
	 */
	public <T extends CommandSender> void send(T... senders) {
		final TextComponent mainComponent = build();

		for (final CommandSender sender : senders)
			Remain.sendComponent(sender, mainComponent);
	}

	// --------------------------------------------------------------------
	// Static
	// --------------------------------------------------------------------

	/**
	 * Create a new interactive chat component
	 * You can then build upon your text to add interactive elements
	 *
	 * @param text
	 * @return
	 */
	public static SimpleComponent of(String... text) {
		return new SimpleComponent(text);
	}
}
