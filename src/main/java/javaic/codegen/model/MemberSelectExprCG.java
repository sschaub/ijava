package javaic.codegen.model;

import javaic.parsetree.MemberSelectExprJSG;
import javaic.parsetree.SourceLocation;


public class MemberSelectExprCG extends ExpressionCG {

  public ExpressionCG expr; // for instance expressions
  
  public String id;
  
  public String fqClassName; // for static reference
  
  public boolean constructor;
  
  public boolean method;
  
  public MemberSelectExprCG(MemberSelectExprJSG exprJSG) {
    super(exprJSG.loc);
    id = exprJSG.id;
    if (exprJSG.classType != null)
      fqClassName = exprJSG.classType.qualifiedName;
    constructor = exprJSG.isConstructor;
    method = exprJSG.methType != null;
  }
  
  public MemberSelectExprCG(SourceLocation loc) {
    super(loc);
  }

  public MemberSelectExprCG(SourceLocation loc, String fqClassName, String id) {
    super(loc);
    this.id = id;
    this.fqClassName = fqClassName;
  }
}
