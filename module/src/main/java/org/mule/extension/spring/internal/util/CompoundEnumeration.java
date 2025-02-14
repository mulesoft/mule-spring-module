/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/***
 * An enumeration of enumerations.
 * 
 * @param <E>
 *
 * @since 4.2.0
 */
public class CompoundEnumeration<E> implements Enumeration<E> {

  private Enumeration<E>[] enums;
  private int index = 0;

  public CompoundEnumeration(Enumeration<E>[] enums) {
    this.enums = enums;
  }

  private boolean next() {
    while (this.index < this.enums.length) {
      if (this.enums[this.index] != null && this.enums[this.index].hasMoreElements()) {
        return true;
      }

      ++this.index;
    }

    return false;
  }

  /**
   * Checks if the enumeration has more elements.
   * 
   * @return true if there are still elements, false otherwise.
   */
  public boolean hasMoreElements() {
    return this.next();
  }

  /**
   * Returns the next element in the {@link CompoundEnumeration}
   *
   * @return
   */
  public E nextElement() {
    if (!this.next()) {
      throw new NoSuchElementException();
    } else {
      return this.enums[this.index].nextElement();
    }
  }
}
