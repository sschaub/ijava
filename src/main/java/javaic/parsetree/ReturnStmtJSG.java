package javaic.parsetree;

public class ReturnStmtJSG extends StatementJSG {
  public ExpressionJSG returnExpr;

  public ReturnStmtJSG(SourceLocation loc, ExpressionJSG returnExpr) {
    super(loc);
    this.returnExpr = returnExpr;
  }
  
  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitReturnStmt(this);
  }

}
