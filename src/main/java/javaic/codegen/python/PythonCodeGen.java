package javaic.codegen.python;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javaic.LogFactory;
import javaic.codegen.CodeGenBase;
import javaic.codegen.CodeGenTreeBuilder;
import javaic.codegen.model.CompileUnitCG;
import javaic.parsetree.ParseTreeJSG;

public class PythonCodeGen extends CodeGenBase {
  
  Logger logger = LogFactory.getLogger(getClass());

  public PythonCodeGen(ParseTreeJSG ptJSG) {
    super(ptJSG, "/javaic/codegen/python/python.stg");
  }

  @Override
  public CodeGenTreeBuilder createCodeGenTreeBuilder() {
    return new PythonCGTreeBuilder(new PythonCGNodeFactory());
  }

  @Override
  public String getBaseOutputPath() {
    return "pyoutput";
  }  
  
  
  @Override
  public String getOutputBaseFilename(CompileUnitCG compileUnit) throws IOException {
    return super.getOutputBaseFilename(compileUnit) + ".py";
  }

  @Override
  public void postProcessCompileUnits(List<CompileUnitCG> compileUnitCGList) {
    try {
      Set<String> subPackages = new HashSet<>();
      for (CompileUnitCG cuCG : compileUnitCGList) {
        String packageName = cuCG.getPackageName();
        if (packageName != null) {
          String[] components = packageName.split("\\.");
          String path = components[0];
          subPackages.add(path);
          for (String component : Arrays.copyOfRange(components, 1, components.length - 1)) {
            path += "." + component;
            subPackages.add(path);
          }      
        }
      }
      
      String baseDirectory = getBaseOutputPath();
      
      // Create __init__.py files required for Python packages
      for (String subPackage : subPackages) {
        String filename = subPackage.replace('.', File.separatorChar); 
        //System.out.println(baseDirectory + File.separatorChar + filename);
        new File(new File(baseDirectory + File.separatorChar + filename), "__init__.py").createNewFile();
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    
  }

}
