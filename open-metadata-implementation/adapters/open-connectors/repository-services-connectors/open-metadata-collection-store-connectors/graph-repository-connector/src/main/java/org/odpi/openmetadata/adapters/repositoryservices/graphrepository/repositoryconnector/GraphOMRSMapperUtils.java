/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;



import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GraphOMRSMapperUtils {

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSMapperUtils.class);

    /*
     * Default CTOR
     */
    public GraphOMRSMapperUtils() {

    }

    public InstanceProvenanceType mapProvenanceOrdinalToEnum(Integer provenanceOrdinal)
    {
        if (provenanceOrdinal == null) {
            return null;
        }
        InstanceProvenanceType instanceProvenanceType;
        switch (provenanceOrdinal) {
            case 0:
                instanceProvenanceType = InstanceProvenanceType.UNKNOWN;
                break;
            case 1:
                instanceProvenanceType = InstanceProvenanceType.LOCAL_COHORT;
                break;
            case 2:
                instanceProvenanceType = InstanceProvenanceType.EXPORT_ARCHIVE;
                break;
            case 3:
                instanceProvenanceType = InstanceProvenanceType.CONTENT_PACK;
                break;
            case 4:
                instanceProvenanceType = InstanceProvenanceType.DEREGISTERED_REPOSITORY;
                break;
            case 5:
                instanceProvenanceType = InstanceProvenanceType.CONFIGURATION;
                break;
            case 6:
                instanceProvenanceType = InstanceProvenanceType.EXTERNAL_SOURCE;
                break;
            default:
                instanceProvenanceType = InstanceProvenanceType.UNKNOWN;
                break;
        }
        return instanceProvenanceType;
    }


    public InstanceStatus mapStatusOrdinalToEnum(Integer statusOrdinal)
    {
        if (statusOrdinal == null) {
            return null;
        }
        InstanceStatus instanceStatus;
        switch (statusOrdinal) {
            case 0:
                instanceStatus = InstanceStatus.UNKNOWN;
                break;
            case 1:
                instanceStatus = InstanceStatus.DRAFT;
                break;
            case 2:
                instanceStatus = InstanceStatus.PREPARED;
                break;
            case 3:
                instanceStatus = InstanceStatus.PROPOSED;
                break;
            case 4:
                instanceStatus = InstanceStatus.APPROVED;
                break;
            case 5:
                instanceStatus = InstanceStatus.REJECTED;
                break;
            case 6:
                instanceStatus = InstanceStatus.APPROVED_CONCEPT;
                break;
            case 7:
                instanceStatus = InstanceStatus.UNDER_DEVELOPMENT;
                break;
            case 8:
                instanceStatus = InstanceStatus.DEVELOPMENT_COMPLETE;
                break;
            case 9:
                instanceStatus = InstanceStatus.APPROVED_FOR_DEPLOYMENT;
                break;
            case 10:
                instanceStatus = InstanceStatus.STANDBY;
                break;
            case 15:
                instanceStatus = InstanceStatus.ACTIVE;
                break;
            case 20:
                instanceStatus = InstanceStatus.FAILED;
                break;
            case 21:
                instanceStatus = InstanceStatus.DISABLED;
                break;
            case 22:
                instanceStatus = InstanceStatus.COMPLETE;
                break;
            case 30:
                instanceStatus = InstanceStatus.DEPRECATED;
                break;
            case 50:
                instanceStatus = InstanceStatus.OTHER;
                break;
            case 99:
                instanceStatus = InstanceStatus.DELETED;
                break;
            default:
                instanceStatus = InstanceStatus.UNKNOWN;
                break;
        }
        return instanceStatus;
    }



    public ClassificationOrigin mapClassificationOriginOrdinalToEnum(Integer originOrdinal)
    {
        if (originOrdinal == null) {
            return null;
        }
        ClassificationOrigin classificationOrigin;
        switch (originOrdinal) {
            case 0:
                classificationOrigin = ClassificationOrigin.ASSIGNED;
                break;
            case 1:
                classificationOrigin = ClassificationOrigin.PROPAGATED;
                break;
            default:
                classificationOrigin = ClassificationOrigin.ASSIGNED;
                break;
        }
        return classificationOrigin;
    }



    public Map<String,String> getQualifiedPropertyNamesForTypeDef(TypeDef typeDef, String repositoryName, OMRSRepositoryHelper repositoryHelper) {

        final  String QUALIFIED_PROPERTY_SEPARATOR = "x";

        Map<String,String> qualifiedPropertyNames = new HashMap<>();

        /*
         *  Process this type...
         */

        if (typeDef != null) {

            List<TypeDefAttribute> propertiesDefinition = typeDef.getPropertiesDefinition();

            if (propertiesDefinition != null) {
                for (TypeDefAttribute tda : propertiesDefinition) {
                    String propName = tda.getAttributeName();
                    String qualifiedName = typeDef.getName() + QUALIFIED_PROPERTY_SEPARATOR + propName;
                    qualifiedPropertyNames.put(propName, qualifiedName);
                }
            }

            /*
             *  Work up the TypeDef hierarchy extracting the property definitions.
             */

            TypeDefLink superTypeLink = typeDef.getSuperType();

            while (superTypeLink != null) {
                String superTypeName = superTypeLink.getName();
                TypeDef superTypeDef = repositoryHelper.getTypeDefByName(repositoryName, superTypeName);
                List<TypeDefAttribute> superTypePropertiesDefinition = superTypeDef.getPropertiesDefinition();

                // In the event of duplicate property names in the hierarchy the qualified name will be the occurrence that is nearest the top.
                if (superTypePropertiesDefinition != null) {
                    for (TypeDefAttribute tda : superTypePropertiesDefinition) {
                        String propName = tda.getAttributeName();
                        String qualifiedName = superTypeName + QUALIFIED_PROPERTY_SEPARATOR + propName;
                        qualifiedPropertyNames.put(propName, qualifiedName);
                    }
                }

                superTypeLink = superTypeDef.getSuperType();
            }
        }

        return qualifiedPropertyNames;
    }

    /*
     * Return a de-duplicated map of (short) property name --> TDA, where each entry is the one found in the most superior type def
     * in the type hierarchy. This is implemented by starting at the specified type and working up the hierarchy, overwriting a
     * previous entry in the map with each higher definition of the same (short) property name.
     */
    Map<String,TypeDefAttribute>  getUniquePropertyDefsForTypeDef(String               sourceName,
                                                                  TypeDef              typeDef,
                                                                  OMRSRepositoryHelper repositoryHelper)
    {
        Map<String,TypeDefAttribute> uniquePropertyDefsForType = new HashMap<>();

        List<TypeDefAttribute> propertiesDefinition = typeDef.getPropertiesDefinition();

        if (propertiesDefinition != null)
        {
            for (TypeDefAttribute propDef : propertiesDefinition)
            {
                uniquePropertyDefsForType.put(propDef.getAttributeName(), propDef);
            }
        }

        /*
         * Move up the TypeDef hierarchy merging the higher level property definitions.
         */
        TypeDefLink superTypeLink = typeDef.getSuperType();

        while (superTypeLink != null)
        {
            TypeDef superTypeDef = repositoryHelper.getTypeDefByName(sourceName, superTypeLink.getName());
            if (superTypeDef != null)
            {
                List<TypeDefAttribute> superTypePropertiesDefinition = superTypeDef.getPropertiesDefinition();
                if (superTypePropertiesDefinition != null)
                {
                    for (TypeDefAttribute propDef : superTypePropertiesDefinition)
                    {
                        uniquePropertyDefsForType.put(propDef.getAttributeName(), propDef);
                    }
                }
                superTypeLink = superTypeDef.getSuperType();
            }
            else
            {
                superTypeLink = null; // finish
            }
        }
        return uniquePropertyDefsForType;
    }

}
