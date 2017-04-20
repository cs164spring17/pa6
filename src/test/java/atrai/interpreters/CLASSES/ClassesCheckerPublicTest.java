package atrai.interpreters.CLASSES;

import atrai.interpreters.common.SemanticException;
import atrai.interpreters.util.InterpreterTestHelper;
import org.junit.Test;

/**
 * Created by alex on 4/10/17.
 */
public class ClassesCheckerPublicTest {
    private static ClassesChecker inp = new ClassesChecker();
    private static InterpreterTestHelper t = new InterpreterTestHelper(ClassesChecker::new);

    @Test(expected = SemanticException.class)
    public void testExtraArgumentSelfCast() throws Exception {
        t.testCase(
                "class X extends object\n" +
                        "{\n" +
                        "  constOne():int = 1;\n" +
                        "  bad():int = 1 + self[X].constOne(1);\n" +
                        "}\n"
        );
    }

    @Test(expected = SubtypeError.class)
    public void testIllTypedParam() throws Exception {
        t.testCase(
                "class X extends object\n" +
                        "{\n" +
                        "  g(x:int):int = x;\n" +
                        "  bad():int = 1 + self.g(\"foo\");\n" +
                        "}\n"
        );
    }

    @Test(expected = SubtypeError.class)
    public void testNonBooleanConditionWhile() throws Exception {
        t.testCase(
                "class X extends object\n" +
                        "{\n" +
                        "  f():int = while 1 do 2;\n" +
                        "}\n"
        );
    }

    @Test(expected = RuntimeException.class)
    public void testRejectsCircularInheritance() throws Exception {
        t.testCase("class X extends Y {" +
                "  a(): int = 3;" +
                "  b(x: int): int = 4;" +
                "  c(x: int, y: Y): X = self;" +
                "}" +
                "class Y extends X {" +
                "  b(x: int): int = 5;" +
                "  d(x: int, y: X): Y = self;" +
                "}");
    }

    @Test(expected = SubtypeError.class)
    public void testAddNonStringNonInteger() throws Exception {
        t.testCase(
                "class X extends object\n" +
                        "{\n" +
                        "  f(x:string):string = x + (1 == 1);\n" +
                        "}\n"
        );
    }

    @Test(expected = RuntimeException.class)
    public void testUnknownReturnType() throws Exception {
        t.testCase("class Y extends object {" +
                "  d(x: int, y: X): Z = 3;" +
                "}");
    }

    @Test(expected = SemanticException.class)
    public void testUndefinedVariableRead() throws Exception {
        t.testCase(
                "class X extends object\n" +
                        "{\n" +
                        "  f(x:int):int = x + y;\n" +
                        "}\n"
        );
    }

