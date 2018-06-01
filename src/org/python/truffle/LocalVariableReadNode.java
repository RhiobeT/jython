package org.python.truffle;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class LocalVariableReadNode extends ExpressionNode {

  private final FrameSlot slot;
  
  
  public LocalVariableReadNode(FrameSlot slot) {
    this.slot = slot;
  }
  
  
  @Specialization(guards = "isUninitialized()")
  public Object doNull() {
    return null;
  }
  
  
  @Specialization(guards = "isInitialized()", rewriteOn = {FrameSlotTypeException.class})
  public int doInt(VirtualFrame frame) throws FrameSlotTypeException {
    return frame.getInt(this.slot);
  }
  
  
  @Specialization(guards = "isInitialized()", rewriteOn = {FrameSlotTypeException.class})
  public double doDouble(VirtualFrame frame) throws FrameSlotTypeException {
    return frame.getDouble(this.slot);
  }
  
  
  @Specialization(guards = "isInitialized()", replaces = {"doInt", "doDouble"})
  public Object doObject(VirtualFrame frame) {
      return frame.getValue(this.slot);
  }
  
  
  protected boolean isInitialized() {
    return this.slot.getKind() != FrameSlotKind.Illegal;
  }

  
  protected boolean isUninitialized() {
    return this.slot.getKind() == FrameSlotKind.Illegal;
  }  
  
}
