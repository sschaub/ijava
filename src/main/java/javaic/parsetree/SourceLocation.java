package javaic.parsetree;

public class SourceLocation {
  public final String filename;
  public final long line;
  public final long col;
  
  public SourceLocation(String filename, long line, long col) {
    super();
    this.filename = filename;
    this.line = line;
    this.col = col;
  }

  @Override
  public String toString() {
    return filename + "(" + line + "," + col + ")";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (col ^ (col >>> 32));
    result = prime * result + ((filename == null) ? 0 : filename.hashCode());
    result = prime * result + (int) (line ^ (line >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SourceLocation other = (SourceLocation) obj;
    if (col != other.col)
      return false;
    if (filename == null) {
      if (other.filename != null)
        return false;
    } else if (!filename.equals(other.filename))
      return false;
    if (line != other.line)
      return false;
    return true;
  }


  
}
