package atrai.interpreters.CLASSES;

import atrai.antlr.ANTLRTokenizer;
import atrai.antlr.GenericAntlrToUntypedTree;
import atrai.antlr.Location;
import atrai.core.Pattern;
import atrai.core.Transformer;
import atrai.core.UntypedTree;
import atrai.interpreters.common.Environment;
import atrai.interpreters.common.Interpreter;
import atrai.interpreters.common.SemanticException;

import java.util.Iterator;

import static atrai.interpreters.common.DynamicTypeChecker.*;

public class ClassesChecker extends Interpreter {
    private static final String grammarName = "atrai.antlr.CLASSES";
    private static final ANTLRTokenizer tokenizer = new ANTLRTokenizer(grammarName);
    private static final Pattern typeExtractor = Pattern.parse("(%CLS @ typed @ @_%)", tokenizer);
    private String currentClassName = null;
    private String currentMethodName = null;
    private Environment currentEnv = null;

    public UntypedTree parseFile(String pgm) {
        GenericAntlrToUntypedTree p = new GenericAntlrToUntypedTree();
        return p.parseFileToUntypedTree("CLS", grammarName, "prog", pgm);
    }

    public Object interpret(UntypedTree st) {
        try {
            return null;
        } catch (SemanticException ex) {
            String location = ex.getLocation() != null ? " at " + ex.getLocation() : "";
            System.err.println("Fatal Error: " + ex.getMessage() + location);
            throw ex;
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public UntypedTree parseString(String pgm) {
        GenericAntlrToUntypedTree p = new GenericAntlrToUntypedTree();
        return p.parseStringToUntypedTree("CLS", grammarName, "prog", pgm);
    }


    private String extractType(Object source) {
        Object[] tmp = typeExtractor.match(source);
        return (String) tmp[1];
    }
}
