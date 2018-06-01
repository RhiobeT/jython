package org.python.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;

public class IntegerLiteralNode extends LiteralNode {

  private final int val;
  
  
  public IntegerLiteralNode(int val) {
    this.val = val;
  }
  

  @Override
  public int executeInteger(VirtualFrame frame) {
    return val;
  }
  
  
  @Override
  public Object executeGeneric(VirtualFrame frame) {
    return val;
  }

}
