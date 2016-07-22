package javaic.codegen.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javaic.parsetree.CompileUnitJSG;
import javaic.parsetree.TypeDeclJSG;
import javaic.semantics.JavaiType;

public abstract class CompileUnitCG {

  public final CompileUnitJSG compileUnitJSG;
  
  public String result; // output of compile
  
  public CompileUnitCG(CompileUnitJSG compileUnitJSG) {
    super();
    this.compileUnitJSG = compileUnitJSG;
  }

  public String getPackageName() {
    return compileUnitJSG.packageName;
  }
  
  public abstract void setTypeDecls(List<TypeDeclCG> typeDeclCGList);
  
  public String getBaseFilename() {
    String name = new File(compileUnitJSG.filename).getName();
    return name.substring(0, name.lastIndexOf('.'));
  }

  public List<JavaiType> getAllTypesUsed() {
    List<JavaiType> types = new ArrayList<>();    
    for (JavaiType type : compileUnitJSG.typesUsed) {
      boolean typeDefinedInThisCU = compileUnitJSG.decls.stream()
          .anyMatch(decl -> decl.getFQName().equals(type.qualifiedName)); 
      if (!typeDefinedInThisCU) {
        types.add(type);
      }
    }
    return types;
  }
  
  public boolean typeInheritsFromClassesInUnit(JavaiType type) {
    // TODO: Fix this test
    return compileUnitJSG.decls.stream()
        .anyMatch(decl -> type.qualifiedName.contains(decl.getFQName()));
  }


}
