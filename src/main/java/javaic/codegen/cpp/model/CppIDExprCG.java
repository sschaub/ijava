package javaic.codegen.cpp.model;

import javaic.codegen.model.IDExprCG;
import javaic.parsetree.IDExprJSG;

public class CppIDExprCG extends IDExprCG {

  public CppIDExprCG(IDExprJSG idExprJSG) {
    super(idExprJSG);
  }

  public String getIdR() {
    return id + (id.equals("this") || id.equals("super") ? "" : "_");
  }
  
  

}
