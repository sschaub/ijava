package javaic.codegen.model;

import javaic.parsetree.LitExprJSG;

public class LitExprCG extends ExpressionCG {
  
  public LitExprJSG litExprJSG;

  public LitExprCG(LitExprJSG litExprJSG) {
    super(litExprJSG.loc);
    this.litExprJSG = litExprJSG;
  }
  
  
}
