package javaic.codegen.python;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javaic.LogFactory;
import javaic.codegen.CGNodeFactory;
import javaic.codegen.CodeGenTreeBuilder;
import javaic.codegen.model.BlockStmtCG;
import javaic.codegen.model.CatchCG;
import javaic.codegen.model.CompileUnitCG;
import javaic.codegen.model.ExprStmtCG;
import javaic.codegen.model.ExpressionCG;
import javaic.codegen.model.IDExprCG;
import javaic.codegen.model.MemberSelectExprCG;
import javaic.codegen.model.MethodDeclCG;
import javaic.codegen.model.MethodInvokeExprCG;
import javaic.codegen.model.StatementCG;
import javaic.codegen.model.TryStmtCG;
import javaic.codegen.model.TypeDeclCG;
import javaic.codegen.model.VarDeclCG;
import javaic.codegen.model.VerbatimExprCG;
import javaic.codegen.python.model.PyAsmtExprCG;
import javaic.codegen.python.model.PyBinExprCG;
import javaic.codegen.python.model.PyBlockStmtCG;
import javaic.codegen.python.model.PyCompileUnitCG;
import javaic.codegen.python.model.PyIDExprCG;
import javaic.codegen.python.model.PyMemberSelectExprCG;
import javaic.codegen.python.model.PyMethodDeclCG;
import javaic.codegen.python.model.PyMethodInvokeExprCG;
import javaic.codegen.python.model.PyReturnStmtCG;
import javaic.codegen.python.model.PyTypeDeclCG;
import javaic.codegen.python.model.PyVarDeclCG;
import javaic.codegen.python.model.PyVarDeclStmtCG;
import javaic.parsetree.BinExprJSG;
import javaic.parsetree.BlockStmtJSG;
import javaic.parsetree.CastExprJSG;
import javaic.parsetree.CompileUnitJSG;
import javaic.parsetree.ExprStmtJSG;
import javaic.parsetree.ExpressionJSG.OperatorKind;
import javaic.parsetree.IDExprJSG;
import javaic.parsetree.InstanceOfExprJSG;
import javaic.parsetree.MemberSelectExprJSG;
import javaic.parsetree.MethodDeclJSG;
import javaic.parsetree.MethodInvokeExprJSG;
import javaic.parsetree.NewClassExprJSG;
import javaic.parsetree.SourceLocation;
import javaic.parsetree.TryStmtJSG;
import javaic.parsetree.TypeDeclJSG;
import javaic.parsetree.VarDeclJSG;
import javaic.semantics.JavaiType;
import javaic.semantics.JavaiType.Kind;
import javaic.semantics.VarDeclaration;

public class PythonCGTreeBuilder extends CodeGenTreeBuilder {
  
  Logger log = LogFactory.getLogger(getClass());
  
  List<PyMethodDeclCG> testMethods;
  List<TypeDeclCG> testClasses;

  public PythonCGTreeBuilder(CGNodeFactory cgNodeFactory) {
    super(cgNodeFactory);
  }

  @Override
  public MethodDeclCG visitMethodDecl(MethodDeclJSG methodDeclJSG) {
    PyMethodDeclCG methodDeclCG = (PyMethodDeclCG) super.visitMethodDecl(methodDeclJSG);
    
    if (methodDeclCG == null)
      return null;

    methodDeclCG.name = PythonRenderUtil.getMethodNameOverload(methodDeclCG);
    
    if (!methodDeclCG.isStatic && !methodDeclCG.isConstructor()) {
      methodDeclCG.parms.add(0, new PyVarDeclCG(methodDeclCG.loc, "self", null, false));
    } else {
      methodDeclCG.parms.add(0, new PyVarDeclCG(methodDeclCG.loc, "cls_", null, false));
    }

    if (methodDeclCG.name.startsWith("test") && methodDeclCG.parms.size() == 1) {
      testMethods.add(methodDeclCG);
    }
    
    return methodDeclCG;
  }

