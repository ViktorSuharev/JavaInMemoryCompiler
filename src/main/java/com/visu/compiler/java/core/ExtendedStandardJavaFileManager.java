package com.visu.compiler.java.core;

import com.visu.compiler.java.core.compiled.JavaClassObject;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;

public class ExtendedStandardJavaFileManager {//extends ForwardingJavaFileManager<JavaFileManager> {
/*    private List<JavaClassObject> javaClassObject = new ArrayList<>();
    private DynamicClassLoader cl;

    *//**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     * @param cl
     *//*
    protected ExtendedStandardJavaFileManager(JavaFileManager fileManager, DynamicClassLoader cl) {
        super(fileManager);
        this.cl = cl;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(
            JavaFileManager.Location location,
            String className,
            JavaFileObject.Kind kind,
            FileObject sibling) {
        try {
            JavaClassObject innerClass = new JavaClassObject(className);
            javaClassObject.add(innerClass);
            cl.addCode(innerClass);
            return innerClass;
        } catch (Exception e) {
            throw new RuntimeException("Error while creating in-memory output file for " + className, e);
        }
    }

    @Override
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return cl;
    }*/
}
