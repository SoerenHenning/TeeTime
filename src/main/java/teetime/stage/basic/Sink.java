package teetime.stage.basic;

import teetime.framework.AbstractConsumerStage;

public class Sink<T> extends AbstractConsumerStage<T> {

	// PERFORMANCE let the sink remove all available input at once by using a new method receiveAll() that clears the pipe's buffer

	@Override
	protected void execute(final T element) {
		// do nothing; just consume
	}

}
