package com.constructiveactivists.organizationmodule.mappers.organization;

import com.constructiveactivists.organizationmodule.controllers.request.organization.OrganizationUpdateRequest;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationUpdateMapper {
    OrganizationEntity toDomain(OrganizationUpdateRequest organizationUpdateRequest);
}
