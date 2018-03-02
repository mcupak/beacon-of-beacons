/* 
 * Copyright 2018 DNAstack.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dnastack.bob.service.mapper.impl;

import com.dnastack.bob.persistence.entity.User;
import com.dnastack.bob.service.dto.UserDto;
import com.dnastack.bob.service.mapper.api.UserMapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of a mapper of users to their DTOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class UserMapperImpl implements UserMapper {

    private static final long serialVersionUID = -6151540547488232872L;

    @Override
    public User mapDtoToEntity(UserDto o) {
        return (o == null) ? null : User.builder().userName(o.getUserName()).build();
    }

    @Override
    public UserDto mapEntityToDto(User u, boolean showInternal) {
        return (u == null) ? null : UserDto.builder().userName(u.getUserName()).build();
    }

    @Override
    public User mapEntityToEntity(User u) {
        return (u == null) ? null : User.builder().userName(u.getUserName()).build();
    }

    @Override
    public UserDto mapDtoToDto(UserDto u) {
        return (u == null) ? null : UserDto.builder().userName(u.getUserName()).build();
    }

    @Override
    public Set<UserDto> mapEntitiesToDtos(Collection<User> us, boolean showInternal) {
        return (us == null)
               ? null
               : us.parallelStream().map((User u) -> mapEntityToDto(u, showInternal)).collect(Collectors.toSet());
    }

    @Override
    public Set<User> mapDtosToEntities(Collection<UserDto> us) {
        return (us == null)
               ? null
               : us.parallelStream().map((UserDto u) -> mapDtoToEntity(u)).collect(Collectors.toSet());
    }

}
