package javaic.parsetree;

import java.util.ArrayList;
import java.util.List;

import javaic.semantics.JavaiType;

public class MethodDeclJSG extends TypeMemberJSG {
	public String name;
	
	public JavaiType returnType;
	
	public List<VarDeclJSG> parms = new ArrayList<>();

	public BlockStmtJSG body;

	public MethodDeclJSG(SourceLocation loc, String name, JavaiType returnType,
			List<VarDeclJSG> parms) {
	  super(loc);
		this.name = name;
		this.returnType = returnType;
		this.parms = parms;
	}


  public MethodDeclJSG(SourceLocation loc, String name) {
    super(loc);
    this.name = name;
  }
  
  public boolean isAbstract() {
    return body == null;
  }
  
  public boolean isConstructor() {
    return returnType == null;
  }


  @Override
  public String toString() {
    return returnType + " " + name + "(" + parms + ")";
  }

}
