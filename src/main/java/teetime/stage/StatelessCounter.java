package teetime.stage;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.framework.pipe.ReflexivePipe;

public class StatelessCounter<T> extends AbstractStage {

	private final InputPort<T> inputPort = this.createInputPort();
	private final InputPort<CounterContainer> counterInputPort = this.createInputPort();

	private final OutputPort<T> outputPort = this.createOutputPort();
	private final OutputPort<CounterContainer> counterOutputPort = this.createOutputPort();

	public StatelessCounter() {
		new ReflexivePipe<CounterContainer>(counterOutputPort, counterInputPort);
		counterOutputPort.send(new CounterContainer());
	}

	@Override
	protected void execute() {
		T element = inputPort.receive();
		if (element == null) {
			returnNoElement();
		}

		outputPort.send(element);

		CounterContainer counterContainer = counterInputPort.receive();
		// null check not necessary since counterContainer is initialized in the ctor and can never be accessed from outside
		counterContainer.counter = counterContainer.counter + 1;
		counterOutputPort.send(counterContainer);
	}

	public InputPort<T> getInputPort() {
		return inputPort;
	}

	public OutputPort<T> getOutputPort() {
		return outputPort;
	}

	private static class CounterContainer {
		long counter;
	}

}