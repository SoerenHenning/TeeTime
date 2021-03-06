package teetime.framework;

import java.util.List;

import teetime.framework.performancelogging.StateChange;

/**
 * Provides access to package-private statistics methods of the {@link teetime.framework.AbstractStage}.
 *
 * @author Christian Wulf (chw)
 */
public final class StateStatistics {

	private StateStatistics() {
		// utility class
	}

	/**
	 * This method is used to collect the List of States
	 *
	 * @return List of states this stage saved during its run.
	 */
	public static List<StateChange> getStates(final AbstractStage stage) {
		return stage.getStates();
	}

	/**
	 * This method is called by Pipes if the sending of the next element needs to be delayed because of full Queue.
	 */
	public static void sendingFailed(final AbstractStage stage) {
		stage.sendingFailed();
	}

	/**
	 * This method is called when the element is successfully added to the Pipe.
	 */
	public static void sendingSucceeded(final AbstractStage stage) {
		stage.sendingSucceeded();
	}

	/**
	 * This method is called when the Thread returns to a Stage that send an element before.
	 */
	// public void sendingReturned();

	/**
	 *
	 * @param stage
	 * @return
	 */
	public static long getActiveWaitingTime(final AbstractStage stage) {
		return stage.getActiveWaitingTime();
	}
}
