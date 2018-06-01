package org.python.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class SequenceNode extends ExpressionNode {
  @Children private final ExpressionNode[] statements;
  
  
  public SequenceNode(ExpressionNode[] statements) {
    this.statements = statements;
  }
  
  
  @ExplodeLoop
  @Override
  public Object executeGeneric(VirtualFrame frame) {
    for (ExpressionNode statement : this.statements) {
      statement.executeGeneric(frame);
    }
    return null;
  }
  
}
