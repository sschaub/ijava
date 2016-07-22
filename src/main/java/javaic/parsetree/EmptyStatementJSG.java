package javaic.parsetree;

public class EmptyStatementJSG extends StatementJSG {

  public EmptyStatementJSG(SourceLocation loc) {
    super(loc);
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitEmptyStmt(this);

  }

}
