package org.soak.plugin.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionHelper {

    public static <T> T runMethod(Object obj, String methodName, Object... parameters) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method foundMethod = Arrays
                .stream(obj.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .filter(method -> method.getParameterCount() == parameters.length)
                .filter(method -> {
                    Class<?>[] types = method.getParameterTypes();
                    for (int i = 0; i < types.length; i++) {
                        if (!types[i].isInstance(parameters[i])) {
                            return false;
                        }
                    }
                    return true;
                })
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("No method found of " + obj.getClass().getName() + "." + methodName + "(" + Arrays.stream(parameters).map(obj2 -> obj2.getClass().getSimpleName()).collect(Collectors.joining(", ")) + ")"));
        //noinspection unchecked
        return (T) foundMethod.invoke(obj, parameters);
    }
}
