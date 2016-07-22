package javaic.codegen.model;

import javaic.parsetree.CastExprJSG;
import javaic.semantics.JavaiType;

public class CastExprCG extends ExpressionCG {
  
  public JavaiType type;
  public ExpressionCG castExpr;

  public CastExprCG(CastExprJSG castExprJSG) {
    super(castExprJSG.loc);
    type = castExprJSG.type;
  }

}
