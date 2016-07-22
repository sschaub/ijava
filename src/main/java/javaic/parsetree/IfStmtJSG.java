package javaic.parsetree;

public class IfStmtJSG extends StatementJSG {
  
  public ExpressionJSG condExpr;
  
  public StatementJSG thenStmt, elseStmt;

  public IfStmtJSG(SourceLocation loc, ExpressionJSG condExpr, StatementJSG thenStmt,
      StatementJSG elseStmt) {
    super(loc);
    this.condExpr = condExpr;
    this.thenStmt = thenStmt;
    this.elseStmt = elseStmt;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitIfStmt(this);
  }
  
  

}
