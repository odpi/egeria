/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SpineAttribute;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Mapping methods to map between SpineAttribute and the omrs equivalents.
 */
public class SpineAttributeMapper extends ClassificationMapper{
    private static final Logger log = LoggerFactory.getLogger( SpineAttributeMapper.class);
    private static final String className = SpineAttributeMapper.class.getName();
    private static final String typeName = "SpineAttribute";

    public SpineAttributeMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }
    @Override
    protected Set<String> mapKnownAttributesToOmrs(Classification omasClassification, InstanceProperties omrsClassificationProperties) {
        return  new HashSet<>();
    }

    @Override
    protected String getTypeName() {
        return typeName;
    }

    @Override
    protected org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification createOmasClassification() {
        return new SpineAttribute();
    }
    @Override
    protected InstanceProperties updateOMRSAttributes(Classification omasClassification) {
        return new InstanceProperties();
    }
}
