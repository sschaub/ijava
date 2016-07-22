package javaic.parsetree;

public class CatchJSG {
  public VarDeclJSG catchParm;
  public BlockStmtJSG catchBlock;
  public SourceLocation loc;
  public CatchJSG(SourceLocation loc, VarDeclJSG catchParm, BlockStmtJSG catchBlock) {
    this.loc = loc;
    this.catchParm = catchParm;
    this.catchBlock = catchBlock;
  }
  
  
}
