package javaic.parsetree;

public class BreakStmtJSG extends StatementJSG {

  public BreakStmtJSG(SourceLocation loc) {
    super(loc);
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitBreakStmt(this);    
  }

}
