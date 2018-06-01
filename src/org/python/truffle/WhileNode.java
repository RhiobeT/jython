package org.python.truffle;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.LoopNode;

public class WhileNode extends ExpressionNode {

  @Child protected LoopNode loopNode;
  
  
  public WhileNode(ExpressionNode condition, ExpressionNode body) {
    this.loopNode = Truffle.getRuntime().createLoopNode(new RepeatNode(condition, body, null));
  }
  
  
  @Override
  public Object executeGeneric(VirtualFrame frame) {
    try {
      this.loopNode.executeLoop(frame);
    } catch (BreakException e) {

    }
    return null;
  }

}
