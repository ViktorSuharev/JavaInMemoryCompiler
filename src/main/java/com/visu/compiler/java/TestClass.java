package com.visu.compiler.java;

import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class TestClass {
    public void assertBoxCodeCompilation() throws Exception {
        CompileSourceInMemory compiler = new CompileSourceInMemory();
        JavaFileObject testing = new JavaSourceFromString("Testing", createTestingCode("String"));
        JavaFileObject animalClass = new JavaSourceFromString("Animal", createAnimalCode());
        JavaFileObject boxClass = new JavaSourceFromString("Box", createBoxCode());
        JavaFileObject catClass = new JavaSourceFromString("Cat", createCatCode());

        boolean result = compiler.compile(Arrays.asList(
                testing,
                animalClass,
                boxClass,
                catClass
        ));
        if (result) {
            System.out.println("Compiled successfully");
//            compiler.run(name);
        }
    }

    private String createTestingCode(String className) {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
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
