package com.visu.compiler.java;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        CompileSourceInMemory compiler = new CompileSourceInMemory();
        String name = "HelloWorld";
        boolean result = compiler.compile(name, createCode());
        if (result) {
            compiler.run(name);
        }
    }

    private static String createCode() {
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

class Animal {}
class Cat extends Animal {}
class Dog extends Animal {}
class Box {}
