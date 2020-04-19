package org.mineacademy.fo.model;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class ChunkKeeper {
    private static final Keeper keeper = MinecraftVersion.atLeast(MinecraftVersion.V.v1_14) ? new ChunkKeeperNew() : new ChunkKeeperOld();

    public static void setForceLoaded(Chunk chunk, boolean b) {
        if (b)
            keeper.keep(chunk);
        else
            keeper.unKeep(chunk);
    }

    private interface Keeper {
        void keep(Chunk chunk);

        void unKeep(Chunk chunk);
    }

    private static class ChunkKeeperNew implements Keeper {

        @Override
        public void keep(Chunk chunk) {
            chunk.setForceLoaded(true);
        }

        @Override
        public void unKeep(Chunk chunk) {
            chunk.setForceLoaded(false);
        }
    }

    private static class ChunkKeeperOld implements Listener, Keeper {
        private final Set<Chunk> keepChunks = new HashSet<>();
        private final Method cancelMethod = ReflectionUtil.getMethod(ChunkUnloadEvent.class, "setCancelled");

        public ChunkKeeperOld() {
            Common.registerEvents(this);
        }

        @Override
        public void keep(Chunk chunk) {
            keepChunks.add(chunk);
        }

        @Override
        public void unKeep(Chunk chunk) {
            keepChunks.remove(chunk);
        }

        @EventHandler
        public void onChunkUnload(ChunkUnloadEvent e) {
            if (keepChunks.contains(e.getChunk())) {
                try {
                    cancelMethod.invoke(e, true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
