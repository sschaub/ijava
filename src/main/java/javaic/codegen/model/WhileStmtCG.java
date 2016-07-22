package javaic.codegen.model;

import javaic.parsetree.WhileStmtJSG;

public class WhileStmtCG extends StatementCG {

  public ExpressionCG condExpr;
  
  public StatementCG stmt;
  
  public WhileStmtCG(WhileStmtJSG whileStmtJSG) {
    super(whileStmtJSG.loc);
  }

}
