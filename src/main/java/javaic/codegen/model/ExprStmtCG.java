package javaic.codegen.model;

import javaic.parsetree.SourceLocation;
import javaic.parsetree.StatementJSG;

public class ExprStmtCG extends StatementCG {
  
  public ExpressionCG exprCG;

  public ExprStmtCG(StatementJSG stmtJSG) {
    super(stmtJSG.loc);
  }
  
  public ExprStmtCG(ExpressionCG exprCG) {
    super(exprCG.loc);
    this.exprCG = exprCG;
  }
  
  public ExprStmtCG(SourceLocation loc, String expr) {
    super(loc);
    this.exprCG = new VerbatimExprCG(expr);
  }

}
