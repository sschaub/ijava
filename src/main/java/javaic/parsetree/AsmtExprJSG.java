package javaic.parsetree;

import javaic.semantics.JavaiType;

public class AsmtExprJSG extends ExpressionJSG {
  
  public ExpressionJSG lhs;
  
  public ExpressionJSG exprJSG;
  
  public AsmtExprJSG(SourceLocation loc, JavaiType type, ExpressionJSG lhs, ExpressionJSG exprJSG) {
    super(loc, type);
    this.lhs = lhs;
    this.exprJSG = exprJSG;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitAsmtExpr(this);
    
  }

}
