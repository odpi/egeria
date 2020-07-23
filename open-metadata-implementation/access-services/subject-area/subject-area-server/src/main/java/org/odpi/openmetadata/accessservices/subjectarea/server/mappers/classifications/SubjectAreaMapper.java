/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Mapping methods to map between SubjectArea and the omrs equivalents.
 */
public class SubjectAreaMapper extends ClassificationMapper{
    private static final Logger log = LoggerFactory.getLogger( SubjectAreaMapper.class);
    private static final String className = SubjectAreaMapper.class.getName();
    private static final String typeName = "SubjectArea";

    public SubjectAreaMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }
    @Override
    protected Set<String> mapKnownAttributesToOmrs(Classification omasClassification, InstanceProperties omrsClassificationProperties) {
        SubjectArea canonicalVocabulary = (SubjectArea)omasClassification;
        String stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(),"name",omrsClassificationProperties,"");
        canonicalVocabulary.setName(stringValue);
        return SubjectArea.getPropertyNames();
    }

    @Override
    protected String getTypeName() {
        return typeName;
    }

    @Override
    protected Classification createOmasClassification() {
        return new SubjectArea();
    }
    @Override
    protected InstanceProperties updateOMRSAttributes(Classification omasClassification) {
        InstanceProperties instanceProperties = new InstanceProperties();
        SubjectArea canonicalVocabulary = (SubjectArea)omasClassification;
        if (canonicalVocabulary.getName()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"name",canonicalVocabulary.getName(),"updateOMRSAttributes");
        }
        return instanceProperties;
    }
}
