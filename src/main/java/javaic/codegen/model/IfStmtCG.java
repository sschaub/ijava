package javaic.codegen.model;

import javaic.parsetree.IfStmtJSG;

public class IfStmtCG extends StatementCG {
  
  public ExpressionCG condExpr;
  
  public StatementCG thenStmt, elseStmt;

  public IfStmtCG(IfStmtJSG ifStmtJSG) {
    super(ifStmtJSG.loc);
  }

}
