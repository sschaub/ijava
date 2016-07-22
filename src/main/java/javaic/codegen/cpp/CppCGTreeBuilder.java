package javaic.codegen.cpp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javaic.LogFactory;
import javaic.codegen.CGNodeFactory;
import javaic.codegen.CodeGenException;
import javaic.codegen.CodeGenTreeBuilder;
import javaic.codegen.cpp.model.CppAsmtExprCG;
import javaic.codegen.cpp.model.CppBinExprCG;
import javaic.codegen.cpp.model.CppLitExprCG;
import javaic.codegen.cpp.model.CppMemberSelectExprCG;
import javaic.codegen.cpp.model.CppMethodDeclCG;
import javaic.codegen.cpp.model.CppMethodInvokeExprCG;
import javaic.codegen.cpp.model.CppNewClassExprCG;
import javaic.codegen.cpp.model.CppTypeDeclCG;
import javaic.codegen.model.AsmtExprCG;
import javaic.codegen.model.ExprStmtCG;
import javaic.codegen.model.ExpressionCG;
import javaic.codegen.model.IDExprCG;
import javaic.codegen.model.MemberSelectExprCG;
import javaic.codegen.model.MethodDeclCG;
import javaic.codegen.model.MethodInvokeExprCG;
import javaic.codegen.model.TypeDeclCG;
import javaic.codegen.model.VerbatimExprCG;
import javaic.parsetree.AsmtExprJSG;
import javaic.parsetree.BinExprJSG;
import javaic.parsetree.CastExprJSG;
import javaic.parsetree.ExpressionJSG;
import javaic.parsetree.ExpressionJSG.OperatorKind;
import javaic.parsetree.IDExprJSG;
import javaic.parsetree.InstanceOfExprJSG;
import javaic.parsetree.MemberSelectExprJSG;
import javaic.parsetree.MethodDeclJSG;
import javaic.parsetree.MethodInvokeExprJSG;
import javaic.parsetree.NewClassExprJSG;
import javaic.parsetree.SourceLocation;
import javaic.parsetree.TypeDeclJSG;
import javaic.semantics.JavaiType;
import javaic.semantics.VarDeclaration;

public class CppCGTreeBuilder extends CodeGenTreeBuilder {

  Logger log = LogFactory.getLogger(getClass());

  List<CppMethodDeclCG> testMethods;

  private String curClassFQName;

  public CppCGTreeBuilder(CGNodeFactory cgNodeFactory) {
    super(cgNodeFactory);
  }
  
  

  @Override
  public ExpressionCG visitMemberSelectExpr(
      MemberSelectExprJSG memberSelectExprJSG) {
    ExpressionCG exprCG = super.visitMemberSelectExpr(memberSelectExprJSG);
    
    if (exprCG instanceof MemberSelectExprCG) {
      MemberSelectExprCG memSelExprCG = (MemberSelectExprCG) exprCG;
      if (memSelExprCG.id.equals("class")) {
        memSelExprCG.id = "class_()";
        memSelExprCG.method = true;
      } else if (memSelExprCG.expr != null) {
        if (memSelExprCG.id.equals("length") && memberSelectExprJSG.expr.type.elementType != null) {
          // handle array length
          
          CppMemberSelectExprCG cppMemSelExprCG = new CppMemberSelectExprCG(exprCG.loc);
          cppMemSelExprCG.id = "length";
          cppMemSelExprCG.expr = memSelExprCG.expr;
          cppMemSelExprCG.method = true;
          CppMethodInvokeExprCG miExprCG = new CppMethodInvokeExprCG(cppMemSelExprCG);
          exprCG = miExprCG;
        } else if (memSelExprCG.expr instanceof IDExprCG) {
          // Convert expressions like super.foo() to BaseClass::foo()
          IDExprCG idExprCG = (IDExprCG)memSelExprCG.expr;
          if (idExprCG.getId().equals("super")) {
            memSelExprCG.expr = null;
            memSelExprCG.fqClassName = memberSelectExprJSG.expr.type.qualifiedName;
          }
        }
      } 
    }    
    return exprCG;
  }
  
  @Override
  public IDExprCG visitIDExpr(IDExprJSG idExprJSG) {
    IDExprCG idExprCG = super.visitIDExpr(idExprJSG);

    if (idExprJSG.name.equals("this")) {
      idExprCG.setId("this");
    } else if (idExprJSG.decl instanceof VarDeclaration) {
      VarDeclaration decl = (VarDeclaration) idExprJSG.decl;
      if (decl.isStatic) 
        idExprCG.setId(CppRenderUtil.renderQualifiedName(decl.getEnclosingClass().getFQName()) + "::" + idExprJSG.name);
//      else if (!decl.isLocal()) {
//        if (decl.getEnclosingClass().getFQName().equals(curClassFQName))
//          idExprCG.setId("this->" + idExprJSG.name);
//        else
//          idExprCG.setId("outer->" + idExprJSG.name);
//      }
    } else {
      log.warning(idExprJSG.loc + ": Unexpected declaration type: " + idExprJSG.decl.getClass().getCanonicalName() + " for " + idExprJSG.name);
    }

    
    return idExprCG;
    
  }

