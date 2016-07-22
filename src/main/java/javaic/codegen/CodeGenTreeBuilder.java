package javaic.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javaic.LogFactory;
import javaic.codegen.model.ArrayIndexExprCG;
import javaic.codegen.model.AsmtExprCG;
import javaic.codegen.model.BinExprCG;
import javaic.codegen.model.BlockStmtCG;
import javaic.codegen.model.BreakStmtCG;
import javaic.codegen.model.CastExprCG;
import javaic.codegen.model.CatchCG;
import javaic.codegen.model.CompileUnitCG;
import javaic.codegen.model.CondExprCG;
import javaic.codegen.model.ContinueStmtCG;
import javaic.codegen.model.ExprStmtCG;
import javaic.codegen.model.ExpressionCG;
import javaic.codegen.model.IDExprCG;
import javaic.codegen.model.IfStmtCG;
import javaic.codegen.model.InstanceOfExprCG;
import javaic.codegen.model.LitExprCG;
import javaic.codegen.model.MemberSelectExprCG;
import javaic.codegen.model.MethodDeclCG;
import javaic.codegen.model.MethodInvokeExprCG;
import javaic.codegen.model.NewArrayExprCG;
import javaic.codegen.model.NewClassExprCG;
import javaic.codegen.model.ReturnStmtCG;
import javaic.codegen.model.StatementCG;
import javaic.codegen.model.ThrowStmtCG;
import javaic.codegen.model.TryStmtCG;
import javaic.codegen.model.TypeDeclCG;
import javaic.codegen.model.UnaryExprCG;
import javaic.codegen.model.VarDeclCG;
import javaic.codegen.model.VarDeclStmtCG;
import javaic.codegen.model.WhileStmtCG;
import javaic.parsetree.ArrayIndexExprJSG;
import javaic.parsetree.AsmtExprJSG;
import javaic.parsetree.BinExprJSG;
import javaic.parsetree.BlockStmtJSG;
import javaic.parsetree.BreakStmtJSG;
import javaic.parsetree.CastExprJSG;
import javaic.parsetree.CatchJSG;
import javaic.parsetree.CompileUnitJSG;
import javaic.parsetree.CondExprJSG;
import javaic.parsetree.ContinueStmtJSG;
import javaic.parsetree.EmptyStatementJSG;
import javaic.parsetree.ExprStmtJSG;
import javaic.parsetree.ExpressionJSG;
import javaic.parsetree.IDExprJSG;
import javaic.parsetree.IfStmtJSG;
import javaic.parsetree.InstanceOfExprJSG;
import javaic.parsetree.JavaiVisitor;
import javaic.parsetree.LitExprJSG;
import javaic.parsetree.MemberSelectExprJSG;
import javaic.parsetree.MethodDeclJSG;
import javaic.parsetree.MethodInvokeExprJSG;
import javaic.parsetree.NewArrayExprJSG;
import javaic.parsetree.NewClassExprJSG;
import javaic.parsetree.ReturnStmtJSG;
import javaic.parsetree.StatementJSG;
import javaic.parsetree.ThrowStmtJSG;
import javaic.parsetree.TryStmtJSG;
import javaic.parsetree.TypeDeclJSG;
import javaic.parsetree.UnaryExprJSG;
import javaic.parsetree.VarDeclJSG;
import javaic.parsetree.VarDeclStmtJSG;
import javaic.parsetree.WhileStmtJSG;

public class CodeGenTreeBuilder extends JavaiVisitor<Object> {
  
  Logger log = LogFactory.getLogger(getClass());
  
  final CGNodeFactory cgNodeFactory;

  public CodeGenTreeBuilder(CGNodeFactory cgNodeFactory) {
    this.cgNodeFactory = cgNodeFactory;
  }

  @Override
  public CompileUnitCG visitCompileUnit(CompileUnitJSG compileUnitJSG) {
    List<TypeDeclCG> jtdCGList = new ArrayList<>();
    for (TypeDeclJSG typeDecl : compileUnitJSG.decls) {
      TypeDeclCG jtdCG = visitTypeDecl(typeDecl);
      jtdCGList.add(jtdCG);      
    }
    
    CompileUnitCG jcuCG = cgNodeFactory.createCompileUnitCG(compileUnitJSG);
    jcuCG.setTypeDecls(jtdCGList);
    return jcuCG;
  }