    @Test
    public void testCallBaseOverride() throws Exception {
        t.testCase("class X extends object { \n" +
                        "   a: int; \n" +
                        "   b: string; \n" +
                        "   f(s: string, a: boolean): object = if a then self.g(b+1) else s; \n" +
                        "   g(a: string): int = 2; \n" +
                        "   p(): int = 3;" +
                        "} \n" +
                        "class Y extends X {\n" +
                        "   c: boolean;\n" +
                        "   f(s: string, a: boolean): object = if c then self[X].f(\"\", 3>2) else new Y;\n" +
                        "   p(): int = 4;" +
                        "}\n" +
                        "class Z extends X {\n" +
                        "   h(a: X, c: boolean): X = let b: boolean = c in if b then new Y else new Z;\n" +
                        "   m(): X = self.h(new Z, 3>2);\n" +
                        "   n(d: X): Z = if d is y: Z then y else new Z;\n" +
                        "}\n",
                "(%CLS 0 prog (%CLS 1 cls class (%CLS 2 type X%) extends (%CLS 3 type object%) { (%CLS 4 field (%CLS 5 iden a%) : (%CLS 6 type int%) ;%) (%CLS 7 field (%CLS 8 iden b%) : (%CLS 9 type string%) ;%) (%CLS 10 method (%CLS 11 iden f%) ( (%CLS 12 paramlist (%CLS 13 param (%CLS 14 iden s%) : (%CLS 15 type string%)%) , (%CLS 16 param (%CLS 17 iden a%) : (%CLS 18 type boolean%)%)%) ) : (%CLS 19 type object%) = (%CLS 20 typed (%CLS 20 expr if (%CLS 22 typed (%CLS 22 iden a%) boolean%) then (%CLS 23 typed (%CLS 23 expr (%CLS 24 typed (%CLS 24 expr self%) X%) . (%CLS 25 iden g%) ( (%CLS 26 arglist (%CLS 27 typed (%CLS 27 expr (%CLS 29 typed (%CLS 29 iden b%) string%) + (%CLS 31 typed (%CLS 31 num 1%) int%)%) string%)%) )%) int%) else (%CLS 33 typed (%CLS 33 iden s%) string%)%) object%) ;%) (%CLS 34 method (%CLS 35 iden g%) ( (%CLS 36 paramlist (%CLS 37 param (%CLS 38 iden a%) : (%CLS 39 type string%)%)%) ) : (%CLS 40 type int%) = (%CLS 42 typed (%CLS 42 num 2%) int%) ;%) (%CLS 43 method (%CLS 44 iden p%) ( ) : (%CLS 45 type int%) = (%CLS 47 typed (%CLS 47 num 3%) int%) ;%) }%) (%CLS 48 cls class (%CLS 49 type Y%) extends (%CLS 50 type X%) { (%CLS 51 field (%CLS 52 iden c%) : (%CLS 53 type boolean%) ;%) (%CLS 54 method (%CLS 55 iden f%) ( (%CLS 56 paramlist (%CLS 57 param (%CLS 58 iden s%) : (%CLS 59 type string%)%) , (%CLS 60 param (%CLS 61 iden a%) : (%CLS 62 type boolean%)%)%) ) : (%CLS 63 type object%) = (%CLS 64 typed (%CLS 64 expr if (%CLS 66 typed (%CLS 66 iden c%) boolean%) then (%CLS 67 typed (%CLS 67 expr (%CLS 68 typed (%CLS 68 expr self%) Y%) [ (%CLS 69 type X%) ] . (%CLS 70 iden f%) ( (%CLS 71 arglist (%CLS 73 typed (%CLS 73 string \"\"%) string%) , (%CLS 74 typed (%CLS 74 expr (%CLS 76 typed (%CLS 76 num 3%) int%) > (%CLS 78 typed (%CLS 78 num 2%) int%)%) boolean%)%) )%) object%) else (%CLS 79 typed (%CLS 79 expr new (%CLS 80 type Y%)%) Y%)%) object%) ;%) (%CLS 81 method (%CLS 82 iden p%) ( ) : (%CLS 83 type int%) = (%CLS 85 typed (%CLS 85 num 4%) int%) ;%) }%) (%CLS 86 cls class (%CLS 87 type Z%) extends (%CLS 88 type X%) { (%CLS 89 method (%CLS 90 iden h%) ( (%CLS 91 paramlist (%CLS 92 param (%CLS 93 iden a%) : (%CLS 94 type X%)%) , (%CLS 95 param (%CLS 96 iden c%) : (%CLS 97 type boolean%)%)%) ) : (%CLS 98 type X%) = (%CLS 99 typed (%CLS 99 expr let (%CLS 100 iden b%) : boolean = (%CLS 103 typed (%CLS 103 iden c%) boolean%) in (%CLS 104 typed (%CLS 104 expr if (%CLS 106 typed (%CLS 106 iden b%) boolean%) then (%CLS 107 typed (%CLS 107 expr new (%CLS 108 type Y%)%) Y%) else (%CLS 109 typed (%CLS 109 expr new (%CLS 110 type Z%)%) Z%)%) X%)%) X%) ;%) (%CLS 111 method (%CLS 112 iden m%) ( ) : (%CLS 113 type X%) = (%CLS 114 typed (%CLS 114 expr (%CLS 115 typed (%CLS 115 expr self%) Z%) . (%CLS 116 iden h%) ( (%CLS 117 arglist (%CLS 118 typed (%CLS 118 expr new (%CLS 119 type Z%)%) Z%) , (%CLS 120 typed (%CLS 120 expr (%CLS 122 typed (%CLS 122 num 3%) int%) > (%CLS 124 typed (%CLS 124 num 2%) int%)%) boolean%)%) )%) X%) ;%) (%CLS 125 method (%CLS 126 iden n%) ( (%CLS 127 paramlist (%CLS 128 param (%CLS 129 iden d%) : (%CLS 130 type X%)%)%) ) : (%CLS 131 type Z%) = (%CLS 132 typed (%CLS 132 expr if (%CLS 134 typed (%CLS 134 iden d%) X%) is (%CLS 135 iden y%) : (%CLS 136 type Z%) then (%CLS 138 typed (%CLS 138 iden y%) Z%) else (%CLS 139 typed (%CLS 139 expr new (%CLS 140 type Z%)%) Z%)%) Z%) ;%) }%)%)");
    }