  CppMethodInvokeExprCG createMethodInvokeExprForGlobalFunction(SourceLocation loc, String function, ExpressionCG arg) {
    CppMemberSelectExprCG cppMemSelExprCG = new CppMemberSelectExprCG(loc);
    cppMemSelExprCG.id = function;
    cppMemSelExprCG.global = true;
    cppMemSelExprCG.method = true;
    CppMethodInvokeExprCG miExprCG = new CppMethodInvokeExprCG(cppMemSelExprCG, arg);
    return miExprCG;
  }



  @Override
  public MethodDeclCG visitMethodDecl(MethodDeclJSG methodDeclJSG) {
    CppMethodDeclCG methodDeclCG = (CppMethodDeclCG) super.visitMethodDecl(methodDeclJSG);

    if (methodDeclCG == null)
      return null;
    
    if (methodDeclCG.isConstructor()) {
       ExprStmtCG stmt = (ExprStmtCG) methodDeclCG.body.statements.remove(0); // call to base constructor
       if (((MethodInvokeExprCG)stmt.exprCG).args.size() > 0) {
         methodDeclCG.baseConstructorCall = (MethodInvokeExprCG) stmt.exprCG;
         methodDeclCG.baseConstructorCall.methodExprCG.id = CppRenderUtil.renderQualifiedName(methodDeclCG.baseConstructorCall.methodOwnerType.qualifiedName);
       }
    }
    
    if (methodDeclCG.name.startsWith("test") && methodDeclCG.parms.size() == 0) {
      testMethods.add(methodDeclCG);
    }
    
    return methodDeclCG;
  }



  @Override
  public ExpressionCG visitCastExpr(CastExprJSG castExprJSG) {
    if (castExprJSG.type == JavaiType.BOOLEAN || 
        castExprJSG.type == JavaiType.CHAR || 
        castExprJSG.type == JavaiType.BYTE || 
        castExprJSG.type == JavaiType.SHORT || 
        castExprJSG.type == JavaiType.INT || 
        castExprJSG.type == JavaiType.DOUBLE || 
        castExprJSG.type == JavaiType.LONG          
        ) {
      return (ExpressionCG) super.visitCastExpr(castExprJSG);
      
    } else {
      ExpressionCG expr = (ExpressionCG) castExprJSG.castExpr.accept(this);
      return createMethodInvokeExprForGlobalFunction(castExprJSG.loc, "jcl_dynamicCast<" + 
          CppRenderUtil.typeToText(castExprJSG.loc, castExprJSG.type) + ">", expr);
    }
      
  }



  @Override
  public TypeDeclCG visitTypeDecl(TypeDeclJSG typeDeclJSG) {
    testMethods = new ArrayList<>();
    curClassFQName = typeDeclJSG.getFQName();
    CppTypeDeclCG typeDeclCG = (CppTypeDeclCG) super.visitTypeDecl(typeDeclJSG);
    
    String className = typeDeclCG.getQualifiedName();
    // TODO: Check specifically for junit_framework_Test; need to handle abstract test classes
    if (typeDeclCG.getQualifiedName().endsWith("Test")) {
      for (CppMethodDeclCG method : typeDeclCG.methods) {
        if (method.isConstructor() && method.parms.size() == 1) {
          method.setOptionalParmFlag(true);
        }
      }
      
    }
    return typeDeclCG;
  }
  
  



  @Override
  public ExpressionCG visitMethodInvokeExpr(MethodInvokeExprJSG methodInvokeExprJSG) {
    ExpressionCG expr = (ExpressionCG) super.visitMethodInvokeExpr(methodInvokeExprJSG);
    if (expr instanceof CppMethodInvokeExprCG) {
      CppMethodInvokeExprCG invokeExpr = (CppMethodInvokeExprCG) expr;

      if (methodInvokeExprJSG.methodDecl != null) {
        List<JavaiType> formals = invokeExpr.methodDecl.parmTypes;
        List<ExpressionCG> actualsCG = invokeExpr.args;
        List<ExpressionJSG> actualsJSG = methodInvokeExprJSG.args;
        boxArguments(formals, actualsCG, actualsJSG);
      }
      
      // Add filename and line number to calls to assert / fail in test classes

      if (invokeExpr.methodExprCG.fqClassName != null &&
          (invokeExpr.methodExprCG.fqClassName.equals("junit.framework.TestCase") ||
              invokeExpr.methodExprCG.fqClassName.equals("junit.framework.Assert")) && 
          (invokeExpr.methodExprCG.id.startsWith("assert") || invokeExpr.methodExprCG.id.equals("fail"))) {
        invokeExpr.args.add(new VerbatimExprCG("__FILE__"));
        invokeExpr.args.add(new VerbatimExprCG("__LINE__"));
      }
    }
    return expr;
  }

