package com.metasoft.veyra.platform.health.domain.model.commands;

public record RegisterAllergyCommand(Long residentId,String reaction,
                                     String allergenName,
                                     String typeOfAllergy,
                                     String severityLevel) {
    public RegisterAllergyCommand{
        if (residentId==null){
            throw new IllegalArgumentException("resident id cannot be null");
        }
        if (allergenName==null|| allergenName.isBlank()){throw new IllegalArgumentException("allergen name cannot be null or blank");}
        if (typeOfAllergy==null||typeOfAllergy.isBlank()){throw new IllegalArgumentException("typeOfAllergy cannot be null or blank");}
        if (severityLevel==null || severityLevel.isBlank()){throw new IllegalArgumentException("severity level cannot be null or blank");}
        if (reaction==null || reaction.isBlank()){throw new IllegalArgumentException("reaction cannot be null or blank");}

    }
}
