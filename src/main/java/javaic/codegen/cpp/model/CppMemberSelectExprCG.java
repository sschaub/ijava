package javaic.codegen.cpp.model;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.ExpressionCG;
import javaic.codegen.model.MemberSelectExprCG;
import javaic.parsetree.MemberSelectExprJSG;
import javaic.parsetree.SourceLocation;

public class CppMemberSelectExprCG extends MemberSelectExprCG {
    
  public boolean global;

  public CppMemberSelectExprCG(MemberSelectExprJSG exprJSG) {
    super(exprJSG);
  }

  public CppMemberSelectExprCG(SourceLocation loc) {
    super(loc);
  }
  
  public CppMemberSelectExprCG(SourceLocation loc, ExpressionCG expr, String id, boolean method) {
    super(loc);
    this.expr = expr;
    this.id = id;
    this.method = method;
  }
  
  public String getFullClassName() {
    return CppRenderUtil.renderQualifiedName(fqClassName);
  }
  
  public String getId() {
    if (method) {
      return id;
    } else {
      return id + "_";
    }
  }
}
