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
import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.dto.BeaconDto;
import com.dnastack.bob.service.dto.BeaconResponseDto;
import com.dnastack.bob.service.dto.ChromosomeDto;
import com.dnastack.bob.service.dto.OrganizationDto;
import com.dnastack.bob.service.dto.QueryDto;
import com.dnastack.bob.service.dto.ReferenceDto;
import com.dnastack.bob.service.processor.api.BeaconResponse;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Convertor of entities to TOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class EntityDtoConvertor {

    /**
     * Converts a beacon to a beacon TO.
     *
     * @param b            beacon
     * @param showInternal
     *
     * @return beacon TO
     */
    public static BeaconDto getBeaconDto(Beacon b, boolean showInternal) {
        if (b == null) {
            return null;
        }

        BeaconDto beacon = new BeaconDto();
        beacon.setId(b.getId());
        beacon.setName(b.getName());
        beacon.setAggregator(b.getAggregator());
        beacon.setOrganization(b.getOrganization().getName());
        beacon.setDescription(b.getDescription());
        beacon.setEnabled(b.getEnabled());
        beacon.setVisible(b.getVisible());

        if (showInternal) {
            beacon.setEmail(b.getEmail());
            beacon.setHomePage(b.getHomePage());
            beacon.setUrl(b.getUrl());
            beacon.setSupportedReferences(getReferenceDtos(b.getSupportedReferences()));
        }

        return beacon;
    }

    public static Beacon getBeacon(BeaconDto b) {
        if (b == null) {
            return null;
        }

        Beacon beacon = new Beacon();
        beacon.setId(b.getId());
        beacon.setName(b.getName());
        beacon.setAggregator(b.isAggregator());
        beacon.setDescription(b.getDescription());
        beacon.setEnabled(b.isEnabled());
        beacon.setVisible(b.isVisible());

        beacon.setEmail(b.getEmail());
        beacon.setHomePage(b.getHomePage());
        beacon.setUrl(b.getUrl());
        beacon.setSupportedReferences(getReferences(b.getSupportedReferences()));

        return beacon;
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
        Set<BeaconDto> res = new HashSet<>();
        for (Beacon br : bs) {
            res.add(getBeaconDto(br, showInternal));
        }

        return res;
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
        Set<ReferenceDto> res = new HashSet<>();
        for (Reference br : qs) {
            res.add(getReferenceDto(br));
        }

        return res;
    }

    public static Set<Reference> getReferences(Collection<ReferenceDto> qs) {
        Set<Reference> res = new HashSet<>();
        for (ReferenceDto br : qs) {
            res.add(getReference(br));
        }

        return res;
    }

    /**
     * Converts a query to a query TO.
     *
     * @param q query
     *
     * @return query TO
     */
    public static QueryDto getQueryDto(Query q) {
        return (q == null) ? null : new QueryDto(getChromosomeDto(q.getChromosome()), q.getPosition(), q.getAllele(), getReferenceDto(q.getReference()));
    }

    /**
     * Converts a collection of queries to a collection of query TOs.
     *
     * @param qs queries
     *
     * @return query TOs
     */
    public static Set<QueryDto> getQueryDtos(Collection<Query> qs) {
        Set<QueryDto> res = new HashSet<>();
        for (Query br : qs) {
            res.add(getQueryDto(br));
        }

        return res;
    }

    /**
     * Converts a organization to a organization TO.
     *
     * @param o organization
     *
     * @return organization TO
     */
    public static OrganizationDto getOrganizationDto(Organization o) {
        return (o == null) ? null : new OrganizationDto(o.getId(), o.getName(), o.getDescription(), o.getUrl(), o.getAddress());
    }

    public static Organization getOrganization(OrganizationDto o) {
        return (o == null) ? null : new Organization(o.getId(), o.getName(), o.getDescription(), o.getUrl(), o.getAddress());
    }

    /**
     * Converts a collection of queries to a collection of organization TOs.
     *
     * @param qs queries
     *
     * @return organization TOs
     */
    public static Set<OrganizationDto> getOrganizationDtos(Collection<Organization> qs) {
        Set<OrganizationDto> res = new HashSet<>();
        for (Organization br : qs) {
            res.add(getOrganizationDto(br));
        }

        return res;
    }

    /**
     * Converts a beacon response to a beacon response TO.
     *
     * @param br beacon response
     *
     * @return beacon response TO
     */
    public static BeaconResponseDto getBeaconResponseDto(BeaconResponse br) {
        return (br == null) ? null : new BeaconResponseDto(getBeaconDto(br.getBeacon(), false), getQueryDto(br.getQuery()), br.getResponse());
    }

    /**
     * Converts a collection of beacon responses to a collection of beacon response TOs.
     *
     * @param brs beacon responses
     *
     * @return beacon response TOs
     */
    public static Set<BeaconResponseDto> getBeaconResponseDtos(Collection<BeaconResponse> brs) {
        Set<BeaconResponseDto> res = new HashSet<>();
        for (BeaconResponse br : brs) {
            res.add(getBeaconResponseDto(br));
        }

        return res;
    }
}
