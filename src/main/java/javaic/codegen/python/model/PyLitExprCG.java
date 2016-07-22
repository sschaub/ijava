package javaic.codegen.python.model;

import javaic.codegen.CodeGenException;
import javaic.codegen.model.LitExprCG;
import javaic.codegen.python.PythonRenderUtil;
import javaic.parsetree.LitExprJSG;
import javaic.semantics.JavaiType;

public class PyLitExprCG extends LitExprCG {

  public PyLitExprCG(LitExprJSG litExprJSG) {
    super(litExprJSG);
  }

  @Override
  public String toString() {
    if (litExprJSG.type == JavaiType.BYTE ||
        litExprJSG.type == JavaiType.DOUBLE || litExprJSG.type == JavaiType.INT ||
        litExprJSG.type == JavaiType.LONG || litExprJSG.type == JavaiType.SHORT
        ) {
      return litExprJSG.toString();
    } else if (litExprJSG.type == JavaiType.BOOLEAN) {
      if (litExprJSG.value.equals("true"))
        return "True";
      else
        return "False";
    } else if (litExprJSG.type == JavaiType.CHAR) {
      return '"' + PythonRenderUtil.makeEscapedLiteral(litExprJSG.toString()) + '"';
    } else if (litExprJSG.type == JavaiType.STRING) {
      return "jcl_String(\"" + PythonRenderUtil.makeEscapedLiteral(litExprJSG.toString()) + "\")";
    } else if (litExprJSG.type == JavaiType.NULL) {
      return "None"; 
    } else {
      throw new CodeGenException(litExprJSG, "Unhandled literal expression: " + litExprJSG);
    }
  }

}
