package atrai.interpreters.CLASSES;

import atrai.interpreters.common.SemanticException;

import java.util.LinkedHashMap;

class MethodType {
    private final ClassType thisType;
    private final String name;
    private final LinkedHashMap<String, String> paramTypes = new LinkedHashMap<>();
    private String returnType;

    public String getName() {
        return name;
    }

    MethodType(String name, ClassType thisType) {
        this.thisType = thisType;
        this.name = name;
    }

    void addParam(String paramName, String paramType) {
        if (getParamTypes().containsKey(paramName))
            throw new SemanticException("Cannot have duplicate parameter " + paramName + " in " + name + " of " + thisType.getTypeName() + ".");
        getParamTypes().put(paramName, paramType);
    }

    void setReturnType(String returnType) {
        if (this.returnType != null)
            throw new SemanticException("Cannot redefine return type of " + name + " in class " + thisType.getTypeName() + " to " + returnType + " from " + returnType + ".");
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": (");
        boolean first = true;
        for (String paramName : getParamTypes().keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            String paramType = getParamTypes().get(paramName);
            sb.append(paramName).append(":").append(paramType);
        }
        sb.append(") -> ").append(getReturnType());
        return sb.toString();
    }

    String getReturnType() {
        return returnType;
    }

    LinkedHashMap<String, String> getParamTypes() {
        return paramTypes;
    }

    int getNumParams() {
        return paramTypes.size();
    }
}
