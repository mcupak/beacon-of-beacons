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
package com.dnastack.bob.service.util;

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Organization;
import com.dnastack.bob.persistence.entity.Query;
import com.dnastack.bob.persistence.entity.User;
import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.dto.BeaconDto;
import com.dnastack.bob.service.dto.BeaconResponseDto;
import com.dnastack.bob.service.dto.ChromosomeDto;
import com.dnastack.bob.service.dto.OrganizationDto;
import com.dnastack.bob.service.dto.QueryDto;
import com.dnastack.bob.service.dto.ReferenceDto;
import com.dnastack.bob.service.dto.UserDto;
import com.dnastack.bob.service.processor.api.BeaconResponse;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Convertor of entities to TOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class EntityDtoConverter {

    /**
     * Converts a beacon to a beacon TO.
     *
     * @param b            beacon
     * @param showInternal true if you want it to return internal fields, false otherwise
     *
     * @return beacon TO
     */
    public static BeaconDto getBeaconDto(Beacon b, boolean showInternal) {
        if (b == null) {
            return null;
        }

        BeaconDto.BeaconDtoBuilder builder = BeaconDto.builder().id(b.getId()).name(b.getName()).aggregator(b.getAggregator()).organization(b.getOrganization().getName()).description(b.getDescription()).enabled(b.getEnabled()).visible(b.getVisible());

        if (showInternal) {
            builder.email(b.getEmail()).homePage(b.getHomePage()).url(b.getUrl()).supportedReferences(getReferenceDtos(b.getSupportedReferences()));
        }

        return builder.build();
    }

    public static Beacon getBeacon(BeaconDto b) {
        if (b == null) {
            return null;
        }

        Beacon.BeaconBuilder builder = Beacon.builder().id(b.getId()).name(b.getName()).aggregator(b.isAggregator()).description(b.getDescription()).enabled(b.isEnabled()).visible(b.isVisible()).email(b.getEmail()).homePage(b.getHomePage()).url(b.getUrl()).supportedReferences(getReferences(b.getSupportedReferences()));

        return builder.build();
    }

    /**
     * Converts a collection of beacons to a collection of beacon TOs.
     *
     * @param bs           beacons
     * @param showInternal
     *
     * @return beacon TOs
     */
    public static Set<BeaconDto> getBeaconDtos(Collection<Beacon> bs, boolean showInternal) {
        return bs.parallelStream().map((Beacon br) -> getBeaconDto(br, showInternal)).collect(Collectors.toSet());
    }

    /**
     * Converts a chromosome to a chromosome TO.
     *
     * @param c chromosome
     *
     * @return chromosome TO
     */
    public static ChromosomeDto getChromosomeDto(Chromosome c) {
        return (c == null) ? null : ChromosomeDto.valueOf(c.name());
    }

    /**
     * Converts a reference to a reference TO.
     *
     * @param r reference
     *
     * @return reference TO
     */
    public static ReferenceDto getReferenceDto(Reference r) {
        return (r == null) ? null : ReferenceDto.valueOf(r.name());
    }

    public static Reference getReference(ReferenceDto r) {
        return (r == null) ? null : Reference.valueOf(r.name());
    }

    public static Set<ReferenceDto> getReferenceDtos(Collection<Reference> qs) {
        return qs.parallelStream().map((Reference br) -> getReferenceDto(br)).collect(Collectors.toSet());
    }

    public static Set<Reference> getReferences(Collection<ReferenceDto> qs) {
        return qs.parallelStream().map((ReferenceDto br) -> getReference(br)).collect(Collectors.toSet());
    }

    /**
     * Converts a query to a query TO.
     *
     * @param q query
     *
     * @return query TO
     */
    public static QueryDto getQueryDto(Query q) {
        return (q == null) ? null : QueryDto.builder().chromosome(getChromosomeDto(q.getChromosome())).position(q.getPosition()).allele(q.getAllele()).reference(getReferenceDto(q.getReference())).build();
    }

    /**
     * Converts a collection of queries to a collection of query TOs.
     *
     * @param qs queries
     *
     * @return query TOs
     */
    public static Set<QueryDto> getQueryDtos(Collection<Query> qs) {
        return qs.parallelStream().map((Query br) -> getQueryDto(br)).collect(Collectors.toSet());
    }

    /**
     * Converts a organization to a organization TO.
     *
     * @param o organization
     *
     * @return organization TO
     */
    public static OrganizationDto getOrganizationDto(Organization o) {
        return (o == null) ? null : OrganizationDto.builder().id(o.getId()).name(o.getName()).address(o.getAddress()).description(o.getDescription()).url(o.getUrl()).build();
    }

    /**
     * Converts an organization TO to aa organization.
     *
     * @param o organization TO
     *
     * @return organization
     */
    public static Organization getOrganization(OrganizationDto o) {
        return (o == null) ? null : Organization.builder().id(o.getId()).name(o.getName()).description(o.getDescription()).url(o.getUrl()).address(o.getAddress()).build();
    }

    /**
     * Converts a collection of queries to a collection of organization TOs.
     *
     * @param qs queries
     *
     * @return organization TOs
     */
    public static Set<OrganizationDto> getOrganizationDtos(Collection<Organization> qs) {
        return qs.parallelStream().map((Organization br) -> getOrganizationDto(br)).collect(Collectors.toSet());
    }

    /**
     * Converts a user to a user TO.
     *
     * @param o user
     *
     * @return user TO
     */
    public static UserDto getUserDto(User o) {
        return (o == null) ? null : UserDto.builder().userName(o.getUserName()).ipAddress(o.getIp()).build();
    }

    /**
     * Converts an user TO to an user.
     *
     * @param o user TO
     *
     * @return user
     */
    public static User getUser(UserDto o) {
        return (o == null) ? null : User.builder().userName(o.getUserName()).ip(o.getIpAddress()).build();
    }

    /**
     * Converts a collection of queries to a collection of user TOs.
     *
     * @param qs queries
     *
     * @return user TOs
     */
    public static Set<UserDto> getUserDtos(Collection<User> qs) {
        return qs.parallelStream().map((User br) -> getUserDto(br)).collect(Collectors.toSet());
    }

    /**
     * Converts a beacon response to a beacon response TO.
     *
     * @param br beacon response
     *
     * @return beacon response TO
     */
    public static BeaconResponseDto getBeaconResponseDto(BeaconResponse br) {
        return (br == null) ? null : BeaconResponseDto.builder().beacon(getBeaconDto(br.getBeacon(), false)).query(getQueryDto(br.getQuery())).response(br.getResponse()).build();
    }

    /**
     * Converts a collection of beacon responses to a collection of beacon response TOs.
     *
     * @param brs beacon responses
     *
     * @return beacon response TOs
     */
    public static Set<BeaconResponseDto> getBeaconResponseDtos(Collection<BeaconResponse> brs) {
        return brs.parallelStream().map((BeaconResponse br) -> getBeaconResponseDto(br)).collect(Collectors.toSet());
    }
}
