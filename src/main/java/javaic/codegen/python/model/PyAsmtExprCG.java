package javaic.codegen.python.model;

import javaic.codegen.model.AsmtExprCG;
import javaic.codegen.model.ExpressionCG;
import javaic.parsetree.AsmtExprJSG;

public class PyAsmtExprCG extends AsmtExprCG {

  public PyAsmtExprCG(AsmtExprJSG asmtExprJSG) {
    super(asmtExprJSG);
  }

  public PyAsmtExprCG(ExpressionCG lhsCG, ExpressionCG exprCG) {
    super(lhsCG.loc);
    this.lhsCG = lhsCG;
    this.exprCG = exprCG;
  }

}