  @Override
  public ExpressionCG visitMemberSelectExpr(
      MemberSelectExprJSG memberSelectExprJSG) {
    ExpressionCG exprCG = super.visitMemberSelectExpr(memberSelectExprJSG);
    
    if (exprCG instanceof MemberSelectExprCG) {
      MemberSelectExprCG memSelExprCG = (MemberSelectExprCG) exprCG;
      if (memSelExprCG.id.equals("class")) {
        memSelExprCG.id = "class_";
      } else if (memSelExprCG.expr != null) {
        if (memSelExprCG.id.equals("length") && memberSelectExprJSG.expr.type.kind == Kind.ARRAY) {
          // handle array length
          
          PyMemberSelectExprCG pyMemSelExprCG = new PyMemberSelectExprCG(exprCG.loc);
          pyMemSelExprCG.id = "len";
          pyMemSelExprCG.global = true;
          PyMethodInvokeExprCG miExprCG = new PyMethodInvokeExprCG(pyMemSelExprCG, memSelExprCG.expr);
          exprCG = miExprCG;
        } else if (memSelExprCG.expr instanceof IDExprCG) {
          // Convert expressions like super.foo() to super().foo()
          IDExprCG idExprCG = (IDExprCG)memSelExprCG.expr;
          if (idExprCG.getId().equals("super")) {
            VerbatimExprCG vexprCG = new VerbatimExprCG();
            vexprCG.exprText = "super()";
            memSelExprCG.expr = vexprCG;
          }
        }
      } else if (memSelExprCG.constructor) {
        if (memSelExprCG.id.equals("super")) {          
          // Map super() to super(cls, cls).constructor
          memSelExprCG.id = "super(__class__, cls_).constructor";
        } else if (memSelExprCG.id.equals("this")) {
          // Map this() to cls.constructor()
          memSelExprCG.id = "cls_.constructor";
        }

        
      } 
    }    
    return exprCG;
  }
  
  

  @Override
  public BlockStmtCG visitBlockStmt(BlockStmtJSG blockStmtJSG) {
    BlockStmtCG blockStmt = super.visitBlockStmt(blockStmtJSG);
    if (blockStmt.statements.size() == 0) 
      // add pass statement to empty block
      blockStmt.statements.add(new ExprStmtCG(new VerbatimExprCG("pass")));
  
    
    return blockStmt;
  }

  @Override
  public IDExprCG visitIDExpr(IDExprJSG idExprJSG) {
    IDExprCG idExprCG = super.visitIDExpr(idExprJSG);
            
    if (idExprJSG.name.equals("this")) {
      idExprCG.setId("self");
    } else if (idExprJSG.decl instanceof VarDeclaration) {
      VarDeclaration decl = (VarDeclaration) idExprJSG.decl;
      if (decl.isStatic) 
        idExprCG.setId(PythonRenderUtil.renderQualifiedName(decl.getEnclosingClass().getFQName()) + "." + idExprJSG.name);
//      else if (!decl.isLocal())
//        idExprCG.setId("self." + idExprJSG.name);
    } else {
      log.warning(idExprJSG.loc + ": Unexpected declaration type: " + idExprJSG.decl.getClass().getCanonicalName() + " for " + idExprJSG.name);
    }
    
    return idExprCG;      
  }

  @Override
  public ExpressionCG visitCastExpr(CastExprJSG castExprJSG) {
    ExpressionCG arg = convertExpr(castExprJSG.castExpr);
    if (castExprJSG.type== JavaiType.INT || castExprJSG.type == JavaiType.SHORT ||
        castExprJSG.type == JavaiType.LONG) {
      return createMethodInvokeExprForGlobalFunction(castExprJSG.loc, "int", arg);
    } else if (castExprJSG.type == JavaiType.DOUBLE) {
      return createMethodInvokeExprForGlobalFunction(castExprJSG.loc, "float", arg);
    } else if (castExprJSG.type.kind == JavaiType.Kind.CLASSORINTERFACE) {
      // Verify that expression is of expected type
      return createMethodInvokeExprForGlobalFunction(castExprJSG.loc, "jcl_cast", arg, 
          new VerbatimExprCG(PythonRenderUtil.renderQualifiedName(castExprJSG.type.qualifiedName)));
    } else {
      return arg;
    }
  }

  @Override
  public ExpressionCG visitInstanceOfExpr(InstanceOfExprJSG instanceOfExprJSG) {
    // Ignore InstanceOf operator for Python
    String typeName = PythonRenderUtil.renderQualifiedName(instanceOfExprJSG.testType.qualifiedName);
    if (typeName.equals("java_lang_String"))
      typeName = "str";
    return (ExpressionCG) createMethodInvokeExprForGlobalFunction(instanceOfExprJSG.loc, "jcl_isinstance", 
        convertExpr(instanceOfExprJSG.expr), 
        new VerbatimExprCG(typeName));
  }

