package javaic.parsetree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ParseTreeJSG {
  
  List<CompileUnitJSG> units  = new ArrayList<>();
  HashMap<String, TypeDeclJSG> typeDeclMap = new HashMap<>();

  public ParseTreeJSG() {

  }

  public void add(CompileUnitJSG compilationUnit) {
    units.add(compilationUnit);
    for (TypeDeclJSG typeDecl : compilationUnit.decls) {
      typeDeclMap.put(typeDecl.getFQName(), typeDecl);
    }
    
  }

  public List<CompileUnitJSG> getUnits() {
    return units;
  }
  
  public HashMap<String, TypeDeclJSG> getTypeDecls() {
    return typeDeclMap;
  }

  public TypeDeclJSG findParent(TypeDeclJSG typeDecl) {
    if (typeDecl.extendsClass == null)
      return null;
    return typeDeclMap.get(typeDecl.extendsClass.qualifiedName);
  }

}
