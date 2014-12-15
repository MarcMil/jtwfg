/*
 * Copyright 2014 Peti Koch und Adrian Elsener
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

package ch.petikoch.libs.jtwfg;

import ch.petikoch.libs.jtwfg.assertion.Preconditions;

import java.util.*;

/**
 * The representation of a cycle between tasks in a "task wait for model" graph.
 * <p/>
 * Immutable / thread-safe.
 *
 * @param <T>
 * 		The type of the ID of the tasks. Something with a meaningful {@link Object#equals(Object)} and {@link
 * 		Object#hashCode()} implementation like {@link String}, {@link Long} or a class of your domain model which is fine
 * 		to use as a key e.g. in a {@link java.util.HashMap}. If T implements Comparable, then you get sorted collections.
 */
public class DeadlockCycle<T> {

	private final List<T> involvedTasks;

	DeadlockCycle(final List<T> involvedTasks) {
		Preconditions.checkArgument(involvedTasks != null && !involvedTasks.isEmpty(), "There are no involved tasks: " + involvedTasks);
		this.involvedTasks = Collections.unmodifiableList(involvedTasks);
	}

	@SuppressWarnings("UnusedDeclaration")
	public List<T> getInvolvedTasks() {
		return involvedTasks;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final DeadlockCycle<T> that = (DeadlockCycle<T>) o;

		if (involvedTasks.size() == that.involvedTasks.size()) {
			if (involvedTasks.size() == 0) {
				return true;
			} else {
				Set<T> thisTaskSet = new HashSet<>(involvedTasks);
				Set<T> thatTaskSet = new HashSet<>(that.involvedTasks);
				if (thisTaskSet.equals(thatTaskSet)) {
					// the order is important
					T firstElement = involvedTasks.iterator().next();
					int indexOfFirstElementInThat = calculateIndex(that.involvedTasks, firstElement);
					if (indexOfFirstElementInThat > 0) {
						List<T> reorderedCopyOfThatInvolvedTasks = new ArrayList<>(that.involvedTasks.size());
						reorderedCopyOfThatInvolvedTasks.addAll(that.involvedTasks.subList(indexOfFirstElementInThat, that.involvedTasks.size()));
						if (indexOfFirstElementInThat > 1) {
							reorderedCopyOfThatInvolvedTasks.addAll(that.involvedTasks.subList(1, indexOfFirstElementInThat));
						}
						reorderedCopyOfThatInvolvedTasks.add(that.involvedTasks.get(indexOfFirstElementInThat));
						return involvedTasks.equals(reorderedCopyOfThatInvolvedTasks);
					} else {
						return involvedTasks.equals(that.involvedTasks);
					}
				}
			}
		}

		return false;
	}

	private int calculateIndex(final List<T> tasks, final T element) {
		int index = 0;
		for (T t : tasks) {
			if (t.equals(element)) {
				return index;
			}
			index++;
		}
		throw new IllegalStateException("Element " + element + " not found in " + tasks);
	}

	@Override
	public int hashCode() {
		return involvedTasks.size();
	}

	@Override
	public String toString() {
		String result = DeadlockCycle.class.getSimpleName();
		result += ": ";
		for (int i = 0; i < involvedTasks.size() - 1; i++) {
			result += involvedTasks.get(i) + " -> ";
		}
		result += involvedTasks.get(involvedTasks.size() - 1);
		return result;
	}
}
