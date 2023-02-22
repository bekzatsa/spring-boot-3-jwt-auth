package com.bekzatsk.authservice.dto.mapper

import com.bekzatsk.authservice.dto.UserUpdateRequestDto
import com.bekzatsk.authservice.model.User
import org.mapstruct.*

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserPatchOperationMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun patch(userUpdateRequestDto: UserUpdateRequestDto, @MappingTarget user: User)
}
