package com.metasoft.veyra.platform.profiles.application.internal.outboundservices.storage;
import java.util.Map;



public interface StorageService {
    Map<String, String> upload(byte[] fileData, String fileName);
    void delete(String publicId);
}
