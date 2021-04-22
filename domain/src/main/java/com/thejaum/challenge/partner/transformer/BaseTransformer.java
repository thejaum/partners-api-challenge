package com.thejaum.challenge.partner.transformer;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public abstract class BaseTransformer<DTO, ENTITY>{

    public DTO toDtoMapper(ENTITY entity,Class<DTO> dtoReference) {
        DTO dto = getModelMapper().map(entity, dtoReference);
        dto = afterToDtoMapper(dto);
        return dto;
    }
    public DTO afterToDtoMapper(DTO dto){
        return dto;
    }

    public ENTITY toEntityMapper(DTO dto,Class<ENTITY> entityReference) {
        ENTITY entity = getModelMapper().map(dto, entityReference);
        entity = afterToEntityMapper(entity);
        return entity;
    }
    private ENTITY afterToEntityMapper(ENTITY entity){
        return entity;
    }

    private ModelMapper getModelMapper() {
        ModelMapper model = new ModelMapper();
        model.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        model.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return model;
    }
}
