package com.metasoft.veyra.platform.profiles.application.internal.commandservices;

import com.metasoft.veyra.platform.profiles.application.internal.outboundservices.storage.StorageService;
import com.metasoft.veyra.platform.profiles.domain.exceptions.PersonProfileNotFoundException;
import com.metasoft.veyra.platform.profiles.domain.model.aggregates.PersonProfile;
import com.metasoft.veyra.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.metasoft.veyra.platform.profiles.domain.model.commands.DeletePersonProfileCommand;
import com.metasoft.veyra.platform.profiles.domain.model.commands.UpdatePersonProfileCommand;
import com.metasoft.veyra.platform.profiles.domain.services.PersonProfileCommandService;
import com.metasoft.veyra.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PersonProfileCommandServiceImpl implements PersonProfileCommandService {
    private final PersonProfileRepository personProfileRepository;
    private final StorageService storageService;
    public PersonProfileCommandServiceImpl(PersonProfileRepository personProfileRepository, StorageService storageService) {
        this.personProfileRepository = personProfileRepository;

        this.storageService = storageService;
    }

    @Override
    public Optional<PersonProfile> handle(CreatePersonProfileCommand command) {

        Map<String, String> uploadResult;
        try {
            uploadResult = storageService.upload(
                    command.photoData(),
                    command.photoFileName()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error uploading photo to storage: " + e.getMessage());

        }
        String photoUrl=uploadResult.get("url");
        String photoPublicId=uploadResult.get("publicId");

        var personProfile= new PersonProfile(command.dni(), command.firstName(), command.lastName(), command.birthDate(), command.Age()
                , command.emailAddress(), command.street(), command.number(), command.city(), command.postalCode(), command.country()
                , photoUrl,photoPublicId, command.phoneNumber());

        try {
            personProfileRepository.save(personProfile);
        }catch (Exception e) {
            try {
                storageService.delete(photoPublicId);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Error while deleting uploaded photo after failed person profile save: " + ex.getMessage());
            }
            throw new IllegalArgumentException("Could not save person profile");
        }
        return Optional.of(personProfile);
    }

    @Override
    public Optional<PersonProfile> handle(UpdatePersonProfileCommand command) {
        var result = personProfileRepository.findById(command.personProfileId());

        if (result.isEmpty()) {
            throw new PersonProfileNotFoundException(command.personProfileId());
        }

        var personProfile = result.get();
        String oldPhotoPublicId = null;
        String newPhotoUrl ;
        String newPhotoPublicId ;

        if (command.hasNewPhoto()) {
            oldPhotoPublicId = personProfile.getPhoto().photoPublicId();

            Map<String, String> uploadResult;
            try {
                uploadResult = storageService.upload(
                        command.photoData(),
                        command.photoFileName()
                );
            } catch (Exception e) {
                throw new IllegalArgumentException("Error uploading new photo to storage: " + e.getMessage());
            }

            newPhotoUrl = uploadResult.get("url");
            newPhotoPublicId = uploadResult.get("publicId");
        } else {
            newPhotoUrl = personProfile.getPhoto().photoUrl();
            newPhotoPublicId = personProfile.getPhoto().photoPublicId();
        }

        try {
            var updatedPersonProfile = personProfile.updatePersonProfile(
                    command.dni(),
                    command.firstName(),
                    command.lastName(),
                    command.birthDate(),
                    command.age(),
                    command.emailAddress(),
                    command.street(),
                    command.number(),
                    command.city(),
                    command.postalCode(),
                    command.country(),
                    newPhotoUrl,
                    newPhotoPublicId,
                    command.phoneNumber()
            );

            personProfileRepository.save(updatedPersonProfile);

            if (oldPhotoPublicId != null) {
                try {
                    storageService.delete(oldPhotoPublicId);
                } catch (Exception e) {

                    System.err.println("Warning: Could not delete old photo from storage: " + e.getMessage());
                }
            }

            return Optional.of(updatedPersonProfile);

        } catch (Exception e) {
            if (command.hasNewPhoto() && newPhotoPublicId != null) {
                try {
                    storageService.delete(newPhotoPublicId);
                } catch (Exception ex) {
                    System.err.println("Warning: Could not delete new uploaded photo after failed update: " + ex.getMessage());
                }
            }
            throw new IllegalArgumentException("Could not update person profile: " + e.getMessage());
        }
    }

    @Override
    public void handle(DeletePersonProfileCommand command) {
        if(!personProfileRepository.existsById(command.personProfileId())) {
            throw new IllegalArgumentException("Could not find person profile with id "+command.personProfileId());
        }
        try {
            personProfileRepository.deleteById(command.personProfileId());
        }catch (Exception e) {
            throw new IllegalArgumentException("Error while delete person profile:%s");
        }
    }

}
