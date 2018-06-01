package org.python.truffle;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class MultNode extends BinaryNode {
  
  @Specialization
  public int doIntegers(int left, int right) {
    return left * right;
  }
  
    
  @Specialization
  public double doDoubles(double left, double right) {
    return left * right;
  }
  
  
  @Specialization
  public double doDoubleAndInteger(double left, int right) {
    return left * right;
  }

}
