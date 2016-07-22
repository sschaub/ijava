package javaic.codegen.cpp.model;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.ExpressionCG;
import javaic.codegen.model.VarDeclCG;
import javaic.parsetree.VarDeclJSG;

public class CppVarDeclCG extends VarDeclCG {
  
  public String className;
  
  public ExpressionCG initExpr;

  public CppVarDeclCG(VarDeclJSG varDeclJSG) {
    super(varDeclJSG);
    if (varDeclJSG.parent != null) {
      className = CppRenderUtil.renderQualifiedName(varDeclJSG.parent.getFQName());
    }
  }
  
  public String getType() {
    return CppRenderUtil.typeToText(loc, jType);
  }
 

  @Override
  public void setInitExpr(ExpressionCG expression) {
    initExpr = (ExpressionCG) expression;
    
  }

  @Override
  public ExpressionCG getInitExpr() {
    return initExpr;
  }

  public boolean isDoFieldInit() {
    return !isStatic && initExpr != null;
  }
}
