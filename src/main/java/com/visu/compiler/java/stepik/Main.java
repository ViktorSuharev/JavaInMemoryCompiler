package com.visu.compiler.java.stepik;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Main {
    public static void main(String[] args) throws Exception {
        testAnimal_success();
        testCat_success();
        testDog_success();
        testString_fail();
        testObject_fail();
    }

    private static void testAnimal_success() throws Exception {
        try {
            String result = testBoxCode("Animal");
        } catch (CompileException e) {
            System.out.println("Incorrect: Box doesn't support Animal type as a parameter");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private static void testCat_success() throws Exception {
        try {
            String result = testBoxCode("Cat");
        } catch (CompileException e) {
            System.out.println("Incorrect: Box doesn't support Cat type as a parameter");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private static void testDog_success() throws Exception {
        try {
            String result = testBoxCode("Dog");
        } catch (CompileException e) {
            System.out.println("Incorrect: Box doesn't support Dog type as a parameter");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private static void testString_fail() throws Exception {
        try {
            String result = testBoxCode("String");
            System.out.println("Incorrect: Box supports String type as a parameter");
            System.exit(0);
        } catch (CompileException e) {
        }
    }

    private static void testObject_fail() throws Exception {
        try {
            String result = testBoxCode("Object");
            System.out.println("Incorrect: Box supports Object type as a parameter");
            System.exit(0);
        } catch (CompileException e) {
        }
    }

    private static String testBoxCode(String className) throws Exception {
        RuntimeCompiler compiler = new RuntimeCompiler();
        JavaFileObject testing = new StringJavaFileObject("com.visu.compiler.java.stepik.Testing", createTestingCode(className));
        return compiler.compile(Collections.singletonList(testing));
    }

    private static final String TEST_CODE_TEMPLATE =
            "package com.visu.compiler.java.stepik;\n" +
                    "class Testing {\n" +
                    "    public static void test() {\n" +
                    "        Box<TYPE> box = new Box<>();\n" +
                    "        TYPE animal = new TYPE();\n" +
                    "        box.add(animal);\n" +
                    "        TYPE stored = box.get();\n" +
                    "        if (stored == animal) {\n" +
                    "            System.out.println(\"success\");\n" +
                    "        } else {\n" +
                    "            System.out.println(\"fail\");\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

    private static String createTestingCode(String className) {
        return TEST_CODE_TEMPLATE.replaceAll("TYPE", className);
    }
}

class RuntimeCompiler {
    public String compile(List<JavaFileObject> files) throws Exception {
        DynamicClassLoader classLoader = new DynamicClassLoader(Main.class.getClassLoader());
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null), classLoader);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, files);

        boolean compilationResult = task.call();
        if (!compilationResult) {
            throw new CompileException(getCompilationErrorMessage(diagnostics));
        }

        Class testingClass = Class.forName(fileManager.getCompiledCode().get(0).getClassName(), true, classLoader);
        Method method = testingClass.getDeclaredMethod("test");
        method.setAccessible(true);

        Object obj = method.invoke(null);
//        return obj.toString();

        return "stop";
    }

    private String getCompilationErrorMessage(DiagnosticCollector<JavaFileObject> diagnostics) {
        StringBuilder sb = new StringBuilder();
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            sb.append("[")
                    .append("code: ").append(diagnostic.getCode()).append("\n")
                    .append("message: ").append(diagnostic.getMessage(null))
                    .append("]").append("\n");
        }

        return sb.toString();
    }
}

class DynamicClassLoader extends ClassLoader {
    private Map<String, JavaClassObject> customCompiledCode = new HashMap<>();
    private ClassLoader parent;

    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
        this.parent = parent;
    }

    public void addCode(JavaClassObject cc) {
        customCompiledCode.put(cc.getName(), cc);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        JavaClassObject cc = customCompiledCode.get(name);
        if (cc == null) {
            return findClass(name);
        }
        byte[] byteCode = cc.getByteCode();
        return defineClass(name, byteCode, 0, byteCode.length);
    }
}

class ClassFileManager extends ForwardingJavaFileManager {
    private List<JavaClassObject> compiledCode = new ArrayList<>();
    private DynamicClassLoader cl;

    protected ClassFileManager(JavaFileManager fileManager, DynamicClassLoader cl) {
        super(fileManager);
        this.cl = cl;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(
            JavaFileManager.Location location, String className,
            JavaFileObject.Kind kind, FileObject sibling) throws IOException {

        try {
            JavaClassObject innerClass = new JavaClassObject(className);
            compiledCode.add(innerClass);
            cl.addCode(innerClass);
            return innerClass;
        } catch (Exception e) {
            throw new RuntimeException("Error while creating in-memory output file for " + className, e);
        }
    }

    @Override
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return cl;
    }

    public List<JavaClassObject> getCompiledCode() {
        return new ArrayList<>(compiledCode);
    }
}

class JavaClassObject extends SimpleJavaFileObject {
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private String className;

    public JavaClassObject(String className) throws Exception {
        super(new URI(className), Kind.CLASS);
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return baos;
    }

    public byte[] getByteCode() {
        return baos.toByteArray();
    }
}

class StringJavaFileObject extends SimpleJavaFileObject {
    final String content;

    public StringJavaFileObject(String className, String content) {
        super(URI.create("string:///" + className.replace('.','/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.content = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return content;
    }
}

class CompileException extends Exception {
    public CompileException(String message) {
        super(message);
    }
}

class Box<T extends Animal> {
    private T animal;

    void add(T t) {
        this.animal = t;
    }

    T get() {
        return this.animal;
    }
}

class Animal {}
class Cat extends Animal {}
class Dog extends Animal {}
