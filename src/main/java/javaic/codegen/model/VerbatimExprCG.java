package javaic.codegen.model;


public class VerbatimExprCG extends ExpressionCG {
  
  public String exprText;

  public VerbatimExprCG() {
    super(null);
  }

  public VerbatimExprCG(String exprText) {
    super(null);
    this.exprText = exprText; 
  }
  
  
}
