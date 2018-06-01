package org.python.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;

public class DoubleLiteralNode extends LiteralNode {

  private final double val;
  
  
  public DoubleLiteralNode(double val) {
    this.val = val;
  }
  

  @Override
  public double executeDouble(VirtualFrame frame) {
    return val;
  }
  
  
  @Override
  public Object executeGeneric(VirtualFrame frame) {
    return val;
  }

}
