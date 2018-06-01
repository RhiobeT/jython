package org.python.truffle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;

public class ModuleNode {
  
  private List<ExpressionNode> mainStatements;
  private Map<String, Function> functionDeclarations;
  private Stack<FrameDescriptor> frameDescriptors;
  private FrameDescriptor mainFrameDescriptor;
  
  
  public ModuleNode() {
    this.mainStatements = new ArrayList<ExpressionNode>();
    this.functionDeclarations = new HashMap<String, Function>();
    this.frameDescriptors = new Stack<>();
    this.mainFrameDescriptor = new FrameDescriptor();
    this.pushFrameDescriptor(this.mainFrameDescriptor);
  }
  
  
  public void addMainStatement(ExpressionNode statement) {
    this.mainStatements.add(statement);
  }
  
  
  public void addFunctionDeclaration(String name, Function function) {
    this.functionDeclarations.put(name, function);
  }
  
  
  public Function getFunction(String function) {
    return this.functionDeclarations.get(function);
  }
  
  
  public FrameDescriptor popFrameDescriptor() {
    return this.frameDescriptors.pop();
  }
  
  
  public FrameDescriptor peekFrameDescriptor() {
    return this.frameDescriptors.peek();
  }
  
  
  public void pushFrameDescriptor(FrameDescriptor frameDescriptor) {
    this.frameDescriptors.push(frameDescriptor);
  }
  

  public void execute() {
    Truffle.getRuntime()
        .createCallTarget(
            new Function(new SequenceNode(this.mainStatements.toArray(new ExpressionNode[0])), new FrameSlot[0],
                this.mainFrameDescriptor))
        .call(new Object[] {new String[0]});
  }

}
