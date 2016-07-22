package javaic.codegen.model;

import javaic.parsetree.SourceLocation;
import javaic.parsetree.VarDeclJSG;
import javaic.semantics.JavaiType;

public abstract class VarDeclCG {
  
  public SourceLocation loc;
  
  protected String name;
  
  public JavaiType jType;
  
  public boolean isStatic;

  public VarDeclCG(VarDeclJSG varDeclJSG) {
    this.loc = varDeclJSG.loc;
    name = varDeclJSG.name;
    jType = varDeclJSG.jType;
    isStatic = varDeclJSG.isStatic;
  }
  
  public VarDeclCG(SourceLocation loc, String name, JavaiType jType,
      boolean isStatic) {
    this.loc = loc;
    this.name = name;
    this.jType = jType;
    this.isStatic = isStatic;
  }

  public abstract void setInitExpr(ExpressionCG expression);
  
  public abstract ExpressionCG getInitExpr();
  
  public String getName() {
    return name;
  }
  
}
