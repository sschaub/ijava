package javaic.codegen.cpp.model;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.UnaryExprCG;
import javaic.parsetree.UnaryExprJSG;

public class CppUnaryExprCG extends UnaryExprCG {

  public CppUnaryExprCG(UnaryExprJSG unaryExprJSG) {
    super(unaryExprJSG);
  }

  @Override
  public String getOp() {
    return CppRenderUtil.getOp(loc, opKind);
  }

}
