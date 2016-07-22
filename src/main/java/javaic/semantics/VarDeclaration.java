package javaic.semantics;

import javaic.parsetree.SourceLocation;


public class VarDeclaration extends Declaration {

  public VarDeclaration(SourceLocation loc, Declaration parent, String id, JavaiType type, boolean isStatic) {
    super(loc, parent, id, type, isStatic);
  }


  public boolean isLocal() {
    return parent instanceof MethodDeclaration || id.equals("super");
  }
  
  public ClassDeclaration getEnclosingClass() {
    if (!isLocal()) 
      return (ClassDeclaration) parent;
    
    return null;
  }
  
}
