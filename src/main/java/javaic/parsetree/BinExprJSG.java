package javaic.parsetree;

import javaic.semantics.JavaiType;

public class BinExprJSG extends ExpressionJSG {
  public ExpressionJSG.OperatorKind opKind;
  
  public ExpressionJSG left, right;
  
  public boolean isAssign;

  public BinExprJSG(SourceLocation loc, JavaiType type, OperatorKind opKind, ExpressionJSG left, ExpressionJSG right, boolean isAssign) {
    super(loc, type);
    this.opKind = opKind;
    this.left = left;
    this.right = right;
    this.isAssign = isAssign;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitBinExpr(this);
  }
  
  
}
