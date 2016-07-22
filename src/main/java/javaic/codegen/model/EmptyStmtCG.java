package javaic.codegen.model;

import javaic.parsetree.StatementJSG;

public class EmptyStmtCG extends StatementCG {

  public EmptyStmtCG(StatementJSG stmtJSG) {
    super(stmtJSG.loc);
  }

}
