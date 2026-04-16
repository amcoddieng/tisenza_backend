package com.tissenza.tissenza_backend.controller;

import com.tissenza.tissenza_backend.exception.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected <T> ResponseEntity<ApiResponse<T>> successResponse(T data, String message) {
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> successResponse(T data) {
        return ResponseEntity.ok(ApiResponse.success(data, "Opération réussie"));
    }

    protected <T> ResponseEntity<ApiResponse<T>> createdResponse(T data, String message) {
        return new ResponseEntity<>(ApiResponse.success(data, message), HttpStatus.CREATED);
    }

    protected <T> ResponseEntity<ApiResponse<T>> notFoundResponse(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> errorResponse(String message) {
        return ResponseEntity.badRequest().body(ApiResponse.error(message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> conflictResponse(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(message));
    }
}