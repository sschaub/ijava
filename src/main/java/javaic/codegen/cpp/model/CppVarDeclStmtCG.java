package javaic.codegen.cpp.model;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.VarDeclStmtCG;
import javaic.parsetree.StatementJSG;

public class CppVarDeclStmtCG extends VarDeclStmtCG {

  public CppVarDeclStmtCG(StatementJSG stmtJSG) {
    super(stmtJSG);
  }
  
  public String getType() {
    return CppRenderUtil.typeToText(loc, varDeclCG.jType);
  }

  @Override
  public String getId() {
    return super.getId() + "_";
  }
}
