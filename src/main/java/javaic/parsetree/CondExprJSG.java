package javaic.parsetree;

import javaic.semantics.JavaiType;

public class CondExprJSG extends ExpressionJSG {
  
  public ExpressionJSG condExpr;
  
  public ExpressionJSG trueExpr, falseExpr;

  public CondExprJSG(SourceLocation loc, JavaiType type, ExpressionJSG condExpr,
      ExpressionJSG trueExpr, ExpressionJSG falseExpr) {
    super(loc, type);
    this.condExpr = condExpr;
    this.trueExpr = trueExpr;
    this.falseExpr = falseExpr;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitCondExpr(this);
  }

}
