package javaic.parsetree;

import javaic.semantics.Declaration;
import javaic.semantics.JavaiType;

public class IDExprJSG extends ExpressionJSG {
  public String name;
  
  public final Declaration decl;

  public IDExprJSG(SourceLocation loc, JavaiType typeJSG, String name, Declaration decl) {
    super(loc, typeJSG);
    this.name = name;
    this.decl = decl;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitIDExpr(this);
  }
  
  
}
