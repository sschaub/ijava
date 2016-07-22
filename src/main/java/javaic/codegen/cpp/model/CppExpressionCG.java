package javaic.codegen.cpp.model;

import javaic.codegen.model.ExpressionCG;
import javaic.parsetree.ExpressionJSG;

public class CppExpressionCG extends ExpressionCG {

  public CppExpressionCG(ExpressionJSG exprJSG) {
    super(exprJSG.loc);
  }

}
