package ru.ashaposhnikov.bencode;

import java.util.Iterator;

public class PeekingIterator<E> implements Iterator<E> {

  private final Iterator<? extends E> iterator;
  private boolean hasPeeked;
  private E peekedElement;

  public PeekingIterator(Iterator<? extends E> iterator) {
    this.iterator = iterator;
  }

  public boolean hasNext() {
    return hasPeeked || iterator.hasNext();
  }

  public E next() {
    if (!hasPeeked) {
      return iterator.next();
    }
    E result = peekedElement;
    hasPeeked = false;
    peekedElement = null;
    return result;
  }

  public void remove() {
	throw new RuntimeException("cannot remove");
  }

  public E peek() {
    if (!hasPeeked) {
      peekedElement = iterator.next();
      hasPeeked = true;
    }
    return peekedElement;
  }

  public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
	  return new PeekingIterator<T>(iterator);
  }
}

