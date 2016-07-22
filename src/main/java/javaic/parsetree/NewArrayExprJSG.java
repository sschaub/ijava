package javaic.parsetree;

import java.util.List;

import javaic.semantics.JavaiType;

public class NewArrayExprJSG extends ExpressionJSG {
  public JavaiType elType;
  
  public List<ExpressionJSG> dimensExpr, initExpr;

  public NewArrayExprJSG(SourceLocation loc, JavaiType type, JavaiType elType, List<ExpressionJSG> dimensExpr,
      List<ExpressionJSG> initExpr) {
    super(loc, type);
    this.elType = elType;
    this.dimensExpr = dimensExpr;
    this.initExpr = initExpr;
  }
  
  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitNewArrayExpr(this);
  }

}
