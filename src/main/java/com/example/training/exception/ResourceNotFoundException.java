package com.example.training.exception;

public class ResourceNotFoundException
        extends RuntimeException {

    // ① コンストラクタ
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