  @Override
  public TypeDeclCG visitTypeDecl(TypeDeclJSG typeDeclJSG) {
    List<VarDeclCG> fieldList = new ArrayList<>();
    for (VarDeclJSG field : typeDeclJSG.fields) {
      VarDeclCG jvdCG = visitVarDecl(field);
      fieldList.add(jvdCG);
    }
    
    List<MethodDeclCG> methodList = new ArrayList<>();
    for (MethodDeclJSG method : typeDeclJSG.methods) {
      MethodDeclCG jmdCG = visitMethodDecl(method);
      if (jmdCG != null)
        methodList.add(jmdCG);
    }
    
    TypeDeclCG typeDeclCG = cgNodeFactory.createTypeDeclCG(typeDeclJSG);
    typeDeclCG.setFields(fieldList);
    typeDeclCG.setMethods(methodList);
    
    return typeDeclCG;
  }

  @Override
  public VarDeclCG visitVarDecl(VarDeclJSG varDeclJSG) {
    VarDeclCG varDeclCG = cgNodeFactory.createVarDeclCG(varDeclJSG);
    
    if (varDeclJSG.expr != null) {
      varDeclCG.setInitExpr((ExpressionCG) varDeclJSG.expr.accept(this));
    }
    return varDeclCG;
  }
  
  @Override
  public MethodDeclCG visitMethodDecl(MethodDeclJSG methodDeclJSG) {
    if (methodDeclJSG.returnType != null && methodDeclJSG.returnType.qualifiedName != null && 
        methodDeclJSG.returnType.qualifiedName.equals("junit.framework.Test"))
      // eliminate methods that return JUnit tests... we are handling all JUnit test methods
      // by explicit check
      return null;
    
    
    MethodDeclCG methodDeclCG = cgNodeFactory.createMethodDeclCG(methodDeclJSG);
    
//    // TODO: Need better test for test cases 
//    if (methodDeclCG.isConstructor() && methodDeclCG.name.endsWith("Test"))
//      // Eliminate test case constructors
//      return null;
        
    List<VarDeclCG> parms = new ArrayList<>();
    for (VarDeclJSG parm : methodDeclJSG.parms) {
      VarDeclCG vdCG = visitVarDecl(parm);
      parms.add(vdCG);
    }
        
    methodDeclCG.setParms(parms);
    
    if (methodDeclJSG.body != null)
      methodDeclCG.setBody(visitBlockStmt(methodDeclJSG.body));
    
    return methodDeclCG;
  }


  @Override
  public BlockStmtCG visitBlockStmt(BlockStmtJSG blockStmtJSG) {
    BlockStmtCG blockStmtCG = cgNodeFactory.createBlockStmtCG(blockStmtJSG);
    List<StatementCG> statements = blockStmtCG.getStatements();
    for (StatementJSG stmt : blockStmtJSG.statements) {
      StatementCG stmtCG = (StatementCG) stmt.accept(this);
      if (stmtCG != null) {
          statements.add(stmtCG);
      }
    }
       
    return blockStmtCG;
  }

  @Override
  public Object visitEmptyStmt(EmptyStatementJSG emptyStatementJSG) {
    return cgNodeFactory.createEmptyStmtCG(emptyStatementJSG);
  }

  @Override
  public ExprStmtCG visitExprStmt(ExprStmtJSG exprStmtJSG) {
    ExprStmtCG exprStmtCG = cgNodeFactory.createExprStmtCG(exprStmtJSG);
    exprStmtCG.exprCG = convertExpr(exprStmtJSG.exprJSG);
    return exprStmtCG;
  }

  @Override
  public Object visitBreakStmt(BreakStmtJSG breakStmtJSG) {
    BreakStmtCG breakStmtCG = cgNodeFactory.createBreakStmtCG(breakStmtJSG);
    return breakStmtCG;
  }

  @Override
  public Object visitContinueStmt(ContinueStmtJSG continueStmtJSG) {
    ContinueStmtCG continueStmtCG = cgNodeFactory.createContinueStmtCG(continueStmtJSG);
    return continueStmtCG;
  }

