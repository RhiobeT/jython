package org.python.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;

public class StringLiteralNode extends LiteralNode {

  private final String val;
  
  
  public StringLiteralNode(String val) {
    this.val = val;
  }
  

  @Override
  public String executeString(VirtualFrame frame) {
    return val;
  }
  
  
  @Override
  public Object executeGeneric(VirtualFrame frame) {
    return val;
  }

}