  PyMethodInvokeExprCG createMethodInvokeExprForGlobalFunction(SourceLocation loc, String function, ExpressionCG arg) {
    PyMemberSelectExprCG pyMemSelExprCG = new PyMemberSelectExprCG(loc);
    pyMemSelExprCG.id = function;
    pyMemSelExprCG.global = true;
    PyMethodInvokeExprCG miExprCG = new PyMethodInvokeExprCG(pyMemSelExprCG, arg);
    return miExprCG;
  }

  PyMethodInvokeExprCG createMethodInvokeExprForGlobalFunction(SourceLocation loc, String function, ExpressionCG... args) {
    PyMemberSelectExprCG pyMemSelExprCG = new PyMemberSelectExprCG(loc);
    pyMemSelExprCG.id = function;
    pyMemSelExprCG.global = true;
    PyMethodInvokeExprCG miExprCG = new PyMethodInvokeExprCG(pyMemSelExprCG);
    miExprCG.args = Arrays.asList(args);
    return miExprCG;
  }

  @Override
  public ExpressionCG visitBinExpr(BinExprJSG binExprJSG) {
    PyBinExprCG binExpr = (PyBinExprCG) super.visitBinExpr(binExprJSG);
    
    if (binExpr.opKind == OperatorKind.RIGHTSHIFT) {
      return createMethodInvokeExprForGlobalFunction(binExprJSG.loc, "jcl_rshift", 
          convertExpr(binExprJSG.left), convertExpr(binExprJSG.right));
    } else if (binExpr.opKind == OperatorKind.PLUS) {
      if (binExprJSG.type == JavaiType.STRING) {
        // Coerce non-string operands to string
        if (binExprJSG.left.type != JavaiType.STRING) {
          binExpr.left = createMethodInvokeExprForGlobalFunction(binExpr.left.loc, "jcl_tostr", binExpr.left);
        }
        if (binExprJSG.right.type != JavaiType.STRING) {
          binExpr.right = createMethodInvokeExprForGlobalFunction(binExpr.right.loc, "jcl_tostr", binExpr.right);
        }
      }
    } else if (binExpr.opKind == OperatorKind.DIVIDE) {
      ExpressionCG exprCG = binExpr;
      if (binExprJSG.type == JavaiType.INT || binExprJSG.type == JavaiType.SHORT || 
          binExprJSG.type == JavaiType.LONG) {
        exprCG = createMethodInvokeExprForGlobalFunction(binExprJSG.loc, "jcl_divint", 
            convertExpr(binExprJSG.left), convertExpr(binExprJSG.right));
        if (binExpr.isAssign)
          exprCG = new PyAsmtExprCG(convertExpr(binExprJSG.left), exprCG);
      } else if (binExprJSG.type == JavaiType.DOUBLE) {
        exprCG = createMethodInvokeExprForGlobalFunction(binExprJSG.loc, "jcl_divfloat", 
            convertExpr(binExprJSG.left), convertExpr(binExprJSG.right));
        if (binExpr.isAssign)
          exprCG = new PyAsmtExprCG(convertExpr(binExprJSG.left), exprCG);
      }
      return exprCG;
    }
    return binExpr;
    
  }

  @Override
  public Object visitMethodInvokeExpr(MethodInvokeExprJSG methodInvokeExprJSG) {
    ExpressionCG expr = (ExpressionCG) super.visitMethodInvokeExpr(methodInvokeExprJSG);
    if (expr instanceof PyMethodInvokeExprCG) {
      PyMethodInvokeExprCG invokeExpr = (PyMethodInvokeExprCG) expr;
      // overload all method calls except assertions and certain library calls
      
//      if (invokeExpr.methodExprCG.id.equals("toString"))
//        return createMethodInvokeExprForGlobalFunction(methodInvokeExprJSG.loc, "jcl_String", invokeExpr.methodExprCG.expr);
      
      if (!invokeExpr.methodOwnerType.qualifiedName.equals("java.lang.String") &&
          (invokeExpr.methodExprCG.fqClassName == null || !(
              (invokeExpr.methodExprCG.fqClassName.equals("java.lang.System") && invokeExpr.methodExprCG.id.equals("arraycopy")) 
               || invokeExpr.methodExprCG.fqClassName.equals("java.util.Arrays")))) {
        
        invokeExpr.methodExprCG.id = PythonRenderUtil.getMethodNameOverload(invokeExpr.methodExprCG.id,
            methodInvokeExprJSG.methodDecl.parmTypes);
      }
      
      if (invokeExpr.methodExprCG.constructor) {
        invokeExpr.args.add(new PyIDExprCG(invokeExpr.loc, "self"));
      }
    }
    return expr;
  }
  

