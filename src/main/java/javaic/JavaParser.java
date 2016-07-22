package javaic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javaic.parsetree.CompileUnitJSG;
import javaic.parsetree.JavaToJavaiVisitor;
import javaic.parsetree.ParseTreeJSG;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;

public class JavaParser {
  public ParseTreeJSG parse(String[] args) throws Exception {
    JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
    if (javaCompiler == null) {
      // Ensure that tools.jar is on the classpath and matches the JRE running this code...
      throw new RuntimeException("No system Java compiler available.");
    }
    Iterable<? extends JavaFileObject> compilationUnits = javaCompiler.getStandardFileManager(null, null, null)
        .getJavaFileObjectsFromStrings(new ArrayList<String>(Arrays.asList(args)));
    
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    List<String> options = new ArrayList<>();
    JavacTask task = (JavacTask) compiler.getTask(null, null, null,
        options, null, compilationUnits);
    Iterable<? extends CompilationUnitTree> asts = task.parse();
    JavaToJavaiVisitor visitor = new JavaToJavaiVisitor();

    task.analyze();
    
    visitor.setTrees(Trees.instance(task));

    ParseTreeJSG ptJSG = new ParseTreeJSG();
    for (CompilationUnitTree ast : asts) {
      visitor.setCurrentCompilationUnit(ast);
      visitor.scan();
      ptJSG.add(visitor.getCompilationUnit());
    }
    
    return ptJSG;

  }
}
