package javaic.codegen.model;

import javaic.parsetree.ArrayIndexExprJSG;

public class ArrayIndexExprCG extends ExpressionCG {
  
  public ExpressionCG arrayExprCG;
  
  public ExpressionCG indexExprCG;
  
  public ArrayIndexExprCG(ArrayIndexExprJSG arrayIndexExprJSG) {
    super(arrayIndexExprJSG.loc);
  }

}
