/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
        return (us == null) ? null : us.parallelStream().map((User u) -> mapEntityToDto(u, showInternal)).collect(Collectors.toSet());
    }

    @Override
    public Set<User> mapDtosToEntities(Collection<UserDto> us) {
        return (us == null) ? null : us.parallelStream().map((UserDto u) -> mapDtoToEntity(u)).collect(Collectors.toSet());
    }

}