  private void boxArguments(List<JavaiType> formals,
      List<ExpressionCG> actualsCG, List<ExpressionJSG> actualsJSG) {
    // Perform autoboxing
    for (int i = 0; i < actualsCG.size(); ++i) {
      ExpressionJSG arg = actualsJSG.get(i);
      if (formals.get(i).kind == JavaiType.Kind.CLASSORINTERFACE && arg.type != JavaiType.NULL && arg.type.kind == JavaiType.Kind.PRIMITIVE) {
        actualsCG.set(i, box(actualsCG.get(i)));
      }
    }
  }



  @Override
  public ExpressionCG visitBinExpr(BinExprJSG binExprJSG) {
    ExpressionCG expr = (ExpressionCG) super.visitBinExpr(binExprJSG);
    
    if (expr instanceof CppBinExprCG) {
      CppBinExprCG binExpr = (CppBinExprCG)expr;
      if (binExpr.opKind == OperatorKind.PLUS) {
        if (binExprJSG.type == JavaiType.STRING) {
          // Coerce non-string operands to string
          if (binExprJSG.left.type != JavaiType.STRING) {
            binExpr.left = createMethodInvokeExprForGlobalFunction(binExpr.left.loc, "jcl_tostr", binExpr.left);
          }
          if (binExprJSG.right.type != JavaiType.STRING) {
            binExpr.right = createMethodInvokeExprForGlobalFunction(binExpr.right.loc, "jcl_tostr", binExpr.right);
          }
          CppMemberSelectExprCG memSelectCG = new CppMemberSelectExprCG(binExprJSG.loc,
              binExpr.left, "concat", true);
          memSelectCG.method = true;
          CppMethodInvokeExprCG methodInvokeExprCG = new CppMethodInvokeExprCG(memSelectCG, binExpr.right);
          if (binExpr.isAssign)
            expr = new CppAsmtExprCG(convertExpr(binExprJSG.left), methodInvokeExprCG);
          else
            expr = methodInvokeExprCG;
        }
        
      } else if (binExpr.opKind == OperatorKind.EQ) {
        if (binExprJSG.right.type != JavaiType.NULL && binExprJSG.right.type.kind == JavaiType.Kind.PRIMITIVE && binExprJSG.left.type.kind == JavaiType.Kind.CLASSORINTERFACE) {
          // need to unpack the primitive value
          binExpr.left = unbox(binExpr.left);
          return binExpr;
        }
      } else if (binExpr.opKind == OperatorKind.UNSIGNED_RIGHTSHIFT) {
        binExpr.left = createMethodInvokeExprForGlobalFunction(binExpr.left.loc, "jcl_tounsigned", binExpr.left);
        binExpr.opKind = OperatorKind.RIGHTSHIFT;
        return binExpr;
      }
    }
    return expr;

  }
  
  private ExpressionCG unbox(ExpressionCG expr) {
    return new CppMethodInvokeExprCG(new CppMemberSelectExprCG(expr.loc,
        expr, "intValue", true));
  }

  private ExpressionCG box(ExpressionCG expr) {
    if (expr instanceof CppLitExprCG) {
      List<ExpressionCG> args = new ArrayList<>();
      args.add(expr);
      return new CppNewClassExprCG(expr.loc, new JavaiType("java.lang.Integer", new ArrayList<>()), args); 
    } else {
      throw new CodeGenException(expr.loc, "Unsupported boxing expression type");
    }
  }

  @Override
  public Object visitInstanceOfExpr(InstanceOfExprJSG instanceOfExprJSG) {

      ExpressionCG expr = (ExpressionCG) instanceOfExprJSG.expr.accept(this);
      return createMethodInvokeExprForGlobalFunction(instanceOfExprJSG.loc, "instanceOf<" + 
          CppRenderUtil.typeToText(instanceOfExprJSG.loc, instanceOfExprJSG.testType) + ">", expr);
    
  }

  @Override
  public Object visitNewClassExpr(NewClassExprJSG newClassExprJSG) {
    CppNewClassExprCG newClassExprCG = (CppNewClassExprCG) super.visitNewClassExpr(newClassExprJSG);
    
    if (newClassExprJSG.methodDecl != null) {
      List<JavaiType> formals = newClassExprJSG.methodDecl.parmTypes;
      boxArguments(formals, newClassExprCG.args, newClassExprJSG.args);
    }
    
    return newClassExprCG;
  }



  @Override
  public AsmtExprCG visitAsmtExpr(AsmtExprJSG asmtExpr) {
   
    AsmtExprCG asmtExprCG = super.visitAsmtExpr(asmtExpr);
    
    if (asmtExpr.exprJSG.type != JavaiType.NULL && asmtExpr.exprJSG.type.kind == JavaiType.Kind.CLASSORINTERFACE && asmtExpr.lhs.type.kind == JavaiType.Kind.PRIMITIVE) {
      // need to unpack the primiasmtExprCG.exprCGtive value
      asmtExprCG.exprCG = unbox(asmtExprCG.exprCG);
    }
    return asmtExprCG;
  }
  

  
  
}
