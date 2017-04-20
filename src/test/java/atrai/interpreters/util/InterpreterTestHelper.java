package atrai.interpreters.util;

import atrai.core.UntypedTree;
import atrai.interpreters.common.Interpreter;

import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class InterpreterTestHelper {
    private Supplier<Interpreter> inp;

    public InterpreterTestHelper(Supplier<Interpreter> inp) {
        this.inp = inp;
    }

    public void testCase(String program, Class<? extends Throwable> exType) throws Exception {
        try {
            inp.get().interpretString(program);
            fail("No exception thrown");
        } catch (Exception e) {
            if (!exType.isAssignableFrom(e.getClass())) {
                fail("Expected Exception: " + exType.getCanonicalName() + ", Got: " + e.getClass().getCanonicalName());
            }
        }
    }

    public void testCase(String program, String result) throws Exception {
        Object obj = inp.get().interpretString(program);
        System.out.println(((UntypedTree) obj).toIndentedString());
        assertEquals(result, String.valueOf(obj));
    }

    public Object testCase(String program) throws Exception {
        return inp.get().interpretString(program);
    }
}
