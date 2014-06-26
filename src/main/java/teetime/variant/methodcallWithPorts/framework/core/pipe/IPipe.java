package teetime.variant.methodcallWithPorts.framework.core.pipe;

import teetime.variant.methodcallWithPorts.framework.core.InputPort;

public interface IPipe<T> {

	void add(T element);

	T removeLast();

	boolean isEmpty();

	int size();

	T readLast();

	void close();

	boolean isClosed();

	InputPort<T> getTargetPort();

	void setTargetPort(InputPort<T> targetPort);

}