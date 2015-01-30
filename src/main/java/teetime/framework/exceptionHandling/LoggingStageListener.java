package teetime.framework.exceptionHandling;

import teetime.framework.Stage;

public class LoggingStageListener extends StageExceptionListener {

	@Override
	public boolean onStageException(final Exception e, final Stage throwingStage) {
		logger.warn("Exception arised from" + throwingStage.getId(), e);
		return false;
	}

}
