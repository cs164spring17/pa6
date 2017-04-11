package atrai.interpreters.CLASSES;

import atrai.antlr.Location;

class SubtypeError extends RuntimeException {
    SubtypeError(Object t1, Object t2, Location location) {
        super("Type error: " + " " + t1 + " is not subtype of " + t2 + " at " + location);
    }
}
