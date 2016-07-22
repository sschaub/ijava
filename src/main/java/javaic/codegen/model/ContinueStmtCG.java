package javaic.codegen.model;

import javaic.parsetree.ContinueStmtJSG;

public class ContinueStmtCG extends StatementCG {

  public ContinueStmtCG(ContinueStmtJSG continueStmtJSG) {
    super(continueStmtJSG.loc);
  }

}
