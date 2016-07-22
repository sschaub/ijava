package javaic.codegen;

import javaic.parsetree.JASGNode;
import javaic.parsetree.SourceLocation;

@SuppressWarnings("serial")
public class CodeGenException extends RuntimeException {
  
  private SourceLocation loc;

  public CodeGenException(JASGNode node) {
    this.loc = node.loc;
  }

  public CodeGenException(JASGNode node, String arg0) {
    super(arg0);
    this.loc = node.loc;
  }

  public CodeGenException(SourceLocation loc, String arg0) {
    super(arg0);
    this.loc = loc;
  }
  
  @Override
  public String getMessage() {
    return loc + " : " + super.getMessage();
  }
  
  

}
