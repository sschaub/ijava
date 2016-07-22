package javaic.parsetree;

import javaic.semantics.JavaiType;

public class ArrayIndexExprJSG extends ExpressionJSG {
  public ExpressionJSG arrayExprJSG, indexExprJSG;

  public ArrayIndexExprJSG(SourceLocation loc, JavaiType type, ExpressionJSG arrayExprJSG,
      ExpressionJSG indexExprJSG) {
    super(loc, type);
    this.arrayExprJSG = arrayExprJSG;
    this.indexExprJSG = indexExprJSG;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitArrayIndexExpr(this);
  }
  
  
}
