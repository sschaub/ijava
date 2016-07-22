package javaic.parsetree;

import javaic.semantics.JavaiType;

public class VarDeclJSG extends TypeMemberJSG {
	
	public String name;
	
	public JavaiType jType;
	
	public ExpressionJSG expr;

	public VarDeclJSG(SourceLocation loc, String name, JavaiType type, ExpressionJSG expr) {
	  super(loc);
		this.name = name;
		this.jType = type;
		this.expr = expr;
	}
	
  public VarDeclJSG(SourceLocation loc, String name, JavaiType type) {
    super(loc);
    this.name = name;
    this.jType = type;
  }

  public String toString() {
    return jType + " " + name;
  }
}
