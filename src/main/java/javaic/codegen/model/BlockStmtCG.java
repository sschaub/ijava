package javaic.codegen.model;


import java.util.ArrayList;
import java.util.List;

import javaic.parsetree.SourceLocation;
import javaic.parsetree.StatementJSG;

public abstract class BlockStmtCG extends StatementCG {
  
  public List<StatementCG> statements;

  public BlockStmtCG(StatementJSG stmtJSG) {
    super(stmtJSG.loc);
    statements = new ArrayList<>();
  }

  public BlockStmtCG(SourceLocation loc, List<StatementCG> statements) {
    super(loc);
    this.statements = statements;
  }

  public List<StatementCG> getStatements() {
    return statements;
  }

  public void setStatements(List<StatementCG> statements) {
    this.statements = statements;
  }
 

}