  @Override
  public TypeDeclCG visitTypeDecl(TypeDeclJSG typeDeclJSG) {
    testMethods = new ArrayList<>();
    
    PyTypeDeclCG typeDeclCG = (PyTypeDeclCG) super.visitTypeDecl(typeDeclJSG);
    
    if (typeDeclCG.isInterface())
      return typeDeclCG;
    
    // Initialize instance variables
    
    PyMethodDeclCG constructor = null;
    for (int i = 0; i < typeDeclCG.methods.size(); ++i) {
      PyMethodDeclCG methodDeclCG = typeDeclCG.methods.get(i);
    
      if (methodDeclCG.isConstructor()) {
        constructor = methodDeclCG;
        processConstructor(typeDeclJSG, constructor);
            
      }
    }
    
    if (constructor == null) {
      // Generate default constructor to initialize instance variables
      PyBlockStmtCG constructorBody = new PyBlockStmtCG(typeDeclJSG.loc, new ArrayList<>());
      constructorBody.statements.add(new ExprStmtCG(typeDeclJSG.loc, "super().__init__()")); 
      List<VarDeclCG> parms = new ArrayList<>();
      parms.add(new PyVarDeclCG(typeDeclJSG.loc, "self", null, false));
      constructor = new PyMethodDeclCG(typeDeclJSG.loc, "__init__", null, 
          parms, constructorBody, false);
      typeDeclCG.methods.add(constructor);
      processConstructor(typeDeclJSG, constructor);
    }
    
    // Now, handle JUnit classes
    
    String className = typeDeclCG.getQualifiedName();
    // TODO: Check specifically for junit_framework_Test; need to handle abstract test classes
    if (className.endsWith("Test") && !typeDeclCG.isAbstract()) {
      if (typeDeclCG.typeDeclJSG.extendsClass == null || 
          typeDeclCG.typeDeclJSG.extendsClass.qualifiedName.equals("java.lang.Object")) {
        typeDeclCG.typeDeclJSG.extendsClass = new JavaiType("junit.framework.TestCase", new ArrayList<>());
      }
      // Add runTest method
      List<StatementCG> stmtList = new ArrayList<>();
      stmtList.add(new ExprStmtCG(new VerbatimExprCG("test = jcl_makeTest(" + className + ")")));
      for (PyMethodDeclCG decl : testMethods) {
        String stmt = "test._calltestmethod(test."+ decl.name + ")";
        stmtList.add(new ExprStmtCG(new VerbatimExprCG(stmt)));
      }
      PyBlockStmtCG blockStmtCG = new PyBlockStmtCG(typeDeclJSG.loc, stmtList);
      List<VarDeclCG> parms = new ArrayList<>();
      parms.add(new PyVarDeclCG(typeDeclJSG.loc, "cls_", null, false));
      PyMethodDeclCG methodDeclCG = new PyMethodDeclCG(typeDeclJSG.loc, "jcl_runTest", JavaiType.VOID, parms, blockStmtCG, true);
      typeDeclCG.methods.add(methodDeclCG);
      testClasses.add(typeDeclCG);
    }
    return typeDeclCG;
  }

