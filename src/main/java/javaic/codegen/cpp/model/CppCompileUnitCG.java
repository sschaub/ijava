package javaic.codegen.cpp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.CompileUnitCG;
import javaic.codegen.model.TypeDeclCG;
import javaic.parsetree.CompileUnitJSG;
import javaic.parsetree.TypeDeclJSG;
import javaic.semantics.JavaiType;

public class CppCompileUnitCG extends CompileUnitCG {

  public List<CppTypeDeclCG> decls;

  public CppCompileUnitCG(CompileUnitJSG compileUnitJSG) {
    super(compileUnitJSG);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void setTypeDecls(List<TypeDeclCG> typeDeclCGList) {
    decls = (List)typeDeclCGList;
    
  }
  
  public List<String> getIncludeFilenamesForHeader() {
    return getAllTypesUsed().stream()
        .filter(type -> !typeInheritsFromClassesInUnit(type))
        .map(type -> typeToHeaderFilename(type))
        .collect(Collectors.toList());
  }
  
  
  String typeToHeaderFilename(JavaiType type) {
    return type.qualifiedName.replace('.', '/') + ".h";
  }

  public List<String> getIncludeFilenamesForCpp() {
    return getAllTypesUsed().stream()
        .filter(type -> typeInheritsFromClassesInUnit(type))
        .map(type -> typeToHeaderFilename(type))
        .collect(Collectors.toList());
  }

  
  public List<String> getClassesUsed() {
    List<String> classesUsed = new ArrayList<>();
    for (JavaiType type : compileUnitJSG.typesUsed) {
      boolean found = false;
      for (Iterator<TypeDeclJSG> it = compileUnitJSG.decls.iterator(); 
            it.hasNext() && !found; ) {
        String typeName = it.next().getFQName();
        if (type.qualifiedName.equals(typeName))
          found = true;
      }
      if (!found) {
        classesUsed.add(CppRenderUtil.renderQualifiedName(type.qualifiedName));
      }
    }
    return classesUsed;
    
  }

  public String getHeaderPreprocessorName() {
    return this.getBaseFilename().replace(".", "_").replace("\\", "_").replace("/", "_").toUpperCase();
  }
}
