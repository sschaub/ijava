package javaic.parsetree;

import javaic.semantics.JavaiType;

public class CastExprJSG extends ExpressionJSG {
  public ExpressionJSG castExpr;
  
  public CastExprJSG(SourceLocation loc, JavaiType type, ExpressionJSG castExpr) {
    super(loc, type);
    this.castExpr = castExpr;
  }
  
  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitCastExpr(this);
  }
  
  
}
