package com.metasoft.veyra.platform.profiles.application.internal.commandservices;

import com.metasoft.veyra.platform.profiles.application.internal.outboundservices.storage.StorageService;
import com.metasoft.veyra.platform.profiles.domain.exceptions.BusinessNameAlreadyExists;
import com.metasoft.veyra.platform.profiles.domain.exceptions.RucAlreadyExistsException;
import com.metasoft.veyra.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.metasoft.veyra.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.BusinessName;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.Ruc;
import com.metasoft.veyra.platform.profiles.domain.services.BusinessProfileCommandService;
import com.metasoft.veyra.platform.profiles.infrastructure.persistence.jpa.repositories.BusinessProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class BusinessProfileCommandServiceImpl implements BusinessProfileCommandService {
    private final StorageService storageService;
private final BusinessProfileRepository businessProfileRepository;

    public BusinessProfileCommandServiceImpl(StorageService storageService, BusinessProfileRepository businessProfileRepository) {
        this.storageService = storageService;
        this.businessProfileRepository = businessProfileRepository;
    }

    @Override
    public Optional<BusinessProfile> handle(CreateBusinessProfileCommand command) {
        if (businessProfileRepository.existsByBusinessName(new BusinessName(command.businessName()))) {
            throw new BusinessNameAlreadyExists(command.businessName());
        } else if (businessProfileRepository.existsByRuc(new Ruc(command.ruc()))) {
            throw new RucAlreadyExistsException(command.ruc());
        }
        Map<String, String> uploadResult;
        try {
            uploadResult = storageService.upload(
                    command.photoData(),
                    command.photoFileName()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error uploading photo to storage: " + e.getMessage());

        }
        String photoUrl = uploadResult.get("url");
        String photoPublicId = uploadResult.get("publicId");


        var businessProfile = new BusinessProfile(command.businessName(), command.emailAddress(),command.phoneNumber(), command.street()
                , command.number(), command.city(), command.postalCode(), command.country()
                 , photoUrl, photoPublicId, command.ruc());
        try {
            businessProfileRepository.save(businessProfile);
        } catch (Exception e) {
            try {
                storageService.delete(photoPublicId);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Error while deleting uploaded photo after failed person profile save: " + ex.getMessage());
            }
            throw new IllegalArgumentException("Could not save person profile");
        }
        return Optional.of(businessProfile);
    }
}
