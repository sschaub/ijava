package javaic.parsetree;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javaic.LogFactory;
import javaic.parsetree.ExpressionJSG.OperatorKind;
import javaic.semantics.ClassDeclaration;
import javaic.semantics.Declaration;
import javaic.semantics.JavaiType;
import javaic.semantics.MethodDeclaration;
import javaic.semantics.VarDeclaration;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.ExecutableType;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.IntersectionTypeTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.TreeInfo;

public class JavaiParseTreeBuilder extends TreePathScanner<Object, Declaration> {

  CompilationUnitTree ast;
  CompileUnitJSG compileUnitJSG;
  Trees typetrees;
  Stack<TypeDeclJSG> curTypeDeclStack = new Stack<>();
  String filename;
  Set<String> libraryTypes;
//  SymbolTable symtab = new SymbolTable();
  static int anonymousClassCounter = 0;
  boolean inAnonymousOrNestedClass;
  MethodSymbol currentMethod;
  MethodSymbol anonymousClassContext; // method containing anonymous class
  ClassSymbol outerClassContext; // class containing nested class
  Map<String, VarDeclJSG> anonymousClassCaptures;
  ClassDeclaration anonymousClass;
  
  Logger logger = LogFactory.getLogger(getClass());
  
  public JavaiParseTreeBuilder() {
    libraryTypes = new HashSet<String>(Arrays.asList(
        "junit.framework.Test"
        ));
  }

  public void scan() {
    super.scan(ast, null);
  }

  public void setCurrentCompilationUnit(CompilationUnitTree ast) {
    this.ast = ast;
    this.filename = ast.getSourceFile().getName();

  }

  public CompileUnitJSG getCompilationUnit() {
    return compileUnitJSG;
  }

  // Visits a class, interface, or enum
  @Override
  public Object visitClass(ClassTree classTree, Declaration context) {
    
    SourceLocation loc = getLocation(classTree);
    
    String name = classTree.getSimpleName().toString();
    if (curTypeDeclStack.size() > 0) {
      inAnonymousOrNestedClass = true;
      outerClassContext = ((JCClassDecl)classTree).sym.outermostClass();
      anonymousClassCaptures = new HashMap<>();
    } else {
      inAnonymousOrNestedClass = false;
      outerClassContext = null;
    }
    
    if (context != null && name.length() > 0) {
      // named, nested class
      name = context.id + "." + name;
    }
    
    TypeDeclJSG.Kind kind;
    switch (classTree.getKind()) {
    case CLASS:
      kind = TypeDeclJSG.Kind.CLASS;
      break;
    case INTERFACE:
      kind = TypeDeclJSG.Kind.INTERFACE;
      break;
    default:
      throw new ParseException(getLocation(classTree),
          String.format("Unsupported kind %s for class %s", classTree.getKind(), name));
    }
    TypeDeclJSG typeDeclJSG = new TypeDeclJSG(loc, kind, name, compileUnitJSG.packageName);
    
    curTypeDeclStack.push(typeDeclJSG);

    compileUnitJSG.decls.add(typeDeclJSG);
    typeDeclJSG.setAbstract(classTree.getModifiers().getFlags().contains(Modifier.ABSTRACT));

    // Setup Inheritance information
    if (classTree.getExtendsClause() != null) {
      typeDeclJSG.extendsClass = getTypeJSG(classTree.getExtendsClause());
    }
    
    if (classTree.getImplementsClause() != null) {
      typeDeclJSG.extendsInterfaces = classTree.getImplementsClause().stream()
          .map(impl -> getTypeJSG(impl))
          .collect(Collectors.toList());
    }
    
    logger.fine("Visiting " + kind + " declaration: " + name);
    
    ClassDeclaration classDecl = new ClassDeclaration(getLocation(classTree), null, compileUnitJSG.packageName, name, null, false);
    
    visitClassMembers(classTree, typeDeclJSG, classDecl);
    
    if (curTypeDeclStack.size() > 1 && name.length() > 0) {
      // named, nested class
      TypeDeclJSG outerTypeDeclJSG = null;
      if (!classTree.getModifiers().getFlags().contains(Modifier.STATIC)) {
        outerTypeDeclJSG = getOuterTypeDecl();
      }
      updateNestedConstructor(loc, null, typeDeclJSG, classDecl, outerTypeDeclJSG);      
    }
    
    curTypeDeclStack.pop();
    inAnonymousOrNestedClass = false;
    outerClassContext = null;

//    symtab.endScope();
    return typeDeclJSG;

  }

//  private String constructMethodSignature(String fqName, MethodTree methTree) {
//    
//    String parmList =
//        String.join(",", methTree.getParameters().stream().map(parm -> parm.getType().toString()).collect(Collectors.toList()));
//    String returnType = (methTree.getReturnType() == null) ? "" : methTree.getReturnType().toString();
//    return fqName + "." + methTree.getName() + "(" + parmList + "):" + returnType;
//  }

  protected JavaiType getTypeJSG(Tree tree) {    
    return getTypeJSG(getLocation(tree), getType(tree));
  }
  
  protected Type getType(Tree tree) {
    TreePath path = TreePath.getPath(ast, tree);
    return (Type) typetrees.getTypeMirror(path);
  }

