package com.bobocode;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This queue should be implemented using generic liked nodes. E.g. class Node<T>. In addition, this specific
 * should be thread-safe, which means that queue can be used by different threads simultaneously, and should work correct.
 *
 * @param <T> a generic parameter
 */
public class ConcurrentLinkedQueue<T> implements Queue<T> {

    private Node<T> first;
    private Node<T> last;
    private AtomicInteger size = new AtomicInteger(0);

    @Override
    synchronized public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (first == null) {
            first = last = newNode;
        } else {
            last.next = newNode;
            last = newNode;
        }
        size.getAndIncrement();
    }

    @Override
    synchronized public T poll() {
        if (first == null) {
            return null;
        } else {
            T element = first.element;
            first = first.next;
            if (first == null) {
                last = null;
            }
            size.getAndDecrement();
            return element;
        }
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }

    private class Node<T> {
        private T element;
        private Node<T> next;

        public Node(T element) {
            this.element = element;
        }
    }
}
