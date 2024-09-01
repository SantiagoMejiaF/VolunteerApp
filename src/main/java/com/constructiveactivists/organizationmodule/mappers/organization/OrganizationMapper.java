package com.constructiveactivists.organizationmodule.mappers.organization;

import com.constructiveactivists.organizationmodule.controllers.request.organization.OrganizationRequest;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    OrganizationEntity toDomain (OrganizationRequest organizationRequest);
}
