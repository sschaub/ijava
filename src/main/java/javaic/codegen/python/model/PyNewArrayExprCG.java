package javaic.codegen.python.model;

import javaic.codegen.model.NewArrayExprCG;
import javaic.codegen.python.PythonRenderUtil;
import javaic.parsetree.NewArrayExprJSG;

public class PyNewArrayExprCG extends NewArrayExprCG {

  public PyNewArrayExprCG(NewArrayExprJSG newArrayExprJSG) {
    super(newArrayExprJSG);
  }
  
  
  public String getElDefaultValue() {
    return PythonRenderUtil.getDefaultValueForType(getUltimateElType(elType));
  }

}
