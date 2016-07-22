//package javaic.semantics;
//
//import org.antlr.v4.runtime.ParserRuleContext;
//
//public class SemanticError extends RuntimeException {
//
//  private ParserRuleContext context;
//
//  public SemanticError(String message, ParserRuleContext ctx) {
//    super(message);
//    this.context = ctx;
//  }
//
//  public SemanticError(Throwable cause) {
//    super(cause);
//  }
//
//  public SemanticError(String message, Throwable cause) {
//    super(message, cause);
//  }
//
//  public ParserRuleContext getContext() {
//    return context;
//  }
//
//
//
//}
