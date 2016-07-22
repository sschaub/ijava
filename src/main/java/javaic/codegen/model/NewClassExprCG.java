package javaic.codegen.model;

import java.util.ArrayList;
import java.util.List;

import javaic.parsetree.NewClassExprJSG;
import javaic.parsetree.SourceLocation;
import javaic.semantics.JavaiType;

public abstract class NewClassExprCG extends ExpressionCG {
  
  public List<ExpressionCG> args = new ArrayList<>();
  
  public JavaiType type;

  public NewClassExprCG(NewClassExprJSG exprJSG) {
    super(exprJSG.loc);
    this.type = exprJSG.type;
  }

  public NewClassExprCG(SourceLocation loc, JavaiType type,
      List<ExpressionCG> args) {
    super(loc);
    this.type = type;
    this.args = args;
  }

  public abstract String getId();

}