  // transform a constructor to a factory method
  // called after the constructor is visited in an initial pass through the class
  private void processConstructor(TypeDeclJSG typeDeclJSG,
      PyMethodDeclCG constructor) {
    
    ExprStmtCG exprStmt = (ExprStmtCG) constructor.body.statements.get(0);
    boolean callsAnotherConstructor = ((MethodInvokeExprCG)exprStmt.exprCG).methodExprCG.id.startsWith("cls_.constructor");
    
    int count = 0;
    // Add instance variable /constant initialization statements to constructor body
    for (VarDeclJSG varDecl : typeDeclJSG.fields) {
      if (!varDecl.isStatic) {
        ExpressionCG exprCG;
        boolean initializerProvided = (varDecl.expr != null); 
                    
        // Determine whether this variable is initialized in constructor body
        boolean initialized = false;

        for (Iterator<StatementCG> it = constructor.body.statements.iterator(); it.hasNext(); ) {
          StatementCG curStmt = it.next();
          if (curStmt instanceof ExprStmtCG) {
            ExpressionCG exprStmtCG = ((ExprStmtCG)curStmt).exprCG;
            if (exprStmtCG instanceof PyAsmtExprCG) {
              ExpressionCG lhs = ((PyAsmtExprCG)exprStmtCG).lhsCG;
              if (lhs instanceof PyIDExprCG) {
                PyIDExprCG lhsIDExpr = (PyIDExprCG)lhs;
                if (lhsIDExpr.getId().equals("self." + varDecl.name)) {
                  initialized = true;
                }
              }
            }
            
          }
        }
        if (initializerProvided || !initialized) {
          if (initializerProvided)
            exprCG = convertExpr(varDecl.expr);
          else
            exprCG = new VerbatimExprCG(PythonRenderUtil.getDefaultValueForType(varDecl.jType));
          PyAsmtExprCG asmtExprCG = new PyAsmtExprCG(
              new PyIDExprCG(constructor.loc, "self." + varDecl.name),
              exprCG);
          ExprStmtCG stmt = new ExprStmtCG(asmtExprCG);

          // If this constructor calls another constructor, do not
          // override the initialization work performed by the other constructor
          if (!callsAnotherConstructor) {
            constructor.body.statements.add(count+1, stmt);
            ++count;
          }
        }
      }
    }
    
    // refactor to factory method
    constructor.returnType = new JavaiType(typeDeclJSG.getFQName(), new ArrayList<>());
    constructor.isStatic = true;
    
    // transform initial call to this or super
//    ExprStmtCG callToSuperOrThisCG = (ExprStmtCG) constructor.body.statements.get(0);
//    List<ExpressionCG> args = ((PyMethodInvokeExprCG)callToSuperOrThisCG.exprCG).args;
//    PyVarDeclCG varDeclCG = new PyVarDeclCG(constructor.loc, "self", constructor.returnType, false);
//    varDeclCG.setInitExpr((PyMethodInvokeExprCG)callToSuperOrThisCG.exprCG);

    List<JavaiType> collect = new ArrayList<>();
    for (VarDeclCG parm : constructor.parms) {
      String name = ((PyVarDeclCG)parm).getRawName();
      if (!name.equals("self") && !name.equals("cls_"))
        collect.add(parm.jType);
    }
    constructor.name =  PythonRenderUtil.getMethodNameOverload("constructor",
        collect);
//    constructor.body.statements.set(0, new ExprStmtCG(varDeclCG));
    constructor.body.statements.add(0, new ExprStmtCG(new VerbatimExprCG("if not self: self = cls_()")));
    constructor.parms.add(new PyVarDeclCG(constructor.loc, "self", null, false));
    
    constructor.body.statements.add(new PyReturnStmtCG(new PyIDExprCG(constructor.loc,  "self")));   

  }

  @Override
  public CompileUnitCG visitCompileUnit(CompileUnitJSG compileUnitJSG) {
    testClasses = new ArrayList<>();
    PyCompileUnitCG compileUnitCG = (PyCompileUnitCG) super.visitCompileUnit(compileUnitJSG); 
    compileUnitCG.testClasses = testClasses;
    return compileUnitCG;
  }

  @Override
  public PyMethodInvokeExprCG visitNewClassExpr(NewClassExprJSG newClassExprJSG) {

    PyMethodInvokeExprCG invokeExprCG = new PyMethodInvokeExprCG(
            new PyMemberSelectExprCG(newClassExprJSG.loc, newClassExprJSG.type.qualifiedName, 
                PythonRenderUtil.getMethodNameOverload("constructor",
                    newClassExprJSG.methodDecl.parmTypes)));
    invokeExprCG.args = convertExprList(newClassExprJSG.args);
    invokeExprCG.args.add(new PyIDExprCG(newClassExprJSG.loc, "None")); // for self
    return invokeExprCG;
  }

  
  @Override
  public TryStmtCG visitTryStmt(TryStmtJSG tryStmtJSG) {
    TryStmtCG tryStmtCG =  super.visitTryStmt(tryStmtJSG);
    for (CatchCG catchItem : tryStmtCG.catchList) {
      if (catchItem.catchParm.jType.qualifiedName.equals("java.lang.NullPointerException")) {
        catchItem.catchParm.jType = new JavaiType("TypeError");
      }
    }
    return tryStmtCG;
  }

}