  @Override
  public IfStmtCG visitIfStmt(IfStmtJSG ifStmtJSG) {
    IfStmtCG ifStmtCG = cgNodeFactory.createIfStmtCG(ifStmtJSG);
    ifStmtCG.condExpr = convertExpr(ifStmtJSG.condExpr);
    ifStmtCG.thenStmt = convertStmt(ifStmtJSG.thenStmt);
    ifStmtCG.elseStmt = convertStmt(ifStmtJSG.elseStmt);
    return ifStmtCG;
  }

  @Override
  public ReturnStmtCG visitReturnStmt(ReturnStmtJSG returnStmtJSG) {
    ReturnStmtCG returnStmtCG = cgNodeFactory.createReturnStmtCG(returnStmtJSG);
    returnStmtCG.returnExprCG = convertExpr(returnStmtJSG.returnExpr);
    return returnStmtCG;
  }

  @Override
  public ThrowStmtCG visitThrowStmt(ThrowStmtJSG throwStmtJSG) {
    ThrowStmtCG throwStmtCG = cgNodeFactory.createThrowStmtCG(throwStmtJSG);
    throwStmtCG.throwExpr = convertExpr(throwStmtJSG.throwExpr);
    return throwStmtCG;
  }

  @Override
  public TryStmtCG visitTryStmt(TryStmtJSG tryStmtJSG) {
    TryStmtCG tryStmtCG = cgNodeFactory.createTryStmtCG(tryStmtJSG);
    tryStmtCG.tryStmt = (BlockStmtCG) tryStmtJSG.tryStmt.accept(this);
    tryStmtCG.catchList = new ArrayList<>();
    for (CatchJSG catchJSG : tryStmtJSG.catchList) {
      CatchCG catchCG = cgNodeFactory.createCatchCG(catchJSG);
      catchCG.catchBlock = (BlockStmtCG) catchJSG.catchBlock.accept(this);
      catchCG.catchParm = cgNodeFactory.createVarDeclCG(catchJSG.catchParm);
      
      tryStmtCG.catchList.add(catchCG);
    }
      
    return tryStmtCG;
  }

  @Override
  public VarDeclStmtCG visitVarDeclStmt(VarDeclStmtJSG varDeclStmtJSG) {
    VarDeclStmtCG varDeclStmtCG = cgNodeFactory.createVarDeclStmtCG(varDeclStmtJSG);
    varDeclStmtCG.varDeclCG = cgNodeFactory.createVarDeclCG(varDeclStmtJSG.varDecl);
    varDeclStmtCG.varDeclCG.setInitExpr(convertExpr(varDeclStmtJSG.varDecl.expr));
    return varDeclStmtCG;
  }

  @Override
  public WhileStmtCG visitWhileStmt(WhileStmtJSG whileStmtJSG) {
    WhileStmtCG whileStmtCG = cgNodeFactory.createWhileStmtCG(whileStmtJSG);
    whileStmtCG.condExpr = convertExpr(whileStmtJSG.condExpr);
    whileStmtCG.stmt = convertStmt(whileStmtJSG.stmt);
    return whileStmtCG;
  }
  
  @Override
  public AsmtExprCG visitAsmtExpr(AsmtExprJSG asmtExpr) {
    AsmtExprCG asmtExprCG = cgNodeFactory.createAsmtExprCG(asmtExpr);
    asmtExprCG.lhsCG = convertExpr(asmtExpr.lhs);
    asmtExprCG.exprCG = convertExpr(asmtExpr.exprJSG);
    return asmtExprCG;
  }

  @Override
  public IDExprCG visitIDExpr(IDExprJSG idExprJSG) {
    IDExprCG idExprCG = cgNodeFactory.createIDExprCG(idExprJSG);
    return idExprCG;
  }

  @Override
  public LitExprCG visitLitExpr(LitExprJSG litExprJSG) {
    LitExprCG litExprCG = cgNodeFactory.createLitExprCG(litExprJSG);
    return litExprCG;
  }

  @Override
  public ArrayIndexExprCG visitArrayIndexExpr(ArrayIndexExprJSG arrayIndexExprJSG) {
    ArrayIndexExprCG arrayIndexExprCG = cgNodeFactory.createArrayIndexExprCG(arrayIndexExprJSG);
    arrayIndexExprCG.arrayExprCG = convertExpr(arrayIndexExprJSG.arrayExprJSG);
    arrayIndexExprCG.indexExprCG = convertExpr(arrayIndexExprJSG.indexExprJSG);
    return arrayIndexExprCG;
  }

