package javaic.codegen.python.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javaic.codegen.model.CompileUnitCG;
import javaic.codegen.model.TypeDeclCG;
import javaic.parsetree.CompileUnitJSG;
import javaic.parsetree.TypeDeclJSG;
import javaic.semantics.JavaiType;

public class PyCompileUnitCG extends CompileUnitCG {

  public List<PyTypeDeclCG> decls;
  
  public List<TypeDeclCG> testClasses;

  public PyCompileUnitCG(CompileUnitJSG compileUnitJSG) {
    super(compileUnitJSG);
  }
  
  @Override
  public String getPackageName() {
    return compileUnitJSG.packageName;
  }
  
  List<JavaiType> getFilteredTypesUsed() {
    return getAllTypesUsed().stream()
        .filter(type -> !type.qualifiedName.startsWith("classpath."))
        .collect(Collectors.toList());
  }
  
  
  public List<String> getModuleListForHeader() {
    return getFilteredTypesUsed().stream()
        .filter(type -> !typeInheritsFromClassesInUnit(type))
        .map(type -> type.qualifiedName)
        .collect(Collectors.toList());
  }

  public List<String> getModuleListForFooter() {
    return getFilteredTypesUsed().stream()
        .filter(type -> typeInheritsFromClassesInUnit(type))
        .map(type -> type.qualifiedName)
        .collect(Collectors.toList());
  }


  @SuppressWarnings("unchecked")
  @Override
  public void setTypeDecls(List<TypeDeclCG> typeDeclCGList) {
    decls = (List)typeDeclCGList;
    
  }

}
