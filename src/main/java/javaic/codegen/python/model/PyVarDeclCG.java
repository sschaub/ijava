package javaic.codegen.python.model;

import javaic.codegen.model.ExpressionCG;
import javaic.codegen.model.VarDeclCG;
import javaic.codegen.model.VerbatimExprCG;
import javaic.codegen.python.PythonRenderUtil;
import javaic.parsetree.SourceLocation;
import javaic.parsetree.VarDeclJSG;
import javaic.semantics.JavaiType;

public class PyVarDeclCG extends VarDeclCG {
  
  ExpressionCG initExprCG;

  public PyVarDeclCG(VarDeclJSG varDeclJSG) {
    super(varDeclJSG);
  }
  
  public PyVarDeclCG(SourceLocation loc, String name, JavaiType jType,
      boolean isStatic) {
    super(loc, name, jType, isStatic);
  }

  public String getType() {
    return PythonRenderUtil.renderQualifiedName(jType.qualifiedName);
  }

  @Override
  public void setInitExpr(ExpressionCG expression) {
    initExprCG = expression;
  }

  @Override
  public ExpressionCG getInitExpr() {
    if (initExprCG == null)
      return new VerbatimExprCG(PythonRenderUtil.getDefaultValueForType(jType));
    else
      return initExprCG;
  }

  @Override
  public String getName() {
    return PythonRenderUtil.renderId(name);
  }
  
  public String getRawName() {
    return name;
  }

}
