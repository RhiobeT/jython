package org.python.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;

public class BreakNode extends ExpressionNode {

  @Override
  public Object executeGeneric(VirtualFrame virtualFrame) {
    throw new BreakException();
  }
  
}
