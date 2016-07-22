package javaic.parsetree;

import javaic.semantics.JavaiType;

public abstract class ExpressionJSG extends JASGNode {
  
  public enum OperatorKind {
    AND, OR, NOT, PLUS, MINUS, MULTIPLY, DIVIDE, MODULUS,
    EQ, NOTEQ, LESSTHAN, LESSOREQ, GREATERTHAN, GREATEROREQ, LEFTSHIFT, RIGHTSHIFT, UNSIGNED_RIGHTSHIFT, XOR,
    BITAND, BITOR, BITNOT
  }

  public JavaiType type;


  public ExpressionJSG(SourceLocation loc, JavaiType type) {
    super(loc);
    this.type = type;
  }


  public abstract Object accept(JavaiVisitor javaiVisitor);

}
