package org.python.truffle;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class NotNode extends UnaryNode {

  @Specialization
  public boolean doBoolean(boolean value) {
    return !value;
  }
    
}
