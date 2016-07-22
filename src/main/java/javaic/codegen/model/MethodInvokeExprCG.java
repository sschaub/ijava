package javaic.codegen.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javaic.parsetree.MethodInvokeExprJSG;
import javaic.semantics.JavaiType;
import javaic.semantics.MethodDeclaration;

public class MethodInvokeExprCG extends ExpressionCG {
  
  public MemberSelectExprCG methodExprCG;
  
  public List<ExpressionCG> args = new ArrayList<>();
  
  public JavaiType methodOwnerType;
  
  public MethodDeclaration methodDecl;

  public MethodInvokeExprCG(MethodInvokeExprJSG methodInvokeExprJSG) {
    super(methodInvokeExprJSG.loc);
    this.methodOwnerType = methodInvokeExprJSG.methodOwnerType;
    this.methodDecl = methodInvokeExprJSG.methodDecl;
  }

  public MethodInvokeExprCG(MemberSelectExprCG methodExprCG, ExpressionCG... args) {
    super(methodExprCG.loc);
    this.methodExprCG = methodExprCG;
    this.args = Arrays.asList(args);
  }

}
