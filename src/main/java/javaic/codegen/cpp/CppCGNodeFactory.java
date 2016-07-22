package javaic.codegen.cpp;

import javaic.codegen.CGNodeFactory;
import javaic.codegen.CodeGenException;
import javaic.codegen.cpp.model.CppArrayIndexExprCG;
import javaic.codegen.cpp.model.CppAsmtExprCG;
import javaic.codegen.cpp.model.CppBinExprCG;
import javaic.codegen.cpp.model.CppBlockStmtCG;
import javaic.codegen.cpp.model.CppBreakStmtCG;
import javaic.codegen.cpp.model.CppCastExprCG;
import javaic.codegen.cpp.model.CppCatchCG;
import javaic.codegen.cpp.model.CppCompileUnitCG;
import javaic.codegen.cpp.model.CppCondExprCG;
import javaic.codegen.cpp.model.CppContinueStmtCG;
import javaic.codegen.cpp.model.CppIDExprCG;
import javaic.codegen.cpp.model.CppIfStmtCG;
import javaic.codegen.cpp.model.CppLitExprCG;
import javaic.codegen.cpp.model.CppMemberSelectExprCG;
import javaic.codegen.cpp.model.CppMethodDeclCG;
import javaic.codegen.cpp.model.CppMethodInvokeExprCG;
import javaic.codegen.cpp.model.CppNewArrayExprCG;
import javaic.codegen.cpp.model.CppNewClassExprCG;
import javaic.codegen.cpp.model.CppReturnStmtCG;
import javaic.codegen.cpp.model.CppThrowStmtCG;
import javaic.codegen.cpp.model.CppTryStmtCG;
import javaic.codegen.cpp.model.CppTypeDeclCG;
import javaic.codegen.cpp.model.CppUnaryExprCG;
import javaic.codegen.cpp.model.CppVarDeclCG;
import javaic.codegen.cpp.model.CppVarDeclStmtCG;
import javaic.codegen.cpp.model.CppWhileStmtCG;
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
import javaic.codegen.model.EmptyStmtCG;
import javaic.codegen.model.ExprStmtCG;
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
import javaic.parsetree.IDExprJSG;
import javaic.parsetree.IfStmtJSG;
import javaic.parsetree.InstanceOfExprJSG;
import javaic.parsetree.LitExprJSG;
import javaic.parsetree.MemberSelectExprJSG;
import javaic.parsetree.MethodDeclJSG;
import javaic.parsetree.MethodInvokeExprJSG;
import javaic.parsetree.NewArrayExprJSG;
import javaic.parsetree.NewClassExprJSG;
import javaic.parsetree.ReturnStmtJSG;
import javaic.parsetree.ThrowStmtJSG;
import javaic.parsetree.TryStmtJSG;
import javaic.parsetree.TypeDeclJSG;
import javaic.parsetree.UnaryExprJSG;
import javaic.parsetree.VarDeclJSG;
import javaic.parsetree.VarDeclStmtJSG;
import javaic.parsetree.WhileStmtJSG;

public class CppCGNodeFactory extends CGNodeFactory {

  @Override
  public CompileUnitCG createCompileUnitCG(CompileUnitJSG compileUnitJSG) {
    return new CppCompileUnitCG(compileUnitJSG);
  }

  @Override
  public MethodDeclCG createMethodDeclCG(MethodDeclJSG methodDeclJSG) {
    return new CppMethodDeclCG(methodDeclJSG);
  }

  @Override
  public TypeDeclCG createTypeDeclCG(TypeDeclJSG typeDeclJSG) {
    return new CppTypeDeclCG(typeDeclJSG);
  }

  @Override
  public VarDeclCG createVarDeclCG(VarDeclJSG varDeclJSG) {
    return new CppVarDeclCG(varDeclJSG);
  }

  @Override
  public IDExprCG createIDExprCG(IDExprJSG idExprJSG) {
    return new CppIDExprCG(idExprJSG);
  }

  @Override
  public AsmtExprCG createAsmtExprCG(AsmtExprJSG asmtExprJSG) {
    return new CppAsmtExprCG(asmtExprJSG);
  }

