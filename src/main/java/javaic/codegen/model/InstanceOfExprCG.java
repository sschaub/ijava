package javaic.codegen.model;

import javaic.parsetree.InstanceOfExprJSG;
import javaic.semantics.JavaiType;

public class InstanceOfExprCG extends ExpressionCG {

  public ExpressionCG expr;
  
  public JavaiType type;
  
  public InstanceOfExprCG(InstanceOfExprJSG instanceOfExprJSG) {
    super(instanceOfExprJSG.loc);
    type = instanceOfExprJSG.testType;
  }

}
