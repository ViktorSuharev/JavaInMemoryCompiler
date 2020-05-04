package com.visu.compiler.java.core.core;

import com.visu.compiler.java.core.filemanager.ClassFileManager;
import com.visu.compiler.java.core.source.StringJavaFileObject;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.ArrayList;
import java.util.List;

public class DynamicCompiler {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private JavaFileManager fileManager ;

    public DynamicCompiler(String fullName, String srcCode){
        this.fileManager = initFileManager();
    }

    public JavaFileManager initFileManager() {
        if (fileManager == null) {
            fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null));
        }

        return fileManager;
    }

    public void compile(List<JavaFileObject> jFiles){
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        compiler.getTask(null, fileManager, null, null, null, jFiles)
                .call();
    }
}
