package javaic.parsetree;

@SuppressWarnings("serial")
public class ParseException extends RuntimeException {
  
  public final SourceLocation sourceLocation;

  public ParseException(SourceLocation sourceLocation, String msg) {
    super(sourceLocation + " " + msg);
    this.sourceLocation = sourceLocation;
  }


}
