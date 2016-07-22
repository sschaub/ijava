package javaic.codegen.python.model;

import javaic.codegen.model.BinExprCG;
import javaic.codegen.python.PythonRenderUtil;
import javaic.parsetree.BinExprJSG;

public class PyBinExprCG extends BinExprCG {

  public PyBinExprCG(BinExprJSG binExprJSG) {
    super(binExprJSG);
  }

  @Override
  public String getOp() {
    return PythonRenderUtil.getOp(loc, opKind) + (isAssign ? "=" : "");
  }

}
