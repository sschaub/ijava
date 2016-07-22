package javaic.parsetree;

public abstract class StatementJSG extends JASGNode {

  public StatementJSG(SourceLocation loc) {
    super(loc);
  }

  public abstract Object accept(JavaiVisitor javaiVisitor);

}
