package javaic.parsetree;

import java.util.List;

public class TryStmtJSG extends StatementJSG {
  public BlockStmtJSG tryStmt;
  
  public List<CatchJSG> catchList;

  public TryStmtJSG(SourceLocation loc, BlockStmtJSG tryStmt, List<CatchJSG> catchList) {
    super(loc);
    this.tryStmt = tryStmt;
    this.catchList = catchList;
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitTryStmt(this);
  }

}
