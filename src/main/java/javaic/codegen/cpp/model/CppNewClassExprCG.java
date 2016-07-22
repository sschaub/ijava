package javaic.codegen.cpp.model;

import java.util.List;
import java.util.stream.Collectors;

import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.model.ExpressionCG;
import javaic.codegen.model.NewClassExprCG;
import javaic.parsetree.NewClassExprJSG;
import javaic.parsetree.SourceLocation;
import javaic.semantics.JavaiType;

public class CppNewClassExprCG extends NewClassExprCG {

  public CppNewClassExprCG(NewClassExprJSG exprJSG) {
    super(exprJSG);
  }

  public CppNewClassExprCG(SourceLocation loc, JavaiType type,
      List<ExpressionCG> args) {
    super(loc, type, args);
  }

  @Override
  public String getId() {
    return CppRenderUtil.renderQualifiedName(type.qualifiedName);
  }

  
  public String getTypeArgsR() {
    if (type.classTypeArgs != null && type.classTypeArgs.size() > 0) {
      return "<" + String.join(",",  
          type.classTypeArgs.stream().map(arg -> CppRenderUtil.typeToText(loc, arg)).collect(Collectors.toList())) + ">";
    } else {
      return "";
    }
  }
}
