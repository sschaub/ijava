package javaic.semantics;

import javaic.parsetree.AsmtExprJSG;
import javaic.parsetree.BlockStmtJSG;
import javaic.parsetree.JavaiVisitor;
import javaic.parsetree.StatementJSG;

public class JavaiSemanticCheck extends JavaiVisitor {

  
  
  @Override
  public Object visitAsmtExpr(AsmtExprJSG asmtExpr) {
    System.out.println("Hit " + asmtExpr.lhs + " = " + asmtExpr.exprJSG);
    return null;
  }

  @Override
  public Object visitBlockStmt(BlockStmtJSG blockStmt) {
    System.out.println("Visiting a block statement...");
    return super.visitBlockStmt(blockStmt);
  }
  
  

}
