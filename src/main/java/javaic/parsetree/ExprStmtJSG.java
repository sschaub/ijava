package javaic.parsetree;

public class ExprStmtJSG extends StatementJSG {

  public ExpressionJSG exprJSG;

  public ExprStmtJSG(SourceLocation loc, ExpressionJSG exprJSG) {
    super(loc);
    this.exprJSG = exprJSG;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitExprStmt(this);
  }

}
