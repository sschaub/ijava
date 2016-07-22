package javaic.codegen.cpp.model;

import javaic.codegen.CodeGenException;
import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.LitExprCG;
import javaic.codegen.python.PythonRenderUtil;
import javaic.parsetree.LitExprJSG;
import javaic.semantics.JavaiType;

public class CppLitExprCG extends LitExprCG {

  public CppLitExprCG(LitExprJSG litExprJSG) {
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
        return "true";
      else
        return "false";
    } else if (litExprJSG.type == JavaiType.CHAR) {
      return "'" + CppRenderUtil.makeEscapedLiteral(litExprJSG.toString()) + "'";
    } else if (litExprJSG.type == JavaiType.STRING) {    
      return "(new " + CppRenderUtil.renderQualifiedName("java.lang.String") + "(\"" + CppRenderUtil.makeEscapedLiteral(litExprJSG.toString()) + "\"))";
    } else if (litExprJSG.type == JavaiType.NULL) {
      return "NULL"; 
    } else {
      throw new CodeGenException(litExprJSG, "Unhandled literal expression: " + litExprJSG);
    }
  }

}
