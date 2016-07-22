package javaic.codegen.python.model;

import javaic.codegen.model.CatchCG;
import javaic.parsetree.CatchJSG;

public class PyCatchCG extends CatchCG {

  public PyCatchCG(CatchJSG catchJSG) {
    super(catchJSG);
  }
  
  public String getCatchParmType() {
    PyVarDeclCG parm = (PyVarDeclCG) catchParm;
    if (parm.jType.qualifiedName.equals("java.lang.ArrayIndexOutOfBoundsException"))
      return "IndexError";
    else
      return parm.getType();
  }
  
  public String getCatchParmName() {
    return catchParm.getName();
  }

}
