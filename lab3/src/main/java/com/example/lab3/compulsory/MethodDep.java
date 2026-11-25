package com.example.lab3.compulsory;

import org.springframework.stereotype.Component;

@Component
public class MethodDep {
    public MethodDep() {
        System.out.println("[1] MethodDep instanțiat");
    }
}
