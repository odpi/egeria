/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CollectionMemberStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

/**
 * OpenMetadataBaseClient supports the common properties and functions for the open metadata based clients.
 */
public abstract class OpenMetadataBaseClient
{
    final protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    final protected PropertyHelper          propertyHelper          = new PropertyHelper();
    final protected OpenMetadataStoreClient openMetadataStoreClient;   /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           number of elements that can be returned on a call
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any REST API calls.
     */
    public OpenMetadataBaseClient(String serverName,
                                  String serverPlatformURLRoot,
                                  int    maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, maxPageSize);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           number of elements that can be returned on a call
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any REST API calls.
     */
    public OpenMetadataBaseClient(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password,
                                  int    maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }



    /*
     * Mapping functions
     */


    /**
     * Convert the collection properties into a set of element properties for the open metadata client.
     *
     * @param collectionProperties supplied collection properties
     * @return element properties
     */
    protected ElementProperties getElementProperties(CollectionProperties collectionProperties)
    {
        if (collectionProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   collectionProperties.getQualifiedName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NAME.name,
                                                                 collectionProperties.getName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 collectionProperties.getDescription());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.COLLECTION_TYPE.name,
                                                                 collectionProperties.getCollectionType());

            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    collectionProperties.getAdditionalProperties());

            elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              collectionProperties.getExtendedProperties());

            return elementProperties;
        }

        return null;
    }


    /**
     * Convert the digital product properties into a set of element properties for the open metadata client.
     *
     * @param digitalProductProperties supplied digital product properties
     * @return element properties
     */
    protected ElementProperties getElementProperties(DigitalProductProperties digitalProductProperties)
    {
        if (digitalProductProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.PRODUCT_NAME.name,
                                                                                   digitalProductProperties.getProductName());

            if (digitalProductProperties.getProductStatus() != null)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PRODUCT_STATUS.name,
                                                                     digitalProductProperties.getProductStatus().getName());
            }

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.PRODUCT_TYPE.name,
                                                                 digitalProductProperties.getProductType());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 digitalProductProperties.getDescription());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.INTRODUCTION_DATE.name,
                                                               digitalProductProperties.getIntroductionDate());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.MATURITY.name,
                                                                 digitalProductProperties.getMaturity());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.SERVICE_LIFE.name,
                                                                 digitalProductProperties.getServiceLife());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.CURRENT_VERSION.name,
                                                                 digitalProductProperties.getCurrentVersion());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.NEXT_VERSION_DATE.name,
                                                               digitalProductProperties.getNextVersionDate());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.WITHDRAW_DATE.name,
                                                               digitalProductProperties.getWithdrawDate());

            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    digitalProductProperties.getAdditionalProperties());

            return elementProperties;
        }

        return null;
    }


    /**
     * Convert the digital product properties into a set of element properties for the open metadata client.
     *
     * @param collectionMembershipProperties supplied collection membership properties
     * @return element properties
     */
    protected ElementProperties getElementProperties(CollectionMembershipProperties collectionMembershipProperties)
    {
        if (collectionMembershipProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.MEMBERSHIP_RATIONALE.name,
                                                                                   collectionMembershipProperties.getMembershipRationale());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.EXPRESSION.name,
                                                                 collectionMembershipProperties.getExpression());

            if (collectionMembershipProperties.getStatus() != null)
            {
                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                   OpenMetadataProperty.MEMBERSHIP_STATUS.name,
                                                                   CollectionMemberStatus.getOpenTypeName(),
                                                                   collectionMembershipProperties.getStatus().getName());
            }

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                 collectionMembershipProperties.getUserDefinedStatus());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.CONFIDENCE.name,
                                                              collectionMembershipProperties.getConfidence());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD.name,
                                                                 collectionMembershipProperties.getSteward());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                 collectionMembershipProperties.getStewardTypeName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                 collectionMembershipProperties.getStewardPropertyName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.SOURCE.name,
                                                                 collectionMembershipProperties.getSource());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NOTES.name,
                                                                 collectionMembershipProperties.getNotes());

            return elementProperties;
        }

        return null;
    }
}
