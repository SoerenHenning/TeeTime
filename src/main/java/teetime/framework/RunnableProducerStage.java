/**
 * Copyright (C) 2015 TeeTime (http://teetime.sourceforge.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package teetime.framework;

import teetime.framework.exceptionHandling.AbstractExceptionListener;
import teetime.framework.signal.StartingSignal;
import teetime.framework.signal.TerminatingSignal;

public final class RunnableProducerStage extends AbstractRunnableStage {

	public RunnableProducerStage(final Stage stage, final AbstractExceptionListener listener) {
		super(stage, listener);
	}

	@Override
	protected void beforeStageExecution(final Stage stage) {
		final StartingSignal startingSignal = new StartingSignal();
		stage.onSignal(startingSignal, null);
	}

	@Override
	protected void executeStage(final Stage stage) {
		stage.executeStage();
	}

	@Override
	protected void afterStageExecution(final Stage stage) {
		final TerminatingSignal terminatingSignal = new TerminatingSignal();
		stage.onSignal(terminatingSignal, null);
	}

}
