package javaic.parsetree;

import javaic.semantics.JavaiType;

public class MemberSelectExprJSG extends ExpressionJSG {
  public enum SelectType { STATIC, INSTANCE };
  
  public SelectType selectType;
  
  public ExpressionJSG expr; // for instance reference (null if static)
  
  public JavaiType classType; // for static reference
  
  public JavaiType methType; // for method, null for var
  
  public String id; 
  
  public final boolean isConstructor; // true if this is a call to super() or this()

  public MemberSelectExprJSG(SourceLocation loc, JavaiType type, ExpressionJSG expr, JavaiType methType, String id, boolean isConstructor) {
    super(loc, type);
    this.selectType = SelectType.INSTANCE;
    this.expr = expr;
    this.methType = methType;
    this.id = id;
    this.isConstructor = isConstructor;
  }

  public MemberSelectExprJSG(SourceLocation loc, JavaiType type, ExpressionJSG expr, String id) {
    super(loc, type);
    this.selectType = SelectType.INSTANCE;
    this.expr = expr;
    this.id = id;
    this.isConstructor = false;
  }

  public MemberSelectExprJSG(SourceLocation loc, JavaiType type, JavaiType classType, JavaiType methType, String id) {
    super(loc, type);
    this.selectType = SelectType.STATIC;
    this.classType = classType;
    this.methType = methType;
    this.id = id;
    this.isConstructor = false;
  }

  public MemberSelectExprJSG(SourceLocation loc, JavaiType type, JavaiType classType, String id) {
    super(loc, type);
    this.selectType = SelectType.STATIC;
    this.classType = classType;
    this.id = id;
    this.isConstructor = false;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitMemberSelectExpr(this);
  }
  
  
}
