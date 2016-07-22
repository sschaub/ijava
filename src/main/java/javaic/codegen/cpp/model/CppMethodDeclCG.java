package javaic.codegen.cpp.model;

import java.util.ArrayList;
import java.util.List;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.BlockStmtCG;
import javaic.codegen.model.MethodDeclCG;
import javaic.codegen.model.MethodInvokeExprCG;
import javaic.codegen.model.VarDeclCG;
import javaic.parsetree.MethodDeclJSG;
import javaic.parsetree.SourceLocation;
import javaic.semantics.JavaiType;

public class CppMethodDeclCG extends MethodDeclCG {
  
  public List<CppVarDeclCG> parms;
  
  public CppBlockStmtCG body;

  public MethodInvokeExprCG baseConstructorCall; // for constructor method
  
  public String fqClassName;
  
  public boolean optionalParmFlag; // true if this is the constructor of a test class

  public CppMethodDeclCG(MethodDeclJSG methodDeclJSG) {
    super(methodDeclJSG);
    fqClassName = CppRenderUtil.renderQualifiedName(methodDeclJSG.parent.getFQName());
    if (returnType == null) 
        name = fqClassName; // constructor
       
  }

  public CppMethodDeclCG(SourceLocation loc, String fqClassName, String name, JavaiType returnType,
      ArrayList<CppVarDeclCG> parms, CppBlockStmtCG body, boolean isStatic) {
    super(loc, name, returnType, isStatic);
    this.parms = parms;
    this.body = body;
    this.fqClassName = CppRenderUtil.renderQualifiedName(fqClassName);
  }

  @Override
  public void setParms(List<VarDeclCG> parms) {
    this.parms = (List)parms;
  }
  
  public boolean isVirtual() {
    return !isStatic && !isConstructor();
  }
  
  
  public String getReturnType() {
    return returnType == null ? null : CppRenderUtil.typeToText(loc, returnType);
  }

  @Override
  public void setBody(BlockStmtCG blockStmtCG) {
    this.body = (CppBlockStmtCG) blockStmtCG;
  }

  @Override
  public BlockStmtCG getBody() {
    return body;
  }

  public boolean isOptionalParmFlag() {
    return optionalParmFlag;
  }

  public void setOptionalParmFlag(boolean optionalParmFlag) {
    this.optionalParmFlag = optionalParmFlag;
  }
 
}
