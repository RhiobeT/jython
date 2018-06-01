package org.python.truffle;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class GreaterThanNode extends BinaryNode {

  @Specialization
  public boolean doIntegers(int left, int right) {
    return left > right;
  }

    
  @Specialization
  public boolean doDoubles(double left, double right) {
    return left > right;
  }
  
}
