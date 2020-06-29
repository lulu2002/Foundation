package org.mineacademy.fo.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

/**
 * 2020-06-15 下午 02:41
 */
@Getter
@Setter
public class Message {
    private String content;
    private SimpleSound sound;

    public void send(Player... players) {
        for (Player player : players) {
            Common.tell(player, content);

            if (sound != null)
                sound.play(player);
        }
    }
}