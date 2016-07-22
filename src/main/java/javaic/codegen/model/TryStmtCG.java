package javaic.codegen.model;

import java.util.List;

import javaic.parsetree.TryStmtJSG;

public class TryStmtCG extends StatementCG {

  public BlockStmtCG tryStmt;
  
  public List<CatchCG> catchList;

  public TryStmtCG(TryStmtJSG tryStmtJSG) {
    super(tryStmtJSG.loc);
  }

}
