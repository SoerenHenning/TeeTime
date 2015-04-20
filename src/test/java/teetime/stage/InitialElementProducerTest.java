/**
 * Copyright (C) 2015 Christian Wulf, Nelson Tavares de Sousa (http://teetime.sourceforge.net)
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
package teetime.stage;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static teetime.framework.test.StageTester.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Nils Christian Ehmke
 */
public class InitialElementProducerTest {

	private InitialElementProducer<Integer> producer;

	@Before
	public void initializeProducer() {
		producer = new InitialElementProducer<Integer>();
	}

	@Test
	public void producerShouldByDefaultSendNothing() {
		List<Integer> results = new ArrayList<Integer>();

		test(producer).and().receive(results).from(producer.getOutputPort()).start();
		assertThat(results, is(empty()));
	}

	@Test
	public void producerShouldSendDefinedValues() {
		producer.setIter(new Integer[] { 1, 2, 3 });
		List<Integer> results = new ArrayList<Integer>();

		test(producer).and().receive(results).from(producer.getOutputPort()).start();
		assertThat(results, contains(1, 2, 3));
	}

}