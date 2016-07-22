package javaic.codegen.cpp;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javaic.LogFactory;
import javaic.codegen.CodeGenBase;
import javaic.codegen.CodeGenTreeBuilder;
import javaic.codegen.model.CompileUnitCG;
import javaic.parsetree.ParseTreeJSG;

public class CppCodeGenHeaders extends CodeGenBase {
  
  Logger logger = LogFactory.getLogger(getClass());

  public CppCodeGenHeaders(ParseTreeJSG ptJSG) {
    super(ptJSG, "/javaic/codegen/cpp/cppheader.stg");
  }

  @Override
  public String getBaseOutputPath() {
    return "cppoutput";
  }    
  
  @Override
  public String getOutputBaseFilename(CompileUnitCG compileUnit) throws IOException {
    return super.getOutputBaseFilename(compileUnit) + ".h";
  }

  @Override
  public CodeGenTreeBuilder createCodeGenTreeBuilder() {
    return new CppCGTreeBuilder(new CppCGNodeFactory());
  }

  @Override
  public void postProcessCompileUnits(List<CompileUnitCG> compileUnitCGList) {
    // TODO Auto-generated method stub
    
  }
  
}
