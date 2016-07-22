package javaic;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javaic.codegen.CodeGenBase;
import javaic.codegen.CodeGenException;
import javaic.codegen.cpp.CppCodeGenCpp;
import javaic.codegen.cpp.CppCodeGenHeaders;
import javaic.codegen.model.CompileUnitCG;
import javaic.codegen.python.PythonCodeGen;
import javaic.parsetree.CompileUnitJSG;
import javaic.parsetree.ParseException;
import javaic.parsetree.ParseTreeJSG;
import javaic.parsetree.SourceLocation;

public class Main {

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: javaic language filenames...");
      System.exit(-1);
    }
    
    
    String language = args[0];
    String[] filenames = Arrays.copyOfRange(args, 1, args.length);

    if (!(language.equals("python") || language.equals("cpp"))) {
      System.err.println("Language must be one of: python, cpp");
      System.exit(-1);
    }
    
    try {
      JavaParser parser = new JavaParser();
      ParseTreeJSG ptJSG = parser.parse(filenames);
      
      List<CodeGenBase> cgList = new ArrayList<>();
      switch (language) {
      case "python":
        cgList.add(new PythonCodeGen(ptJSG));
        break;
      case "cpp":
        cgList.add(new CppCodeGenCpp(ptJSG)); // must be first - does some preprocessing
        cgList.add(new CppCodeGenHeaders(ptJSG));
        break;
      default:
        System.err.println("Language must be one of: python, cpp");
        System.exit(-1);
      }
      System.out.println("\n\nBeginning code generation...");
      for (CodeGenBase cgb : cgList) {
        cgb.preprocessTree();
      }
      
      List<CompileUnitCG> compileUnitCGList = new ArrayList<>();
      for (CompileUnitJSG cuJSG : ptJSG.getUnits()) {
        try {
          for (CodeGenBase cgb : cgList) {
            CompileUnitCG compileUnitCG = cgb.processCompileUnit(cuJSG);
            compileUnitCGList.add(compileUnitCG);
            cgb.output(compileUnitCG);
          }
          
        } catch (CodeGenException ex) {
          System.err.println(ex.getMessage());
        }
      }
      
      for (CodeGenBase cgb : cgList) {
        cgb.postProcessCompileUnits(compileUnitCGList);
      }

    } catch (ParseException ex) {
      SourceLocation ctx = ex.sourceLocation;
      if (ctx != null) {
        System.err.print("(" + ctx.line + "," + ctx.col + "): ");
      }
      System.err.println("Internal Compiler Error: " + ex.getMessage());
      ex.printStackTrace();
    }
  }

}
