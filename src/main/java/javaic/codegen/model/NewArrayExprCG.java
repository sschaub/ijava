package javaic.codegen.model;

import java.util.List;

import javaic.codegen.python.PythonRenderUtil;
import javaic.parsetree.NewArrayExprJSG;
import javaic.semantics.JavaiType;

public class NewArrayExprCG extends ExpressionCG {

  public JavaiType elType;
  
  public List<ExpressionCG> dimensExpr, initExpr;
  
  public NewArrayExprCG(NewArrayExprJSG newArrayExprJSG) {
    super(newArrayExprJSG.loc);
    this.elType = newArrayExprJSG.elType;
  }
  
  public ExpressionCG getFirstDimenExpr() {
    return dimensExpr.get(0);
  }


  public JavaiType getUltimateElType(JavaiType jType) {
    if (jType.elementType == null)
      return jType;
    else 
      return getUltimateElType(jType.elementType);
  }

}
