package javaic.codegen.model;

import java.util.List;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.parsetree.MethodDeclJSG;
import javaic.parsetree.SourceLocation;
import javaic.semantics.JavaiType;

public abstract class MethodDeclCG {
  
  public SourceLocation loc;
    
  public boolean isStatic;
  
  public String name;
  
  public JavaiType returnType;

  public MethodDeclCG(MethodDeclJSG methodDeclJSG) {
    loc = methodDeclJSG.loc;
    isStatic = methodDeclJSG.isStatic; 
    returnType = methodDeclJSG.returnType;
    name = methodDeclJSG.name;
  }
  
  public MethodDeclCG(SourceLocation loc, String name, JavaiType returnType, boolean isStatic) {
    this.loc = loc;
    this.name = name;
    this.returnType = returnType;
    this.isStatic = isStatic;
  }
  
  public boolean isConstructor() {
    return returnType == null;
  }

  public abstract void setParms(List<VarDeclCG> parms);
  
  public abstract void setBody(BlockStmtCG blockStmtCG);
  
  public abstract BlockStmtCG getBody();
  
  
}
