package org.python.truffle;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

public class Function extends RootNode {

  private final FrameSlot[] args;
  @Child protected ExpressionNode body;
  
  
  public Function(ExpressionNode body, FrameSlot args[], FrameDescriptor frameDescriptor) {
    super(null, frameDescriptor);
    this.body = body;
    Truffle.getRuntime().createCallTarget(this);
    this.args = args;
  }
  
  
  @Override
  public Object execute(VirtualFrame frame) {
    try {
      for (int i = 0; i < this.args.length; i++) {
        this.args[i].setKind(FrameSlotKind.Object);
        frame.setObject(this.args[i], frame.getArguments()[i]);
      }
      return body.executeGeneric(frame);
    } catch (ReturnException e) {
      return e.getResult();
    }
  }
  
}
