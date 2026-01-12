/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * ReferenceableHandler manages methods on generic referenceables.
 */
public class ReferenceableHandler<B> extends OpenMetadataAPITemplateHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param auditLog destination for audit log events.
     */
    public ReferenceableHandler(OpenMetadataAPIGenericConverter<B> converter,
                                Class<B>                           beanClass,
                                String                             serviceName,
                                String                             serverName,
                                InvalidParameterHandler            invalidParameterHandler,
                                RepositoryHandler                  repositoryHandler,
                                OMRSRepositoryHelper               repositoryHelper,
                                String                             localServerUserId,
                                OpenMetadataServerSecurityVerifier securityVerifier,
                                AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              auditLog);
    }


    /**
     * Return the bean with the requested qualified name.
     * The match is exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching B bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getBeanGUIDByQualifiedName(String       userId,
                                             String       typeGUID,
                                             String       typeName,
                                             String       name,
                                             String       nameParameterName,
                                             boolean      forLineage,
                                             boolean      forDuplicateProcessing,
                                             Date         effectiveTime,
                                             String       methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataType.REFERENCEABLE.typeGUID;
        String resultTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        return this.getBeanGUIDByUniqueName(userId,
                                            name,
                                            nameParameterName,
                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                            resultTypeGUID,
                                            resultTypeName,
                                            null,
                                            null,
                                            SequencingOrder.CREATION_DATE_RECENT,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Create the property facet for the vendor properties.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of the software capability
     * @param vendorProperties properties for the vendor
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void setVendorProperties(String               userId,
                                    String               referenceableGUID,
                                    Map<String, String>  vendorProperties,
                                    boolean              forLineage,
                                    boolean              forDuplicateProcessing,
                                    Date                 effectiveTime,
                                    String               methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String referenceableGUIDParameter = "referenceableGUID";
        final String propertyFacetGUIDParameter = "propertyFacet[x]";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);

        List<EntityDetail> propertyFacets = this.getAttachedEntities(userId,
                                                                     referenceableGUID,
                                                                     referenceableGUIDParameter,
                                                                     OpenMetadataType.REFERENCEABLE.typeName,
                                                                     OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName,
                                                                     OpenMetadataType.PROPERTY_FACET.typeName,
                                                                     null,
                                                                     null,
                                                                     2,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     0,
                                                                     invalidParameterHandler.getMaxPagingSize(),
                                                                     effectiveTime,
                                                                     methodName);

        if (vendorProperties != null)
        {
            PropertyFacetBuilder builder = new PropertyFacetBuilder(referenceableGUID + "_" + PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                                                    null,
                                                                    PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                                                    vendorProperties,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

            if (propertyFacets != null)
            {
                for (EntityDetail propertyFacet : propertyFacets)
                {
                    if (propertyFacet != null)
                    {
                        String description = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataProperty.DESCRIPTION.name,
                                                                                propertyFacet.getProperties(),
                                                                                methodName);

                        if (PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE.equals(description))
                        {
                            this.updateBeanInRepository(userId,
                                                        null,
                                                        null,
                                                        propertyFacet.getGUID(),
                                                        propertyFacetGUIDParameter,
                                                        OpenMetadataType.PROPERTY_FACET.typeGUID,
                                                        OpenMetadataType.PROPERTY_FACET.typeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        builder.getInstanceProperties(methodName),
                                                        true,
                                                        effectiveTime,
                                                        methodName);
                        }
                    }
                }
            }
            else
            {
                this.addAnchorGUIDToBuilder(userId,
                                            referenceableGUID,
                                            referenceableGUIDParameter,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            builder,
                                            methodName);

                String propertyFacetGUID = createBeanInRepository(userId,
                                                                  null,
                                                                  null,
                                                                  OpenMetadataType.PROPERTY_FACET.typeGUID,
                                                                  OpenMetadataType.PROPERTY_FACET.typeName,
                                                                  builder,
                                                                  effectiveTime,
                                                                  methodName);

                InstanceProperties relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                         null,
                                                                                                         OpenMetadataProperty.SOURCE.name,
                                                                                                         serviceName,
                                                                                                         methodName);
                linkElementToElement(userId,
                                     null,
                                     null,
                                     referenceableGUID,
                                     referenceableGUIDParameter,
                                     OpenMetadataType.REFERENCEABLE.typeName,
                                     propertyFacetGUID,
                                     propertyFacetGUIDParameter,
                                     OpenMetadataType.PROPERTY_FACET.typeName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeGUID,
                                     OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName,
                                     relationshipProperties,
                                     null,
                                     null,
                                     effectiveTime,
                                     methodName);

            }
        }
        else
        {
            if (propertyFacets != null)
            {
                for (EntityDetail propertyFacet : propertyFacets)
                {
                    if (propertyFacet != null)
                    {
                        String description = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataProperty.DESCRIPTION.name,
                                                                                propertyFacet.getProperties(),
                                                                                methodName);

                        if (PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE.equals(description))
                        {
                            this.deleteBeanInRepository(userId,
                                                        null,
                                                        null,
                                                        propertyFacet.getGUID(),
                                                        propertyFacetGUIDParameter,
                                                        OpenMetadataType.PROPERTY_FACET.typeGUID,
                                                        OpenMetadataType.PROPERTY_FACET.typeName,
                                                        false,
                                                        null,
                                                        null,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
                        }
                    }
                }
            }
        }
    }
}
