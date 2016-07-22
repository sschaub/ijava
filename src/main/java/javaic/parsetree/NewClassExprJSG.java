package javaic.parsetree;

import java.util.List;

import javaic.semantics.JavaiType;
import javaic.semantics.MethodDeclaration;

public class NewClassExprJSG extends ExpressionJSG {
  
  public List<ExpressionJSG> args;  
  
  public MethodDeclaration methodDecl;

  public NewClassExprJSG(SourceLocation loc, JavaiType type,
      List<ExpressionJSG> args, MethodDeclaration methodDecl) {
    super(loc, type);
    this.args = args;
    this.methodDecl = methodDecl;
  }
  
  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitNewClassExpr(this);
  }

}
