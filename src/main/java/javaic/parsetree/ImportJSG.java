package javaic.parsetree;

public class ImportJSG {
  public String importQN;
  
  public boolean isPackage;

  public ImportJSG(String name, boolean isPackage) {
    importQN = name;
    this.isPackage = isPackage;
  }
  
  public String getPackageName() {
     return importQN.substring(0, importQN.lastIndexOf('.'));
  }
  
  // returns "*" if isPackage is true
  public String getClassName() {
    return importQN.substring(importQN.lastIndexOf('.') + 1);
  }

}
