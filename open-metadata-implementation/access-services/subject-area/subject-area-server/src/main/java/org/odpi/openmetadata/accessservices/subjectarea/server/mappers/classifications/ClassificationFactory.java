/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

 // This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.commonservices.generichandlers.*;
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
    private OMRSRepositoryHelper repositoryHelper;
    private OpenMetadataAPIGenericHandler genericHandler;


    public ClassificationFactory(OpenMetadataAPIGenericHandler genericHandler){
        this.genericHandler= genericHandler;
        this.repositoryHelper=genericHandler.getRepositoryHelper();
    }

    /**
     * Get an OMAS Classification based on the name and then map the supplied omrs classificaiton to the omas one.
     * @param name name of the classification. Note this may not match the classification name if the classification is a sub type of the supplied name.
     * @param omrsClassification the supplied omrs classification to map
     * @return the omas classification, null if not known.
     */
    public org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification getOMASClassification(String name, Classification omrsClassification)  {
        final String serviceName = genericHandler.getServiceName();
        if (this.repositoryHelper.isTypeOf(serviceName,name,"SpineAttribute")) {
            return new SpineAttributeMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }
        if (this.repositoryHelper.isTypeOf(serviceName,name,"CanonicalVocabulary")) {
            return new CanonicalVocabularyMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }
      
        if (this.repositoryHelper.isTypeOf(serviceName,name,"Confidence")) {
            return new ConfidenceMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }
       
        if (this.repositoryHelper.isTypeOf(serviceName,name,"Criticality")) {
            return new CriticalityMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }
       
        if (this.repositoryHelper.isTypeOf(serviceName,name,"GlossaryProject")) {
            return new GlossaryProjectMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }
      
        if (this.repositoryHelper.isTypeOf(serviceName,name,"SpineObject")) {
            return new SpineObjectMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Taxonomy")) {
            return new TaxonomyMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }
        
        if (this.repositoryHelper.isTypeOf(serviceName,name,"Retention")) {
            return new RetentionMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }
        
        if (this.repositoryHelper.isTypeOf(serviceName,name,"SubjectArea")) {
            return new SubjectAreaMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }
       
        if (this.repositoryHelper.isTypeOf(serviceName,name,"ObjectIdentifier")) {
            return new ObjectIdentifierMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Confidentiality")) {
            return new ConfidentialityMapper(genericHandler).mapOmrsToBean(omrsClassification);
        }
        return null;
    }
    /**
     * Get an OMRS Classification based on the supplied omas classification.
     * @param omasClassification the supplied omas classification to map
     * @return the omas classification, null if not known.
     */
    public Classification getOMRSClassification(org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification) {
        final String name = omasClassification.getClassificationName();
        final String serviceName = genericHandler.getServiceName();

        if (this.repositoryHelper.isTypeOf(serviceName,name,"SpineAttribute")) {
            return new SpineAttributeMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }
        if (this.repositoryHelper.isTypeOf(serviceName,name,"CanonicalVocabulary")) {
            return new CanonicalVocabularyMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Confidence")) {
            return new ConfidenceMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Criticality")) {
            return new CriticalityMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"GlossaryProject")) {
            return new GlossaryProjectMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"SpineObject")) {
            return new SpineObjectMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Taxonomy")) {
            return new TaxonomyMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Retention")) {
            return new RetentionMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"SubjectArea")) {
            return new SubjectAreaMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"ObjectIdentifier")) {
            return new ObjectIdentifierMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }

        if (this.repositoryHelper.isTypeOf(serviceName,name,"Confidentiality")) {
            return new ConfidentialityMapper(genericHandler).mapBeanToOmrs(omasClassification);
        }
        return null;

    }
}
