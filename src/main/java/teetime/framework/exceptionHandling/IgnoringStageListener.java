package teetime.framework.exceptionHandling;

import teetime.framework.Stage;

public class IgnoringStageListener extends StageExceptionListener {

	public IgnoringStageListener() {
		super();
	}

	@Override
	public boolean onStageException(final Exception e, final Stage throwingStage) {
		return false;
	}
}
