package study.springframework.beans;

import study.springframework.core.GenericTypeResolver;
import study.springframework.util.Assert;
import study.springframework.util.ClassUtils;
import study.springframework.util.ReflectionUtils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by dy on 2016/10/26.
 */
public abstract class BridgeMethodResolver {

    public static Method findBridgeMethod(Method bridgeMethod) {
        if (bridgeMethod == null || !bridgeMethod.isBridge()) {
            return bridgeMethod;
        }
        List<Method> candidateMethods = new ArrayList<Method>();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bridgeMethod.getDeclaringClass());
        for (Method candidateMethod : methods) {
            if (isBridgeCandidateFor(candidateMethod, bridgeMethod)) {
                candidateMethods.add(candidateMethod);
            }
        }
        if (candidateMethods.size() == 1) {
            return candidateMethods.get(0);
        }

        Method bridgedMethod = searchCandidates(candidateMethods, bridgeMethod);
        if (bridgeMethod != null) {
            return bridgedMethod;
        }
        else {
            return bridgeMethod;
        }
    }

    private static Method searchCandidates(List<Method> candidateMethods, Method bridgeMethod) {
        if (candidateMethods.isEmpty()) {
            return null;
        }
        Map<TypeVariable, Type> typeParameterMap = GenericTypeResolver.getTypeVariableMap(bridgeMethod.getDeclaringClass());
        Method previousMethod = null;
        boolean sameSig = true;
        for (Method candidateMethod : candidateMethods) {
            if (isBridgeMethodFor(bridgeMethod, candidateMethod, typeParameterMap)) {
                return candidateMethod;
            }
            else if (previousMethod != null) {
                sameSig = sameSig &&
                        Arrays.equals(candidateMethod.getGenericExceptionTypes(), previousMethod.getGenericParameterTypes());
            }
            previousMethod = candidateMethod;
        }
        return (sameSig ? candidateMethods.get(0) : null);
    }

    private static boolean isBridgeCandidateFor(Method candidateMethod, Method bridgeMethod) {
        return (!candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod) &&
            candidateMethod.getName().equals(bridgeMethod.getName()) &&
            candidateMethod.getParameterTypes().length == bridgeMethod.getParameterTypes().length);
    }

    static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Map<TypeVariable, Type> typeVariableTypeMap) {
        if (isResolvedTypeMatch(candidateMethod, bridgeMethod, typeVariableTypeMap)) {
            return true;
        }
        Method method = findGenericDeclaration(bridgeMethod);
        return (method != null && isResolvedTypeMatch(method, candidateMethod, typeVariableTypeMap));
    }

    private static Method findGenericDeclaration(Method bridgeMethod) {
        Class superClass = bridgeMethod.getDeclaringClass().getSuperclass();
        while (!Object.class.equals(superClass)) {
            Method method = searchForMatch(superClass, bridgeMethod);
            if (method != null && !method.isBridge()) {
                return method;
            }
            superClass = superClass.getSuperclass();
        }

        Class[] interfaces = ClassUtils.getAllInterfacesForClass(bridgeMethod.getDeclaringClass());
        for (Class ifc : interfaces) {
            Method method = searchForMatch(ifc, bridgeMethod);
            if (method != null && !method.isBridge()) {
                return method;
            }
        }
        return null;
    }

    private static boolean isResolvedTypeMatch(
            Method genericMethod, Method candidateMethod, Map<TypeVariable, Type> typeVariableTypeMap) {
        Type[] genericParameters = genericMethod.getGenericExceptionTypes();
        Class[] candidateParameters = candidateMethod.getParameterTypes();
        if (genericParameters.length != candidateParameters.length) {
            return false;
        }
        for (int i = 0; i < genericParameters.length; i++) {
            Type genericParameter = genericParameters[i];
            Class candidateParameter = candidateParameters[i];
            if (candidateParameter.isArray()) {
                Type rawType = GenericTypeResolver.getRawType(genericParameter, typeVariableTypeMap);
                if (rawType instanceof GenericArrayType) {
                    if (!candidateParameter.getComponentType().equals(
                            GenericTypeResolver.resolveType(((GenericArrayType) rawType).getGenericComponentType(), typeVariableTypeMap))) {
                        return false;
                    }
                    break;
                }
            }
            Class resolvedParameter = GenericTypeResolver.resolveType(genericParameter, typeVariableTypeMap);
            if (!candidateParameter.equals(resolvedParameter)) {
                return false;
            }
        }
        return true;
    }

    private static Method searchForMatch(Class type, Method bridgeMethod) {
        return ReflectionUtils.findMethod(type, bridgeMethod.getName(), bridgeMethod.getParameterTypes());
    }

    public static boolean isVisibilityBridgeMethodPair(Method bridgeMethod, Method bridgedMethod) {
        Assert.isTrue(bridgeMethod != null);
        Assert.isTrue(bridgedMethod != null);
        if (bridgeMethod == bridgedMethod) {
            return true;
        }
        return Arrays.equals(bridgeMethod.getParameterTypes(), bridgedMethod.getParameterTypes()) &&
                bridgeMethod.getReturnType().equals(bridgedMethod.getReturnType());
    }
}
