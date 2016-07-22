package javaic.codegen.cpp.model;

import java.util.List;

import javaic.codegen.model.BlockStmtCG;
import javaic.codegen.model.StatementCG;
import javaic.parsetree.SourceLocation;
import javaic.parsetree.StatementJSG;

public class CppBlockStmtCG extends BlockStmtCG {
  
  public CppBlockStmtCG(StatementJSG stmtJSG) {
    super(stmtJSG);
  }

  public CppBlockStmtCG(SourceLocation loc, List<StatementCG> statements) {
    super(loc, statements);
  }


}
