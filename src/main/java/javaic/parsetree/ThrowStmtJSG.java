package javaic.parsetree;

public class ThrowStmtJSG extends StatementJSG {
  public ExpressionJSG throwExpr;

  public ThrowStmtJSG(SourceLocation loc, ExpressionJSG throwExpr) {
    super(loc);
    this.throwExpr = throwExpr;
  }
  
  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitThrowStmt(this);
  }

}
