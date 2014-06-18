package teetime.examples.throughput.methodcall.stage;

import teetime.util.list.CommittableQueue;

public class Relay<T> extends AbstractStage<T, T> {

	public Relay() {
		this.setReschedulable(true);
	}

	@Override
	public void executeWithPorts() {
		T element = this.getInputPort().receive();
		if (null == element) {
			if (this.getInputPort().pipe.isClosed()) {
				this.setReschedulable(false);
				System.out.println("got end signal; pipe.size: " + this.getInputPort().pipe.size());
				return;
			}
			return;
		}

		this.send(element);
	}

	@Override
	public T execute(final Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onIsPipelineHead() {
		// do nothing
	}

	@Override
	protected void execute4(final CommittableQueue<T> elements) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute5(final T element) {
		// TODO Auto-generated method stub

	}

}
