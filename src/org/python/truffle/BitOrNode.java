package org.python.truffle;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class BitOrNode extends BinaryNode {
  
  @Specialization
  public int doIntegers(int left, int right) {
    return left | right;
  }

}
