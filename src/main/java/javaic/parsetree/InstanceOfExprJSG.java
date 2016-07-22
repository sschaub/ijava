package javaic.parsetree;

import javaic.semantics.JavaiType;

public class InstanceOfExprJSG extends ExpressionJSG {
  
  public ExpressionJSG expr;
  
  public JavaiType testType;

  public InstanceOfExprJSG(SourceLocation loc, JavaiType type, ExpressionJSG expr, JavaiType testType) {
    super(loc, type);
    this.expr = expr;
    this.testType = testType;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitInstanceOfExpr(this);

  }
  
  

}
