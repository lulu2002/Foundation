package org.mineacademy.fo.remain;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.MinecraftVersion.V;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.exception.FoException;

import java.lang.reflect.Method;

/**
 * Advanced spawning of entities, enables manipulation of
 * an entity before it's added to the world.
 */
public final class NmsEntity {

	/**
	 * The world to add the entity to
	 */
	private final World bukkitWorld;

	/**
	 * The NMS entity class
	 */
	@Getter
	private final Object nmsEntity;

	/**
	 * Create an entity at X:0 Y:0 Z:0 in the first existing world
	 * You can use {@link EntityType#getEntityClass()} to get the class
	 *
	 * @param entityClass
	 */
	public NmsEntity(final Class<?> entityClass) {
		this(new Location(Bukkit.getWorlds().get(0), 0, 0, 0), entityClass);
	}

	/**
	 * Create an entity at the given location
	 * You can use {@link EntityType#getEntityClass()} to get the class
	 *
	 * @param location
	 * @param entityClass
	 */
	public NmsEntity(final Location location, final Class<?> entityClass) {
		try {
			NmsAccessor.call();
		} catch (final Throwable t) {
			throw new FoException(t, "Failed to setup entity reflection! MC version: " + MinecraftVersion.getCurrent());
		}

		this.bukkitWorld = location.getWorld();
		this.nmsEntity = MinecraftVersion.equals(V.v1_7) ? getHandle(location, entityClass) : createEntity(location, entityClass);
	}

	//
	// Return the entity handle, used for MC 1.7.10 to add entity
	//
	private static Object getHandle(final Location location, final Class<?> entityClass) {
		final Entity entity = new Location(location.getWorld(), -1, 0, -1).getWorld().spawn(location, (Class<? extends Entity>) entityClass);

		try {
			return entity.getClass().getMethod("getHandle").invoke(entity);
		} catch (final ReflectiveOperationException ex) {
			throw new Error(ex);
		}
	}

	//
	// Creates the entity and registers in the NMS server
	//
	private Object createEntity(final Location location, final Class<?> entityClass) {
		try {
			return NmsAccessor.createEntity.invoke(bukkitWorld, location, entityClass);

		} catch (final ReflectiveOperationException e) {
			throw new FoException(e, "Error creating entity " + entityClass + " at " + location);
		}
	}

	/**
	 * Adds the entity to the world for the given spawn reason, calls Bukkit {@link CreatureSpawnEvent}
	 *
	 * @param <T>
	 * @param reason
	 * @return
	 */
	public <T extends Entity> T addEntity(final SpawnReason reason) {
		try {

			return (T) NmsAccessor.addEntity(bukkitWorld, nmsEntity, reason);

		} catch (final ReflectiveOperationException e) {
			throw new FoException(e, "Error creating entity " + nmsEntity + " for " + reason);
		}
	}

	/**
	 * Get the Bukkit entity
	 *
	 * @return
	 */
	public Entity getBukkitEntity() {
		try {
			return (Entity) NmsAccessor.getBukkitEntity.invoke(nmsEntity);

		} catch (final ReflectiveOperationException e) {
			throw new FoException(e, "Error getting bukkit entity from " + nmsEntity);
		}
	}
}

/**
 * A helper class accessing NMS internals
 */
final class NmsAccessor {

	/**
	 * The create entity method
	 */
	static final Method createEntity;

	/**
	 * The get bukkit entity method
	 */
	static final Method getBukkitEntity;

	/**
	 * The add entity method
	 */
	static final Method addEntity;

	/**
	 * Does the {@link #addEntity} field have consumer function input?
	 */
	private static volatile boolean hasEntityConsumer = false;

	/**
	 * Is the current Minecraft version older than 1.8.8 ?
	 */
	private static volatile boolean olderThan18;

	/**
	 * Static block initializer
	 */
	static void call() {
	}

	/**
	 * Load this class
	 */
	static {
		try {
			final Class<?> nmsEntity = ReflectionUtil.getNMSClass("Entity");
			final Class<?> ofcWorld = ReflectionUtil.getOBCClass("CraftWorld");

			olderThan18 = MinecraftVersion.olderThan(V.v1_8);

			createEntity = MinecraftVersion.newerThan(V.v1_7) ? ofcWorld.getDeclaredMethod("createEntity", Location.class, Class.class) : null;
			getBukkitEntity = nmsEntity.getMethod("getBukkitEntity");

			if (MinecraftVersion.newerThan(V.v1_10)) {
				hasEntityConsumer = true;
				addEntity = ofcWorld.getDeclaredMethod("addEntity", nmsEntity, SpawnReason.class, Class.forName("org.bukkit.util.Consumer"));

			} else if (MinecraftVersion.newerThan(V.v1_7))
				addEntity = ofcWorld.getDeclaredMethod("addEntity", nmsEntity, SpawnReason.class);
			else
				addEntity = ReflectionUtil.getNMSClass("World").getDeclaredMethod("addEntity", nmsEntity, SpawnReason.class);

		} catch (final ReflectiveOperationException ex) {
			throw new FoException(ex, "Error setting up nms entity accessor!");
		}
	}

	/**
	 * Adds an entity to the given world
	 *
	 * @param bukkitWorld
	 * @param nmsEntity
	 * @param reason
	 * @return
	 * @throws ReflectiveOperationException
	 */
	static Object addEntity(final World bukkitWorld, final Object nmsEntity, final SpawnReason reason) throws ReflectiveOperationException {
		if (olderThan18) {
			addEntity.invoke(Remain.getHandleWorld(bukkitWorld), nmsEntity, reason);

			return getBukkitEntity.invoke(nmsEntity);
		}

		if (hasEntityConsumer)
			return addEntity.invoke(bukkitWorld, nmsEntity, reason, null);

		return addEntity.invoke(bukkitWorld, nmsEntity, reason);
	}
}
