package org.python.truffle;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild(value = "expr", type = ExpressionNode.class)
public abstract class LocalVariableWriteNode extends ExpressionNode {

  protected final FrameSlot slot;
  
  
  public LocalVariableWriteNode(FrameSlot slot) {
    this.slot = slot;
  }
  
  
  @Specialization(guards = "isIntKind()")
  public int writeInt(VirtualFrame frame, int value) {
    frame.setInt(this.slot, value);
    return value;
  }
  
    
  @Specialization(guards = "isDoubleKind()")
  public double writeDouble(VirtualFrame frame, double value) {
    frame.setDouble(this.slot, value);
    return value;
  }
  
  
  @Specialization(replaces = {"writeInt", "writeDouble"})
  public Object writeGeneric(VirtualFrame frame, Object value) {
    this.slot.setKind(FrameSlotKind.Object);
    frame.setObject(this.slot, value);
    return value;
  }
  
  
  protected final boolean isIntKind() {
    if (this.slot.getKind() == FrameSlotKind.Int) {
      return true;
    } else if (this.slot.getKind() == FrameSlotKind.Illegal) {
      this.slot.setKind(FrameSlotKind.Int);
      return true;
    }
    return false;
  }

  
  protected final boolean isDoubleKind() {
    if (this.slot.getKind() == FrameSlotKind.Double) {
      return true;
    } else if (this.slot.getKind() == FrameSlotKind.Illegal) {
      this.slot.setKind(FrameSlotKind.Double);
      return true;
    }
    return false;
  }
  
}
