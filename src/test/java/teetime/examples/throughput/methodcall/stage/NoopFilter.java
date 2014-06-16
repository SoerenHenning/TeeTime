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
package teetime.examples.throughput.methodcall.stage;

import teetime.examples.throughput.methodcall.ConsumerStage;
import teetime.util.list.CommittableQueue;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class NoopFilter<T> extends ConsumerStage<T, T> {

	@SuppressWarnings("unchecked")
	@Override
	public T execute(final Object obj) {
		return (T) obj;
	}

	// @Override
	// public void execute3() {
	// T element = this.getInputPort().receive();
	// // this.getOutputPort().send(element);
	// }

	@Override
	protected void execute4(final CommittableQueue<T> elements) {
		T element = elements.removeFromHead();
		this.execute5(element);
	}

	@Override
	protected void execute5(final T element) {
		this.send(element); // "send" calls the next stage and so on
	}

}