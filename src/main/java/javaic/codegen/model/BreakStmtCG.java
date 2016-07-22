package javaic.codegen.model;

import javaic.parsetree.BreakStmtJSG;

public class BreakStmtCG extends StatementCG {

  public BreakStmtCG(BreakStmtJSG breakStmtJSG) {
    super(breakStmtJSG.loc);
  }

}
