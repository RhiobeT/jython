package org.python.truffle;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RepeatingNode;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class RepeatNode extends Node implements RepeatingNode {

  @Child protected ExpressionNode condition;
  @Child protected ExpressionNode body;
  @Child protected ExpressionNode post;
  
  
  public RepeatNode(ExpressionNode condition, ExpressionNode body, ExpressionNode post) {
    this.condition = condition;
    this.body = body;
    this.post = post;
  }
  
  
  @Override
  public boolean executeRepeating(VirtualFrame virtualFrame) {
    try {
      if (this.condition.executeBoolean(virtualFrame)) {
        this.body.executeGeneric(virtualFrame);
        if (this.post != null) {
          this.post.executeGeneric(virtualFrame);
        }
        return true;
      } else {
        return false;
      }
    } catch (UnexpectedResultException e) {
      CompilerDirectives.transferToInterpreter();
      throw new UnsupportedSpecializationException(this, new Node[] {this.condition}, e.getResult());
    }
  }
  
}
