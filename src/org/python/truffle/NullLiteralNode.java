package org.python.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;

public class NullLiteralNode extends LiteralNode {
  
  @Override
  public Object executeGeneric(VirtualFrame frame) {
    return null;
  }

}
