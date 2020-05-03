package com.visu.compiler.java;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class CompileSourceInMemory {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

    public boolean compile(List<JavaFileObject> files) {
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, files);

        boolean result = task.call();
        if (!result) {
            printCompilationError(diagnostics);
        }

        return result;
    }

    public void run(String name) throws Exception {
        try {
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("").toURI().toURL() });
            Class.forName(name, true, classLoader)
                    .getDeclaredMethod("main", new Class[] { String[].class })
                    .invoke(null, new Object[] { null });
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e);
        } catch (NoSuchMethodException e) {
            System.err.println("No such method: " + e);
        } catch (IllegalAccessException e) {
            System.err.println("Illegal access: " + e);
        } catch (InvocationTargetException e) {
            System.err.println("Invocation target: " + e);
        }
    }

    private void printCompilationError(DiagnosticCollector<JavaFileObject> diagnostics) {
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.getCode());
            System.out.println(diagnostic.getKind());
            System.out.println(diagnostic.getPosition());
            System.out.println(diagnostic.getStartPosition());
            System.out.println(diagnostic.getEndPosition());
            System.out.println(diagnostic.getSource());
            System.out.println(diagnostic.getMessage(null));
        }
    }
}
