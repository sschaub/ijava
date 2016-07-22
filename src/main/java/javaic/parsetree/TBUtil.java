//package javaic.parsetree;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javaic.semantics.JType;
//
//import org.antlr.v4.runtime.Token;
//
//public class TBUtil {
//
//	public static List<String> IDsToStringList(List<Token> ids) {
//		return  ids.stream().map(p -> p.getText()).collect(Collectors.toList());
//	}
//
//	public static List<JType> TypesToTypeList(List<TypeContext> types) {
//		return types.stream().map(p -> p.result).collect(Collectors.toList());
//	}
//
//}
