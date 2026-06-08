package com.metasoft.veyra.platform.hcm.domain.model.valueobjects;

public record TypeOfContract(String typeOfContract) {
    public TypeOfContract{
        if (typeOfContract==null|| typeOfContract.isBlank()){
            throw  new IllegalArgumentException(" type of contract cannot be null or blank");
        }
    }
}
