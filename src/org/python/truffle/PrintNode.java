package org.python.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class PrintNode extends ExpressionNode {
  
  @Children protected ExpressionNode[] expressions;

  
  public PrintNode(ExpressionNode[] expressions) {
    this.expressions = expressions;
  }
  
 
  @ExplodeLoop
  @Override
  public Object executeGeneric(VirtualFrame frame) {
    for (int i = 0; i < this.expressions.length - 1; i++) {
      System.out.print(this.expressions[i].executeGeneric(frame) + " ");
    }
    System.out.println(this.expressions[this.expressions.length - 1].executeGeneric(frame));
    return null;
  }

}
