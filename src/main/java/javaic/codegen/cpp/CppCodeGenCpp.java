package javaic.codegen.cpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javaic.LogFactory;
import javaic.codegen.CodeGenBase;
import javaic.codegen.CodeGenTreeBuilder;
import javaic.codegen.model.CompileUnitCG;
import javaic.parsetree.BlockStmtJSG;
import javaic.parsetree.ExprStmtJSG;
import javaic.parsetree.ExpressionJSG;
import javaic.parsetree.IDExprJSG;
import javaic.parsetree.MemberSelectExprJSG;
import javaic.parsetree.MethodDeclJSG;
import javaic.parsetree.MethodInvokeExprJSG;
import javaic.parsetree.ParseTreeJSG;
import javaic.parsetree.ReturnStmtJSG;
import javaic.parsetree.SourceLocation;
import javaic.parsetree.StatementJSG;
import javaic.parsetree.TypeDeclJSG;
import javaic.parsetree.VarDeclJSG;
import javaic.semantics.JavaiType;
import javaic.semantics.MethodDeclaration;
import javaic.semantics.VarDeclaration;

public class CppCodeGenCpp extends CodeGenBase {
  
  Logger logger = LogFactory.getLogger(getClass());

  public CppCodeGenCpp(ParseTreeJSG ptJSG) {
    super(ptJSG, "/javaic/codegen/cpp/cpp.stg");
  }

  @Override
  public String getBaseOutputPath() {
    return "cppoutput";
  }    
  
  @Override
  public String getOutputBaseFilename(CompileUnitCG compileUnit) throws IOException {
    return super.getOutputBaseFilename(compileUnit) + ".cpp";
  }

  @Override
  public CodeGenTreeBuilder createCodeGenTreeBuilder() {
    return new CppCGTreeBuilder(new CppCGNodeFactory());
  }

  @Override
  public void preprocessTree() {
    do {
      overloadsWereAdded = false;
      processOverloads();
    } while (overloadsWereAdded); 
  }
  
  boolean overloadsWereAdded;
  
  private void processOverloads() {
    HashMap<String, TypeDeclJSG> typeDecls = ptJSG.getTypeDecls();
    for (TypeDeclJSG typeDecl : typeDecls.values()) {
      if (typeDecl.extendsClass == null && typeDecl.extendsInterfaces.size() == 0)
        continue;
      for (MethodDeclJSG methodDecl : new ArrayList<>(typeDecl.methods)) {
        if (methodDecl.isConstructor())
          continue;
        
        List<MethodDeclJSG> overloads = new ArrayList<>();
        // compute set of overloads for this method in all ancestors and add missing ones to tree
        TypeDeclJSG parent = ptJSG.findParent(typeDecl); 
        while (parent != null) {
          computeOverloads(methodDecl, parent, overloads);
          parent = ptJSG.findParent(parent);
        }
        
        for (JavaiType ifType : typeDecl.extendsInterfaces) {
          TypeDeclJSG interfaceJSG = typeDecls.get(ifType.qualifiedName);
          if (interfaceJSG != null)
            computeOverloads(methodDecl, interfaceJSG, overloads);
        }
        
        addMissingOverloads(typeDecl, methodDecl, overloads);
      }
    }

  }

  private void addMissingOverloads(TypeDeclJSG typeDecl,
      MethodDeclJSG method,
      List<MethodDeclJSG> overloads) {
    for (MethodDeclJSG testMethod: typeDecl.methods) {
      if (testMethod.name.equals(method.name) && !parmTypesMatch(testMethod, method)) {
        // found an overload; remove from overloads if present
        for (int i = 0; i < overloads.size(); ++i) {
          if (parmTypesMatch(overloads.get(i), testMethod)) {
            overloads.remove(i);
            break;
          }
        }
      }
    }
    
    // Now, add remaining overloads
    for (MethodDeclJSG overload : overloads) {
      overloadsWereAdded = true;
      MethodDeclJSG newOverload = new MethodDeclJSG(overload.loc, overload.name, overload.returnType, overload.parms);
      newOverload.parent = typeDecl;
      if (overload.body != null) {
        SourceLocation loc = overload.loc;
        // Create body for overload
        ArrayList<ExpressionJSG> args = new ArrayList<>();
        for (VarDeclJSG parm : overload.parms) {
          args.add(new IDExprJSG(loc, parm.jType, parm.name, 
              new VarDeclaration(loc, 
                  new MethodDeclaration(loc, null, null, null, null, false), 
                  parm.name, parm.jType, false)));
        }
        TypeDeclJSG parent = ptJSG.findParent(typeDecl);
        
        MethodInvokeExprJSG methodInvokeExprJSG = new MethodInvokeExprJSG(loc, overload.returnType, 
            new MemberSelectExprJSG(loc, overload.returnType, 
                new IDExprJSG(loc, 
                    overload.parent.getJType(), "super", 
                    new VarDeclaration(loc, null, "super", null, false)),
                    new JavaiType(overload.returnType, null), overload.name, false), 
            null, 
            args, 
            null);
        StatementJSG stmt;
        if (overload.returnType == JavaiType.VOID)
          stmt = new ExprStmtJSG(loc, methodInvokeExprJSG);
        else
          stmt = new ReturnStmtJSG(loc, methodInvokeExprJSG);
        newOverload.body = new BlockStmtJSG(newOverload.loc);
        newOverload.body.statements.add(stmt);
      }
      typeDecl.methods.add(newOverload);
    }
    
  }

  private void computeOverloads(MethodDeclJSG methodDecl, TypeDeclJSG typeDecl,
      List<MethodDeclJSG> overloads) {
    for (MethodDeclJSG method: typeDecl.methods) {
      if (method.name.equals(methodDecl.name) && !parmTypesMatch(methodDecl, method)) {
        // add to overloads if not present
        boolean found = false;
        for (MethodDeclJSG test: overloads) {
          if (parmTypesMatch(test, method))
            found = true;
        }
        if (!found)
          overloads.add(method);
      }
    }
    
  }

  private boolean parmTypesMatch(MethodDeclJSG method1, MethodDeclJSG method2) {
    if (method1.parms.size() != method2.parms.size())
      return false;
    for (int i = 0; i < method1.parms.size(); ++i) {
      if (!method1.parms.get(i).jType.equals(method2.parms.get(i).jType))
        return false;
    }
    return true;
  }


  
}
