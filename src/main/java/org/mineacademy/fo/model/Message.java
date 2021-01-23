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
	private String[] content;
	private SimpleSound sound;

	public Message(SimpleSound sound, String... content) {
		this.content = content;
		this.sound = sound;
	}

	public Message(CompSound compSound, String... content) {
		this.content = content;
		this.sound = new SimpleSound(compSound.getSound(), 1F, 1F);
	}

	public void send(CommandSender sender) {
		Common.tell(sender, content);
		sendOtherNotifications(sender);
	}

	public void sendOtherNotifications(CommandSender sender) {
		if (sender instanceof Player)
			sound.play((Player) sender);
	}
}