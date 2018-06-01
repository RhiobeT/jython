package org.python.truffle;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class IfNode extends ExpressionNode {

  @Child ExpressionNode condition;
  @Child ExpressionNode thenNode;
  @Child ExpressionNode elseNode;
  
  
  public IfNode(ExpressionNode condition, ExpressionNode thenNode, ExpressionNode elseNode) {
    this.condition = condition;
    this.thenNode = thenNode;
    this.elseNode = elseNode;
  }
  
  
  @Override
  public Object executeGeneric(VirtualFrame frame) {
    if (this.testCondition(frame)) {
      thenNode.executeGeneric(frame);
    } else if (elseNode != null) {
      elseNode.executeGeneric(frame);
    }
    return null;
  }
  
  
  private boolean testCondition(VirtualFrame frame) {
    try {
      return this.condition.executeBoolean(frame);
    } catch (UnexpectedResultException e) {
      CompilerDirectives.transferToInterpreter();
      throw new UnsupportedSpecializationException(this, new Node[] {this.condition}, e.getResult());
    }
  }
  
}
