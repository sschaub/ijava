//package javaic.semantics;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import javaic.antlr.JavaiBaseListener;
//import javaic.antlr.JavaiLexer;
//import javaic.antlr.JavaiParser.ArrayCreatorContext;
//import javaic.antlr.JavaiParser.ClassDeclarationContext;
//import javaic.antlr.JavaiParser.CreatedNameContext;
//import javaic.antlr.JavaiParser.ExprArrayIndexContext;
//import javaic.antlr.JavaiParser.ExprBinContext;
//import javaic.antlr.JavaiParser.ExprFieldContext;
//import javaic.antlr.JavaiParser.ExprIdContext;
//import javaic.antlr.JavaiParser.ExprLitContext;
//import javaic.antlr.JavaiParser.ExprMethodCallContext;
//import javaic.antlr.JavaiParser.ExprNewContext;
//import javaic.antlr.JavaiParser.ExprParContext;
//import javaic.antlr.JavaiParser.ExprThisContext;
//import javaic.antlr.JavaiParser.ExprUnContext;
//import javaic.antlr.JavaiParser.FieldDeclarationContext;
//import javaic.antlr.JavaiParser.FormalParameterContext;
//import javaic.antlr.JavaiParser.FormalParameterListContext;
//import javaic.antlr.JavaiParser.LitBoolContext;
//import javaic.antlr.JavaiParser.LitCharContext;
//import javaic.antlr.JavaiParser.LitFloatContext;
//import javaic.antlr.JavaiParser.LitIntContext;
//import javaic.antlr.JavaiParser.LitNullContext;
//import javaic.antlr.JavaiParser.LitStrContext;
//import javaic.antlr.JavaiParser.LocalVarDeclStatementContext;
//import javaic.antlr.JavaiParser.MethodDeclarationContext;
//import javaic.antlr.JavaiParser.MethodHeaderContext;
//import javaic.antlr.JavaiParser.PrimitiveTypeContext;
//import javaic.antlr.JavaiParser.TypeClassContext;
//import javaic.antlr.JavaiParser.TypePrimitiveContext;
//
//public class SemanticCheck extends JavaiBaseListener {
//  
//  private SymbolTable table = new SymbolTable();
//  private ClassDeclaration curClassDecl;
//
//  @Override
//  public void exitPrimitiveType(PrimitiveTypeContext ctx) {
//    Type t;
//    switch (ctx.getStart().getType()) {
//    case JavaiLexer.INT:
//      t = Type.INT;
//      break;
//    case JavaiLexer.FLOAT:
//      t = Type.FLOAT;
//      break;
//    case JavaiLexer.STRING:
//      t = Type.STRING;
//      break;
//    case JavaiLexer.CHAR:
//      t = Type.CHAR;
//      break;
//    case JavaiLexer.BOOLEAN:
//      t = Type.BOOL;
//      break;
//    default:
//      throw new SemanticError("Unexpected primitive type: " + ctx.getText(), ctx);
//    }
//    
//    ctx.theType = t;
//  }
//
//  @Override
//  public void exitTypeClass(TypeClassContext ctx) {
//    Type t = Type.fromName(ctx.Identifier().getText());
//    if (t == null) {
//      throw new RuntimeException("Undefined class: " + ctx.getText());
//    }
//    if (ctx.getText().endsWith("]")) {
//      t = Type.createArray(t);
//    }
//    ctx.theType = t;
//  }
//
//  @Override
//  public void exitTypePrimitive(TypePrimitiveContext ctx) {
//    Type t = ctx.primitiveType().theType;
//    if (ctx.getText().endsWith("]")) {
//      t = Type.createArray(t);
//    }    
//    ctx.theType = t;
//  }
//
//  // --------------- Statements -------------------------------
//  
//  @Override
//  public void exitLocalVarDeclStatement(LocalVarDeclStatementContext ctx) {
//    String id = ctx.Identifier().getText();
//    Declaration decl = table.lookup(id);
//    Type type = ctx.type().theType;
//    
//    if (decl != null && decl.getScope() == table.getScope()) {
//      throw new RuntimeException("Duplicate identifier: " + id);
//    }
//    ctx.decl = new VarDeclaration(type, id);
//    table.define(id, ctx.decl);
//  }
//  
//  // ---------------- Method Processing -----------------------
//
//  @Override
//  public void exitMethodHeader(MethodHeaderContext ctx) {
//
//    String id = ctx.Identifier().getText();
//    Declaration decl = table.lookup(id); 
//    if (decl != null && decl.getScope() == table.getScope())
//      throw new RuntimeException("Redefined identifier: " + id);
//    
//    List<VarDeclaration> formals = new ArrayList<>();
//    FormalParameterListContext ctxFormalsList = ctx.formalParameters().formalParameterList();
//    if (ctxFormalsList != null) {
//      for (FormalParameterContext parm : ctxFormalsList.formalParameter()) {
//        // TODO: Check for duplicate formal parameter names
//        formals.add(new VarDeclaration(parm.type().theType, parm.Identifier().getText()));
//      }
//    }
//    
//    Type t = null;
//    if (ctx.type() != null) {
//      t = ctx.type().theType;
//    } else if (ctx.getTokens(JavaiLexer.VOID).size() == 1) {
//      t = Type.VOID;
//    }
//    
//    if (t == null && !id.equals(curClassDecl.getId())) {
//      throw new SemanticError("Constructor name must match name of class.", ctx);
//    }
//    
//    ctx.decl = new MethodDeclaration(t, id, formals, curClassDecl);
//    curClassDecl.addMethod(ctx.decl);
//    table.define(id, ctx.decl);
//    table.beginScope();
//    for (VarDeclaration vdecl : formals) {
//      table.define(vdecl.getId(), vdecl);
//    }
//  }
//
//  @Override
//  public void exitMethodDeclaration(MethodDeclarationContext ctx) {
//    table.endScope();
//    ctx.decl = ctx.methodHeader().decl; // propagate declaration info
//  }
//  
//  // ------------------ Class Processing ----------------------
//
//  @Override
//  public void enterClassDeclaration(ClassDeclarationContext ctx) {
//    String id = ctx.Identifier().getText();
//    
//    if (table.lookup(id) != null)
//      throw new SemanticError("Redefined class: " + id, ctx);
//
//    curClassDecl = ctx.decl = new ClassDeclaration(id);
//    table.define(id, curClassDecl);
//    Type.createClass(id, curClassDecl);
//    table.beginScope();
//  }
//
//  @Override
//  public void exitClassDeclaration(ClassDeclarationContext ctx) {
//    table.endScope();
//  }
//  
//  @Override
//  public void exitFieldDeclaration(FieldDeclarationContext ctx) {
//    String id = ctx.Identifier().getText();
//    Declaration decl = table.lookup(id); 
//    Type type = ctx.type().theType;
//    
//    if (decl != null && decl.getScope() == table.getScope())
//      throw new SemanticError("Redefined identifier: " + id, ctx);
//
//    ctx.decl = new VarDeclaration(type, id);
//    table.define(id, ctx.decl);    
//    curClassDecl.addField(ctx.decl);
//    
//    
//  }
//  
//  
//
//  // -------------------- Expressions --------------------------
//  
//  @Override
//  public void exitExprId(ExprIdContext ctx) {
//    String id = ctx.Identifier().getText();
//    Declaration decl = table.lookup(id);
//    if (decl == null) {
//      throw new SemanticError("Undefined identifier: " + id, ctx);
//    }
//    
//    if (!(decl instanceof VarDeclaration)) {
//      throw new SemanticError("Not a variable: " + id, ctx);
//    }
//    
//    ctx.decl = decl;
//    ctx.theType = ((VarDeclaration)decl).getType();
//  }
//
//  @Override
//  public void exitExprMethodCall(ExprMethodCallContext ctx) {
//    ClassDeclaration cdecl = null;
//    MethodDeclaration mdecl = null;
//
//    if (ctx.expression() != null) {
//      Type t = ctx.expression().theType;
//      if (!t.isClass()) {
//        throw new SemanticError("Cannot invoke methods on type " + t.getName(), ctx);
//      }
//      ClassType ct = (ClassType)t;
//      cdecl = ct.getDecl();
//    } else {
//      cdecl = curClassDecl;
//    }
//    
//    String methodName = ctx.Identifier().getText();
//    for (MethodDeclaration md : cdecl.getMethods()) {
//      if (md.getId().equals(methodName)) {
//        mdecl = md;
//        break;
//      }
//    }
//    if (mdecl == null) {
//      throw new SemanticError("No such methods " + methodName + " in class " + cdecl.getId(), ctx);
//    }
//    ctx.decl = mdecl;
//    ctx.theType = mdecl.getType();
//  }
//
//  @Override
//  public void exitExprNew(ExprNewContext ctx) {
//    if (ctx.Identifier() != null) {
//      String name = ctx.Identifier().getText();
//      Type t = Type.fromName(name);
//      if (t == null || !t.isClass()) {
//        throw new SemanticError("No such class: " + name, ctx);
//      }
//      ctx.theType = t;
//    } else {
//      ctx.theType = ctx.arrayCreator().theType;
//    }
//  }
//
//  @Override
//  public void exitCreatedName(CreatedNameContext ctx) {
//    if (ctx.Identifier() != null) {
//      String name = ctx.Identifier().getText();
//      Type t = Type.fromName(name);
//      if (t == null || !t.isClass()) {
//        throw new SemanticError("No such class: " + name, ctx);
//      }
//      ctx.theType = t;
//    } else {
//      ctx.theType = ctx.primitiveType().theType;
//    }
//  }
//
//  @Override
//  public void exitArrayCreator(ArrayCreatorContext ctx) {
//    if (ctx.expression().theType != Type.INT) {
//      throw new SemanticError("Array instantiation must use int type for size expression", ctx);
//    }
//    ctx.theType = Type.createArray(ctx.createdName().theType);
//  }
//
//  @Override
//  public void exitExprUn(ExprUnContext ctx) {
//    ctx.theType = ctx.expression().theType;
//  }
//
//  public static Set<Integer> boolOp = new HashSet<>(Arrays.asList(
//      JavaiLexer.AND, JavaiLexer.OR, JavaiLexer.BANG
//      ));
//  
//  public static Set<Integer> mathOp = new HashSet<>(Arrays.asList(
//      JavaiLexer.ADD, JavaiLexer.SUB, JavaiLexer.MUL, JavaiLexer.DIV, JavaiLexer.MOD
//      ));
//
//  public static Set<Integer> relOp = new HashSet<>(Arrays.asList(
//      JavaiLexer.EQUAL, JavaiLexer.NOTEQUAL, JavaiLexer.LE, JavaiLexer.LT, JavaiLexer.GE, JavaiLexer.GT
//      ));
//
//  @Override
//  public void exitExprBin(ExprBinContext ctx) {
//
//    Type type1 = ctx.expr1.theType;
//    Type type2 = ctx.expr2.theType;
//    
//    // String concatenation check
//    if (ctx.op.getType() == JavaiLexer.ADD && (type1 == Type.STRING || type2 == Type.STRING)) {
//      ctx.theType = Type.STRING;
//      return;    
//    }
//
//    // Mod check
//    if (ctx.op.getType() == JavaiLexer.MOD && !(type1 == Type.INT && type2 == Type.INT)) {
//      throw new SemanticError("Operands for " + ctx.op.getText() + " must be int.", ctx);
//    }
//
//    // Math ops
//    if (mathOp.contains(ctx.op.getType())) {
//      if (!(type1.isNumeric() && type2.isNumeric())) {
//        throw new SemanticError("Operands for " + ctx.op.getText() + " must be numeric.", ctx);
//      }
//      if (type1 == Type.FLOAT || type2 == Type.FLOAT) {
//        ctx.theType = Type.FLOAT;
//        return;
//      }
//      if (type1 == Type.INT || type2 == Type.INT) {
//        ctx.theType = Type.INT;
//        return;
//      }
//      ctx.theType = Type.CHAR;
//      return;
//    }
//    
//    // Relational ops
//    if (relOp.contains(ctx.op.getType())) {
//      // TODO: Verify type compatibility of operands
//      
//      ctx.theType = Type.BOOL;
//      return;
//    }
//    
//    // Boolean ops
//    if (boolOp.contains(ctx.op.getType())) {
//      if (type1 != Type.BOOL || type2 != Type.BOOL) {
//        throw new SemanticError("Operands for " + ctx.op.getText() + " must be boolean.", ctx);
//      }
//      ctx.theType = Type.BOOL;
//      return;
//    }
//
//    ctx.theType = ctx.expr1.theType;
//  }
//
//  @Override
//  public void exitExprField(ExprFieldContext ctx) {
//    String fieldName = ctx.Identifier().getText();
//    if (!ctx.expression().theType.isClass()) {
//      throw new SemanticError("Cannot access field of " + ctx.expression().theType.getName(), ctx);
//    }
//    
//    ClassDeclaration cdecl = ((ClassType)ctx.expression().theType).getDecl();
//    VarDeclaration vdecl = cdecl.getField(fieldName);
//    if (vdecl == null) {
//      throw new SemanticError("No such field " + fieldName + " in class " + cdecl.getId(), ctx);
//    }
//    
//    ctx.theType = vdecl.getType();
//  }
//
//  @Override
//  public void exitExprLit(ExprLitContext ctx) {
//    if (ctx.literal() instanceof LitIntContext)
//      ctx.theType = Type.INT;
//    else if (ctx.literal() instanceof LitFloatContext)
//      ctx.theType = Type.FLOAT;
//    else if (ctx.literal() instanceof LitBoolContext)
//      ctx.theType = Type.BOOL;
//    else if (ctx.literal() instanceof LitCharContext)
//      ctx.theType = Type.CHAR;
//    else if (ctx.literal() instanceof LitStrContext)
//      ctx.theType = Type.STRING;
//    else if (ctx.literal() instanceof LitNullContext)
//      ctx.theType = Type.VOID;
//    else
//      throw new RuntimeException("Internal error: Unexpected literal encountered: " + ctx.literal().getClass().getSimpleName());
// 
//  }
//
//  @Override
//  public void exitExprArrayIndex(ExprArrayIndexContext ctx) {
//    if (ctx.index.theType != Type.INT) {
//      throw new SemanticError("Array index must be type int.", ctx);
//    }
//    if (!ctx.arr.theType.isArray()) {
//      throw new SemanticError("Cannot index " + ctx.arr.getText() + " (type " + ctx.arr.theType + ")", ctx);
//    }
//    ctx.theType = ((ArrayType)ctx.arr.theType).getElementType();
//    
//  }
//
//  @Override
//  public void exitExprPar(ExprParContext ctx) {
//    ctx.theType = ctx.expression().theType;
//  }
//
//  @Override
//  public void exitExprThis(ExprThisContext ctx) {
//    ctx.theType = curClassDecl.getClassType();
//  }
//
//}
