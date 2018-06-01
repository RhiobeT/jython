package org.python.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;

public class ReturnNode extends ExpressionNode {

  @Child protected ExpressionNode expression;
  
  
  public ReturnNode(ExpressionNode expression) {
    this.expression = expression;
  }
  
  
  public Object executeGeneric(VirtualFrame frame) {
    throw new ReturnException(this.expression.executeGeneric(frame));
  }
  
}
