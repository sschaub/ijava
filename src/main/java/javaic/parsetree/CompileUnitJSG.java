package javaic.parsetree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javaic.semantics.JavaiType;

public class CompileUnitJSG {
  public String packageName;
  
  public String filename;
  
  public List<ImportJSG> imports = new ArrayList<>();
  
  public List<TypeDeclJSG> decls = new ArrayList<>();
  
  public Set<JavaiType> typesUsed = new HashSet<>();


  public CompileUnitJSG(String packageName, List<ImportJSG> imports,
      List<TypeDeclJSG> decls) {
    this.packageName = packageName;
    this.imports = imports;
    this.decls = decls;
  } 
  
  
  public CompileUnitJSG() {
  }


  public CompileUnitJSG(String pkgName) {
    this.packageName = pkgName;
  }


//  public static CompileUnitJSG fromAntlr(String packageName, List<ImportDeclarationContext> imports,
//      List<TypeDeclarationContext> types) {
//    return new CompileUnitJSG(packageName,
//        imports.stream().map(p -> p.result).collect(Collectors.toList()),
//        types.stream().map(p -> p.result).collect(Collectors.toList()));
//  }


  @Override
  public String toString() {
    return "CompileUnit [packageName=" + packageName + ", imports=" + imports
        + ", decls=" + decls + "]";
  }


}
