package org.python.truffle;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class FunctionInvocationNode extends ExpressionNode {
  protected String function;
  protected ModuleNode moduleNode;
  protected Function functionDeclaration;
  
  @Children protected ExpressionNode[] arguments;
  
  
  public FunctionInvocationNode(String function, ExpressionNode[] arguments, ModuleNode moduleNode) {
    this.function = function;
    this.arguments = arguments;
    this.moduleNode = moduleNode;
    this.functionDeclaration = null;
  }
  
  
  @ExplodeLoop
  @Override
  public Object executeGeneric(VirtualFrame frame) {
    Object[] arguments = new Object[this.arguments.length];
    for (int i = 0; i < arguments.length; i++) {
      arguments[i] = this.arguments[i].executeGeneric(frame);
    }
    
    if (this.functionDeclaration == null) {
      this.functionDeclaration = this.moduleNode.getFunction(this.function);
    }
    
    if (this.functionDeclaration != null) {
      return Truffle.getRuntime().createDirectCallNode(this.functionDeclaration.getCallTarget()).call(arguments);
    }
    
    System.err.println("Error: couldn't find declaration for function " + this.function);
    return null;
  } 

}
