package javaic.parsetree;

import java.util.ArrayList;

import javaic.parsetree.ExpressionJSG.OperatorKind;
import javaic.semantics.Declaration;
import javaic.semantics.JavaiType;
import javaic.semantics.MethodDeclaration;
import javaic.semantics.VarDeclaration;

import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.UnaryTree;

/**
 * To do:
 * - assignment shortcuts
 * - pre/post increment/decrement operators
 * - loops
 * - nested classes
 * 
 * @author SchaubS
 *
 */

public class JavaToJavaiVisitor extends JavaiParseTreeBuilder {

  @Override
  public Object visitSwitch(SwitchTree tree, Declaration context) {
    throw new ParseException(getLocation(tree), "Java feature not implemented.");
  }
  
  int firstTimeLabel = 0;
  
  @Override
  public BlockStmtJSG visitDoWhileLoop(DoWhileLoopTree doWhileLoopTree, Declaration context) {
    SourceLocation loc = getLocation(doWhileLoopTree);
    
    StatementJSG doLoopBody = doVisitStatementTree(doWhileLoopTree.getStatement(), context);
    BlockStmtJSG whileBody;
    if (doLoopBody instanceof BlockStmtJSG) {
      whileBody = (BlockStmtJSG)doLoopBody;
    } else {
      whileBody = new BlockStmtJSG(loc);
      whileBody.statements.add(doLoopBody);
      doLoopBody = whileBody;
    }
    String firstTimeName = "firstTime" + firstTimeLabel;
    ++firstTimeLabel;
    VarDeclaration firstTimeDecl = new VarDeclaration(loc, context, firstTimeName, JavaiType.BOOLEAN, false);
    IDExprJSG idExpr = new IDExprJSG(loc, JavaiType.BOOLEAN, firstTimeName, firstTimeDecl);
    whileBody.statements.add(0, new ExprStmtJSG(loc, 
        new AsmtExprJSG(loc, JavaiType.BOOLEAN, idExpr, 
            new LitExprJSG(loc,  JavaiType.BOOLEAN, "false"))));
    ExpressionJSG condExpr = (ExpressionJSG)doWhileLoopTree.getCondition().accept(this, context);
    condExpr = new BinExprJSG(loc, JavaiType.BOOLEAN, OperatorKind.OR, idExpr, condExpr, false);
    
    BlockStmtJSG newBlockJSG = new BlockStmtJSG(loc);
    newBlockJSG.statements.add(new VarDeclStmtJSG(loc, 
        new VarDeclJSG(loc, firstTimeName, JavaiType.BOOLEAN, 
            new LitExprJSG(loc, JavaiType.BOOLEAN, "true"))));
    newBlockJSG.statements.add(new WhileStmtJSG(loc, condExpr, whileBody));
    return newBlockJSG;
  }
  

  @Override
  public BlockStmtJSG visitForLoop(ForLoopTree forLoopTree, Declaration context) {
    SourceLocation loc = getLocation(forLoopTree);
    
    BlockStmtJSG blockStmtJSG = new BlockStmtJSG(loc);
    for (StatementTree stmtTree : forLoopTree.getInitializer()) {
      blockStmtJSG.statements.add(doVisitStatementTree(stmtTree, context));
    }
    
    StatementJSG loopBody = doVisitStatementTree(forLoopTree.getStatement(), context);
    
    BlockStmtJSG whileBodyStmtJSG;
    if (loopBody instanceof BlockStmtJSG)
      whileBodyStmtJSG = (BlockStmtJSG) loopBody;
    else {
      whileBodyStmtJSG = new BlockStmtJSG(loc);
      whileBodyStmtJSG.statements.add(loopBody);
    }
    
    for (StatementTree stmtTree : forLoopTree.getUpdate()) {
      whileBodyStmtJSG.statements.add(doVisitStatementTree(stmtTree, context));
    }
    
    ExpressionJSG condExpr;
    
    if (forLoopTree.getCondition() != null) {
      condExpr  = (ExpressionJSG)forLoopTree.getCondition().accept(this, context);  
    } else {
      condExpr = new LitExprJSG(loc, JavaiType.BOOLEAN, "true");
    }
   
    
    blockStmtJSG.statements.add(new WhileStmtJSG(loc, condExpr, whileBodyStmtJSG));
    
    return blockStmtJSG;
  }
  
  

