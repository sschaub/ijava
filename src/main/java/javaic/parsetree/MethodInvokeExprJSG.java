package javaic.parsetree;

import java.util.List;

import javaic.semantics.JavaiType;
import javaic.semantics.MethodDeclaration;

public class MethodInvokeExprJSG extends ExpressionJSG {
  public MemberSelectExprJSG methodExpr;
  
  public List<ExpressionJSG> args;
  
  public JavaiType methodOwnerType;
  
  public MethodDeclaration methodDecl;

  public MethodInvokeExprJSG(SourceLocation loc, JavaiType type, MemberSelectExprJSG methodExpr, JavaiType jMethOwnerType, List<ExpressionJSG> args, MethodDeclaration methodDecl) {
    super(loc, type);
    this.methodExpr = methodExpr;
    this.args = args;
    this.methodOwnerType = jMethOwnerType;
    this.methodDecl = methodDecl;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitMethodInvokeExpr(this);
  }
  
  
}
