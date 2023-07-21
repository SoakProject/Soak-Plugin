import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ImplementedPercent {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new RuntimeException("Requires 2 arguments\n- The paper-api.jar path\n- The soak source code inside src/main");
        }
        File paperPath = new File(args[0]);
        File soakPath = new File(args[1], "org/soak/wrapper");

        JarFile paperJar = new JarFile(paperPath);
        Set<String> entriesWithoutFile = new HashSet<>();
        Set<String> entriesImplementedElseware = new HashSet<>();
        Map<Class<?>, File> entriesWithFile = new HashMap<>();

        Map<String, Double> files = paperJar
                .stream()
                .filter(entry -> !entry.isDirectory())
                .filter(entry -> entry.getName().startsWith("org/bukkit"))
                .filter(entry -> !entry.getName().contains("$"))
                .<Map.Entry<Class<?>, File>>map(entry -> {
                    String fullName = entry.getName();
                    Class<?> bukkitClass;
                    try {
                        bukkitClass = Class.forName(fullName.substring(0, fullName.length() - 6).replaceAll("/", "."));
                    } catch (ExceptionInInitializerError e) {
                        System.err.println("Could not load: '" + fullName.substring(0, fullName.length() - 6).replaceAll("/", ".") + "' due to " + e.getException().getMessage());
                        return null;
                    } catch (Throwable e) {
                        System.err.println("Could not load: '" + fullName.substring(0, fullName.length() - 6).replaceAll("/", ".") + "' due to " + e.getMessage());
                        return null;
                    }
                    if (!bukkitClass.isInterface()) {
                        return null;
                    }

                    String classPath = fullName.replaceAll("org/bukkit", "").replaceAll(".class", ".java");
                    var index = classPath.lastIndexOf("/");
                    String path = classPath.substring(0, index);
                    if (!path.endsWith("/")) {
                        path = path + "/";
                    }
                    String soakClassPath = path + "Soak" + classPath.substring(index + 1);

                    File classFile = new File(soakPath, soakClassPath);
                    if (!classFile.exists()) {
                        entriesWithoutFile.add(fullName);
                        return null;
                    }
                    entriesWithFile.put(bukkitClass, classFile);
                    return new AbstractMap.SimpleEntry<>(bukkitClass, classFile);
                })
                .filter(Objects::nonNull)
                .map(entry -> {
                    Class<?> bukkitClass = entry.getKey();
                    File classFile = entry.getValue();
                    String fullName = classFile.getName();

                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(classFile)));
                        List<String> fileLines = br.lines().toList();
                        Collection<Method> methods = getAllMethodsFor(bukkitClass, new LinkedList<>(), entriesWithFile.keySet(), entriesImplementedElseware);

                        List<Boolean> hasImplemented = new LinkedList<>();
                        for (Method method : methods) {
                            Optional<String> opLine = fileLines.stream().filter(line -> line.contains("public ")).filter(line -> line.contains(method.getName())).findFirst();
                            if (opLine.isEmpty()) {
                                continue;
                            }
                            boolean hasParameters = Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).allMatch(type -> opLine.get().contains(type));
                            if (!hasParameters) {
                                continue;
                            }
                            int lineIndex = fileLines.indexOf(opLine.get());
                            String t = fileLines.get(lineIndex + 1);
                            hasImplemented.add(!t.contains("Lazy"));
                        }
                        OptionalDouble average = hasImplemented.stream().mapToInt(m -> m ? 100 : 0).average();
                        if (average.isPresent()) {
                            return new AbstractMap.SimpleEntry<>(fullName, average.getAsDouble());
                        }
                    } catch (FileNotFoundException e) {
                        System.err.println("Could not read soak file: '" + classFile.getPath() + "'");
                        return new AbstractMap.SimpleEntry<>(fullName, -1.0);

                    }
                    return new AbstractMap.SimpleEntry<>(fullName, -1.0);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Double> missingFilesPercent = entriesWithoutFile
                .stream()
                .filter(entry -> !entriesImplementedElseware.contains(entry.replaceAll("/", ".").replaceAll(".class", "")))
                .collect(Collectors.toMap(entry -> entry, entry -> 0.0));

        files.putAll(missingFilesPercent);
        DecimalFormat format = new DecimalFormat("#.##");

        var successfulFiles = files.entrySet().stream().filter(entry -> entry.getValue() >= 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        successfulFiles.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach((entry) -> System.out.println("File: " + entry.getKey() + ": " + format.format(entry.getValue()) + "%"));
        double successfulAverage = successfulFiles.values().stream().mapToDouble(i -> i).average().orElseThrow(() -> new RuntimeException("Could not generate average"));
        System.out.println("org.bukkit interfaces implemented: " + format.format(successfulAverage) + "%");
    }

    private static Collection<Method> getAllMethodsFor(Class<?> clazz, Collection<Method> current, Collection<Class<?>> entriesWithFiles, Collection<String> entriesFound) {
        entriesFound.add(clazz.getName());
        current.addAll(Arrays.stream(clazz.getDeclaredMethods()).filter(method -> Modifier.isAbstract(method.getModifiers())).collect(Collectors.toSet()));
        for (Class<?> clazz1 : clazz.getClasses()) {
            if (entriesWithFiles.contains(clazz1)) {
                continue;
            }
            getAllMethodsFor(clazz1, current, entriesWithFiles, entriesFound);
        }
        return current;
    }
}
