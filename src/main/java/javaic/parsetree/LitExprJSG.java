package javaic.parsetree;

import javaic.semantics.JavaiType;

public class LitExprJSG extends ExpressionJSG {
  public String value;
  
  public LitExprJSG(SourceLocation loc, JavaiType type, String value) {
    super(loc, type);
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitLitExpr(this);
  }
  
  
} 