  private JavaiType getTypeJSG(SourceLocation loc, Type type) {
    if (type == null) {
      return JavaiType.NONE;
    }
    if (type.getKind().isPrimitive()) {

      switch (type.getKind()) {
      case BOOLEAN:
        return JavaiType.BOOLEAN;
      case BYTE:
        return JavaiType.BYTE;
      case SHORT:
        return JavaiType.SHORT;
      case INT:
        return JavaiType.INT;
      case LONG:
        return JavaiType.LONG;
      case DOUBLE:
        return JavaiType.DOUBLE;
      case FLOAT:
        return JavaiType.DOUBLE;
      case CHAR:
        return JavaiType.CHAR;
      default:
        throw new ParseException(loc, "Unhandled type encountered: " + type);
      }
    }

    Symbol classSymbol = (Symbol) type.asElement();
    List<JavaiType> typeArgs = new ArrayList<>();

    switch (type.getKind()) {
    case DECLARED: // class type
      boolean isEnum = ((classSymbol.flags() & Flags.ENUM) != 0);
      if (isEnum) {
        throw new ParseException(loc, "Enums not handled yet");
      }
      String typeName = classSymbol.getSimpleName().toString();
      String qualifiedName = classSymbol.getQualifiedName().toString();
      for (Type t : type.getTypeArguments()) {
        typeArgs.add(getTypeJSG(loc, t));
      }
      
      if (qualifiedName.equals("java.lang.String"))
        return JavaiType.STRING;
      JavaiType jType = new JavaiType(qualifiedName, typeArgs);
      registerTypeUsed(jType);
      return jType;

    case TYPEVAR: 
      if (type.getTypeArguments().size() > 0) {
        throw new ParseException(loc, "Type arguments to a type var not supported.");
      }
      return new JavaiType(JavaiType.Kind.TYPEVAR, type.toString());
    case VOID:
      return JavaiType.VOID;
    case NULL:
      return JavaiType.NULL;
    case ARRAY:
      JavaiType jtComponentType = getTypeJSG(loc, (Type) ((ArrayType) type).getComponentType());
      registerTypeUsed(jtComponentType);
      return new JavaiType(jtComponentType);
    case EXECUTABLE:
      ExecutableType exeType = (ExecutableType)type;
      List<JavaiType> parmTypes = exeType.getParameterTypes().stream()
          .map(parmType -> getTypeJSG(loc, (Type)parmType))
          .collect(Collectors.toList());
      JavaiType returnType = getTypeJSG(loc, (Type)exeType.getReturnType());
      return new JavaiType(returnType, parmTypes);
    case PACKAGE:
      // a component of a package name 
      return JavaiType.NONE;
    case WILDCARD:
      return JavaiType.NONE;
    default:
      throw new ParseException(loc, "Unhandled type: " + type);
    }
    
  }
  
  private void registerTypeUsed(JavaiType jtComponentType) {
    String qualifiedName = jtComponentType.qualifiedName;
    
    if (qualifiedName != null && qualifiedName.equals("org.junit.Assert")) {
      // Substitute JUnit 3 in place of JUnit 4
      jtComponentType.qualifiedName = "junit.framework.Assert";
    }
    
    if (qualifiedName != null && qualifiedName.length() > 0
        && qualifiedName.contains(".") 
        && !libraryTypes.contains(qualifiedName) 
        && !qualifiedName.startsWith("java.")
        && !qualifiedName.startsWith("junit.framework")
        && !qualifiedName.startsWith("org.junit.")
        )
      compileUnitJSG.typesUsed.add(jtComponentType);
    
    if (qualifiedName != null) {
      if ((qualifiedName.equals("java.util.List") || qualifiedName.equals("java.util.ArrayList") ||
          qualifiedName.equals("java.util.Collection")
          || qualifiedName.equals("java.util.Set") || qualifiedName.equals("java.util.HashSet") ||
          qualifiedName.equals("java.util.Comparator") || qualifiedName.equals("java.util.Iterator")) && jtComponentType.classTypeArgs.size() == 0) {
        jtComponentType.classTypeArgs.add(new JavaiType("java.lang.Object"));
      } else if ((qualifiedName.equals("java.util.Map") || qualifiedName.equals("java.util.HashMap") 
          || qualifiedName.equals("java.util.TreeMap"))
          && jtComponentType.classTypeArgs.size() == 0) {
        jtComponentType.classTypeArgs.add(new JavaiType("java.lang.Object"));
        jtComponentType.classTypeArgs.add(new JavaiType("java.lang.Object"));        
      }
    }
  }

  public void setTrees(Trees typetrees) {
    this.typetrees = typetrees;

  }

  @Override
  public Object visitMethod(MethodTree methTree, Declaration context) {
    String name = methTree.getName().toString();
    SourceLocation loc = getLocation(methTree);
    String classFQName = ((ClassDeclaration)context).getFQName();
    logger.info("Visiting " + classFQName + "." + name + "()");
//    String signature = constructMethodSignature(classFQName, methTree);
//    MethodDeclaration methodDecl = (MethodDeclaration) symtab.lookup(signature, getLocation(methTree));
    MethodSymbol methSym = (MethodSymbol) TreeInfo.symbolFor((JCTree) methTree);
    if (curTypeDeclStack.size() == 1)
      currentMethod = methSym;
    MethodDeclaration methodDecl = (MethodDeclaration) declFromSymbol(methSym, loc);
    MethodDeclJSG methodJSG = new MethodDeclJSG(loc, name);
    setDeclJSGModifiers(methTree.getModifiers().getFlags(), methodJSG);
//    symtab.define(name, methodDecl);
//    symtab.beginScope();
    
    if (methTree.getReturnType() != null) {
      methodJSG.returnType = getTypeJSG(methTree.getReturnType());
    }

    methodJSG.parms = methTree.getParameters().stream()
        .map(varTree -> (VarDeclJSG) visitVariable(varTree, methodDecl))
        .collect(Collectors.toList());
    
    if (methTree.getBody() != null) {
    
      methodJSG.body = (BlockStmtJSG) methTree.getBody().accept(this, methodDecl);
      
    }
    if (curTypeDeclStack.size() == 1)
      currentMethod = null;
//    symtab.endScope();
    return methodJSG;
  }

