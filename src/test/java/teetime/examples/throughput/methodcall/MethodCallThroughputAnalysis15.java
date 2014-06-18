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
package teetime.examples.throughput.methodcall;

import java.util.List;

import teetime.examples.throughput.TimestampObject;
import teetime.examples.throughput.methodcall.stage.Clock;
import teetime.examples.throughput.methodcall.stage.CollectorSink;
import teetime.examples.throughput.methodcall.stage.Delay;
import teetime.examples.throughput.methodcall.stage.EndStage;
import teetime.examples.throughput.methodcall.stage.NoopFilter;
import teetime.examples.throughput.methodcall.stage.ObjectProducer;
import teetime.examples.throughput.methodcall.stage.Pipeline;
import teetime.examples.throughput.methodcall.stage.StartTimestampFilter;
import teetime.examples.throughput.methodcall.stage.StopTimestampFilter;
import teetime.framework.core.Analysis;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class MethodCallThroughputAnalysis15 extends Analysis {

	private int numInputObjects;
	private ConstructorClosure<TimestampObject> inputObjectCreator;
	private int numNoopFilters;
	private List<TimestampObject> timestampObjects;

	private Runnable clockRunnable;
	private Runnable runnable;
	private Clock clock;

	@Override
	public void init() {
		super.init();

		this.clockRunnable = this.buildClockPipeline();
		this.runnable = this.buildPipeline(this.clock);
	}

	private Runnable buildClockPipeline() {
		this.clock = new Clock();

		this.clock.setInitialDelayInMs(100);
		this.clock.setIntervalDelayInMs(100);

		final Pipeline<Void, Long> pipeline = new Pipeline<Void, Long>();
		pipeline.setFirstStage(this.clock);
		pipeline.setLastStage(new EndStage<Long>());

		return new RunnableStage(pipeline);
	}

	/**
	 * @param numNoopFilters
	 * @since 1.10
	 */
	private Runnable buildPipeline(final Clock clock) {
		@SuppressWarnings("unchecked")
		final NoopFilter<TimestampObject>[] noopFilters = new NoopFilter[this.numNoopFilters];
		// create stages
		final ObjectProducer<TimestampObject> objectProducer = new ObjectProducer<TimestampObject>(this.numInputObjects, this.inputObjectCreator);
		Delay<TimestampObject> delay = new Delay<TimestampObject>();
		final StartTimestampFilter startTimestampFilter = new StartTimestampFilter();
		for (int i = 0; i < noopFilters.length; i++) {
			noopFilters[i] = new NoopFilter<TimestampObject>();
		}
		final StopTimestampFilter stopTimestampFilter = new StopTimestampFilter();
		final CollectorSink<TimestampObject> collectorSink = new CollectorSink<TimestampObject>(this.timestampObjects);

		final Pipeline<Void, Object> pipeline = new Pipeline<Void, Object>();
		pipeline.setFirstStage(objectProducer);
		pipeline.addIntermediateStage(startTimestampFilter);
		pipeline.addIntermediateStages(noopFilters);
		pipeline.addIntermediateStage(stopTimestampFilter);
		pipeline.addIntermediateStage(delay);
		pipeline.setLastStage(collectorSink);

		SpScPipe.connect(clock.getOutputPort(), delay.getTimestampTriggerInputPort());

		UnorderedGrowablePipe.connect(objectProducer.getOutputPort(), startTimestampFilter.getInputPort());
		UnorderedGrowablePipe.connect(startTimestampFilter.getOutputPort(), noopFilters[0].getInputPort());
		for (int i = 0; i < noopFilters.length - 1; i++) {
			UnorderedGrowablePipe.connect(noopFilters[i].getOutputPort(), noopFilters[i + 1].getInputPort());
		}
		UnorderedGrowablePipe.connect(noopFilters[noopFilters.length - 1].getOutputPort(), stopTimestampFilter.getInputPort());
		UnorderedGrowablePipe.connect(stopTimestampFilter.getOutputPort(), delay.getInputPort());

		UnorderedGrowablePipe.connect(delay.getOutputPort(), collectorSink.getInputPort());

		return new RunnableStage(pipeline);
	}

	@Override
	public void start() {
		super.start();
		Thread clockThread = new Thread(this.clockRunnable);
		clockThread.start();
		this.runnable.run();
		clockThread.interrupt();
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