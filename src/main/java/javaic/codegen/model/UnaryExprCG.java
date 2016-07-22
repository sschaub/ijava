package javaic.codegen.model;

import javaic.parsetree.ExpressionJSG;
import javaic.parsetree.UnaryExprJSG;

public abstract class UnaryExprCG extends ExpressionCG {

  public ExpressionJSG.OperatorKind opKind;
  
  public ExpressionCG expr;

  public UnaryExprCG(UnaryExprJSG unaryExprJSG) {
    super(unaryExprJSG.loc);
    opKind = unaryExprJSG.opKind;
  }
  
  public abstract String getOp();

}
