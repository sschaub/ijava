package javaic.semantics;

import java.util.List;

import javaic.parsetree.SourceLocation;

public class MethodDeclaration extends Declaration {

  public final String className;
  
  public final JavaiType returnType;
  
  public final List<JavaiType> parmTypes;
  
  public final boolean isStatic;
  
  public MethodDeclaration(SourceLocation loc, String className, String methodName, JavaiType returnType, List<JavaiType> parmTypes, boolean isStatic) {
    super(loc, null, methodName, new JavaiType(returnType, parmTypes), isStatic);
    this.className = className;
    this.returnType = returnType;
    this.parmTypes = parmTypes;
    this.isStatic = isStatic;
  }

  public boolean isConstructor() {
    return returnType == null;
  }  

}
