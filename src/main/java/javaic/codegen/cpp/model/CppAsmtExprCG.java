package javaic.codegen.cpp.model;

import javaic.codegen.model.AsmtExprCG;
import javaic.codegen.model.ExpressionCG;
import javaic.parsetree.AsmtExprJSG;

public class CppAsmtExprCG extends AsmtExprCG {

  public CppAsmtExprCG(AsmtExprJSG asmtExprJSG) {
    super(asmtExprJSG);
  }

  public CppAsmtExprCG(ExpressionCG lhsCG, ExpressionCG exprCG) {
    super(lhsCG.loc);
    this.lhsCG = lhsCG;
    this.exprCG = exprCG;
  }
}
