package javaic.parsetree;

import java.util.ArrayList;
import java.util.List;

import javaic.semantics.JavaiType;

public class TypeDeclJSG extends TypeMemberJSG {
  public enum Kind {
    CLASS, INTERFACE
  };

  public Kind kind;

  public String simpleName, packageName;

  public JavaiType extendsClass;

  public List<JavaiType> extendsInterfaces = new ArrayList<>();

  public List<MethodDeclJSG> methods= new ArrayList<>();
  
  public List<VarDeclJSG> fields = new ArrayList<>();

  private boolean isAbstract;

  public TypeDeclJSG(SourceLocation loc) {
    super(loc);
  }

  public TypeDeclJSG(SourceLocation loc, Kind type) {
    super(loc);
    this.kind = type;
  }

  public TypeDeclJSG(SourceLocation loc, Kind kind, String name, String packageName) {
    super(loc);
    this.kind = kind;
    this.simpleName = name;
    this.packageName = packageName;
  };
  
  public boolean isInterface() { return kind == Kind.INTERFACE; }

  public String getFQName() {
    if (packageName == null)
      return simpleName;
    else
      return packageName + "." + simpleName;
  }

  public boolean isAbstract() {
    return kind == Kind.INTERFACE || isAbstract;
  }

  public void setAbstract(boolean isAbstract) {
    this.isAbstract = isAbstract;
    
  }

  public JavaiType getJType() {
    return new JavaiType(getFQName());
  }
  
  @Override
  public String toString() {
    return getFQName();
  }

}
