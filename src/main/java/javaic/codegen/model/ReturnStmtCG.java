package javaic.codegen.model;

import javaic.parsetree.SourceLocation;
import javaic.parsetree.StatementJSG;

public class ReturnStmtCG extends StatementCG {
  
  public ExpressionCG returnExprCG;

  public ReturnStmtCG(StatementJSG stmtJSG) {
    super(stmtJSG.loc);
  }

  public ReturnStmtCG(ExpressionCG returnExprCG) {
    super(returnExprCG.loc);
    this.returnExprCG = returnExprCG;
  }

}
