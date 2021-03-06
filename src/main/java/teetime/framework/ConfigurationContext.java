/**
 * Copyright © 2015 Christian Wulf, Nelson Tavares de Sousa (http://teetime-framework.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package teetime.framework;

import java.util.Set;

/**
 * Represents a context that is used by a configuration and composite stages to connect ports,for example.
 * Stages can be added by executing {@link #declareActive(AbstractStage)}.
 *
 * @since 2.0
 */
final class ConfigurationContext {

	private ThreadService threadService;

	ConfigurationContext(final Configuration configuration) {
		this.threadService = new ThreadService(configuration);
	}

	Set<AbstractStage> getThreadableStages() {
		return threadService.getThreadableStages();
	}

	void initializeServices() {
		threadService.onInitialize();
	}

	void executeConfiguration() {
		this.threadService.onExecute();
	}

	void abortConfigurationRun() {
		this.threadService.onTerminate();
	}

	void waitForConfigurationToTerminate() {
		this.threadService.onFinish();
	}

	ThreadService getThreadService() {
		return threadService;
	}

	void setThreadService(final ThreadService threadService) {
		this.threadService = threadService;
	}

}
