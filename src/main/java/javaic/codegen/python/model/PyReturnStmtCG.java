package javaic.codegen.python.model;

import javaic.codegen.model.ExpressionCG;
import javaic.codegen.model.ReturnStmtCG;
import javaic.parsetree.StatementJSG;

public class PyReturnStmtCG extends ReturnStmtCG {

  public PyReturnStmtCG(StatementJSG stmtJSG) {
    super(stmtJSG);
  }

  public PyReturnStmtCG(ExpressionCG returnExprCG) {
    super(returnExprCG);
  }

}
