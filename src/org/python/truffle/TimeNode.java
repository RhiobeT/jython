package org.python.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;

public class TimeNode extends ExpressionNode {
  
  @Override
  public double executeDouble(VirtualFrame frame) {
    return System.nanoTime() / 1000000000.0;
  }
  
  
  @Override
  public Object executeGeneric(VirtualFrame frame) {
    return System.nanoTime() / 1000000000.0;
  }
  
}
