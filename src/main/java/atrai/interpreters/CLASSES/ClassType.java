package atrai.interpreters.CLASSES;

import atrai.interpreters.common.SemanticException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClassType {
    private final String typeName;
    private final ClassType superType;
    private final Map<String, MethodType> methodTypes = new HashMap<>();
    private final Map<String, String> fieldTypes = new HashMap<>();

    ClassType(String typeName, ClassType superType) {
        this.typeName = typeName;
        this.superType = superType;
    }

    void addField(String fieldName, String fieldType) {
        if (fieldTypes.containsKey(fieldName)) {
            throw new SemanticException("Cannot redefine " + fieldName + " in " + typeName + ".");
        }
        fieldTypes.put(fieldName, fieldType);
    }

    void addMethod(String methodName) {
        if (methodTypes.containsKey(methodName)) {
            throw new SemanticException("Cannot redefine " + methodName + " in " + typeName + ".");
        }
        methodTypes.put(methodName, new MethodType(methodName, this));
    }

    ClassType getSuperType() {
        return superType;
    }

    MethodType getMethod(String methodName) {
        return methodTypes.get(methodName);
    }

    Set<String> getMethodNames() {
        return methodTypes.keySet();
    }

    Set<String> getFieldNames() {
        return fieldTypes.keySet();
    }

    boolean hasField(String fieldName) {
        return fieldTypes.containsKey(fieldName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ").append(typeName);
        if (superType != null) {
            sb.append(" extends ").append(superType.typeName);
        }
        sb.append("{\n");
        for (String fieldName : fieldTypes.keySet()) {
            String fieldType = fieldTypes.get(fieldName);
            sb.append('\t').append(fieldName).append(": ").append(fieldType).append(";\n");
        }
        for (String methodName : methodTypes.keySet()) {
            MethodType methodType = methodTypes.get(methodName);
            sb.append('\t').append(methodType.toString()).append(";\n");
        }
        sb.append("}");
        return sb.toString();
    }

    String getTypeName() {
        return typeName;
    }
}
