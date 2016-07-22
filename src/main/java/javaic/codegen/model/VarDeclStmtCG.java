package javaic.codegen.model;

import javaic.parsetree.StatementJSG;

public class VarDeclStmtCG extends StatementCG {
  
  public VarDeclCG varDeclCG;

  public VarDeclStmtCG(VarDeclCG varDeclCG) {
    super(varDeclCG.loc);
    this.varDeclCG = varDeclCG;
  }

  public VarDeclStmtCG(StatementJSG stmtJSG) {
    super(stmtJSG.loc);
  }
  
  public String getId() {
    return varDeclCG.name;
  }

  public ExpressionCG getInitExpr() {
    return varDeclCG.getInitExpr();
  }
}
