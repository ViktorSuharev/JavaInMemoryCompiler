package com.visu.compiler.java;

import com.visu.compiler.java.core.CompileSourceInMemory;
import com.visu.compiler.java.core.source.StringJavaFileObject;

import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class TestClass {
    public void assertBoxCodeCompilation() throws Exception {
        CompileSourceInMemory compiler = new CompileSourceInMemory();
        JavaFileObject testing = new StringJavaFileObject(TestClass.class.getPackage().getName() + "/Testing", createTestingCode("Cat"));
//        JavaFileObject boxClass = new StringJavaFileObject(TestClass.class.getPackage().getName() + "/Box", createBoxCode());
//        JavaFileObject catClass = new StringJavaFileObject(TestClass.class.getPackage().getName() + "/Cat", createCatCode());
//        JavaFileObject animalClass = new StringJavaFileObject(TestClass.class.getPackage().getName() + "/Animal", createAnimalCode());

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        System.out.println(classLoader);
        boolean result = compiler.compile(
                Arrays.asList(
                        testing
//                        boxClass
                )
        );

        if (result) {
            System.out.println("Compiled successfully");
//            compiler.run(name);
        }
    }

    private String createTestingCode(String className) {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("package com.visu.compiler.java;");
        out.println("public class Testing {");
        out.println("  public static void main(String args[]) {");
        out.println("    Box<" + className + "> box = new Box<>();");
        out.println("    box.add(new " + className + "());");
        out.println("  }");
        out.println("}");
        out.close();

        return writer.toString();
    }

    private String createBoxCode() {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("package com.visu.compiler.java;");
        out.println("public class Box<T extends Animal> {");
        out.println("  private T animal;");
        out.println("  void add(T t) {");
        out.println("    this.animal = t;");
        out.println("  }");
        out.println("}");
        out.close();

        return writer.toString();
    }

    private String createAnimalCode() {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("public class Animal {");
        out.println("}");
        out.close();

        return writer.toString();
    }

    private String createCatCode() {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("public class Cat extends Animal {");
        out.println("}");
        out.close();

        return writer.toString();
    }

    private String createCode() {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("public class HelloWorld {");
        out.println("  public static void main(String args[]) {");
        out.println("    System.out.println(\"This is in another java file\");");
        out.println("  }");
        out.println("}");
        out.close();

        return writer.toString();
    }
}
