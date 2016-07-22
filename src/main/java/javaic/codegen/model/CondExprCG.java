package javaic.codegen.model;

import javaic.parsetree.CondExprJSG;

public class CondExprCG extends ExpressionCG {
  
  public ExpressionCG condExpr;
  
  public ExpressionCG trueExpr, falseExpr;

  public CondExprCG(CondExprJSG condExprJSG) {
    super(condExprJSG.loc);
  }

}
