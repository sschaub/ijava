package javaic.codegen.model;

import javaic.parsetree.ThrowStmtJSG;

public class ThrowStmtCG extends StatementCG {
  public ExpressionCG throwExpr;

  public ThrowStmtCG(ThrowStmtJSG throwStmtJSG) {
    super(throwStmtJSG.loc);
  }

}