    @Test
    public void testCallBaseMethod() throws Exception {
        t.testCase("class X extends object { \n" +
                        "   a: int; \n" +
                        "   b: string; \n" +
                        "   f(s: string, a: boolean): object = if a then self.g(b+1) else s; \n" +
                        "   g(a: string): int = 2; \n" +
                        "   p(): int = 3;" +
                        "} \n" +
                        "class Y extends X {\n" +
                        "   c: boolean;\n" +
                        "   f(s: string, a: boolean): object = if c then self[X].p() else new Y;\n" +
                        "   p(): int = 4;" +
                        "}\n" +
                        "class Z extends X {\n" +
                        "   h(a: X, c: boolean): X = let b: boolean = c in if b then new Y else new Z;\n" +
                        "   m(): X = self.h(new Z, 3>2);\n" +
                        "   n(d: X): Z = if d is y: Z then y else new Z;\n" +
                        "}\n",
                "(%CLS 0 prog (%CLS 1 cls class (%CLS 2 type X%) extends (%CLS 3 type object%) { (%CLS 4 field (%CLS 5 iden a%) : (%CLS 6 type int%) ;%) (%CLS 7 field (%CLS 8 iden b%) : (%CLS 9 type string%) ;%) (%CLS 10 method (%CLS 11 iden f%) ( (%CLS 12 paramlist (%CLS 13 param (%CLS 14 iden s%) : (%CLS 15 type string%)%) , (%CLS 16 param (%CLS 17 iden a%) : (%CLS 18 type boolean%)%)%) ) : (%CLS 19 type object%) = (%CLS 20 typed (%CLS 20 expr if (%CLS 22 typed (%CLS 22 iden a%) boolean%) then (%CLS 23 typed (%CLS 23 expr (%CLS 24 typed (%CLS 24 expr self%) X%) . (%CLS 25 iden g%) ( (%CLS 26 arglist (%CLS 27 typed (%CLS 27 expr (%CLS 29 typed (%CLS 29 iden b%) string%) + (%CLS 31 typed (%CLS 31 num 1%) int%)%) string%)%) )%) int%) else (%CLS 33 typed (%CLS 33 iden s%) string%)%) object%) ;%) (%CLS 34 method (%CLS 35 iden g%) ( (%CLS 36 paramlist (%CLS 37 param (%CLS 38 iden a%) : (%CLS 39 type string%)%)%) ) : (%CLS 40 type int%) = (%CLS 42 typed (%CLS 42 num 2%) int%) ;%) (%CLS 43 method (%CLS 44 iden p%) ( ) : (%CLS 45 type int%) = (%CLS 47 typed (%CLS 47 num 3%) int%) ;%) }%) (%CLS 48 cls class (%CLS 49 type Y%) extends (%CLS 50 type X%) { (%CLS 51 field (%CLS 52 iden c%) : (%CLS 53 type boolean%) ;%) (%CLS 54 method (%CLS 55 iden f%) ( (%CLS 56 paramlist (%CLS 57 param (%CLS 58 iden s%) : (%CLS 59 type string%)%) , (%CLS 60 param (%CLS 61 iden a%) : (%CLS 62 type boolean%)%)%) ) : (%CLS 63 type object%) = (%CLS 64 typed (%CLS 64 expr if (%CLS 66 typed (%CLS 66 iden c%) boolean%) then (%CLS 67 typed (%CLS 67 expr (%CLS 68 typed (%CLS 68 expr self%) Y%) [ (%CLS 69 type X%) ] . (%CLS 70 iden p%) ( )%) int%) else (%CLS 71 typed (%CLS 71 expr new (%CLS 72 type Y%)%) Y%)%) object%) ;%) (%CLS 73 method (%CLS 74 iden p%) ( ) : (%CLS 75 type int%) = (%CLS 77 typed (%CLS 77 num 4%) int%) ;%) }%) (%CLS 78 cls class (%CLS 79 type Z%) extends (%CLS 80 type X%) { (%CLS 81 method (%CLS 82 iden h%) ( (%CLS 83 paramlist (%CLS 84 param (%CLS 85 iden a%) : (%CLS 86 type X%)%) , (%CLS 87 param (%CLS 88 iden c%) : (%CLS 89 type boolean%)%)%) ) : (%CLS 90 type X%) = (%CLS 91 typed (%CLS 91 expr let (%CLS 92 iden b%) : boolean = (%CLS 95 typed (%CLS 95 iden c%) boolean%) in (%CLS 96 typed (%CLS 96 expr if (%CLS 98 typed (%CLS 98 iden b%) boolean%) then (%CLS 99 typed (%CLS 99 expr new (%CLS 100 type Y%)%) Y%) else (%CLS 101 typed (%CLS 101 expr new (%CLS 102 type Z%)%) Z%)%) X%)%) X%) ;%) (%CLS 103 method (%CLS 104 iden m%) ( ) : (%CLS 105 type X%) = (%CLS 106 typed (%CLS 106 expr (%CLS 107 typed (%CLS 107 expr self%) Z%) . (%CLS 108 iden h%) ( (%CLS 109 arglist (%CLS 110 typed (%CLS 110 expr new (%CLS 111 type Z%)%) Z%) , (%CLS 112 typed (%CLS 112 expr (%CLS 114 typed (%CLS 114 num 3%) int%) > (%CLS 116 typed (%CLS 116 num 2%) int%)%) boolean%)%) )%) X%) ;%) (%CLS 117 method (%CLS 118 iden n%) ( (%CLS 119 paramlist (%CLS 120 param (%CLS 121 iden d%) : (%CLS 122 type X%)%)%) ) : (%CLS 123 type Z%) = (%CLS 124 typed (%CLS 124 expr if (%CLS 126 typed (%CLS 126 iden d%) X%) is (%CLS 127 iden y%) : (%CLS 128 type Z%) then (%CLS 130 typed (%CLS 130 iden y%) Z%) else (%CLS 131 typed (%CLS 131 expr new (%CLS 132 type Z%)%) Z%)%) Z%) ;%) }%)%)");
    }

