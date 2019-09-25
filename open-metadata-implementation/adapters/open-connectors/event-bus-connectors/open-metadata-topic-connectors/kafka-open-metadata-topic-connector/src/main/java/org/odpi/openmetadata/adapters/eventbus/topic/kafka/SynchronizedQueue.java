/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Returns a synchronized (thread-safe) queue backed by the specified
 * queue.  In order to guarantee serial access, it is critical that
 * <strong>all</strong> access to the backing queue is accomplished
 * through this instance.<p>
 *
 * It is imperative that the user manually synchronize on the returned
 * list when iterating over it:
 * <pre>
 *  Queue queue = Collections.synchronizedQueue(new ArrayDeque());
 *      ...
 *  synchronized (queue) {
 *      Iterator i = queue.iterator(); // Must be in synchronized block
 *      while (i.hasNext())
 *          foo(i.next());
 *  }
 * </pre>
 * Failure to follow this advice may result in non-deterministic behavior.
 *
 * <p>The returned queue will be serializable if the specified queue is
 * serializable.
 *
 * @param  <T> the class of the objects in the queue
 */

public class SynchronizedQueue<T> implements Queue<T> {

    private final Queue<T> delegate;

    /**
     * 
     * @param delegate The queue to be wrapped in a synchronized queue
     */
    public SynchronizedQueue(Queue<T> delegate) {
        
        this.delegate = delegate;
    }

    @Override
    public int size() {

        synchronized (delegate) {
            return delegate.size();
        }
    }

    @Override
    public boolean isEmpty() {

        synchronized (delegate) {
            return delegate.isEmpty();
        }
    }

    @Override
    public boolean contains(Object o) {

        synchronized (delegate) {
            return delegate.contains(o);
        }
    }

    @Override
    public Iterator<T> iterator() {

        // caller must handle synchronization (this is what the JDK does)
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {

        synchronized (delegate) {
            return delegate.toArray();
        }
    }

    @Override
    public <S> S[] toArray(S[] a) {

        synchronized (delegate) {
            return delegate.toArray(a);
        }
    }

    @Override
    public boolean remove(Object o) {

        synchronized (delegate) {
            return delegate.remove(o);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        synchronized (delegate) {
            return delegate.containsAll(c);
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {

        synchronized (delegate) {
            return delegate.addAll(c);
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        synchronized (delegate) {
            return delegate.removeAll(c);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        synchronized (delegate) {
            return delegate.retainAll(c);
        }
    }

    @Override
    public void clear() {

        synchronized (delegate) {
            delegate.clear();
        }

    }

    @Override
    public boolean add(T e) {

        synchronized (delegate) {
            return delegate.add(e);
        }
    }

    @Override
    public boolean offer(T e) {

        synchronized (delegate) {
            return delegate.offer(e);
        }
    }

    @Override
    public T remove() {

        synchronized (delegate) {
            return delegate.remove();
        }
    }

    @Override
    public T poll() {

        synchronized (delegate) {
            return delegate.poll();
        }
    }

    @Override
    public T element() {

        synchronized (delegate) {
            return delegate.element();
        }
    }

    @Override
    public T peek() {

        synchronized (delegate) {
            return delegate.peek();
        }
    }

}
