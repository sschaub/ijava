package javaic.codegen.model;

import javaic.parsetree.IDExprJSG;
import javaic.parsetree.SourceLocation;

public class IDExprCG extends ExpressionCG {
  
  protected String id;

  public IDExprCG(IDExprJSG idExprJSG) {
    super(idExprJSG.loc);
    id = idExprJSG.name;
  }
  
  public IDExprCG(SourceLocation loc, String id) {
    super(loc);
    this.id = id;
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
    
  }
  
}
