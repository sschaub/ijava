package javaic.semantics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javaic.parsetree.SourceLocation;

public class SymbolTable {
  private List<HashMap<String, Declaration>> declarations;
  
  public SymbolTable() {
    declarations = new ArrayList<>();
    beginScope();
    
//    ClassDeclaration clsString = ((JClassType) Type.STRING).getDecl();
//    clsString.addMethod(new MethodDeclaration(JType.INT, "length", 
//        Arrays.asList(new VarDeclaration(JType.INT, "length")), clsString));
//    clsString.addMethod(new MethodDeclaration(JType.CHAR, "charAt", 
//        Arrays.asList(new VarDeclaration(JType.INT, "index")), clsString));
//    clsString.addMethod(new MethodDeclaration(JType.BOOLEAN, "equals", 
//        Arrays.asList(new VarDeclaration(JType.STRING, "value")), clsString));
//    clsString.addMethod(new MethodDeclaration(JType.STRING, "subString", 
//        Arrays.asList(
//            new VarDeclaration(JType.INT, "begin"),
//            new VarDeclaration(JType.INT, "end")), clsString));
    
  }
  
  public int getScope() {
    return declarations.size();
  }
  
  public void beginScope() {
    declarations.add(new HashMap<String, Declaration>());
  }
  
  public void endScope() {
    declarations.remove(declarations.size() - 1);    
  }
  
  public Declaration lookup(String id) {
    for (int i = declarations.size() - 1; i >= 0; --i) {
      Declaration decl = declarations.get(i).get(id);
      if (decl != null)
        return decl;
    }
    
    return null;
  }
  
  public void define(String id, Declaration decl) {
    declarations.get(declarations.size() - 1).put(id, decl);
  }

  public Declaration lookup(String id, SourceLocation location) {
    for (int i = declarations.size() - 1; i >= 0; --i) {
      Declaration decl = declarations.get(i).get(id);
      if (decl != null && decl.loc.equals(location))
        return decl;
    }
    return null;
  }
}
