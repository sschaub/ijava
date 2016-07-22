package javaic.codegen.python.model;

import javaic.codegen.model.ExpressionCG;
import javaic.codegen.model.MemberSelectExprCG;
import javaic.codegen.model.MethodInvokeExprCG;
import javaic.parsetree.MethodInvokeExprJSG;

public class PyMethodInvokeExprCG extends MethodInvokeExprCG {

  public PyMethodInvokeExprCG(MethodInvokeExprJSG methodInvokeExprJSG) {
    super(methodInvokeExprJSG);
  }

  public PyMethodInvokeExprCG(MemberSelectExprCG memberSelectExprCG, ExpressionCG... args) {
    super(memberSelectExprCG, args);
  }

}
