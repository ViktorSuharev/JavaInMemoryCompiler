package com.visu.compiler.java.core;

import com.visu.compiler.java.core.compiled.JavaClassObject;

import java.util.HashMap;
import java.util.Map;

public class DynamicClassLoader extends ClassLoader {
/*    private Map<String, JavaClassObject> customCompiledCode = new HashMap<>();

    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void addCode(JavaClassObject cc) {
        customCompiledCode.put(cc.getName(), cc);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        JavaClassObject cc = customCompiledCode.get(name);
        if (cc == null) {
            return super.findClass(name);
        }
        byte[] byteCode = cc.getByteCode();
        return defineClass(name, byteCode, 0, byteCode.length);
    }*/
}
