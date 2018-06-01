package org.python.truffle;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class PlusNode extends BinaryNode {
  
  @Specialization
  public int doIntegers(int left, int right) {
    return left + right;
  }
  
   
  @Specialization
  public String doStrings(String left, String right) {
    return left + right;
  }
  
    
  @Specialization
  public double doDoubles(double left, double right) {
    return left + right;
  }
  
  
  @Specialization
  public String doStringAndDouble(String left, double right) {
    return left + right;
  }
  
  
  @Specialization
  public String doDoubleAndString(double left, String right) {
    return left + right;
  }

    
  @Specialization
  public double doDoubleAndInteger(double left, int right) {
    return left + right;
  }
  
}
