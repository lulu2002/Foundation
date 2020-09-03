package org.mineacademy.fo.model;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.mineacademy.fo.remain.CompMaterial;

/**
 * 2020-09-02 12:59 PM
 */
public class SpiralLocation {

    private final Location start;
    private Location current;
    private int spacing;
    private int range = 0;
    private int direction = 1; // 1東,2南,3西,4北

    public SpiralLocation(Location start, int spacing) {
        this.start = start;
        this.current = start;
        this.spacing = spacing;
    }

    public Location next() {
        switch (direction) {
            case 1:
                current.add(spacing, 0, 0);
                break;
            case 2:
                current.add(0, 0, spacing);
                break;
            case 3:
                current.subtract(spacing, 0, 0);
                break;
            case 4:
                current.subtract(0, 0, spacing);
                break;
        }
        checkRange();

        return formattedLocation();
    }

    public Location nextEmpty() {
        while (isSolidBlockUnder())
            next();

        return formattedLocation();
    }

    public void reset() {
        current = start;
    }

    private void checkRange() {
        if (direction == 2 || direction == 4) {
            if (Math.abs(current.getBlockZ()) >= range) {
                direction = (direction == 4 ? 1 : direction + 1); // 超過或相等，不管是哪個方向都要轉彎'
                range += spacing;// 只有右下||左上才會需要增加總距離，等於每轉兩次加一次距離
            }
        } else {
            if (Math.abs(current.getBlockX()) >= range) {
                direction = (direction == 4 ? 1 : direction + 1); // 超過或相等，不管是哪個方向都要轉彎'
            }
        }
    }

    private Location formattedLocation() {
        return current.clone().add(0.5, 0.0, 0.5);
    }

    private boolean isSolidBlockUnder() {
        return CompMaterial.isAir(formattedLocation().getBlock().getRelative(BlockFace.DOWN));
    }
}