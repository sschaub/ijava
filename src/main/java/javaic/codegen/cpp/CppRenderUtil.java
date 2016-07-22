package javaic.codegen.cpp;

import java.util.List;
import java.util.stream.Collectors;

import javaic.codegen.CodeGenException;
import javaic.parsetree.CompileUnitJSG;
import javaic.parsetree.ExpressionJSG.OperatorKind;
import javaic.parsetree.SourceLocation;
import javaic.semantics.JavaiType;

public class CppRenderUtil {

  public CppRenderUtil(CompileUnitJSG compileUnitJSG) {
  }
  
  public static String getOp(SourceLocation loc, OperatorKind opKind) {
    switch (opKind) {
    case AND: return "&&";
    case OR: return "||";
    case NOT: return "!";
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
    case BITAND: return "&";
    case BITOR: return "|";
    case BITNOT: return "~";
    case XOR: return "^";
    default: throw new CodeGenException(loc, "Unimplemented OperatorKind: " + opKind);
    }
  }
  
  public static String typeToText(SourceLocation loc, JavaiType type) {
    switch (type.kind) {
    case CLASSORINTERFACE:

      String name = renderQualifiedName(type.qualifiedName);
      List<String> argTypes = type.classTypeArgs.stream()
          .map(arg -> typeToText(loc, arg)).collect(Collectors.toList());
      return name + ((type.classTypeArgs.size() > 0) ? 
          "<" + String.join(",", argTypes) + ">*" : "*");
    case PRIMITIVE:
      switch (type.name){
      case "boolean":
        return "bool";
      case "byte":
        return "int8_t";
      case "short":
        return "int16_t";
      case "int":
        return "int32_t";
      case "long":
        return "int64_t";
      default:
        return type.name;
      }
    case ARRAY:
      return "Array<" + typeToText(loc, type.elementType) + ">*";
    case TYPEVAR:
      return type.name;
    case METHOD:
      return type.name + "()";
    default:
      throw new CodeGenException(loc, "Unhandled JType kind: " + type.kind);
    }
  }

  public static String getDefaultValueForType(JavaiType jType) {
    if (jType == JavaiType.BOOLEAN)
      return "false";
    else if (jType == JavaiType.BYTE || jType == JavaiType.SHORT || jType == JavaiType.INT || jType == JavaiType.LONG)
      return "0";
    else if (jType == JavaiType.DOUBLE)
      return "0.0";
    else if (jType == JavaiType.CHAR)
      return "char(0)";
    else 
      return "NULL";
  }

  public static String renderQualifiedName(String qualifiedName) {
    return qualifiedName.replace('.', '_');
  }

  public static String makeEscapedLiteral(String string) {
    return string.replace("\n",  "\\n").replace("\t", "\\t").replace("\r", "\\r");
  }

}