    @Test(expected = SubtypeError.class)
    public void testSidewaysMethodCall() throws Exception {
        t.testCase("class X extends object { \n" +
                "   a: int; \n" +
                "   b: string; \n" +
                "   f(s: string, a: boolean): object = if a then self.g(b+1) else s; \n" +
                "   g(a: string): int = 2; \n" +
                "   p(): int = 3;" +
                "} \n" +
                "class Y extends X {\n" +
                "   c: boolean;\n" +
                "   f(s: string, a: boolean): object = if c then self[Z].p() else new Y;\n" +
                "   p(): int = 4;" +
                "}\n" +
                "class Z extends X {\n" +
                "   p(): int = 3;" +
                "}\n");
    }

    @Test(expected = SubtypeError.class)
    public void testInvalidBaseMethodCall() throws Exception {
        t.testCase("class X extends object { \n" +
                "   a: int; \n" +
                "   b: string; \n" +
                "   f(s: string, a: boolean): object = if a then self.g(b+1) else s; \n" +
                "   g(a: string): int = 2; \n" +
                "   p(): int = 3;" +
                "} \n" +
                "class Y extends X {\n" +
                "   c: boolean;\n" +
                "   f(s: string, a: boolean): object = if c then self[Z].f(\"\", 3>2) else new Y;\n" +
                "   p(): int = 4;" +
                "}\n" +
                "class Z extends X {\n" +
                "   h(a: X, c: boolean): X = let b: boolean = c in if b then new Y else new Z;\n" +
                "   m(): X = self.h(new Z, 3>2);\n" +
                "   n(d: X): Z = if d is y: Z then y else new Z;\n" +
                "   f(s: string, a: boolean): object = if c then 1 else new Y;\n" +
                "}\n");
    }

