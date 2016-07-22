package javaic.parsetree;

public class ContinueStmtJSG extends StatementJSG {

  public ContinueStmtJSG(SourceLocation loc) {
    super(loc);
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitContinueStmt(this);
  }

}
