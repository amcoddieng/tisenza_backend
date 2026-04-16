package com.tissenza.tissenza_backend.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String resourceType;
    private final String resourceId;

    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(String.format("%s non trouvé(e) avec l'identifiant: %s", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String resourceType, Long resourceId) {
        this(resourceType, resourceId.toString());
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceType = "UNKNOWN";
        this.resourceId = "UNKNOWN";
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}