  @Override
  public LitExprCG createLitExprCG(LitExprJSG litExprJSG) {
    return new CppLitExprCG(litExprJSG);
  }

  @Override
  public BlockStmtCG createBlockStmtCG(BlockStmtJSG blockStmtJSG) {
    return new CppBlockStmtCG(blockStmtJSG);
  }

  @Override
  public EmptyStmtCG createEmptyStmtCG(EmptyStatementJSG emptyStatementJSG) {
    return new EmptyStmtCG(emptyStatementJSG);
  }

  @Override
  public ExprStmtCG createExprStmtCG(ExprStmtJSG exprStmtJSG) {
    return new ExprStmtCG(exprStmtJSG);
  }

  @Override
  public ArrayIndexExprCG createArrayIndexExprCG(
      ArrayIndexExprJSG arrayIndexExprJSG) {
    return new CppArrayIndexExprCG(arrayIndexExprJSG);
  }

  @Override
  public MemberSelectExprCG createMemberSelectExprCG(
      MemberSelectExprJSG memberSelectExprJSG) {
    return new CppMemberSelectExprCG(memberSelectExprJSG);
  }

  @Override
  public ReturnStmtCG createReturnStmtCG(ReturnStmtJSG returnStmtJSG) {
    return new CppReturnStmtCG(returnStmtJSG);
  }

  @Override
  public VarDeclStmtCG createVarDeclStmtCG(VarDeclStmtJSG varDeclStmtJSG) {
    return new CppVarDeclStmtCG(varDeclStmtJSG);
  }

  @Override
  public MethodInvokeExprCG createMethodInvokeExprCG(
      MethodInvokeExprJSG methodInvokeExprJSG) {
    return new CppMethodInvokeExprCG(methodInvokeExprJSG);
  }

  @Override
  public NewClassExprCG createNewClassExprCG(NewClassExprJSG newClassExprJSG) {
    return new CppNewClassExprCG(newClassExprJSG);
  }

  @Override
  public BinExprCG createBinExprCG(BinExprJSG binExprJSG) {
    return new CppBinExprCG(binExprJSG);
  }

  @Override
  public UnaryExprCG createUnaryExprCG(UnaryExprJSG unaryExprJSG) {
    return new CppUnaryExprCG(unaryExprJSG);
  }

  @Override
  public NewArrayExprCG createNewArrayExprCG(NewArrayExprJSG newArrayExprJSG) {
    return new CppNewArrayExprCG(newArrayExprJSG);
  }

  @Override
  public IfStmtCG createIfStmtCG(IfStmtJSG ifStmtJSG) {
    return new CppIfStmtCG(ifStmtJSG);
  }

  @Override
  public ThrowStmtCG createThrowStmtCG(ThrowStmtJSG throwStmtJSG) {
    return new CppThrowStmtCG(throwStmtJSG);
  }

  @Override
  public TryStmtCG createTryStmtCG(TryStmtJSG tryStmtJSG) {
    return new CppTryStmtCG(tryStmtJSG);
  }

  @Override
  public CatchCG createCatchCG(CatchJSG catchJSG) {
    return new CppCatchCG(catchJSG);
  }

  @Override
  public WhileStmtCG createWhileStmtCG(WhileStmtJSG whileStmtJSG) {
    return new CppWhileStmtCG(whileStmtJSG);
  }

  @Override
  public InstanceOfExprCG createInstanceOfExprCG(
      InstanceOfExprJSG instanceOfExprJSG) {
    throw new CodeGenException(instanceOfExprJSG, "InstanceOf expressions should be Eliminated...");
  }

  @Override
  public CastExprCG createCastExprCG(CastExprJSG castExprJSG) {
    return new CppCastExprCG(castExprJSG);
  }

  @Override
  public BreakStmtCG createBreakStmtCG(BreakStmtJSG breakStmtJSG) {
    return new CppBreakStmtCG(breakStmtJSG);
  }

  @Override
  public ContinueStmtCG createContinueStmtCG(ContinueStmtJSG continueStmtJSG) {
    return new CppContinueStmtCG(continueStmtJSG);
  }

  @Override
  public CondExprCG createCondExprCG(CondExprJSG condExprJSG) {
    return new CppCondExprCG(condExprJSG);
  }

}
