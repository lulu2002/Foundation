package org.mineacademy.fo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for tab completion.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TabUtil {

	/**
	 * A shortcut for completing player names from all players connected online
	 *
	 * @param partialName
	 * @return
	 */
	public static List<String> completePlayerName(String partialName) {
		return complete(partialName, Common.getPlayerNames());
	}

	/**
	 * Return a list of tab completions for the given array,
	 * we attempt to resolve what type of the array it is,
	 * supports for chat colors, command senders, enumerations etc.
	 *
	 * @param <T>
	 * @param partialName
	 * @param all
	 * @return
	 */
	@SafeVarargs
	public static <T> List<String> complete(String partialName, T... all) {
		final List<String> clone = new ArrayList<>();

		for (final T s : all)
			if (s != null) {
				final boolean lowercase = s instanceof Enum;
				final String parsed = SerializeUtil.serialize(s).toString();

				clone.add(lowercase ? parsed.toLowerCase() : parsed);
			}

		return complete(partialName, clone);
	}

	/**
	 * Returns valid tab completions for the given collection
	 *
	 * @param partialName
	 * @param all
	 * @return
	 */
	public static List<String> complete(String partialName, Iterable<String> all) {
		final ArrayList<String> tab = new ArrayList<>();

		for (final String s : all)
			tab.add(s);

		partialName = partialName.toLowerCase();

		for (final Iterator<String> iterator = tab.iterator(); iterator.hasNext();) {
			final String val = iterator.next();

			if (!val.toLowerCase().startsWith(partialName))
				iterator.remove();
		}

		return tab;
	}
}
