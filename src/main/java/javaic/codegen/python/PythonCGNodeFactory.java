package javaic.codegen.python;

import javaic.codegen.CGNodeFactory;
import javaic.codegen.CodeGenException;
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
import javaic.codegen.python.model.PyArrayIndexExprCG;
import javaic.codegen.python.model.PyAsmtExprCG;
import javaic.codegen.python.model.PyBinExprCG;
import javaic.codegen.python.model.PyBlockStmtCG;
import javaic.codegen.python.model.PyBreakStmtCG;
import javaic.codegen.python.model.PyCatchCG;
import javaic.codegen.python.model.PyCompileUnitCG;
import javaic.codegen.python.model.PyCondExprCG;
import javaic.codegen.python.model.PyContinueStmtCG;
import javaic.codegen.python.model.PyIDExprCG;
import javaic.codegen.python.model.PyIfStmtCG;
import javaic.codegen.python.model.PyLitExprCG;
import javaic.codegen.python.model.PyMemberSelectExprCG;
import javaic.codegen.python.model.PyMethodDeclCG;
import javaic.codegen.python.model.PyMethodInvokeExprCG;
import javaic.codegen.python.model.PyNewArrayExprCG;
import javaic.codegen.python.model.PyReturnStmtCG;
import javaic.codegen.python.model.PyThrowStmtCG;
import javaic.codegen.python.model.PyTryStmtCG;
import javaic.codegen.python.model.PyTypeDeclCG;
import javaic.codegen.python.model.PyUnaryExprCG;
import javaic.codegen.python.model.PyVarDeclCG;
import javaic.codegen.python.model.PyVarDeclStmtCG;
import javaic.codegen.python.model.PyWhileStmtCG;
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

public class PythonCGNodeFactory extends CGNodeFactory {

  @Override
  public CompileUnitCG createCompileUnitCG(CompileUnitJSG compileUnitJSG) {
    return new PyCompileUnitCG(compileUnitJSG);
  }

  @Override
  public MethodDeclCG createMethodDeclCG(MethodDeclJSG methodDeclJSG) {
    return new PyMethodDeclCG(methodDeclJSG);
  }

  @Override
  public TypeDeclCG createTypeDeclCG(TypeDeclJSG typeDeclJSG) {
    return new PyTypeDeclCG(typeDeclJSG);
  }

  @Override
  public VarDeclCG createVarDeclCG(VarDeclJSG varDeclJSG) {
    return new PyVarDeclCG(varDeclJSG);
  }

  @Override
  public IDExprCG createIDExprCG(IDExprJSG idExprJSG) {
    return new PyIDExprCG(idExprJSG);
  }

  @Override
  public AsmtExprCG createAsmtExprCG(AsmtExprJSG asmtExprJSG) {
    return new PyAsmtExprCG(asmtExprJSG);
  }

  @Override
  public LitExprCG createLitExprCG(LitExprJSG litExprJSG) {
    return new PyLitExprCG(litExprJSG);
  }

  @Override
  public BlockStmtCG createBlockStmtCG(BlockStmtJSG blockStmtJSG) {
    return new PyBlockStmtCG(blockStmtJSG);
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
    return new PyArrayIndexExprCG(arrayIndexExprJSG);
  }

  @Override
  public MemberSelectExprCG createMemberSelectExprCG(
      MemberSelectExprJSG memberSelectExprJSG) {
    return new PyMemberSelectExprCG(memberSelectExprJSG);
  }

  @Override
  public ReturnStmtCG createReturnStmtCG(ReturnStmtJSG returnStmtJSG) {
    return new PyReturnStmtCG(returnStmtJSG);
  }

  @Override
  public VarDeclStmtCG createVarDeclStmtCG(VarDeclStmtJSG varDeclStmtJSG) {
    return new PyVarDeclStmtCG(varDeclStmtJSG);
  }

  @Override
  public MethodInvokeExprCG createMethodInvokeExprCG(
      MethodInvokeExprJSG methodInvokeExprJSG) {
    return new PyMethodInvokeExprCG(methodInvokeExprJSG);
  }

  @Override
  public NewClassExprCG createNewClassExprCG(NewClassExprJSG newClassExprJSG) {
    throw new CodeGenException(newClassExprJSG, "Should not get here...");
  }

  @Override
  public BinExprCG createBinExprCG(BinExprJSG binExprJSG) {
    return new PyBinExprCG(binExprJSG);
  }

  @Override
  public UnaryExprCG createUnaryExprCG(UnaryExprJSG unaryExprJSG) {
    return new PyUnaryExprCG(unaryExprJSG);
  }

  @Override
  public NewArrayExprCG createNewArrayExprCG(NewArrayExprJSG newArrayExprJSG) {
    return new PyNewArrayExprCG(newArrayExprJSG);
  }

  @Override
  public IfStmtCG createIfStmtCG(IfStmtJSG ifStmtJSG) {
    return new PyIfStmtCG(ifStmtJSG);
  }

  @Override
  public ThrowStmtCG createThrowStmtCG(ThrowStmtJSG throwStmtJSG) {
    return new PyThrowStmtCG(throwStmtJSG);
  }

  @Override
  public TryStmtCG createTryStmtCG(TryStmtJSG tryStmtJSG) {
    return new PyTryStmtCG(tryStmtJSG);
  }

  @Override
  public CatchCG createCatchCG(CatchJSG catchJSG) {
    return new PyCatchCG(catchJSG);
  }

  @Override
  public WhileStmtCG createWhileStmtCG(WhileStmtJSG whileStmtJSG) {
    return new PyWhileStmtCG(whileStmtJSG);
  }

  @Override
  public InstanceOfExprCG createInstanceOfExprCG(
      InstanceOfExprJSG instanceOfExprJSG) {
    throw new CodeGenException(instanceOfExprJSG, "InstanceOf expressions are handled by PythonCGTreeBuilder...");
  }

  @Override
  public CastExprCG createCastExprCG(CastExprJSG castExprJSG) {
    throw new CodeGenException(castExprJSG, "Cast expressions should be replaced with functions...");
  }

  @Override
  public BreakStmtCG createBreakStmtCG(BreakStmtJSG breakStmtJSG) {
    return new PyBreakStmtCG(breakStmtJSG);
  }

  @Override
  public ContinueStmtCG createContinueStmtCG(ContinueStmtJSG continueStmtJSG) {
    return new PyContinueStmtCG(continueStmtJSG);
  }

  @Override
  public CondExprCG createCondExprCG(CondExprJSG condExprJSG) {
    return new PyCondExprCG(condExprJSG);
  }

}
