package javaic.codegen.python.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javaic.codegen.model.MethodDeclCG;
import javaic.codegen.model.TypeDeclCG;
import javaic.codegen.model.VarDeclCG;
import javaic.codegen.python.PythonRenderUtil;
import javaic.parsetree.TypeDeclJSG;

public class PyTypeDeclCG extends TypeDeclCG {
  
  public List<PyVarDeclCG> fields, classFields;
  
  public List<PyMethodDeclCG> methods;
  
  public PyTypeDeclCG(TypeDeclJSG typeDeclJSG) {
    super(typeDeclJSG);
  }

  public String getKindStr() {
    return typeDeclJSG.kind == TypeDeclJSG.Kind.INTERFACE ? "interface" : "class";
  }
  
  public String getQualifiedName() {
    return PythonRenderUtil.renderQualifiedName(typeDeclJSG.getFQName());
  }
  
  
  public String getExtendsClassName() {
    return typeDeclJSG.extendsClass == null ? "java_lang_Object" : 
      PythonRenderUtil.renderQualifiedName(typeDeclJSG.extendsClass.qualifiedName);
  }
  
  public List<String> getImplementedInterfaces() {
    return typeDeclJSG.extendsInterfaces.stream()
        .map(jType -> PythonRenderUtil.renderQualifiedName(jType.qualifiedName))
        .collect(Collectors.toList());
  }

  @Override
  public void setFields(List<VarDeclCG> fieldList) {
    
    fields = (List)fieldList;
    classFields = new ArrayList<PyVarDeclCG>();
    for (PyVarDeclCG field : this.fields) {
      if (field.isStatic) {
        classFields.add(field);
      }
    }
  }

  @Override
  public void setMethods(List<MethodDeclCG> methodList) {
    methods = (List)methodList;
    
  }

  public boolean isInterface() {
    return typeDeclJSG.kind == TypeDeclJSG.Kind.INTERFACE;
  }
}
