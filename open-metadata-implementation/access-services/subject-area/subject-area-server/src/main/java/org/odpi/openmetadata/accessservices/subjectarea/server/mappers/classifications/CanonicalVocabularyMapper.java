/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.CanonicalVocabulary;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Mapping methods to map between CanonicalVocabulary and the omrs equivalents.
 */
public class CanonicalVocabularyMapper extends ClassificationMapper{
    private static final Logger log = LoggerFactory.getLogger( CanonicalVocabularyMapper.class);
    private static final String className = CanonicalVocabularyMapper.class.getName();
    private static final String typeName = "CanonicalVocabulary";

    public CanonicalVocabularyMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }
    @Override
    protected Set<String> mapKnownAttributesToOmrs(org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification, InstanceProperties omrsClassificationProperties) {
        CanonicalVocabulary canonicalVocabulary = (CanonicalVocabulary)omasClassification;
        String stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(),"scope",omrsClassificationProperties,"");
        canonicalVocabulary.setScope(stringValue);
        return CanonicalVocabulary.PROPERTY_NAMES_SET;
    }
    @Override
    protected org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification createOmasClassification() {
        return new CanonicalVocabulary();
    }

    @Override
    protected String getTypeName() {
        return typeName;
    }
    @Override
    protected InstanceProperties updateOMRSAttributes(org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification) {

        InstanceProperties instanceProperties = new InstanceProperties();
        CanonicalVocabulary canonicalVocabulary = (CanonicalVocabulary)omasClassification;
        if (canonicalVocabulary.getScope()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"scope",canonicalVocabulary.getScope(),"updateOMRSAttributes");
        }
        return instanceProperties;
    }
}
