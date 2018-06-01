package org.python.truffle;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class EqualNode extends BinaryNode {

  @Specialization
  public boolean doIntegers(int left, int right) {
    return left == right;
  }
  
}
