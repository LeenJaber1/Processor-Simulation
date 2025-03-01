package org.example.utils;

import org.example.enums.SharedResource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager {
    private final Map<SharedResource, Boolean> resourceStatus = new ConcurrentHashMap<>();

    public ResourceManager() {
        for (SharedResource resource : SharedResource.values()) {
            resourceStatus.put(resource, true); // All resources start as available
        }
    }

    public synchronized boolean tryAcquire(SharedResource resource) {
        if (resourceStatus.get(resource)) {
            resourceStatus.put(resource, false); // Lock resource
            return true; // Acquired successfully
        }
        return false; // Resource is locked
    }

    // Release resource and notify all waiting threads
    public synchronized void release(SharedResource resource) {
        resourceStatus.put(resource, true);
    }

}
