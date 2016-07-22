package javaic.codegen.python.model;

import javaic.codegen.model.VarDeclCG;
import javaic.codegen.model.VarDeclStmtCG;
import javaic.parsetree.StatementJSG;

public class PyVarDeclStmtCG extends VarDeclStmtCG {

  public PyVarDeclStmtCG(VarDeclCG varDeclCG) {
    super(varDeclCG);
  }

  public PyVarDeclStmtCG(StatementJSG stmtJSG) {
    super(stmtJSG);
  }

}
