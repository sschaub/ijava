package javaic.codegen.model;

import java.util.List;

import javaic.parsetree.TypeDeclJSG;

public abstract class TypeDeclCG {
  
  public TypeDeclJSG typeDeclJSG;

  public TypeDeclCG(TypeDeclJSG typeDeclJSG) {
    super();
    this.typeDeclJSG = typeDeclJSG;
  }

  public abstract void setFields(List<VarDeclCG> fieldList);

  public abstract void setMethods(List<MethodDeclCG> methodList);
  

  public boolean isAbstract() {
    return typeDeclJSG.isAbstract();
  }

}
