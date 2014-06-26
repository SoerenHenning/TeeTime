/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
 ***************************************************************************/
package teetime.variant.methodcall.examples.experiment13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import teetime.util.ConstructorClosure;
import teetime.variant.explicitScheduling.examples.throughput.TimestampObject;
import teetime.variant.explicitScheduling.framework.core.Analysis;
import teetime.variant.methodcall.framework.core.AbstractStage;
import teetime.variant.methodcall.framework.core.Stage;
import teetime.variant.methodcall.stage.CollectorSink;
import teetime.variant.methodcall.stage.EndStage;
import teetime.variant.methodcall.stage.NoopFilter;
import teetime.variant.methodcall.stage.ObjectProducer;
import teetime.variant.methodcall.stage.StartTimestampFilter;
import teetime.variant.methodcall.stage.StopTimestampFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class MethodCallThroughputAnalysis13 extends Analysis {

	public abstract class WrappingPipeline {

		public abstract boolean execute();

	}

	private long numInputObjects;
	private ConstructorClosure<TimestampObject> inputObjectCreator;
	private int numNoopFilters;
	private List<TimestampObject> timestampObjects;
	private Runnable runnable;

	@Override
	public void init() {
		super.init();
		this.runnable = this.buildPipeline();
	}

	/**
	 * @param numNoopFilters
	 * @since 1.10
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Runnable buildPipeline() {
		final NoopFilter<TimestampObject>[] noopFilters = new NoopFilter[this.numNoopFilters];
		// create stages
		final ObjectProducer<TimestampObject> objectProducer = new ObjectProducer<TimestampObject>(this.numInputObjects, this.inputObjectCreator);
		final StartTimestampFilter startTimestampFilter = new StartTimestampFilter();
		for (int i = 0; i < noopFilters.length; i++) {
			noopFilters[i] = new NoopFilter<TimestampObject>();
		}
		final StopTimestampFilter stopTimestampFilter = new StopTimestampFilter();
		final CollectorSink<TimestampObject> collectorSink = new CollectorSink<TimestampObject>(this.timestampObjects);

		final List<AbstractStage> stageList = new ArrayList<AbstractStage>();
		stageList.add(objectProducer);
		stageList.add(startTimestampFilter);
		stageList.addAll(Arrays.asList(noopFilters));
		stageList.add(stopTimestampFilter);
		stageList.add(collectorSink);

		// using an array decreases the performance from 60ms to 200ms (by 3x)
		final AbstractStage[] stages = stageList.toArray(new AbstractStage[0]);

		for (int i = 0; i < stages.length - 1; i++) {
			Stage<?, ?> stage = stages[i];
			stage.setSuccessor(stages[i + 1]);
		}
		stages[stages.length - 1].setSuccessor(new EndStage<Object>());

		final WrappingPipeline pipeline = new WrappingPipeline() {
			@Override
			public boolean execute() {
				Object result = stages[0].executeRecursively(null);
				return result != null;
			}

		};

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				boolean success;
				do {
					success = pipeline.execute();
				} while (success);
			}
		};
		return runnable;
	}

	@Override
	public void start() {
		super.start();
		this.runnable.run();
	}

	public void setInput(final int numInputObjects, final ConstructorClosure<TimestampObject> inputObjectCreator) {
		this.numInputObjects = numInputObjects;
		this.inputObjectCreator = inputObjectCreator;
	}

	public int getNumNoopFilters() {
		return this.numNoopFilters;
	}

	public void setNumNoopFilters(final int numNoopFilters) {
		this.numNoopFilters = numNoopFilters;
	}

	public List<TimestampObject> getTimestampObjects() {
		return this.timestampObjects;
	}

	public void setTimestampObjects(final List<TimestampObject> timestampObjects) {
		this.timestampObjects = timestampObjects;
	}
}