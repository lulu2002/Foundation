package org.mineacademy.fo.remain.nbt;

import java.io.Serializable;
import java.util.Set;

import org.mineacademy.fo.MinecraftVersion.V;

/**
 * Base class representing NMS Compounds. For a standalone implementation check
 * {@link NBTContainer}
 *
 * @author tr7zw
 *
 */
public class NBTCompound {

	private final String compundName;
	private final NBTCompound parent;

	protected NBTCompound(NBTCompound owner, String name) {
		this.compundName = name;
		this.parent = owner;
	}

	protected void saveCompound(){
		if(parent != null)
			parent.saveCompound();
	}

	/**
	 * @return The Compound name
	 */
	public String getName() {
		return compundName;
	}

	/**
	 * @return The NMS Compound behind this Object
	 */
	public Object getCompound() {
		return parent.getCompound();
	}

	protected void setCompound(Object compound) {
		parent.setCompound(compound);
	}

	/**
	 * @return The parent Compound
	 */
	public NBTCompound getParent() {
		return parent;
	}

	/**
	 * Merges all data from comp into this compound. This is done in one action, so
	 * it also works with Tiles/Entities
	 *
	 * @param comp
	 */
	public void mergeCompound(NBTCompound comp) {
		NBTReflectionUtil.mergeOtherNBTCompound(this, comp);
		saveCompound();
	}

	/**
	 * Setter
	 *
	 * @param key
	 * @param value
	 */
	public void setString(String key, String value) {
		NBTReflectionUtil.setData(this, WrapperMethod.COMPOUND_SET_STRING, key, value);
		saveCompound();
	}

