package com.constructiveactivists.volunteermodule.mappers.volunteer;

import com.constructiveactivists.volunteermodule.controllers.response.RankedOrganizationResponse;
import com.constructiveactivists.volunteermodule.models.RankedOrganization;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RankedOrganizationMapper {

    RankedOrganization toModel(RankedOrganizationResponse rankedOrganizationResponse);

    RankedOrganizationResponse toResponse(RankedOrganization rankedOrganization);

    List<RankedOrganizationResponse> toResponses(List<RankedOrganization> rankedOrganizations);
}
