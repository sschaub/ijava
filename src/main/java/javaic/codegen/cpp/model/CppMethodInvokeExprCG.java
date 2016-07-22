package javaic.codegen.cpp.model;

import javaic.codegen.model.ExpressionCG;
import javaic.codegen.model.MemberSelectExprCG;
import javaic.codegen.model.MethodInvokeExprCG;
import javaic.parsetree.MethodInvokeExprJSG;

public class CppMethodInvokeExprCG extends MethodInvokeExprCG {

  public CppMethodInvokeExprCG(MemberSelectExprCG methodExprCG, ExpressionCG... args) {
    super(methodExprCG, args);
  }

  public CppMethodInvokeExprCG(MethodInvokeExprJSG methodInvokeExprJSG) {
    super(methodInvokeExprJSG);
  }

}
