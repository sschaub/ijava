package javaic.semantics;

import javaic.parsetree.SourceLocation;

public abstract class Declaration {
  public final SourceLocation loc;
  
  protected Declaration parent;

  public final String id;
  
  public final boolean isStatic;
  
  public final JavaiType type;

  public Declaration(SourceLocation loc, Declaration parent, String id, JavaiType type, boolean isStatic) {
    this.loc = loc;
    this.parent = parent;
    this.id = id;
    this.type = type;
    this.isStatic = isStatic;
  }

  public String getId() {
    return id;
  }

  public void setParent(Declaration parent) {
    this.parent = parent;
  }
  
  public ClassDeclaration getEnclosingClass() {
    if (parent instanceof ClassDeclaration)
      return (ClassDeclaration) parent;
    else 
      return getEnclosingClass();
  }
  
  
}
