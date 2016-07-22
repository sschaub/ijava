package javaic.parsetree;

public class JavaiVisitor<T> {
  public T visitCompileUnit(CompileUnitJSG compileUnitJSG) {
    for (TypeDeclJSG typeDecl : compileUnitJSG.decls)
      visitTypeDecl(typeDecl);
    
    return null;
  }

  public T visitTypeDecl(TypeDeclJSG typeDecl) {
    for (VarDeclJSG fieldDef : typeDecl.fields) 
      visitVarDecl(fieldDef);
  
    for (MethodDeclJSG methDef : typeDecl.methods)
      visitMethodDecl(methDef);
    
    return null;
  }

  public T visitMethodDecl(MethodDeclJSG methDecl) {
    visitBlockStmt(methDecl.body);
    return null;
  }

  public T visitVarDecl(VarDeclJSG varDecl) {
    return null;
    
  }
  
  public T visitExprStmt(ExprStmtJSG exprStmtJSG) {
    return (T) exprStmtJSG.exprJSG.accept(this);
  }

  public T visitAsmtExpr(AsmtExprJSG asmtExpr) {
    return null;
  }

  public T visitArrayIndexExpr(ArrayIndexExprJSG arrayIndexExprJSG) {
    return null;
    
  }

  public T visitBinExpr(BinExprJSG binExprJSG) {
    return null;
    
  }

  public T visitBlockStmt(BlockStmtJSG blockStmtJSG) {
    for (StatementJSG stmt : blockStmtJSG.statements) 
      stmt.accept(this); 
    return null;
  }

  public T visitBreakStmt(BreakStmtJSG breakStmtJSG) {
    return null;
  }

  public T visitCastExpr(CastExprJSG castExprJSG) {
    return null;
  }

  public T visitContinueStmt(ContinueStmtJSG continueStmtJSG) {
    return null;
  }

  public T visitIDExpr(IDExprJSG idExprJSG) {
    return null;
  }

  public T visitIfStmt(IfStmtJSG ifStmtJSG) {
    return null;
  }

  public T visitInstanceOfExpr(InstanceOfExprJSG instanceOfExprJSG) {
    return null;
  }

  public T visitLitExpr(LitExprJSG litExprJSG) {
    return null;
  }

  public T visitMemberSelectExpr(MemberSelectExprJSG memberSelectExprJSG) {
    return null;
  }

  public T visitMethodInvokeExpr(MethodInvokeExprJSG methodInvokeExprJSG) {
    return null;
  }

  public T visitNewArrayExpr(NewArrayExprJSG newArrayExprJSG) {
    return null;
  }

  public T visitNewClassExpr(NewClassExprJSG newClassExprJSG) {
    return null;
  }

  public T visitReturnStmt(ReturnStmtJSG returnStmtJSG) {
    return null;
  }

  public T visitThrowStmt(ThrowStmtJSG throwStmtJSG) {
    return null;
  }

  public T visitTryStmt(TryStmtJSG tryStmtJSG) {
    return null;
  }

  public T visitUnaryExpr(UnaryExprJSG unaryExprJSG) {
    return null;
  }

  public T visitVarDeclStmt(VarDeclStmtJSG varDeclStmtJSG) {
    return null;
  }

  public T visitWhileStmt(WhileStmtJSG whileStmtJSG) {
    return null;
  }

  public T visitEmptyStmt(EmptyStatementJSG emptyStatementJSG) {
    return null;
  }

  public T visitCondExpr(CondExprJSG condExprJSG) {
    return null;
  }

}