    @Test
    public void testHighCoverageFactorial() throws Exception {
        t.testCase("class Fact extends object {\n" +
                        "  numCalls:int;\n" +
                        "  getNumCalls():int = numCalls;\n" +
                        "  fact(n:int):int = {\n" +
                        "    if (isnull (self.getNumCalls()))\n" +
                        "      then numCalls = 1\n" +
                        "      else numCalls = numCalls + 1;\n" +
                        "    let p : int = 1 in (\n" +
                        "      let i : int = 1 in {\n" +
                        "        while i < n do {\n" +
                        "          p = p * i;\n" +
                        "          i = i + 1\n" +
                        "        };\n" +
                        "        print \"Fact = \" + p;\n" +
                        "        print \"#Calls = \" + self.getNumCalls();\n" +
                        "        p" +
                        "      }\n" +
                        "    )\n" +
                        "  };\n" +
                        "}",
                "(%CLS 0 prog (%CLS 1 cls class (%CLS 2 type Fact%) extends (%CLS 3 type object%) { (%CLS 4 field (%CLS 5 iden numCalls%) : (%CLS 6 type int%) ;%) (%CLS 7 method (%CLS 8 iden getNumCalls%) ( ) : (%CLS 9 type int%) = (%CLS 11 typed (%CLS 11 iden numCalls%) int%) ;%) (%CLS 12 method (%CLS 13 iden fact%) ( (%CLS 14 paramlist (%CLS 15 param (%CLS 16 iden n%) : (%CLS 17 type int%)%)%) ) : (%CLS 18 type int%) = (%CLS 19 typed (%CLS 19 expr { (%CLS 20 exprseq (%CLS 21 typed (%CLS 21 expr if (%CLS 23 typed (%CLS 23 expr isnull (%CLS 25 typed (%CLS 25 expr (%CLS 26 typed (%CLS 26 expr self%) Fact%) . (%CLS 27 iden getNumCalls%) ( )%) int%)%) boolean%) then (%CLS 28 typed (%CLS 28 expr (%CLS 29 iden numCalls%) = (%CLS 31 typed (%CLS 31 num 1%) int%)%) int%) else (%CLS 32 typed (%CLS 32 expr (%CLS 33 iden numCalls%) = (%CLS 34 typed (%CLS 34 expr (%CLS 36 typed (%CLS 36 iden numCalls%) int%) + (%CLS 38 typed (%CLS 38 num 1%) int%)%) int%)%) int%)%) int%) ; (%CLS 39 typed (%CLS 39 expr let (%CLS 40 iden p%) : int = (%CLS 43 typed (%CLS 43 num 1%) int%) in (%CLS 45 typed (%CLS 45 expr let (%CLS 46 iden i%) : int = (%CLS 49 typed (%CLS 49 num 1%) int%) in (%CLS 50 typed (%CLS 50 expr { (%CLS 51 exprseq (%CLS 52 typed (%CLS 52 expr while (%CLS 53 typed (%CLS 53 expr (%CLS 55 typed (%CLS 55 iden i%) int%) < (%CLS 57 typed (%CLS 57 iden n%) int%)%) boolean%) do (%CLS 58 typed (%CLS 58 expr { (%CLS 59 exprseq (%CLS 60 typed (%CLS 60 expr (%CLS 61 iden p%) = (%CLS 62 typed (%CLS 62 expr (%CLS 64 typed (%CLS 64 iden p%) int%) * (%CLS 66 typed (%CLS 66 iden i%) int%)%) int%)%) int%) ; (%CLS 67 typed (%CLS 67 expr (%CLS 68 iden i%) = (%CLS 69 typed (%CLS 69 expr (%CLS 71 typed (%CLS 71 iden i%) int%) + (%CLS 73 typed (%CLS 73 num 1%) int%)%) int%)%) int%)%) }%) int%)%) object%) ; (%CLS 74 typed (%CLS 74 expr print (%CLS 75 typed (%CLS 75 expr (%CLS 77 typed (%CLS 77 string \"Fact = \"%) string%) + (%CLS 79 typed (%CLS 79 iden p%) int%)%) string%)%) string%) ; (%CLS 80 typed (%CLS 80 expr print (%CLS 81 typed (%CLS 81 expr (%CLS 83 typed (%CLS 83 string \"#Calls = \"%) string%) + (%CLS 84 typed (%CLS 84 expr (%CLS 85 typed (%CLS 85 expr self%) Fact%) . (%CLS 86 iden getNumCalls%) ( )%) int%)%) string%)%) string%) ; (%CLS 88 typed (%CLS 88 iden p%) int%)%) }%) int%)%) int%)%) int%)%) }%) int%) ;%) }%)%)");
    }

