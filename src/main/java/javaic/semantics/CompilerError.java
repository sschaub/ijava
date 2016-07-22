//package javaic.semantics;
//
//import org.antlr.v4.runtime.ParserRuleContext;
//
//public class CompilerError extends RuntimeException {
//
//  private ParserRuleContext context;
//
//  public CompilerError(String message, ParserRuleContext ctx) {
//    super(message);
//    this.context = ctx;
//  }
//
//  public CompilerError(Throwable cause) {
//    super(cause);
//  }
//
//  public CompilerError(String message, Throwable cause) {
//    super(message, cause);
//  }
//
//  public CompilerError(String message) {
//    super(message);
//  }
//
//  public ParserRuleContext getContext() {
//    return context;
//  }
//
//
//
//}
