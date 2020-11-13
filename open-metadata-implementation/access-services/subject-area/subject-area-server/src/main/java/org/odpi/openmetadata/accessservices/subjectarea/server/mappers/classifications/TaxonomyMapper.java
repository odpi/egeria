/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Taxonomy;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;


/**
 * Mapping methods to map between Taxonomy and the omrs equivalents.
 */
public class TaxonomyMapper extends ClassificationMapper{
    private static final Logger log = LoggerFactory.getLogger( TaxonomyMapper.class);
    private static final String className = TaxonomyMapper.class.getName();
    private static final String typeName = "Taxonomy";

    public TaxonomyMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }
    @Override
    protected Set<String> mapKnownAttributesToOmrs(Classification omasClassification, InstanceProperties omrsClassificationProperties) {
        Taxonomy taxonomy = (Taxonomy)omasClassification;
        String stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(),"organizingPrinciple",omrsClassificationProperties,"");
        taxonomy.setOrganizingPrinciple(stringValue);
        return Taxonomy.getPropertyNames();
    }

    @Override
    protected String getTypeName() {
        return typeName;
    }

    @Override
    protected Classification createOmasClassification() {
        return new Taxonomy();
    }
    @Override
    protected InstanceProperties updateOMRSAttributes(Classification omasClassification) {
        InstanceProperties instanceProperties = new InstanceProperties();
        Taxonomy taxonomy = (Taxonomy)omasClassification;
        if (taxonomy.getOrganizingPrinciple()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,("organizingPrinciple"),taxonomy.getOrganizingPrinciple(),"updateOMRSAttributes");
        }
        return instanceProperties;
    }
}
