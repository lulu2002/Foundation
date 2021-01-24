package org.mineacademy.fo.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompSound;

/**
 * 2020-06-15 下午 02:41
 */
@Getter
@Setter
public class Message {
	private SimpleReplacer content;
	private SimpleSound sound;

	public Message(SimpleSound sound, String... content) {
		this.content = new SimpleReplacer(content);
		this.sound = sound;
	}

	public Message(CompSound compSound, String... content) {
		this.content = new SimpleReplacer(content);
		this.sound = new SimpleSound(compSound.getSound(), 1F, 1F);
	}

	public Message(String... content) {
		this.content = new SimpleReplacer(content);
		this.sound = new SimpleSound("none");
	}

	public Message replace(String from, Object to) {
		this.content.replace(from, to);

		return this;
	}

	public void send(CommandSender sender) {
		Common.tell(sender, getContent());
		sendOtherNotifications(sender);
	}

	public String[] getContent() {
		return content.toArray();
	}

	public String toString() {
		return String.join("\n", getContent());
	}

	public void sendOtherNotifications(CommandSender sender) {
		if (sender instanceof Player)
			sound.play((Player) sender);
	}
}