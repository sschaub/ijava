package javaic.codegen.model;

import javaic.parsetree.BinExprJSG;
import javaic.parsetree.ExpressionJSG;

public abstract class BinExprCG extends ExpressionCG {
  
  public ExpressionJSG.OperatorKind opKind;
  
  public ExpressionCG left, right;
  
  public boolean isAssign;

  public BinExprCG(BinExprJSG binExprJSG) {
    super(binExprJSG.loc);
    this.opKind = binExprJSG.opKind;
    this.isAssign = binExprJSG.isAssign;
  }

  public abstract String getOp();
}
