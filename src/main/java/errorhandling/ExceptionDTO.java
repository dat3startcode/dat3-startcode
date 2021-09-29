
package errorhandling;

public class ExceptionDTO {
    private int code;
    private String message;

  public ExceptionDTO(int code, String description){
      this.code = code;
      this.message = description;
  }

}
