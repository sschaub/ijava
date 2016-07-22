package javaic.codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javaic.codegen.model.CompileUnitCG;
import javaic.parsetree.CompileUnitJSG;
import javaic.parsetree.ParseTreeJSG;
import javaic.util.CustomSTGroupFile;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.misc.STMessage;
import org.stringtemplate.v4.misc.STRuntimeMessage;

public abstract class CodeGenBase implements STErrorListener {
  
  protected ParseTreeJSG ptJSG;
  protected final STGroup templateGroup;

  public CodeGenBase(ParseTreeJSG ptJSG, String templateFilename) {

    this.ptJSG = ptJSG;
    templateGroup = new CustomSTGroupFile(templateFilename);  
    templateGroup.setListener(this);

  }
  
  public CompileUnitCG processCompileUnit(CompileUnitJSG compileUnitJSG) {
    CodeGenTreeBuilder builder = createCodeGenTreeBuilder();
    CompileUnitCG compileUnitCG = builder.visitCompileUnit(compileUnitJSG);

    ST templ = getTemplate("compileUnit");
    templ.add("compileUnit", compileUnitCG);
    compileUnitCG.result = templ.render();
    return compileUnitCG;
  }
  
  public abstract CodeGenTreeBuilder createCodeGenTreeBuilder();

  public void output(CompileUnitCG compileUnit) throws IOException {
    String filename = getOutputBaseFilename(compileUnit);
    
    File file = new File(filename);
    file.getParentFile().mkdirs();
    try (FileWriter fw = new FileWriter(file)) {
      fw.append(compileUnit.result);
    }
  }
  
  public String getOutputBaseFilename(CompileUnitCG compileUnit) throws IOException {
    String packageName = compileUnit.getPackageName();
    String outputDir;
    if (packageName != null) {
      outputDir = getBaseOutputPath() + "/" + packageName.replace('.', '/');
    } else {
      outputDir = getBaseOutputPath();
    }
    
    String filename = compileUnit.getBaseFilename();
    
    return outputDir + "/" + filename;
  }
  
  
  public abstract String getBaseOutputPath();

  public ST getTemplate(String name) {
    return templateGroup.getInstanceOf(name);
  }

  @Override
  public void IOError(STMessage msg) {
    System.err.println(msg);
    
  }

  @Override
  public void compileTimeError(STMessage msg) {
    System.err.println(msg);
    
  }

  @Override
  public void internalError(STMessage msg) {
    System.err.println(msg);
    
  }

  @Override
  public void runTimeError(STMessage msg) {
    System.err.println(msg);
  }

  public void postProcessCompileUnits(List<CompileUnitCG> compileUnitCGList) {
    
  }

  public void preprocessTree() {
    
  }
}
