package javaic.codegen.cpp.model;

import java.util.List;
import java.util.stream.Collectors;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.MethodDeclCG;
import javaic.codegen.model.TypeDeclCG;
import javaic.codegen.model.VarDeclCG;
import javaic.parsetree.TypeDeclJSG;
import javaic.semantics.JavaiType;

public class CppTypeDeclCG extends TypeDeclCG {
  
  public List<CppVarDeclCG> fields;
  
  public List<CppMethodDeclCG> methods;

  public CppTypeDeclCG(TypeDeclJSG typeDeclJSG) {
    super(typeDeclJSG);
  }

  public String getQualifiedName() {
    return CppRenderUtil.renderQualifiedName(typeDeclJSG.getFQName());
  }
  
  public List<String> getExtendsClassNames() {
    List<String> classNames = typeDeclJSG.extendsInterfaces.stream()
        .map(e -> "virtual public " + typeToText(e))
        .collect(Collectors.toList());
    if (typeDeclJSG.extendsClass != null)
      classNames.add("public " + typeToText(typeDeclJSG.extendsClass));
    else if (classNames.size() == 0)
      classNames.add("virtual public java_lang_Object");
      
    return classNames;
  }
  
  String typeToText(JavaiType t) {
    String txt = CppRenderUtil.typeToText(null, t);
    return txt.substring(0, txt.length() - 1); // strip off trailing *
  }
  
  
  public List<VarDeclCG> getStaticFields() {
    return fields.stream().filter(fld -> fld.isStatic).collect(Collectors.toList());
  }

  @Override
  public void setFields(List<VarDeclCG> fieldList) {
    fields = (List)fieldList; 
  }

  @Override
  public void setMethods(List<MethodDeclCG> methodList) {
    methods = (List)methodList;
  }

}
