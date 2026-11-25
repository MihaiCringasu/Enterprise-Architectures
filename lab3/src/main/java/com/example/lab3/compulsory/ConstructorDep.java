package com.example.lab3.compulsory;

import org.springframework.stereotype.Component;

@Component
public class ConstructorDep {
    public ConstructorDep() {
        System.out.println("[1] ConstructorDep instanțiat");
    }
}

