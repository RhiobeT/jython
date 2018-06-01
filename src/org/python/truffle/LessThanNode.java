package org.python.truffle;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class LessThanNode extends BinaryNode {

  @Specialization
  public boolean doIntegers(int left, int right) {
    return left < right;
  }

}
