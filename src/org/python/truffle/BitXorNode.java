package org.python.truffle;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class BitXorNode extends BinaryNode {
  
  @Specialization
  public int doIntegers(int left, int right) {
    return left ^ right;
  }

}
