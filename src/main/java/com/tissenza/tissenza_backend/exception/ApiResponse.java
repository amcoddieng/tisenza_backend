package com.tissenza.tissenza_backend.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String path;

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now(), null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Opération réussie");
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now(), null);
    }

    public static <T> ApiResponse<T> error(String message, String path) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now(), path);
    }
    
    // Méthode pour définir les données (utilisée dans GlobalExceptionHandler)
    public void setData(T data) {
        this.data = data;
    }
    
    // Méthode pour définir le chemin (utilisée dans GlobalExceptionHandler)
    public void setPath(String path) {
        this.path = path;
    }
}
