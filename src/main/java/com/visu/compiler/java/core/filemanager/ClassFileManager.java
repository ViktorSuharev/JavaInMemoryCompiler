package com.visu.compiler.java.core.filemanager;

import com.visu.compiler.java.core.compiled.JavaClassObject;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.security.SecureClassLoader;

public class ClassFileManager extends ForwardingJavaFileManager {
    private JavaClassObject jclassObject;

    public ClassFileManager(StandardJavaFileManager standardManager) {
        super(standardManager);
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return new SecureClassLoader() {
            @Override
            protected Class<?> findClass(String name) {
                byte[] b = jclassObject.getBytes();
                return super.defineClass(name, jclassObject.getBytes(), 0, b.length);
            }
        };
    }

    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        jclassObject = new JavaClassObject(className, kind);
        return jclassObject;
    }
}
