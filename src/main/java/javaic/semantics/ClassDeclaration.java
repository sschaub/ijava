package javaic.semantics;

import javaic.parsetree.SourceLocation;

public class ClassDeclaration extends Declaration {
  
  public final String packageName;
  
  public ClassDeclaration(SourceLocation loc, Declaration parent, String packageName, String id, JavaiType type, boolean isStatic) {
    super(loc, parent, id, type, isStatic);
    this.packageName = packageName;

//    fields = new ArrayList<>();
//    methods = new ArrayList<>();
  }


  public String getFQName() {
    return packageName == null ? id : packageName + "." + id;
  }
}
