package javaic.parsetree;

import javaic.semantics.JavaiType;

public class UnaryExprJSG extends ExpressionJSG {
  public ExpressionJSG.OperatorKind opKind;
  
  public ExpressionJSG expr;

  public UnaryExprJSG(SourceLocation loc, JavaiType type, OperatorKind opKind, ExpressionJSG expr) {
    super(loc, type);
    this.opKind = opKind;
    this.expr = expr;
  }
  
  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitUnaryExpr(this);
  }

}
