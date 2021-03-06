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

import java.util.HashSet;
import java.util.Set;

import teetime.framework.pipe.DummyPipe;

/**
 * Traverses the all stages that are <b>reachable</b> from the given <i>stage</i>.
 * Each stage is visited exactly once (not more, not less).
 *
 * @author Christian Wulf
 *
 */
public class Traverser {

	public static enum Direction {
		BACKWARD(1), FORWARD(2), BOTH(BACKWARD.value | FORWARD.value);

		private final int value;

		private Direction(final int value) {
			this.value = value;
		}

		public boolean represents(final Direction direction) {
			return (value & direction.value) == direction.value;
		}
	}

	public static enum VisitorBehavior {
		CONTINUE, STOP;
	}

	private final Set<AbstractStage> visitedStages = new HashSet<AbstractStage>();

	private final ITraverserVisitor traverserVisitor;
	private final Direction direction;

	public Traverser(final ITraverserVisitor traverserVisitor) {
		this(traverserVisitor, Direction.FORWARD);
	}

	public Traverser(final ITraverserVisitor traverserVisitor, final Direction direction) {
		this.traverserVisitor = traverserVisitor;
		this.direction = direction;
	}

	public void traverse(final AbstractStage stage) {
		// termination condition: stop if the stage already runs or has been terminated
		if (stage.getCurrentState() != StageState.CREATED) {
			return; // NOPMD sequential termination conditions are more readable
		}

		VisitorBehavior behavior = traverserVisitor.visit(stage);
		if (behavior == VisitorBehavior.STOP || !visitedStages.add(stage)) {
			return;
		}

		if (direction.represents(Direction.FORWARD)) {
			for (OutputPort<?> outputPort : stage.getOutputPorts()) {
				visitAndTraverse(outputPort, Direction.FORWARD);
			}
		}

		if (direction.represents(Direction.BACKWARD)) {
			for (InputPort<?> inputPort : stage.getInputPorts()) {
				visitAndTraverse(inputPort, Direction.BACKWARD);
			}
		}
	}

	private void visitAndTraverse(final AbstractPort<?> port, final Direction direction) {
		if (port.getPipe() == null) {
			throw new IllegalStateException("2003 - The port " + port + " is not connected with another port.");
		}

		if (port.getPipe() instanceof DummyPipe) {
			traverserVisitor.visit((DummyPipe) port.getPipe(), port);
			return;
		}

		VisitorBehavior behavior = traverserVisitor.visit(port);

		if (behavior == VisitorBehavior.CONTINUE) {
			AbstractPort<?> nextPort = (direction == Direction.FORWARD) ? port.getPipe().getTargetPort() : port.getPipe().getSourcePort();
			traverse(nextPort.getOwningStage()); // recursive call
		}
	}

	public Set<AbstractStage> getVisitedStages() {
		return visitedStages;
	}
}
