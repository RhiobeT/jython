package org.python.truffle;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class MinusNode extends BinaryNode {
  
  @Specialization
  public int doIntegers(int left, int right) {
    return left - right;
  }

  
  @Specialization
  public double doDoubles(double left, double right) {
    return left - right;
  }  
  
}
