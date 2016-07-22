package javaic.codegen.model;

import javaic.parsetree.CatchJSG;
import javaic.parsetree.SourceLocation;


public class CatchCG {
  public SourceLocation loc;
  public VarDeclCG catchParm;
  public BlockStmtCG catchBlock;
  
  public CatchCG(CatchJSG catchJSG) {
    this.loc = catchJSG.loc;
  }

}
