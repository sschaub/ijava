package javaic.codegen.python.model;

import javaic.codegen.model.IDExprCG;
import javaic.codegen.python.PythonRenderUtil;
import javaic.parsetree.IDExprJSG;
import javaic.parsetree.SourceLocation;

public class PyIDExprCG extends IDExprCG {

  public PyIDExprCG(SourceLocation loc, String id) {
    super(loc, id);
  }

  public PyIDExprCG(IDExprJSG idExprJSG) {
    super(idExprJSG);
  }

  @Override
  public String getId() {
    return PythonRenderUtil.renderId(id);
  }
  
}
