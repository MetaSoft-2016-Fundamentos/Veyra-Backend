package com.metasoft.veyra.platform.profiles.infrastructure.storage.stub;

import com.metasoft.veyra.platform.profiles.application.internal.outboundservices.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@ConditionalOnProperty(name = "integrations.cloudinary.enabled", havingValue = "false")
public class DevStorageServiceStub implements StorageService {

    @Override
    public Map<String, String> upload(byte[] fileData, String fileName) {
        String publicId = "dev/" + UUID.randomUUID();
        String url = "https://localhost/dev-storage/" + publicId;
        log.info("Cloudinary integration disabled. Using dev storage stub for file: {}", fileName);
        return Map.of("url", url, "publicId", publicId);
    }

    @Override
    public void delete(String publicId) {
        log.info("Cloudinary integration disabled. Skipping delete for publicId: {}", publicId);
    }
}
