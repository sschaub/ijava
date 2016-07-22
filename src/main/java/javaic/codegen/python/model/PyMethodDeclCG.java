package javaic.codegen.python.model;

import java.util.List;
import java.util.stream.Collectors;

import javaic.codegen.model.BlockStmtCG;
import javaic.codegen.model.MethodDeclCG;
import javaic.codegen.model.VarDeclCG;
import javaic.parsetree.MethodDeclJSG;
import javaic.parsetree.SourceLocation;
import javaic.semantics.JavaiType;

public class PyMethodDeclCG extends MethodDeclCG {

  public List<VarDeclCG> parms;
  
  public PyBlockStmtCG body;

  public PyMethodDeclCG(MethodDeclJSG methodDeclJSG) {
    super(methodDeclJSG);
    name =  /* isConstructor() ? "__init__" : */ methodDeclJSG.name;
  }
  
  public PyMethodDeclCG(SourceLocation loc, String name, JavaiType returnType,
      List<VarDeclCG> parms, PyBlockStmtCG body, boolean isStatic) {
    super(loc, name, returnType, isStatic);
    setParms(parms);
    this.body = body;
  }
  
  @Override
  public void setParms(List<VarDeclCG> parms) {
    this.parms = parms;
  }

  @Override
  public void setBody(BlockStmtCG blockStmtCG) {
    body = (PyBlockStmtCG) blockStmtCG;    
  }

  @Override
  public BlockStmtCG getBody() {
    return body;
  }
 
}