    @Test(expected = SemanticException.class)
    public void testMissingArgument() throws Exception {
        t.testCase(
                "class X extends object\n" +
                        "{\n" +
                        "  g(x:int):int = x;\n" +
                        "  bad():int = 1 + self.g();\n" +
                        "}\n"
        );
    }

    @Test(expected = RuntimeException.class)
    public void testMismatchedParamInOverride() throws Exception {
        t.testCase("class X extends object {" +
                "  b(x: int): int = 4;" +
                "}" +
                "class Y extends X" +
                "{" +
                "  b(x: boolean): int = 5;" +
                "}");
    }

    @Test
    public void testShadowFieldWithParameter() throws Exception {
        t.testCase("class X extends object {" +
                        "  a: int;" +
                        "  b: int;" +
                        "  f(s: string, a: boolean): object = if a then self.g(b+1) else s;" +
                        "  g(a: int): int = a;" +
                        "}",
                "(%CLS 0 prog (%CLS 1 cls class (%CLS 2 type X%) extends (%CLS 3 type object%) { (%CLS 4 field (%CLS 5 iden a%) : (%CLS 6 type int%) ;%) (%CLS 7 field (%CLS 8 iden b%) : (%CLS 9 type int%) ;%) (%CLS 10 method (%CLS 11 iden f%) ( (%CLS 12 paramlist (%CLS 13 param (%CLS 14 iden s%) : (%CLS 15 type string%)%) , (%CLS 16 param (%CLS 17 iden a%) : (%CLS 18 type boolean%)%)%) ) : (%CLS 19 type object%) = (%CLS 20 typed (%CLS 20 expr if (%CLS 22 typed (%CLS 22 iden a%) boolean%) then (%CLS 23 typed (%CLS 23 expr (%CLS 24 typed (%CLS 24 expr self%) X%) . (%CLS 25 iden g%) ( (%CLS 26 arglist (%CLS 27 typed (%CLS 27 expr (%CLS 29 typed (%CLS 29 iden b%) int%) + (%CLS 31 typed (%CLS 31 num 1%) int%)%) int%)%) )%) int%) else (%CLS 33 typed (%CLS 33 iden s%) string%)%) object%) ;%) (%CLS 34 method (%CLS 35 iden g%) ( (%CLS 36 paramlist (%CLS 37 param (%CLS 38 iden a%) : (%CLS 39 type int%)%)%) ) : (%CLS 40 type int%) = (%CLS 42 typed (%CLS 42 iden a%) int%) ;%) }%)%)");
    }
}
