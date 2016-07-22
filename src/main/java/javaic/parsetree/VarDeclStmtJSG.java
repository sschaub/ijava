package javaic.parsetree;

public class VarDeclStmtJSG extends StatementJSG {
  public VarDeclJSG varDecl;

  public VarDeclStmtJSG(SourceLocation loc, VarDeclJSG varDecl) {
    super(loc);
    this.varDecl = varDecl;
  }
  
  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitVarDeclStmt(this);
  }

}
