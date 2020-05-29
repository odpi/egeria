/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

 // This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory to create new instances of classes of open metadata classifications by name. Return null if the classification is not known.
 */
public class ClassificationFactory {
    private static final Logger log = LoggerFactory.getLogger( ClassificationFactory.class);
    private static final String className =  ClassificationFactory.class.getName();
    protected final OMRSAPIHelper omrsapiHelper;
    private OMRSRepositoryHelper repositoryHelper;

    public ClassificationFactory(OMRSAPIHelper omrsapiHelper) {
        this.omrsapiHelper= omrsapiHelper;
        this.repositoryHelper=omrsapiHelper.getOMRSRepositoryHelper();
    }

    /**
     * Get an OMAS Classification based on the name and then map the supplied omrs classificaiton to the omas one.
     * @param name name of the classification. Note this may not match the classification name if the classification is a sub type of the supplied name.
     * @param omrsClassification the supplied omrs classification to map
     * @return the omas classification, null if not known.
     */
    public org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification getOMASClassification(String name, Classification omrsClassification)  {
        final String serviceName = omrsapiHelper.getServiceName();
        if (this.repositoryHelper.isTypeOf(serviceName,name,"SpineAttribute")) {
            return new SpineAttributeMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }
        if (this.repositoryHelper.isTypeOf(serviceName,name,"CanonicalVocabulary")) {
            return new CanonicalVocabularyMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }
      
        if (this.repositoryHelper.isTypeOf(serviceName,name,"Confidence")) {
            return new ConfidenceMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }
       
        if (this.repositoryHelper.isTypeOf(serviceName,name,"Criticality")) {
            return new CriticalityMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }
       
        if (this.repositoryHelper.isTypeOf(serviceName,name,"GlossaryProject")) {
            return new GlossaryProjectMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }
      
        if (this.repositoryHelper.isTypeOf(serviceName,name,"SpineObject")) {
            return new SpineObjectMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Taxonomy")) {
            return new TaxonomyMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }
        
        if (this.repositoryHelper.isTypeOf(serviceName,name,"Retention")) {
            return new RetentionMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }
        
        if (this.repositoryHelper.isTypeOf(serviceName,name,"SubjectArea")) {
            return new SubjectAreaMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }
       
        if (this.repositoryHelper.isTypeOf(serviceName,name,"ObjectIdentifier")) {
            return new ObjectIdentifierMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Confidentiality")) {
            return new ConfidentialityMapper(this.omrsapiHelper).mapOmrsToBean(omrsClassification);
        }
        return null;
    }
    /**
     * Get an OMRS Classification based on the the supplied omas classification.
     * @param omasClassification the supplied omas classification to map
     * @return the omas classification, null if not known.
     */
    public Classification getOMRSClassification(org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification) {
        final String name = omasClassification.getClassificationName();
        final String serviceName = omrsapiHelper.getServiceName();

        if (this.repositoryHelper.isTypeOf(serviceName,name,"SpineAttribute")) {
            return new SpineAttributeMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }
        if (this.repositoryHelper.isTypeOf(serviceName,name,"CanonicalVocabulary")) {
            return new CanonicalVocabularyMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Confidence")) {
            return new ConfidenceMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Criticality")) {
            return new CriticalityMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"GlossaryProject")) {
            return new GlossaryProjectMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"SpineObject")) {
            return new SpineObjectMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Taxonomy")) {
            return new TaxonomyMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Retention")) {
            return new RetentionMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"SubjectArea")) {
            return new SubjectAreaMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"ObjectIdentifier")) {
            return new ObjectIdentifierMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Confidentiality")) {
            return new ConfidentialityMapper(this.omrsapiHelper).mapBeanToOmrs(omasClassification);
        }
        return null;

    }
}
