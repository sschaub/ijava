package javaic.codegen.cpp.model;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.CastExprCG;
import javaic.parsetree.CastExprJSG;

public class CppCastExprCG extends CastExprCG {

  public CppCastExprCG(CastExprJSG castExprJSG) {
    super(castExprJSG);
  }

  public String getTypeR() {
    return CppRenderUtil.typeToText(loc, type);
  }
}
