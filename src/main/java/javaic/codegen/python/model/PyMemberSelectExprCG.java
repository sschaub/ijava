package javaic.codegen.python.model;

import javaic.codegen.model.MemberSelectExprCG;
import javaic.codegen.python.PythonRenderUtil;
import javaic.parsetree.MemberSelectExprJSG;
import javaic.parsetree.SourceLocation;

public class PyMemberSelectExprCG extends MemberSelectExprCG {
  
  public boolean global;

  public PyMemberSelectExprCG(MemberSelectExprJSG exprJSG) {
    super(exprJSG);    
  }

  public PyMemberSelectExprCG(SourceLocation loc) {
    super(loc);
  }
  
  public PyMemberSelectExprCG(SourceLocation loc, String fqClassName, String id) {
    super(loc, fqClassName, id);
  }

  public String getFullClassName() {
    return PythonRenderUtil.renderQualifiedName(fqClassName);
  }
}
