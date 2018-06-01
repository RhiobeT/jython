package org.python.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public abstract class ExpressionNode extends Node {
  
  public abstract Object executeGeneric(VirtualFrame frame);

  public int executeInteger(VirtualFrame frame) throws UnexpectedResultException {
    return TypesGen.expectInteger(executeGeneric(frame));
  }

  
  public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
    return TypesGen.expectBoolean(executeGeneric(frame));
  }

  
  public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
    return TypesGen.expectDouble(executeGeneric(frame));
  }
  
  
  public String executeString(VirtualFrame frame) throws UnexpectedResultException {
    return TypesGen.expectString(executeGeneric(frame));
  }
  
  
  public Object[] executeObjectArray(VirtualFrame frame) throws UnexpectedResultException {
    return TypesGen.expectObjectArray(executeGeneric(frame));
  }
  
}