  // Returns format: java.lang.String.substring(int):java.lang.String
  private String getMethodSignature(JCTree methTree) {
    Symbol method = (Symbol) TreeInfo.symbol(methTree);
    
    String signature = (method.owner + "." + method.name + method.type).replace(")", "):");
    return signature;
  }

  @Override
  public Object visitVariable(VariableTree varTree, Declaration context) {
    String name = varTree.getName().toString();
    logger.fine("Visiting " + name);
    JavaiType typeJSG = getTypeJSG(varTree.getType());    
    ExpressionJSG exprJSG = varTree.getInitializer() != null ? 
        (ExpressionJSG) varTree.getInitializer().accept(this, context) : null;
    VarDeclJSG varDeclJSG = new VarDeclJSG(getLocation(varTree), name, typeJSG, exprJSG);
    setDeclJSGModifiers(varTree.getModifiers().getFlags(), varDeclJSG);
//    symtab.define(name, new VarDeclaration(getLocation(varTree), context, name, typeJSG, varDeclJSG.isStatic));
    
    return varDeclJSG;
  }
  
  void setDeclJSGModifiers(Set<Modifier> modifiers, TypeMemberJSG member) {
    member.isStatic = modifiers.contains(Modifier.STATIC);
    if (modifiers.contains(Modifier.PUBLIC)) {
      member.visibility = TypeMemberJSG.Visibility.PUBLIC;
    } else if (modifiers.contains(Modifier.PROTECTED)) {
      member.visibility = TypeMemberJSG.Visibility.PROTECTED;
    } else if (modifiers.contains(Modifier.PRIVATE)) {
      member.visibility = TypeMemberJSG.Visibility.PRIVATE;
    } else {
      member.visibility = TypeMemberJSG.Visibility.PUBLIC;
    }
  }

  @Override
  public Object visitBlock(BlockTree blockTree, Declaration context) {
    BlockStmtJSG blockStmtJSG = new BlockStmtJSG(getLocation(blockTree));
    for (StatementTree stmtTree : blockTree.getStatements()) {
      blockStmtJSG.statements.add(doVisitStatementTree(stmtTree, context));
    }
    return blockStmtJSG;
  }

  protected StatementJSG doVisitStatementTree(StatementTree stmtTree, Declaration context) {
    Object obj = stmtTree.accept(this, context);
    if (obj instanceof VarDeclJSG) {
      obj = new VarDeclStmtJSG(getLocation(stmtTree), (VarDeclJSG) obj);
    } else if (!(obj instanceof StatementJSG)) {
      throw new ParseException(getLocation(stmtTree), "Unexpected when visiting StatementTree: " + obj.getClass());
    }
    return (StatementJSG)obj ;
  }
  
  protected ExpressionJSG doVisitExpressionTree(ExpressionTree exprTree, Declaration context) {
    return (ExpressionJSG) exprTree.accept(this, context);
  }

  @Override
  public Object visitAssignment(AssignmentTree asmtTree, Declaration context) {
    ExpressionJSG lhsJSG = (ExpressionJSG) asmtTree.getVariable().accept(this, context);
    ExpressionJSG exprJSG = (ExpressionJSG) asmtTree.getExpression().accept(this, context);
    if (!((JCAssign)asmtTree).isStandalone()) {
      logger.warning("Non standalone assignment: " + getLocation(asmtTree));
    }
    return new AsmtExprJSG(getLocation(asmtTree), getTypeJSG(asmtTree), lhsJSG, exprJSG);
  }

  @Override
  public Object visitExpressionStatement(ExpressionStatementTree exprStmtTree,
      Declaration context) {
    logger.finer("Visiting " + exprStmtTree.toString());
    ExpressionJSG exprJSG = doVisitExpressionTree(exprStmtTree.getExpression(), context);
    return new ExprStmtJSG(getLocation(exprStmtTree), exprJSG);
  }

