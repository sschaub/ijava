package javaic.codegen.cpp.model;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.NewArrayExprCG;
import javaic.parsetree.NewArrayExprJSG;

public class CppNewArrayExprCG extends NewArrayExprCG {

  public CppNewArrayExprCG(NewArrayExprJSG newArrayExprJSG) {
    super(newArrayExprJSG);
  }
  
  public String getUltimateElTypeR() {
    return CppRenderUtil.typeToText(loc, getUltimateElType(elType));
  }

  public String getElTypeR() {
    return CppRenderUtil.typeToText(loc, elType);
  }
  
  public String getElDefaultValue() {
    return CppRenderUtil.getDefaultValueForType(getUltimateElType(elType));
  }

}
