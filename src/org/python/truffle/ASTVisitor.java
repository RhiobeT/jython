package org.python.truffle;

import java.util.List;

import org.python.antlr.AST;
import org.python.antlr.ast.Assign;
import org.python.antlr.ast.BinOp;
import org.python.antlr.ast.Break;
import org.python.antlr.ast.Call;
import org.python.antlr.ast.Compare;
import org.python.antlr.ast.Expr;
import org.python.antlr.ast.FunctionDef;
import org.python.antlr.ast.If;
import org.python.antlr.ast.Import;
import org.python.antlr.ast.Module;
import org.python.antlr.ast.Name;
import org.python.antlr.ast.Num;
import org.python.antlr.ast.Print;
import org.python.antlr.ast.Return;
import org.python.antlr.ast.Str;
import org.python.antlr.ast.UnaryOp;
import org.python.antlr.ast.While;
import org.python.antlr.ast.cmpopType;
import org.python.antlr.ast.operatorType;
import org.python.antlr.ast.unaryopType;
import org.python.antlr.base.expr;
import org.python.antlr.base.stmt;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
import org.python.core.PyString;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;

public class ASTVisitor {
  
  private static ModuleNode moduleNode = null;
  

  public static ModuleNode toTruffleModule(Module module) {
    moduleNode = new ModuleNode();
    for (AST ast : module.getInternalBody()) {
      if (ast instanceof FunctionDef) {
        FunctionDef def = (FunctionDef) ast;
        moduleNode.addFunctionDeclaration(def.getInternalName(), toTruffleFunction(def));
      } else {
        moduleNode.addMainStatement(toTruffleExpression(ast));
      }
    }
    return moduleNode;
  }
  
  
  public static ExpressionNode toTruffleExpression(AST expression) {
    if (expression instanceof Import) {
      return new ImportNode(); // Does nothing
    }
    
    else if (expression instanceof Print) {
      List<expr> expressionsAst = ((Print) expression).getInternalValues();
      ExpressionNode expressions[] = new ExpressionNode[expressionsAst.size()];
      for (int i = 0; i < expressions.length; i++) {
        expressions[i] = toTruffleExpression(expressionsAst.get(i));
      }
      return new PrintNode(expressions);
      
    } else if (expression instanceof If) {
      If ifOriginal = (If) expression;
      List<stmt> thenStatements = ifOriginal.getInternalBody();
      ExpressionNode thenBody[] = new ExpressionNode[thenStatements.size()];
      for (int i = 0; i < thenBody.length; i++) {
        thenBody[i] = toTruffleExpression(thenStatements.get(i));
      }
      List<stmt> elseStatements = ifOriginal.getInternalOrelse();
      if (elseStatements != null && elseStatements.size() > 0) {
        ExpressionNode elseBody[] = new ExpressionNode[elseStatements.size()];
        for (int i = 0; i < elseBody.length; i++) {
          elseBody[i] = toTruffleExpression(elseStatements.get(i));
        }        
        return new IfNode(toTruffleExpression(ifOriginal.getInternalTest()), new SequenceNode(thenBody),
            new SequenceNode(elseBody));
      } else {
        return new IfNode(toTruffleExpression(ifOriginal.getInternalTest()), new SequenceNode(thenBody), null);
      }
    
    } else if (expression instanceof While) {
      While whileOriginal = (While) expression;
      List<stmt> whileStatements = whileOriginal.getInternalBody();
      ExpressionNode whileBody[] = new ExpressionNode[whileStatements.size()];
      for (int i = 0; i < whileBody.length; i++) {
        whileBody[i] = toTruffleExpression(whileStatements.get(i));
      }
      return new WhileNode(toTruffleExpression(whileOriginal.getInternalTest()), new SequenceNode(whileBody));
      
    } else if (expression instanceof Break) {
      return new BreakNode();
      
    } else if (expression instanceof Compare) {
      Compare compare = (Compare) expression;
      if (compare.getInternalOps().get(0).equals(cmpopType.Lt)) {
        return LessThanNodeGen.create(toTruffleExpression(compare.getInternalLeft()),
            toTruffleExpression(compare.getInternalComparators().get(0)));
      } else if (compare.getInternalOps().get(0).equals(cmpopType.Gt)) {
        return GreaterThanNodeGen.create(toTruffleExpression(compare.getInternalLeft()),
            toTruffleExpression(compare.getInternalComparators().get(0)));
      } else if (compare.getInternalOps().get(0).equals(cmpopType.Eq)) {
        return EqualNodeGen.create(toTruffleExpression(compare.getInternalLeft()),
            toTruffleExpression(compare.getInternalComparators().get(0)));
      }
      System.err.println("Error: comparator " + compare.getInternalOps().get(0) + " not implemented yet!");     

    } else if (expression instanceof UnaryOp) {
      UnaryOp unaryop = (UnaryOp) expression;
      if (unaryop.getInternalOp().equals(unaryopType.Not)) {
        return NotNodeGen.create(toTruffleExpression(unaryop.getInternalOperand()));
      }      
      System.err.println("Error: operator " + unaryop.getInternalOp() + " not implemented yet!");     
      
    } else if (expression instanceof BinOp) {
      BinOp binop = (BinOp) expression;
      if (binop.getInternalOp().equals(operatorType.Add)) {
        return PlusNodeGen.create(toTruffleExpression(binop.getInternalLeft()),
            toTruffleExpression(binop.getInternalRight()));
      } else if (binop.getInternalOp().equals(operatorType.Sub)) {
        return MinusNodeGen.create(toTruffleExpression(binop.getInternalLeft()),
            toTruffleExpression(binop.getInternalRight()));
      } else if (binop.getInternalOp().equals(operatorType.BitXor)) {        
        return BitXorNodeGen.create(toTruffleExpression(binop.getInternalLeft()),
            toTruffleExpression(binop.getInternalRight()));      
      } else if (binop.getInternalOp().equals(operatorType.BitOr)) {        
        return BitOrNodeGen.create(toTruffleExpression(binop.getInternalLeft()),
            toTruffleExpression(binop.getInternalRight()));      
      } else if (binop.getInternalOp().equals(operatorType.LShift)) {        
        return BitLeftShiftNodeGen.create(toTruffleExpression(binop.getInternalLeft()),
            toTruffleExpression(binop.getInternalRight()));
      } else if (binop.getInternalOp().equals(operatorType.Mult)) {        
        return MultNodeGen.create(toTruffleExpression(binop.getInternalLeft()),
            toTruffleExpression(binop.getInternalRight()));
      } else if (binop.getInternalOp().equals(operatorType.Div)) {        
        return DivNodeGen.create(toTruffleExpression(binop.getInternalLeft()),
            toTruffleExpression(binop.getInternalRight()));
      }
      System.err.println("Error: operator " + binop.getInternalOp() + " not implemented yet!");     
    
    } else if (expression instanceof Return) {
      Return returnOriginal = (Return) expression;
      return new ReturnNode(toTruffleExpression(returnOriginal.getInternalValue()));      
      
    } else if (expression instanceof Call) {
      Call call = (Call) expression;
      if (call.getInternalFunc().getText().equals("time")) {
        return new TimeNode();
      } else {
        ExpressionNode arguments[] = new ExpressionNode[call.getInternalArgs().size()];
        for (int i = 0; i < arguments.length; i++) {
          arguments[i] = toTruffleExpression(call.getInternalArgs().get(i));
        }
        return new FunctionInvocationNode(call.getInternalFunc().getText(), arguments, moduleNode);
      }
      
    } else if (expression instanceof Name) {
      Name name = (Name) expression;
      return LocalVariableReadNodeGen.create(moduleNode.peekFrameDescriptor().findFrameSlot(name.getInternalId()));
      
    } else if (expression instanceof Assign) {
      Assign assign = (Assign) expression;
      return LocalVariableWriteNodeGen.create(moduleNode.peekFrameDescriptor()
          .findOrAddFrameSlot(((Name) assign.getInternalTargets().get(0)).getInternalId()),
              toTruffleExpression(assign.getInternalValue()));
             
    } else if (expression instanceof Num) {
      Num num = (Num) expression;
      if (num.getInternalN() instanceof PyInteger) {
        return new IntegerLiteralNode(((PyInteger) num.getInternalN()).getValue());
      } else if (num.getInternalN() instanceof PyFloat) {        
        return new DoubleLiteralNode(((PyFloat) num.getInternalN()).getValue());
      }
      
      System.err.println("Error: Literal of type " + num.getInternalN().getClass() + " not implemented yet!");
      
    } else if (expression instanceof Str) {
      Str str = (Str) expression;
      return new StringLiteralNode(((PyString) str.getInternalS()).getString());

    } else if (expression instanceof Expr) {
      Expr expr = (Expr) expression;
      return toTruffleExpression(((Expr) expression).getInternalValue());
      
    }
    
    System.err.println("Error: " + expression.getClass() + " not implemented yet!");
    return null;
  }
  
  
  public static Function toTruffleFunction(FunctionDef function) {
    FrameDescriptor frameDescriptor = new FrameDescriptor();
    moduleNode.pushFrameDescriptor(frameDescriptor);
    
    List<expr> arguments = function.getInternalArgs().getInternalArgs();
    FrameSlot args[] = new FrameSlot[arguments.size()];
    for (int i = 0; i < args.length; i++) {
      args[i] = moduleNode.peekFrameDescriptor().addFrameSlot(arguments.get(i).getText());
    }
    
    List<stmt> statements = function.getInternalBody();
    ExpressionNode body[] = new ExpressionNode[statements.size()];
    for (int i = 0; i < body.length; i++) {
      body[i] = toTruffleExpression(statements.get(i));
    }

    moduleNode.popFrameDescriptor();
    return new Function(new SequenceNode(body), args, frameDescriptor);
  }
  
}
