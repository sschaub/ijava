package javaic.codegen.python.model;

import java.util.List;

import javaic.codegen.model.BlockStmtCG;
import javaic.codegen.model.StatementCG;
import javaic.parsetree.SourceLocation;
import javaic.parsetree.StatementJSG;

public class PyBlockStmtCG extends BlockStmtCG {

  public PyBlockStmtCG(SourceLocation loc, List<StatementCG> statements) {
    super(loc, statements);
  }

  public PyBlockStmtCG(StatementJSG stmtJSG) {
    super(stmtJSG);
  }

}
