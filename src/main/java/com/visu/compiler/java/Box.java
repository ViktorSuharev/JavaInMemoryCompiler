package com.visu.compiler.java;

class Box<T extends Animal> {
    private T animal;

    void add(T t){
        this.animal = t;
    }
}

