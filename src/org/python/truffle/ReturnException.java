package org.python.truffle;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class ReturnException extends ControlFlowException {
  
  /**
   * 
   */
  private static final long serialVersionUID = 3754118234806721497L;
  private final Object result;
  
  
  public ReturnException(Object result) {
    this.result = result;
  }
  
  
  public Object getResult() {
    return this.result;
  }
  
}