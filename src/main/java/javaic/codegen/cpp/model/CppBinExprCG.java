package javaic.codegen.cpp.model;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.BinExprCG;
import javaic.parsetree.BinExprJSG;

public class CppBinExprCG extends BinExprCG {

  public CppBinExprCG(BinExprJSG binExprJSG) {
    super(binExprJSG);
  }

  @Override
  public String getOp() {
    return CppRenderUtil.getOp(loc, opKind) + (isAssign ? "=" : "");
  }

  
}
