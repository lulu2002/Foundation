package org.mineacademy.fo.model;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.mineacademy.fo.TimeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 2019-12-02 下午 08:38
 */
public class SimpleReplacer implements Cloneable {
	private static final String DELIMITER = "\n";
	@Getter
	private String messages;

	public SimpleReplacer(List<String> list) {
		this(list.toArray(new String[0]));
	}

	public SimpleReplacer(String[] list) {
		this(StringUtils.join(list, DELIMITER));
	}

	public SimpleReplacer(String string) {
		this.messages = string;
	}

	public List<String> buildList() {
		ArrayList<String> list = new ArrayList<>();
		Collections.addAll(list, toArray());

		return list;
	}

	public String[] toArray() {
		return messages.split(DELIMITER);
	}

	public SimpleReplacer replaceFirst(String from, Object to) {
		if (messages.contains(from))
			messages = messages.replace(from, to.toString());

		return this;
	}

	public <E extends Object> SimpleReplacer replaceFirst(String from, List<E> to) {
		replaceFirst(from, StringUtils.join(to, DELIMITER));
		return this;
	}

	public SimpleReplacer replace(String from, Object to) {
		if (messages.contains(from))
			messages = messages.replace(from, to.toString());

		return this;
	}

	public SimpleReplacer replaceTime(int time) {
		return TimeUtil.getTimeReplacer(messages, time);
	}

	public <E extends Object> SimpleReplacer replace(String from, Collection<E> to) {
		return replace(from, to, "");
	}

	public <E extends Object> SimpleReplacer replace(String from, Collection<E> to, String linePrefix) {
		String separator = DELIMITER + linePrefix;
		replace(from, separator + StringUtils.join(to, separator));
		return this;
	}

	@Override
	public SimpleReplacer clone() {
		try {
			return (SimpleReplacer) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}