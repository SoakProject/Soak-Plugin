package org.soak.generator.bukkit.classes;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BukkitClassGenerator {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Enter package/class full name");
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();
        if (input.toLowerCase().equals(input)) {
            System.out.println("Enter class simple name");
            String simpleName = scanner.nextLine();
            input = input + "." + simpleName;
        }
        Class<?> bukkitClass = Class.forName(input);
        System.out.println("generating: " + bukkitClass.getTypeName());
        System.out.println("----------");
        for (int i = 0; i < 15; i++) {
            System.out.println();
        }

        String packageName = bukkitClass.getPackageName();
        String simpleClassName = bukkitClass.getSimpleName();
        packageName = packageName.replaceAll("org.bukkit", "org.soak.wrapper");

        String soakClassName = "Soak" + simpleClassName;

        System.out.println("package " + packageName + ";");
        System.out.println();
        System.out.println("public class " + soakClassName + " implements " + simpleClassName + " {");

        for (Method method : bukkitClass.getDeclaredMethods()) {
            if (!Modifier.isAbstract(method.getModifiers())) {
                continue;
            }
            List<String> annotations = Arrays.stream(method.getDeclaredAnnotations())
                    .map(anno -> "@" + anno.annotationType().getSimpleName())
                    .toList();
            String returnType = method.getReturnType().getSimpleName();
            String methodName = method.getName();
            String parameters = Arrays.stream(method.getParameters())
                    .map(parameter -> parameter.getType().getSimpleName() + " " + parameter.getName())
                    .collect(Collectors.joining(", "));
            String parameterTypes = Arrays.stream(method.getParameterTypes())
                    .map(parameter -> parameter.getSimpleName() + ".class")
                    .collect(Collectors.joining(", "));


            for (String anno : annotations) {
                System.out.println("\t" + anno);
            }
            System.out.println("\t@Override");
            System.out.println("\tpublic " + returnType + " " + methodName + "(" + parameters + ") {");
            System.out.println("\t\tthrow NotImplementedException.createByLazy(" + simpleClassName + ".class, \"" + methodName + "\"" + (parameterTypes.isEmpty() ? "" : ", " + parameterTypes) + ");");
            System.out.println("\t}");
            System.out.println();
        }

        System.out.println("}");


    }
}
