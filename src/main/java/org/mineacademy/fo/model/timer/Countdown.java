package org.mineacademy.fo.model.timer;

import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.model.SimpleReplacer;

import java.util.Set;

public abstract class Countdown extends SecondTimer {

	@Override
	public final void run() {
		int difference = getSecondsUntilEnable();

		if (difference > 0) {
			onCountdown(difference);
			broadcastCountdown(difference);
		} else if (difference == 0) {
			execute();
			broadcastRun();
		}
	}

	public abstract int getToggleTimer();

	public abstract int getCurrentTime();

	public abstract Set<Integer> getAnnounceSeconds();

	@Nullable
	public abstract String getCountdownBroadcast();

	@Nullable
	public abstract String getToggledBroadcast();

	protected abstract void execute();

	public void onCountdown(int untilEnable) {

	}

	public int getSecondsUntilEnable() {
		return getToggleTimer() - getCurrentTime();
	}

	public String getUntilEnableFormat() {
		return TimeUtil.formatTime(getSecondsUntilEnable());
	}

	private final void broadcastCountdown(int untilEnable) {
		if (getAnnounceSeconds().contains(untilEnable)) {
			String broadcast = getCountdownBroadcast();

			if (broadcast != null)
				Common.broadcast(new SimpleReplacer(broadcast)
						.replaceTime(untilEnable)
						.getMessages());
		}
	}

	private final void broadcastRun() {
		String broadcast = getToggledBroadcast();

		if (broadcast != null)
			Common.broadcast(broadcast);
	}
}
