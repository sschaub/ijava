package javaic.codegen.python;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javaic.codegen.CodeGenException;
import javaic.codegen.cpp.CppRenderUtil;
import javaic.codegen.python.model.PyMethodDeclCG;
import javaic.parsetree.CompileUnitJSG;
import javaic.parsetree.ExpressionJSG.OperatorKind;
import javaic.parsetree.SourceLocation;
import javaic.semantics.JavaiType;
import javaic.semantics.JavaiType.Kind;

public class PythonRenderUtil {
  
  static Set<String> keywords = new HashSet<>();
  static {
    keywords.addAll(Arrays.asList("lambda", "in"));
  }

  public PythonRenderUtil(CompileUnitJSG compileUnitJSG) {
  }
  
  public static String getOp(SourceLocation loc, OperatorKind opKind) {
    switch (opKind) {
    case AND: return "and";
    case OR: return "or";
    case NOT: return "not";
    case PLUS: return "+";
    case MINUS: return "-";
    case MULTIPLY: return "*";
    case DIVIDE: return "/";
    case MODULUS: return "%";
    case EQ: return "==";
    case NOTEQ: return "!=";
    case LESSTHAN: return "<";
    case LESSOREQ: return "<=";
    case GREATERTHAN: return ">";
    case GREATEROREQ: return ">=";
    case LEFTSHIFT: return "<<";
    case RIGHTSHIFT: return ">>";
    case UNSIGNED_RIGHTSHIFT: return ">>";
    case BITAND: return "&";
    case BITOR: return "|";
    case BITNOT: return "~";
    case XOR: return "^";
    default: throw new CodeGenException(loc, "Unimplemented OperatorKind: " + opKind);
    }
  }
  
  public static String getMethodNameOverload(String name, List<JavaiType> argTypes) {
    String suffix = "_";
    int i = 0;
    for (JavaiType arg : argTypes) {
      if (arg.kind == JavaiType.Kind.CLASSORINTERFACE
          && !arg.qualifiedName.equals("java.lang.Object")
          && !arg.qualifiedName.equals("java.lang.String"))
        i += arg.qualifiedName.length();
      suffix += typeToChar(arg);
    }
    
    return name + suffix + (i > 0 ? String.valueOf(i) : "");    
  }
  
  public static String getMethodNameOverload(PyMethodDeclCG methodDeclCG) {
    return getMethodNameOverload(methodDeclCG.name, 
        methodDeclCG.parms.stream()
          .filter(parm -> !parm.getName().equals("cls_") && !parm.getName().equals("self"))
          .map(parm -> parm.jType).collect(Collectors.toList()));    
  }  
  
  static Map<String, Integer> typeToCharMap = new HashMap<>();
  
  private static String typeToChar(JavaiType arg) {
    // Handle array
    if (arg.kind == Kind.ARRAY)
      return "A" + typeToChar(arg.elementType);
    
    if (arg == JavaiType.INT)
      return "I";
    else if (arg == JavaiType.BYTE)
      return "B";
    else if (arg == JavaiType.SHORT)
      return "S";
    else if (arg == JavaiType.LONG)
      return "L";
    else if (arg == JavaiType.DOUBLE)
      return "D";
    else if (arg == JavaiType.CHAR)
      return "C";
    else if (arg == JavaiType.BOOLEAN)
      return "Z";
    else if (arg.qualifiedName.equals("java.lang.String"))
      return "R";
    else {
      Integer code = typeToCharMap.get(arg.qualifiedName);
      if (code == null) {
        code = typeToCharMap.size() + 1;
        typeToCharMap.put(arg.qualifiedName, code);
      }
      return "O"; // + code;
    }
      
  }

  public static String getDefaultValueForType(JavaiType jType) {
    if (jType == JavaiType.BOOLEAN)
      return "False";
    else if (jType == JavaiType.BYTE || jType == JavaiType.SHORT || jType == JavaiType.INT || jType == JavaiType.LONG || jType == JavaiType.DOUBLE)
      return "0";
    else if (jType == JavaiType.CHAR)
      return "'\\000'";
    else 
      return "None";
  }
  
  public static String renderQualifiedName(String qualifiedName) {
    return qualifiedName.replace('.', '_');
  }

  public static String renderId(String id) {
    if (keywords.contains(id))
      return id + "Kw";
    else 
      return id;
  }

  public static String makeEscapedLiteral(String string) {
    return CppRenderUtil.makeEscapedLiteral(string);
  }

}
