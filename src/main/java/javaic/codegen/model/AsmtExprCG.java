package javaic.codegen.model;

import javaic.parsetree.AsmtExprJSG;
import javaic.parsetree.SourceLocation;

public class AsmtExprCG extends ExpressionCG {
 
  public ExpressionCG lhsCG;
  
  public ExpressionCG exprCG;
  
  public AsmtExprCG(AsmtExprJSG asmtExprJSG) {
    super(asmtExprJSG.loc);
  }

  public AsmtExprCG(SourceLocation loc) {
    super(loc);
  }

}
