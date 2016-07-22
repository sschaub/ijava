package javaic.semantics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javaic.parsetree.ParseException;
import javaic.parsetree.SourceLocation;

public class JavaiType {

  public enum Kind {
    CLASSORINTERFACE, PRIMITIVE, ARRAY, TYPEVAR, METHOD
  }
  
  public static JavaiType NONE = new JavaiType(Kind.PRIMITIVE, "**NONE**");
  public static JavaiType VOID = new JavaiType(Kind.PRIMITIVE, "void");
  public static JavaiType NULL = new JavaiType(Kind.PRIMITIVE, "<null>");
  public static JavaiType BYTE = new JavaiType(Kind.PRIMITIVE, "byte"); 
  public static JavaiType SHORT = new JavaiType(Kind.PRIMITIVE, "short"); 
  public static JavaiType INT = new JavaiType(Kind.PRIMITIVE, "int"); 
  public static JavaiType LONG = new JavaiType(Kind.PRIMITIVE, "long"); 
  public static JavaiType DOUBLE = new JavaiType(Kind.PRIMITIVE, "double"); 
  public static JavaiType CHAR = new JavaiType(Kind.PRIMITIVE, "char"); 
  public static JavaiType BOOLEAN = new JavaiType(Kind.PRIMITIVE, "boolean"); 
  public static JavaiType STRING = new JavaiType("java.lang.String");

  public Kind kind;
  
  // Arrays
  public JavaiType elementType; 

  // Class or interface
  public String name;
  
  public String qualifiedName;

  public List<JavaiType> classTypeArgs;
  
  // Method
  private JavaiType returnType;
  private List<JavaiType> parmTypes;

//  // Primitive
//  public PrimitiveTypeKind primTypeKind;
  
  
  public JavaiType(Kind kind, String name) {
    this.kind = kind;
    this.name = name;
    this.qualifiedName = name;
  }
  
  public JavaiType(String qualifiedName, JavaiType... classTypeArgs) {
    this(qualifiedName, Arrays.asList(classTypeArgs));
  }

  public JavaiType(String qualifiedName, List<JavaiType> classTypeArgs ) {
    this.kind = Kind.CLASSORINTERFACE;
    int lastDotPos = qualifiedName.lastIndexOf('.');
    this.name = (lastDotPos == -1) ? qualifiedName : qualifiedName.substring(lastDotPos + 1);
    this.classTypeArgs = classTypeArgs;
    this.qualifiedName = qualifiedName;
  }

  public JavaiType(JavaiType elementType) {
    kind = Kind.ARRAY;
    this.elementType = elementType;
  }

  public JavaiType(JavaiType returnType,
      List<JavaiType> parmTypes) {
    this.kind = Kind.METHOD;
    this.returnType = returnType;
    this.parmTypes = parmTypes;
  }

  public static JavaiType fromPrimitiveName(SourceLocation loc, String name) {
    switch (name) {
    case "int": return INT;
    case "byte": return BYTE;
    case "short": return SHORT;
    case "long": return LONG;
    case "void": return VOID;
    case "char": return CHAR;
    case "boolean": return BOOLEAN;
    default:
      throw new ParseException(loc, "Unhandled primitive: " + name);
    }    
  }

  @Override
  public String toString() {
    if (kind == Kind.PRIMITIVE)
      return name;
    else if (kind == Kind.CLASSORINTERFACE)
      return qualifiedName;
    
    return "TypeJSG [kind=" + kind +
        ", name=" + name + 
        (qualifiedName != null ? " (" + qualifiedName + ")" : "") +
        (classTypeArgs != null ? "<" + classTypeArgs + ">" : "" ) +
        (elementType != null ? ", elementType=" + elementType : "") + 
         "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JavaiType other = (JavaiType) obj;
    if (qualifiedName == null) {
      if (other.qualifiedName != null)
        return false;
    } else if (!qualifiedName.equals(other.qualifiedName))
      return false;
    return true;
  }


}
