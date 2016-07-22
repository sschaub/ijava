package javaic.parsetree;

public class WhileStmtJSG extends StatementJSG {
  
  public ExpressionJSG condExpr;
  
  public StatementJSG stmt;

  public WhileStmtJSG(SourceLocation loc, ExpressionJSG condExpr, StatementJSG stmt) {
    super(loc);
    this.condExpr = condExpr;
    this.stmt = stmt;
  }
  
  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitWhileStmt(this);
  }


}
