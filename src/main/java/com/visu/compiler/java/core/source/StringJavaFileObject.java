package com.visu.compiler.java.core.source;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class StringJavaFileObject extends SimpleJavaFileObject {
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