  @Override
  public ExpressionJSG visitUnary(UnaryTree unTree, Declaration context) {
    SourceLocation loc = getLocation(unTree);
    ExpressionJSG expr = doVisitExpressionTree(unTree.getExpression(), context);
    Kind kind = unTree.getKind();
    ExpressionJSG.OperatorKind op;
    switch (kind) {
    case PREFIX_INCREMENT:
    case POSTFIX_INCREMENT:
      op = OperatorKind.PLUS;
      break;
    case PREFIX_DECREMENT:
    case POSTFIX_DECREMENT:
      op = OperatorKind.MINUS;
      break;
      
    default:
      return super.visitUnary(unTree, context);
      
    }

    return new BinExprJSG(getLocation(unTree), getTypeJSG(unTree), op, expr, new LitExprJSG(loc, JavaiType.INT, "1"), true);


  }

  int numIterators;

  @Override
  public Object visitEnhancedForLoop(EnhancedForLoopTree forLoopTree,
      Declaration context) {
    SourceLocation loc = getLocation(forLoopTree);
    
    BlockStmtJSG blockStmtJSG = new BlockStmtJSG(loc);
    VarDeclJSG curVar = (VarDeclJSG)visitVariable(forLoopTree.getVariable(), context);

    
    // Define iterator variable
    ExpressionJSG collectionExpr = (ExpressionJSG) forLoopTree.getExpression().accept(this, context);
    JavaiType iterableType = new JavaiType("java.util.Iterable", curVar.jType);
    JavaiType iteratorType = new JavaiType("java.util.Iterator", curVar.jType);
    JavaiType iteratorMethType = new JavaiType(iteratorType, new ArrayList<>());
    ExpressionJSG iterableExpr = new MethodInvokeExprJSG(loc, iteratorType,
        new MemberSelectExprJSG(loc, null, collectionExpr, iteratorMethType, "iterator", false), iterableType, new ArrayList<>(), 
        new MethodDeclaration(loc, "Iterable", "iterator", iteratorType, new ArrayList<>(), false));
    VarDeclJSG iterVar = new VarDeclJSG(loc, "it" + (numIterators++), iteratorType, 
        iterableExpr);
    VarDeclaration iterVarDecl = new VarDeclaration(loc, context, iterVar.name, iterVar.jType, false);
    blockStmtJSG.statements.add(new VarDeclStmtJSG(loc, iterVar));
    
    StatementJSG loopBody = doVisitStatementTree(forLoopTree.getStatement(), context);
    
    BlockStmtJSG whileBodyStmtJSG;
    if (loopBody instanceof BlockStmtJSG)
      whileBodyStmtJSG = (BlockStmtJSG) loopBody;
    else {
      whileBodyStmtJSG = new BlockStmtJSG(loc);
      whileBodyStmtJSG.statements.add(loopBody);
    }
    
    // Define local variable to hold current object
    JavaiType nextMethodType = new JavaiType(curVar.jType, new ArrayList<>());
    curVar.expr = new MethodInvokeExprJSG(loc, curVar.jType,
        new MemberSelectExprJSG(loc, null, new IDExprJSG(loc, iteratorType, iterVar.name, iterVarDecl), nextMethodType, "next", false), iteratorType, new ArrayList<>(), 
        new MethodDeclaration(loc, "Iterator", "next", curVar.jType, new ArrayList<>(), false));
    VarDeclStmtJSG varDeclStmt = new VarDeclStmtJSG(loc, curVar);
    whileBodyStmtJSG.statements.add(0, varDeclStmt);
    
    JavaiType hasNextMethodType = new JavaiType(JavaiType.BOOLEAN, new ArrayList<>());
    ExpressionJSG condExpr = new MethodInvokeExprJSG(loc, JavaiType.BOOLEAN,
        new MemberSelectExprJSG(loc, null, new IDExprJSG(loc, iteratorType, iterVar.name, iterVarDecl), hasNextMethodType, "hasNext", false), iteratorType, new ArrayList<>(), 
        new MethodDeclaration(loc, "Iterator", "hasNext", JavaiType.BOOLEAN, new ArrayList<>(), false));
    blockStmtJSG.statements.add(new WhileStmtJSG(loc, condExpr, whileBodyStmtJSG));
    
    return blockStmtJSG;
  }

}