	/**
	 * Getter
	 *
	 * @param key
	 * @return The stored value or NMS fallback
	 */
	public String getString(String key) {
		return (String) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_STRING, key);
	}

	protected String getContent(String key) {
		return NBTReflectionUtil.getContent(this, key);
	}

	/**
	 * Setter
	 *
	 * @param key
	 * @param value
	 */
	public void setInteger(String key, Integer value) {
		NBTReflectionUtil.setData(this, WrapperMethod.COMPOUND_SET_INT, key, value);
		saveCompound();
	}

	/**
	 * Getter
	 *
	 * @param key
	 * @return The stored value or NMS fallback
	 */
	public Integer getInteger(String key) {
		return (Integer) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_INT, key);
	}

	/**
	 * Setter
	 *
	 * @param key
	 * @param value
	 */
	public void setDouble(String key, Double value) {
		NBTReflectionUtil.setData(this, WrapperMethod.COMPOUND_SET_DOUBLE, key, value);
		saveCompound();
	}

	/**
	 * Getter
	 *
	 * @param key
	 * @return The stored value or NMS fallback
	 */
	public Double getDouble(String key) {
		return (Double) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_DOUBLE, key);
	}

	/**
	 * Setter
	 *
	 * @param key
	 * @param value
	 */
	public void setByte(String key, Byte value) {
		NBTReflectionUtil.setData(this, WrapperMethod.COMPOUND_SET_BYTE, key, value);
		saveCompound();
	}

	/**
	 * Getter
	 *
	 * @param key
	 * @return The stored value or NMS fallback
	 */
	public Byte getByte(String key) {
		return (Byte) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_BYTE, key);
	}

	/**
	 * Setter
	 *
	 * @param key
	 * @param value
	 */
	public void setShort(String key, Short value) {
		NBTReflectionUtil.setData(this, WrapperMethod.COMPOUND_SET_SHORT, key, value);
		saveCompound();
	}

	/**
	 * Getter
	 *
	 * @param key
	 * @return The stored value or NMS fallback
	 */
	public Short getShort(String key) {
		return (Short) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_SHORT, key);
	}

	/**
	 * Setter
	 *
	 * @param key
	 * @param value
	 */
	public void setLong(String key, Long value) {
		NBTReflectionUtil.setData(this, WrapperMethod.COMPOUND_SET_LONG, key, value);
		saveCompound();
	}

	/**
	 * Getter
	 *
	 * @param key
	 * @return The stored value or NMS fallback
	 */
	public Long getLong(String key) {
		return (Long) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_LONG, key);
	}

	/**
	 * Setter
	 *
	 * @param key
	 * @param value
	 */
	public void setFloat(String key, Float value) {
		NBTReflectionUtil.setData(this, WrapperMethod.COMPOUND_SET_FLOAT, key, value);
		saveCompound();
	}

	/**
	 * Getter
	 *
	 * @param key
	 * @return The stored value or NMS fallback
	 */
	public Float getFloat(String key) {
		return (Float) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_FLOAT, key);
	}

	/**
	 * Setter
	 *
	 * @param key
	 * @param value
	 */
	public void setByteArray(String key, byte[] value) {
		NBTReflectionUtil.setData(this, WrapperMethod.COMPOUND_SET_BYTEARRAY, key, value);
		saveCompound();
	}

	/**
	 * Getter
	 *
	 * @param key
	 * @return The stored value or NMS fallback
	 */
	public byte[] getByteArray(String key) {
		return (byte[]) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_BYTEARRAY, key);
	}

	/**
	 * Setter
	 *
	 * @param key
	 * @param value
	 */
	public void setIntArray(String key, int[] value) {
		NBTReflectionUtil.setData(this, WrapperMethod.COMPOUND_SET_INTARRAY, key, value);
		saveCompound();
	}

	/**
	 * Getter
	 *
	 * @param key
	 * @return The stored value or NMS fallback
	 */
	public int[] getIntArray(String key) {
		return (int[]) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_INTARRAY, key);
	}

	/**
	 * Setter
	 *
	 * @param key
	 * @param value
	 */
	public void setBoolean(String key, Boolean value) {
		NBTReflectionUtil.setData(this, WrapperMethod.COMPOUND_SET_BOOLEAN, key, value);
		saveCompound();
	}

	protected void set(String key, Object val) {
		NBTReflectionUtil.set(this, key, val);
		saveCompound();
	}

	/**
	 * Getter
	 *
	 * @param key
	 * @return The stored value or NMS fallback
	 */
	public Boolean getBoolean(String key) {
		return (Boolean) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_BOOLEAN, key);
	}

	/**
	 * Uses Gson to store an {@link Serializable} Object
	 *
	 * @param key
	 * @param value
	 */
	public void setObject(String key, Object value) {
		NBTReflectionUtil.setObject(this, key, value);
		saveCompound();
	}

	/**
	 * Uses Gson to retrieve a stored Object
	 *
	 * @param key
	 * @param type Class of the Object
	 * @return The created Object or null if empty
	 */
	public <T> T getObject(String key, Class<T> type) {
		return NBTReflectionUtil.getObject(this, key, type);
	}

	/**
	 * @param key
	 * @return True if the key is set
	 */
	public Boolean hasKey(String key) {
		final Boolean b = (Boolean) NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_HAS_KEY, key);
		if (b == null)
			return false;
		return b;
	}

	/**
	 * @param key Deletes the given Key
	 */
	public void removeKey(String key) {
		NBTReflectionUtil.remove(this, key);
		saveCompound();
	}

	/**
	 * @return Set of all stored Keys
	 */
	public Set<String> getKeys() {
		return NBTReflectionUtil.getKeys(this);
	}

	/**
	 * Creates a subCompound
	 *
	 * @param name Key to use
	 * @return The subCompound Object
	 */
	public NBTCompound addCompound(String name) {
		if (getType(name) == NBTType.NBTTagCompound)
			return getCompound(name);
		NBTReflectionUtil.addNBTTagCompound(this, name);
		final NBTCompound comp = getCompound(name);
		if(comp == null)
			throw new NbtApiException("Error while adding Compound, got null!");
		saveCompound();
		return comp;
	}

	/**
	 * @param name
	 * @return The Compound instance or null
	 */
	public NBTCompound getCompound(String name) {
		if (getType(name) != NBTType.NBTTagCompound)
			return null;
		final NBTCompound next = new NBTCompound(this, name);
		if (NBTReflectionUtil.valideCompound(next))
			return next;
		return null;
	}

	/**
	 * @param name
	 * @return The retrieved String List
	 */
	public NBTList<String> getStringList(String name) {
		final NBTList<String> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagString, String.class);
		saveCompound();
		return list;
	}

	/**
	 * @param name
	 * @return The retrieved Integer List
	 */
	public NBTList<Integer> getIntegerList(String name) {
		final NBTList<Integer> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagInt, Integer.class);
		saveCompound();
		return list;
	}

	/**
	 * @param name
	 * @return The retrieved Compound List
	 */
	public NBTCompoundList getCompoundList(String name) {
		final NBTCompoundList list = (NBTCompoundList) NBTReflectionUtil.getList(this, name, NBTType.NBTTagCompound, NBTListCompound.class);
		saveCompound();
		return list;
	}

	/**
	 * @param name
	 * @return The type of the given stored key or null
	 */
	public NBTType getType(String name) {
		if (org.mineacademy.fo.MinecraftVersion.equals(V.v1_7))
			return null;
		final Object o = NBTReflectionUtil.getData(this, WrapperMethod.COMPOUND_GET_TYPE, name);
		if (o == null)
			return null;
		return NBTType.valueOf((byte) o);
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		for (final String key : getKeys()) {
			result.append(toString(key));
		}
		return result.toString();
	}

	/**
	 * @param key
	 * @return A string representation of the given key
	 */
	public String toString(String key) {
		final StringBuilder result = new StringBuilder();
		NBTCompound compound = this;
		while (compound.getParent() != null) {
			result.append("   ");
			compound = compound.getParent();
		}
		if (this.getType(key) == NBTType.NBTTagCompound) {
			return this.getCompound(key).toString();
		} else {
			return result + "-" + key + ": " + getContent(key) + System.lineSeparator();
		}
	}

	/**
	 * @return A json valid nbt string for this Compound
	 */
	public String asNBTString() {
		final Object comp = NBTReflectionUtil.gettoCompount(getCompound(), this);
		if (comp == null)
			return "{}";
		return comp.toString();
	}

}