  @Override
  public Object visitBinExpr(BinExprJSG binExprJSG) {
    BinExprCG binExprCG = cgNodeFactory.createBinExprCG(binExprJSG);
    binExprCG.left = convertExpr(binExprJSG.left);
    binExprCG.right = convertExpr(binExprJSG.right);
    return binExprCG;
  }

  @Override
  public Object visitCastExpr(CastExprJSG castExprJSG) {
    CastExprCG castExprCG = cgNodeFactory.createCastExprCG(castExprJSG);
    castExprCG.castExpr = convertExpr(castExprJSG.castExpr);
    
    return castExprCG;
  }

  @Override
  public Object visitInstanceOfExpr(InstanceOfExprJSG instanceOfExprJSG) {
    InstanceOfExprCG instanceOfExprCG = cgNodeFactory.createInstanceOfExprCG(instanceOfExprJSG);
    instanceOfExprCG.expr = convertExpr(instanceOfExprJSG.expr);
    
    return instanceOfExprCG;
  }

  @Override
  public ExpressionCG visitMemberSelectExpr(MemberSelectExprJSG memberSelectExprJSG) {
    MemberSelectExprCG memberSelectExprCG = cgNodeFactory.createMemberSelectExprCG(memberSelectExprJSG);
    memberSelectExprCG.expr = convertExpr(memberSelectExprJSG.expr);
    return memberSelectExprCG;
  }

  @Override
  public Object visitMethodInvokeExpr(MethodInvokeExprJSG methodInvokeExprJSG) {
    MethodInvokeExprCG methodInvokeExprCG = cgNodeFactory.createMethodInvokeExprCG(methodInvokeExprJSG);
    methodInvokeExprCG.methodExprCG = (MemberSelectExprCG) methodInvokeExprJSG.methodExpr.accept(this);
    methodInvokeExprCG.args = convertExprList(methodInvokeExprJSG.args);
    return methodInvokeExprCG;
  }

  @Override
  public Object visitNewArrayExpr(NewArrayExprJSG newArrayExprJSG) {
    NewArrayExprCG newArrayExprCG = cgNodeFactory.createNewArrayExprCG(newArrayExprJSG);
    newArrayExprCG.dimensExpr = convertExprList(newArrayExprJSG.dimensExpr);
    newArrayExprCG.initExpr = convertExprList(newArrayExprJSG.initExpr);

    return newArrayExprCG;
  }

  public List<ExpressionCG> convertExprList(List<ExpressionJSG> exprList) {
    if (exprList != null)
      return exprList.stream()
          .map(expr -> (ExpressionCG) expr.accept(this))
          .collect(Collectors.toList());
    else
      return null;
  }
  
  public StatementCG convertStmt(StatementJSG stmtJSG) {
    if (stmtJSG != null)
      return (StatementCG) stmtJSG.accept(this);
    else
      return null;
  }
  
  public ExpressionCG convertExpr(ExpressionJSG exprJSG) {
    if (exprJSG != null)
      return (ExpressionCG) exprJSG.accept(this);
    else
      return null;
  }

  @Override
  public Object visitNewClassExpr(NewClassExprJSG newClassExprJSG) {
    NewClassExprCG newClassExprCG = cgNodeFactory.createNewClassExprCG(newClassExprJSG);
    newClassExprCG.args = convertExprList(newClassExprJSG.args);
    return newClassExprCG;
  }

  @Override
  public Object visitUnaryExpr(UnaryExprJSG unaryExprJSG) {
    UnaryExprCG unaryExprCG = cgNodeFactory.createUnaryExprCG(unaryExprJSG);
    unaryExprCG.expr = convertExpr(unaryExprJSG.expr);
    return unaryExprCG;
  }

  @Override
  public Object visitCondExpr(CondExprJSG condExprJSG) {
    CondExprCG condExprCG = cgNodeFactory.createCondExprCG(condExprJSG);
    condExprCG.condExpr = convertExpr(condExprJSG.condExpr);
    condExprCG.trueExpr = convertExpr(condExprJSG.trueExpr);
    condExprCG.falseExpr = convertExpr(condExprJSG.falseExpr);
    return condExprCG;
  }
  
  
}
