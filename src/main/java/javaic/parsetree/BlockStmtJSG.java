package javaic.parsetree;

import java.util.ArrayList;
import java.util.List;

public class BlockStmtJSG extends StatementJSG {

  public List<StatementJSG> statements = new ArrayList<>();

  public BlockStmtJSG(SourceLocation loc) {
    super(loc);
  }

  @Override
  public Object accept(JavaiVisitor javaiVisitor) {
    return javaiVisitor.visitBlockStmt(this);
    
  }
}
