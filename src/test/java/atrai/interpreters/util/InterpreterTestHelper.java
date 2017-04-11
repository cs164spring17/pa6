package atrai.interpreters.util;

import atrai.core.UntypedTree;
import atrai.interpreters.common.Interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class InterpreterTestHelper {
    private Interpreter inp;

    public InterpreterTestHelper(Interpreter inp) {
        this.inp = inp;
    }

    public void testCase(String program, Class<? extends Throwable> exType) throws Exception {
        try {
            inp.interpretString(program);
            fail("No exception thrown");
        } catch (Exception e) {
            if (!exType.isAssignableFrom(e.getClass())) {
                fail("Expected Exception: " + exType.getCanonicalName() + ", Got: " + e.getClass().getCanonicalName());
            }
        }
    }

    public void testCase(String program, String result) throws Exception {
        Object obj = inp.interpretString(program);
        System.out.println(((UntypedTree) obj).toIndentedString());
        assertEquals(result, String.valueOf(obj));
    }

    public Object testCase(String program) throws Exception {
        return inp.interpretString(program);
    }
}
