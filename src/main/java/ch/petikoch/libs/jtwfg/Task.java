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

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a task in the graph.
 * <p/>
 * {@link Task} instances are comparable ({@link java.lang.Comparable}), as long the ID type T implements {@link
 * java.lang.Comparable}.
 * <p/>
 * Not thread-safe.
 *
 * @param <T>
 * 		The type of the ID of the task. Something with a meaningful {@link Object#equals(Object)} and {@link
 * 		Object#hashCode()} implementation like {@link String}, {@link Long} or a class of your domain model which is fine
 * 		to use as a key e.g. in a {@link java.util.HashMap}. If T implements Comparable, then you get sorted collections.
 */
public class Task<T> implements Comparable<Task<T>> {

	private final T id;
	private final Set<Task<T>> waitsForTasks = new TreeSet<>();

	Task(T id) {
		Preconditions.checkArgumentNotNull(id, "id must not be null");
		this.id = id;
	}

	Task addWaitFor(Task<T> other) {
		waitsForTasks.add(other);
		return this;
	}

	boolean removeWaitFor(Task<T> other) {
		return waitsForTasks.remove(other);
	}

	/**
	 * @return an unmodifiable set of the "wait for" tasks. If Type T implements comparable, the Set is ordered.
	 */
	public Set<Task<T>> getWaitsForTasks() {
		return Collections.unmodifiableSet(waitsForTasks);
	}

	public T getId() {
		return id;
	}

	// generated by IntelliJ IDEA
	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final Task task = (Task) o;

		if (!id.equals(task.id)) return false;

		return true;
	}

	// generated by IntelliJ IDEA
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	// generated by IntelliJ IDEA
	@Override
	public String toString() {
		return "Task{" +
				"id=" + id +
				'}';
	}

	@Override
	public int compareTo(final Task<T> other) {
		if (this.getId() instanceof Comparable && other.getId() instanceof Comparable) {
			//noinspection unchecked
			return ((Comparable) this.getId()).compareTo(other.getId());
		}
		return 0;
	}
}