  @Override
  public Object visitAnnotatedType(AnnotatedTypeTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitAnnotation(AnnotationTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitArrayAccess(ArrayAccessTree arrayAccessTree, Declaration context) {
    ExpressionJSG indexExprJSG = (ExpressionJSG)arrayAccessTree.getIndex().accept(this, context);
    ExpressionJSG arrayExprJSG = (ExpressionJSG)arrayAccessTree.getExpression().accept(this, context);
    return new ArrayIndexExprJSG(getLocation(arrayAccessTree), getTypeJSG(arrayAccessTree), arrayExprJSG, indexExprJSG);
  }

  @Override
  public Object visitArrayType(ArrayTypeTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitAssert(AssertTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitBinary(BinaryTree binTree, Declaration context) {
    ExpressionJSG left = (ExpressionJSG)binTree.getLeftOperand().accept(this, context);
    ExpressionJSG right = (ExpressionJSG)binTree.getRightOperand().accept(this, context);
    Kind kind = binTree.getKind();
    OpKind opKind = getBinaryOperatorKind(getLocation(binTree), kind);
    if (opKind.isAssign) {
      logger.warning("Use of assignment binary operator: " + getLocation(binTree));
    }
    return new BinExprJSG(getLocation(binTree), getTypeJSG(binTree), opKind.op, left, right, opKind.isAssign); 
  }
  
  static class OpKind {
    boolean isAssign;
    ExpressionJSG.OperatorKind op;
    
    public OpKind(OperatorKind opKind, boolean isAssign) {
      this.isAssign = isAssign;
      this.op = opKind;
    }
    
  }
  
  public OpKind getBinaryOperatorKind(SourceLocation loc, Kind kind) {
    boolean isAssign = false;
    ExpressionJSG.OperatorKind op;
    switch (kind) {
    case AND_ASSIGNMENT: isAssign = true;
    case AND:
      op = ExpressionJSG.OperatorKind.BITAND; break;
    case OR_ASSIGNMENT: isAssign = true;
    case OR:
      op = ExpressionJSG.OperatorKind.BITOR; break;
    case LOGICAL_COMPLEMENT:
      op = ExpressionJSG.OperatorKind.NOT; break;
    case BITWISE_COMPLEMENT:
      op = ExpressionJSG.OperatorKind.BITNOT; break;
    case PLUS_ASSIGNMENT: isAssign = true;      
    case PLUS:
      op = ExpressionJSG.OperatorKind.PLUS; break;
    case MINUS_ASSIGNMENT: isAssign = true;
    case MINUS:
      op = ExpressionJSG.OperatorKind.MINUS; break;
    case MULTIPLY_ASSIGNMENT: isAssign = true;
    case MULTIPLY:
      op = ExpressionJSG.OperatorKind.MULTIPLY; break;
    case DIVIDE_ASSIGNMENT: isAssign = true;  
    case DIVIDE:
      op = ExpressionJSG.OperatorKind.DIVIDE; break;
    case REMAINDER_ASSIGNMENT: isAssign = true;
    case REMAINDER:
      op = ExpressionJSG.OperatorKind.MODULUS; break;
    case EQUAL_TO:
      op = ExpressionJSG.OperatorKind.EQ; break;
    case NOT_EQUAL_TO:
      op = ExpressionJSG.OperatorKind.NOTEQ; break;
    case LESS_THAN:
      op = ExpressionJSG.OperatorKind.LESSTHAN; break;
    case LESS_THAN_EQUAL:
      op = ExpressionJSG.OperatorKind.LESSOREQ; break;
    case GREATER_THAN:
      op = ExpressionJSG.OperatorKind.GREATERTHAN; break;
    case GREATER_THAN_EQUAL:
      op = ExpressionJSG.OperatorKind.GREATEROREQ; break;
    case LEFT_SHIFT_ASSIGNMENT: isAssign = true;
    case LEFT_SHIFT:
      op = ExpressionJSG.OperatorKind.LEFTSHIFT; break;
    case RIGHT_SHIFT_ASSIGNMENT: 
      isAssign = true;
    case RIGHT_SHIFT:
      op = ExpressionJSG.OperatorKind.RIGHTSHIFT; break;
    case UNSIGNED_RIGHT_SHIFT_ASSIGNMENT: 
      isAssign = true;
    case UNSIGNED_RIGHT_SHIFT:
      op = ExpressionJSG.OperatorKind.UNSIGNED_RIGHTSHIFT; break;
    case XOR:
      op = ExpressionJSG.OperatorKind.XOR; break;
    case CONDITIONAL_AND:
      op = ExpressionJSG.OperatorKind.AND; break;
    case CONDITIONAL_OR:
      op = ExpressionJSG.OperatorKind.OR; break;
    default:
      throw new ParseException(loc, "Java binary operator not implemented:" + kind);
      
    }
    
    return new OpKind(op, isAssign);
  }

  @Override
  public Object visitBreak(BreakTree tree, Declaration context) {
    return new BreakStmtJSG(getLocation(tree));
  }

  @Override
  public Object visitCase(CaseTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitCatch(CatchTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitCompilationUnit(CompilationUnitTree compileUnitTree, Declaration context) {
    String pkgName = null;
    if (compileUnitTree.getPackageName() != null) {
      pkgName = compileUnitTree.getPackageName().toString();
    }
    compileUnitJSG = new CompileUnitJSG(pkgName);
    compileUnitJSG.filename = filename;
    super.visitCompilationUnit(compileUnitTree, context);
    return compileUnitJSG;
  }

  @Override
  public Object visitCompoundAssignment(CompoundAssignmentTree asmtTree,
      Declaration context) {
    ExpressionJSG lhsJSG = doVisitExpressionTree(asmtTree.getVariable(), context);
    ExpressionJSG exprJSG = doVisitExpressionTree(asmtTree.getExpression(), context);
    OpKind opKind = getBinaryOperatorKind(getLocation(asmtTree), asmtTree.getKind());
    return new BinExprJSG(getLocation(asmtTree), getTypeJSG(asmtTree), opKind.op, lhsJSG, exprJSG, opKind.isAssign);
  }

  @Override
  public Object visitConditionalExpression(ConditionalExpressionTree tree,
      Declaration context) {

    return new CondExprJSG(getLocation(tree), getTypeJSG(tree), 
        (ExpressionJSG) tree.getCondition().accept(this, context),
        (ExpressionJSG) tree.getTrueExpression().accept(this, context),
        (ExpressionJSG) tree.getFalseExpression().accept(this, context));
  }

  @Override
  public Object visitContinue(ContinueTree tree, Declaration context) {
    return new ContinueStmtJSG(getLocation(tree));
  }

  @Override
  public Object visitDoWhileLoop(DoWhileLoopTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitEmptyStatement(EmptyStatementTree tree, Declaration context) {
    return new EmptyStatementJSG(getLocation(tree));
  }

  @Override
  public Object visitEnhancedForLoop(EnhancedForLoopTree tree, Declaration context) {
    logger.warning(getLocation(tree) + "Java feature not implemented.");
    return new EmptyStatementJSG(getLocation(tree));
  }

  @Override
  public Object visitErroneous(ErroneousTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitForLoop(ForLoopTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitIdentifier(IdentifierTree idTree, Declaration context) {
    String name = idTree.getName().toString();
    Symbol idSymbol = (Symbol) TreeInfo.symbol((JCTree) idTree);
    SourceLocation loc = getLocation(idTree);
    JavaiType type = getTypeJSG(idTree);
    Declaration decl = null;
    if (idSymbol != null) {
      logger.finer(loc + " - " + (idSymbol.getClass()) + "; Owner of " + name + " is: " + idSymbol.owner);
      decl = declFromSymbol(idSymbol, loc);
      if (decl instanceof VarDeclaration) {
        if (idSymbol.isStatic()) { 
          return new MemberSelectExprJSG(loc, type, new JavaiType(idSymbol.owner.getQualifiedName().toString()), name);          
        }
        
        if (name.equals("super") || name.equals("this") || 
            (idSymbol.owner instanceof Symbol.MethodSymbol && idSymbol.owner != anonymousClassContext)) {
          // simple local variable reference
          return new IDExprJSG(loc, type, name, decl);
        }

        if (idSymbol.owner == outerClassContext) {
          // a reference to a variable in outer class

          return new MemberSelectExprJSG(loc, type, 
              new MemberSelectExprJSG(loc, type,
                  new IDExprJSG(loc, getCurTypeDecl().getJType(), "this", decl),
                  "outer"), name);
        }
        
        if (idSymbol.owner == anonymousClassContext) {
          // must capture this variable 
          anonymousClassCaptures.put(name, new VarDeclJSG(loc, name, type, null));
        } 
        
        // a reference to a variable in current class
        ExpressionJSG expr = new IDExprJSG(loc, getCurTypeDecl().getJType(), "this", decl);
        return new MemberSelectExprJSG(loc, type, expr, name);
                 
       
      } else {
        
        throw new ParseException(loc, "Unexpected identifier type: " + decl);
      }
    } else {
    
      if (type == JavaiType.NONE)
        return new IDExprJSG(loc, getTypeJSG(idTree), name, null);

    
      throw new ParseException(loc, "Undefined identifier: " + name);
    }
    
  }

  private Declaration declFromSymbol(Symbol idSymbol, SourceLocation loc) {
    if (idSymbol == null)
      return null;
    
    switch (idSymbol.getKind()) {
    case FIELD:
    case LOCAL_VARIABLE:
    case PARAMETER:
    case EXCEPTION_PARAMETER:
    {
      VarSymbol varSymbol = (VarSymbol)idSymbol;
      Declaration parent = declFromSymbol(varSymbol.owner, loc);
      JavaiType jType = getTypeJSG(loc, varSymbol.type);
      VarDeclaration varDecl = new VarDeclaration(loc, parent, varSymbol.getSimpleName().toString(), jType, varSymbol.isStatic() );
      return varDecl;
    }
    
    case METHOD:
    case CONSTRUCTOR:
    {
      MethodSymbol methSymbol = (MethodSymbol)idSymbol;
      ClassSymbol classSymbol = (ClassSymbol)methSymbol.owner;
      JavaiType returnType = null;
      if (methSymbol.type != null && methSymbol.getReturnType() != null)
        returnType = getTypeJSG(loc, methSymbol.getReturnType());
      List<JavaiType> parmTypes = new ArrayList<>();

      for (VarSymbol parm : methSymbol.params()) {
        parmTypes.add(getTypeJSG(loc, parm.type));
      }
      MethodDeclaration methodDecl = new MethodDeclaration(loc, classSymbol.className(), methSymbol.getSimpleName().toString(), returnType, parmTypes, methSymbol.isStatic());
      return methodDecl;
      
    }
    
    case CLASS:
    {
      ClassSymbol classSymbol = (ClassSymbol)idSymbol;
      ClassDeclaration classDecl = new ClassDeclaration(loc, declFromSymbol(classSymbol.owner, loc), classSymbol.packge().fullname.toString(), classSymbol.getSimpleName().toString(), null, false);
      return classDecl;
    }
    
    case PACKAGE:
      return null;
    
    default:
      logger.warning(loc + "Unhandled declaration type: " + idSymbol.getKind());
      return null;
    }
      
  }

  @Override
  public Object visitIf(IfTree ifTree, Declaration context) {
    ExpressionJSG exprJSG = (ExpressionJSG) ifTree.getCondition().accept(this, context);
    StatementJSG thenStmtJSG = doVisitStatementTree(ifTree.getThenStatement(), context);
    StatementJSG elseStmtJSG = null;
    if (ifTree.getElseStatement() != null) {
      elseStmtJSG = (StatementJSG) ifTree.getElseStatement().accept(this, context);
    }
    
    return new IfStmtJSG(getLocation(ifTree), exprJSG, thenStmtJSG, elseStmtJSG);
  }

  @Override
  public Object visitImport(ImportTree importTree, Declaration context) {
    String qualifiedId = importTree.getQualifiedIdentifier().toString();
    if (importTree.isStatic()) {
      throw new ParseException(getLocation(importTree), "Java feature not implemented: static import");
    }
    compileUnitJSG.imports.add(new ImportJSG(qualifiedId, qualifiedId.endsWith(".*")));
    return null;
  }

  @Override
  public Object visitInstanceOf(InstanceOfTree instanceOfTree, Declaration context) {
    ExpressionJSG expr = (ExpressionJSG)instanceOfTree.getExpression().accept(this, context);
    JavaiType type = getTypeJSG(instanceOfTree.getType());
    return new InstanceOfExprJSG(getLocation(instanceOfTree), getTypeJSG(instanceOfTree), expr, type);
  }

  @Override
  public Object visitIntersectionType(IntersectionTypeTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitLabeledStatement(LabeledStatementTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitLambdaExpression(LambdaExpressionTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitLiteral(LiteralTree litTree, Declaration context) {
    String value = litTree.getValue() == null ? "null" : litTree.getValue().toString();
    return new LitExprJSG(getLocation(litTree), getTypeJSG(litTree), value);
  }

  @Override
  public Object visitMemberReference(MemberReferenceTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitMemberSelect(MemberSelectTree tree, Declaration context) {
    String id = tree.getIdentifier().toString();
    
    Symbol symbol = (Symbol) TreeInfo.symbol((JCTree)tree.getExpression());
    
    ExpressionJSG exprJSG;
    if (symbol != null && symbol.getKind() == ElementKind.CLASS) {
      // static reference
      return new MemberSelectExprJSG(getLocation(tree), getTypeJSG(tree), getTypeJSG(tree.getExpression()), id);
    } else {
      // member reference
      exprJSG = (ExpressionJSG) tree.getExpression().accept(this, context);
      //String typeName = exprJSG.type.qualifiedName;
      if (exprJSG == null) {
        throw new ParseException(getLocation(tree), "Unexpected null!");
      }
      exprJSG.type = getTypeJSG(tree.getExpression());
      return new MemberSelectExprJSG(getLocation(tree), getTypeJSG(tree), exprJSG, id);
    }
    
  }

  @Override
  public Object visitMethodInvocation(MethodInvocationTree methodInvocTree, Declaration context) {
    ExpressionTree methodSelectTree = (ExpressionTree) methodInvocTree.getMethodSelect();
    SourceLocation loc = getLocation(methodInvocTree);
    String signature = getMethodSignature((JCTree)methodSelectTree);   
    
    MethodSymbol method = (MethodSymbol) TreeInfo.symbol((JCTree)methodSelectTree);
    Symbol methodOwner = method.owner;
    MethodDeclaration methodDecl = (MethodDeclaration) declFromSymbol(method, loc);
    MethodType mType = (MethodType)method.type;
    
    logger.finer("Visiting methods invocation: " + signature);
    
    List<ExpressionJSG> exprList= new ArrayList<>();
    for (ExpressionTree expr : methodInvocTree.getArguments()) {
      exprList.add((ExpressionJSG) expr.accept(this,  context));
    }
    
    boolean isStatic = method.getModifiers().contains(Modifier.STATIC);
    JavaiType jMethOwnerType = getTypeJSG(loc, methodOwner.type);
    JavaiType jMethType = getTypeJSG(loc, mType);
    
    String name;
    boolean isConstructor = false;
    MemberSelectExprJSG methodExpr;
    if (methodSelectTree instanceof IdentifierTree) {
      // a method in the current class or superclass
      name = ((IdentifierTree) methodSelectTree).getName().toString();
      if (isStatic)
        methodExpr = new MemberSelectExprJSG(loc, getTypeJSG(methodSelectTree), jMethOwnerType, jMethType, name);
      else {
        ExpressionJSG idExpr;
        Declaration decl = declFromSymbol(method, loc);
        if (name.equals("this") || name.equals("super")) {
          isConstructor = true;
          idExpr = null;
        } else if (inAnonymousOrNestedClass && belongsToClass((ClassSymbol)method.owner, outerClassContext)) {
          // We're in a nested or anonymous class, invoking a method in the outer class
          idExpr = new MemberSelectExprJSG(loc, getOuterTypeDecl().getJType(),
              new IDExprJSG(loc, getCurTypeDecl().getJType(), "this", decl),
              "outer");
        } else {
          idExpr = new IDExprJSG(getLocation(methodInvocTree), getTypeJSG(methodSelectTree), "this", decl);
        }
        
        methodExpr = new MemberSelectExprJSG(getLocation(methodInvocTree), getTypeJSG(methodSelectTree), idExpr, jMethType, name, isConstructor);
      }
    } else if (methodSelectTree instanceof MemberSelectTree) {      
      name = ((MemberSelectTree) methodSelectTree).getIdentifier().toString();
      // visit expression
      methodExpr = (MemberSelectExprJSG) methodSelectTree.accept(this, context);
      methodExpr.methType = jMethType;
    } else {
      throw new ParseException(getLocation(methodInvocTree), "Unhandled method selection node: " + methodSelectTree.getKind());
    }

    return new MethodInvokeExprJSG(getLocation(methodInvocTree), getTypeJSG(methodInvocTree), methodExpr, jMethOwnerType, exprList, methodDecl);
  }
  
  boolean belongsToClass(ClassSymbol test, ClassSymbol classSym) {
    Type needleType = test.type;
    Type haystackType = classSym.type;
    while (!needleType.equals(haystackType) && haystackType instanceof ClassType) {
      if (((ClassType) haystackType).all_interfaces_field.contains(needleType)) {
        return true;
      }
      haystackType = ((ClassType) haystackType).supertype_field;
    }
    
    return haystackType != null && needleType.equals(haystackType);
  }

  @Override
  public Object visitModifiers(ModifiersTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitNewArray(NewArrayTree newArrayTree, Declaration context) {
    JavaiType elType = getTypeJSG(newArrayTree).elementType;
    List<ExpressionJSG> dimens = new ArrayList<>();
    for (ExpressionTree dimen : newArrayTree.getDimensions()) {
      dimens.add((ExpressionJSG)dimen.accept(this, context));
    }
    List<ExpressionJSG> initExprs = new ArrayList<>();
    if (newArrayTree.getInitializers() != null) {    
      for (ExpressionTree initExpr : newArrayTree.getInitializers()) {
        initExprs.add((ExpressionJSG)initExpr.accept(this, context));
      }
    }
    return new NewArrayExprJSG(getLocation(newArrayTree), getTypeJSG(newArrayTree), elType, dimens, initExprs);
  }

  @Override
  public Object visitNewClass(NewClassTree newClassTree, Declaration context) {
    Symbol constructor = ((JCNewClass)newClassTree).constructor;
    SourceLocation loc = getLocation(newClassTree);
    MethodDeclaration constructorMethodDecl = (MethodDeclaration) declFromSymbol(constructor, loc);
    JavaiType jType; 
    if (newClassTree.getIdentifier() instanceof JCTree.JCIdent
        || newClassTree.getIdentifier() instanceof JCTree.JCFieldAccess
        ) {
      jType = getTypeJSG(newClassTree);      
    } else {
      jType = (JavaiType) newClassTree.getIdentifier().accept(this, context);
    }
    
    List<ExpressionJSG> args = newClassTree.getArguments().stream()
        .map(arg -> (ExpressionJSG) arg.accept(this, context)).collect(Collectors.toList());
    ClassTree body = newClassTree.getClassBody();
    NewClassExprJSG newClassExprJSG = new NewClassExprJSG(loc, jType, args, constructorMethodDecl);
    if (body != null) {      
      jType.name = newClassTree.getIdentifier().toString();
      if (jType.name.contains("<"))
        jType.name = jType.name.substring(0, jType.name.indexOf('<'));
      anonymousClassContext = currentMethod;
      TypeDeclJSG newTypeDecl = (TypeDeclJSG) body.accept(this, constructorMethodDecl);
      processAnonymousClass(newClassExprJSG, newTypeDecl, newClassTree, body, context);      
    } else {
      Symbol instantiatedClass = ((Symbol.ClassSymbol)constructor.owner);
      if (instantiatedClass.hasOuterInstance()) {
        // Must insert reference to outer class to parameter list
        VarDeclJSG param = new VarDeclJSG(loc, "outer", getCurTypeDecl().getJType());
        constructorMethodDecl.parmTypes.add(0, param.jType);
        IDExprJSG localRef = new IDExprJSG(loc, param.jType, "this", new VarDeclaration(loc, constructorMethodDecl, "this", param.jType, false));
        args.add(0, localRef);
      }
    }
    
    return newClassExprJSG;
  }
  
  // Postprocesses new anonymous class instantiation (both inside and outside a method) by adding constructor to anonymous class
  // and modifying new class expression to pass the required parameters
  public void processAnonymousClass(NewClassExprJSG newClassExprJSG, TypeDeclJSG typeDeclJSG, NewClassTree newClassTree, ClassTree classTree, Declaration context) {
    String className = newClassExprJSG.type.name;
    SourceLocation loc = getLocation(newClassTree);
//    inAnonymousOrNestedClass = true;
    ClassType type = (ClassType) getType(newClassTree);
    ++anonymousClassCounter;
    
    className += anonymousClassCounter;
    typeDeclJSG.simpleName = className;

//    if (type.supertype_field.toString().equals("java.lang.Object") && type.interfaces_field.size() == 1) {
//      Type itype = type.interfaces_field.get(0);
//      typeDeclJSG.extendsInterfaces.add(new JavaiType(itype.toString(), new ArrayList<>()));
//    } else {
//      typeDeclJSG.extendsClass = new JavaiType(type.supertype_field.toString(), new ArrayList<>());
//    }
    
    ClassDeclaration classDecl = new ClassDeclaration(getLocation(classTree), null, compileUnitJSG.packageName, className, null, false);
    anonymousClass = classDecl;

    TypeDeclJSG outerTypeDeclJSG = null;
    if (anonymousClassContext == null || !anonymousClassContext.isStatic()) {
      outerTypeDeclJSG = getCurTypeDecl(); 
    }
    newClassExprJSG.methodDecl = updateNestedConstructor(loc, newClassExprJSG, typeDeclJSG, classDecl,
        outerTypeDeclJSG);
    newClassExprJSG.type = new JavaiType(compileUnitJSG.packageName == null ? className : compileUnitJSG.packageName + "." + className, new ArrayList<>());
    anonymousClassContext = null;
  }

  // Update constructor for the nested/anonymous type to receive and initialize additional symbols...
  private MethodDeclaration updateNestedConstructor(SourceLocation loc, 
      NewClassExprJSG newClassExprJSG, TypeDeclJSG typeDeclJSG,
      ClassDeclaration classDecl, TypeDeclJSG outerTypeDeclJSG) {
 
    String className = typeDeclJSG.simpleName;
    MethodDeclJSG constrMethodDeclJSG = typeDeclJSG.methods.stream()
        .filter(methDeclJSG -> methDeclJSG.isConstructor()).findFirst().get();

    List<JavaiType> parmTypes = new ArrayList<>();
    
    JavaiType outerType = outerTypeDeclJSG != null ? outerTypeDeclJSG.getJType() : null;  
    if (outerType != null) {
      anonymousClassCaptures.put("outer", new VarDeclJSG(loc, "outer", outerType));
    }
    MethodDeclaration constrMethodDecl = new MethodDeclaration(loc, className, className, null, parmTypes, false);

    JavaiType thisType = typeDeclJSG.getJType();
    for (VarDeclJSG field : anonymousClassCaptures.values()) {
      typeDeclJSG.fields.add(field);
      VarDeclaration fieldDecl = new VarDeclaration(loc, classDecl, field.name, field.jType, false);
      VarDeclJSG param = new VarDeclJSG(loc, field.name, field.jType, null);
      constrMethodDeclJSG.parms.add(param);
      IDExprJSG localRef = new IDExprJSG(loc, param.jType, param.name, new VarDeclaration(loc, constrMethodDecl, param.name, param.jType, false)); 
      constrMethodDeclJSG.body.statements.add(new ExprStmtJSG(loc, 
          new AsmtExprJSG(loc, param.jType, 
              new MemberSelectExprJSG(loc, fieldDecl.type, new IDExprJSG(loc, thisType, "this", null), fieldDecl.id), 
              localRef)));
      parmTypes.add(param.jType);
      if (localRef.name.equals("outer")) {
        localRef = new IDExprJSG(loc, param.jType, "this", new VarDeclaration(loc, constrMethodDecl, "this", param.jType, false));
      }
      if (newClassExprJSG != null)
        newClassExprJSG.args.add(localRef);
    }
    return constrMethodDecl;
  }

  private void visitClassMembers(ClassTree classTree, TypeDeclJSG typeDeclJSG,
      ClassDeclaration classDecl) {
    for (Tree tree : classTree.getMembers()) {
      Object obj = tree.accept(this, classDecl);
      if (obj instanceof TypeMemberJSG) {
        TypeMemberJSG member = (TypeMemberJSG) obj;
        member.parent = typeDeclJSG;
        if (member instanceof MethodDeclJSG) {
          typeDeclJSG.methods.add((MethodDeclJSG)member);
        } else if (member instanceof VarDeclJSG) {
          typeDeclJSG.fields.add((VarDeclJSG)member);
        } else if (member instanceof TypeDeclJSG) {
          // ignore
        } else if (member != null) {
          throw new ParseException(getLocation(tree), String.format("Unrecognized member type in anonymous class"));
        }
      } else if (obj instanceof BlockStmtJSG) {
        MethodDeclJSG methodDeclJSG = new MethodDeclJSG(getLocation(tree), "init_", JavaiType.VOID, new ArrayList<>());
        methodDeclJSG.body = (BlockStmtJSG)obj;
        methodDeclJSG.isStatic = ((BlockTree)tree).isStatic();
        typeDeclJSG.methods.add(methodDeclJSG);
        methodDeclJSG.parent = typeDeclJSG;
      } else {
        
        logger.warning(getLocation(tree) + ": Unhandled class member type: " + obj);
      }
    }
  }

  @Override
  public Object visitOther(Tree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitParameterizedType(ParameterizedTypeTree tree, Declaration context) {
    JavaiType type = getTypeJSG(tree.getType());
    type.classTypeArgs = tree.getTypeArguments().stream().map(arg -> getTypeJSG(arg)).collect(Collectors.toList());
    
    return type;
  }

  @Override
  public Object visitParenthesized(ParenthesizedTree parenTree, Declaration context) {
    return parenTree.getExpression().accept(this,  context);
  }

  @Override
  public Object visitPrimitiveType(PrimitiveTypeTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitReturn(ReturnTree tree, Declaration context) {
    ExpressionJSG expr = null;
    if (tree.getExpression() != null) {
      expr = (ExpressionJSG)tree.getExpression().accept(this, context);
    }
    
    return new ReturnStmtJSG(getLocation(tree), expr);
  }

  @Override
  public Object visitSwitch(SwitchTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitSynchronized(SynchronizedTree tree, Declaration context) {
    // transform to block
    return doVisitStatementTree(tree.getBlock(), context);
  }

  @Override
  public Object visitThrow(ThrowTree throwTree, Declaration context) {
    ExpressionJSG throwExpr = (ExpressionJSG) throwTree.getExpression().accept(this, context);
    return new ThrowStmtJSG(getLocation(throwTree), throwExpr);
  }

  @Override
  public Object visitTry(TryTree tryTree, Declaration context) {
    BlockStmtJSG tryStmt = (BlockStmtJSG) tryTree.getBlock().accept(this, context);
    List<CatchJSG> catchList = new ArrayList<>();
    for (CatchTree catchTree : tryTree.getCatches()) {
      BlockStmtJSG catchStmt = (BlockStmtJSG)catchTree.getBlock().accept(this, context);
      VarDeclJSG varDeclJSG = (VarDeclJSG) catchTree.getParameter().accept(this, context);
      CatchJSG catchJSG = new CatchJSG(getLocation(tryTree), varDeclJSG, catchStmt);
      catchList.add(catchJSG);
    }
    return new TryStmtJSG(getLocation(tryTree), tryStmt, catchList);
  }

  @Override
  public Object visitTypeCast(TypeCastTree castTree, Declaration context) {
    ExpressionJSG expr = (ExpressionJSG) castTree.getExpression().accept(this, context);
    JavaiType type = getTypeJSG(castTree.getType());
    return new CastExprJSG(getLocation(castTree), type, expr);
  }

  @Override
  public Object visitTypeParameter(TypeParameterTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public ExpressionJSG visitUnary(UnaryTree unTree, Declaration context) {
    ExpressionJSG expr = (ExpressionJSG)unTree.getExpression().accept(this, context);
    Kind kind = unTree.getKind();
    ExpressionJSG.OperatorKind op;
    switch (kind) {
    case UNARY_MINUS:
      op = ExpressionJSG.OperatorKind.MINUS; break;
    case UNARY_PLUS:
      op = ExpressionJSG.OperatorKind.PLUS; break;
    case LOGICAL_COMPLEMENT:
      op = ExpressionJSG.OperatorKind.NOT; break;
    case BITWISE_COMPLEMENT:
      op = ExpressionJSG.OperatorKind.BITNOT; break;
      
    default:
      throw new ParseException(getLocation(unTree), "Java feature not implemented:" + kind);
      
    }
    return new UnaryExprJSG(getLocation(unTree), getTypeJSG(unTree), op, expr);
  }

  @Override
  public Object visitUnionType(UnionTypeTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }

  @Override
  public Object visitWhileLoop(WhileLoopTree whileTree, Declaration context) {
    ExpressionJSG exprJSG = (ExpressionJSG) whileTree.getCondition().accept(this, context);
    StatementJSG stmtJSG = doVisitStatementTree(whileTree.getStatement(), context);
    
    return new WhileStmtJSG(getLocation(whileTree), exprJSG, stmtJSG);
  }

  @Override
  public Object visitWildcard(WildcardTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }
 

  public SourceLocation getLocation(Tree tree) {
    long position = typetrees.getSourcePositions().getStartPosition(ast, tree);
    long lineNumber = ast.getLineMap().getLineNumber(position);
    long column = ast.getLineMap().getColumnNumber(position);
    
    return new SourceLocation(filename, lineNumber, column);
  }
  
  TypeDeclJSG getCurTypeDecl() {
    return curTypeDeclStack.peek();
  }

  TypeDeclJSG getOuterTypeDecl() {
    return curTypeDeclStack.get(curTypeDeclStack.size() - 2);
  }

}
