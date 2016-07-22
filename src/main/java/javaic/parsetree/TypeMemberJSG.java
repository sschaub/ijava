package javaic.parsetree;

public abstract class TypeMemberJSG extends JASGNode {

  public enum Visibility {PUBLIC, PRIVATE, PROTECTED};
  
  public TypeDeclJSG parent;
  
  public boolean isStatic;
  
  public Visibility visibility;
  
  public TypeMemberJSG(SourceLocation loc) {
    super(loc);
  }
  
}
