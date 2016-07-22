package javaic.codegen.python.model;

import javaic.codegen.model.UnaryExprCG;
import javaic.codegen.python.PythonRenderUtil;
import javaic.parsetree.UnaryExprJSG;

public class PyUnaryExprCG extends UnaryExprCG {

  public PyUnaryExprCG(UnaryExprJSG unaryExprJSG) {
    super(unaryExprJSG);
  }


  @Override
  public String getOp() {
    return PythonRenderUtil.getOp(loc, opKind);
  }

}
