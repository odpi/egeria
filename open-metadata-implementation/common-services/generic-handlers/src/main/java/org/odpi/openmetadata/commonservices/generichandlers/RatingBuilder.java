/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

/**
 * RatingBuilder is able to build the properties for a Rating entity.
 */
public class RatingBuilder extends OpenMetadataAPIGenericBuilder
{
    private final int     starRating;
    private final String  review;
    private final boolean isPublic;

    /**
     * Constructor.
     *
     * @param starRating stars parameter
     * @param review review comments
     * @param isPublic should this feedback be shareable?
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public RatingBuilder(int                  starRating,
                         String               review,
                         boolean              isPublic,
                         OMRSRepositoryHelper repositoryHelper,
                         String               serviceName,
                         String               serverName)
    {
        super(OpenMetadataType.RATING_TYPE_GUID,
              OpenMetadataType.RATING_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.starRating = starRating;
        this.review = review;
        this.isPublic = isPublic;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getRelationshipInstanceProperties(String  methodName)
    {
        return repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                             null,
                                                             OpenMetadataProperty.IS_PUBLIC.name,
                                                             isPublic,
                                                             methodName);
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataType.STARS_PROPERTY_NAME,
                                                                    OpenMetadataType.STAR_RATING_ENUM_TYPE_GUID,
                                                                    OpenMetadataType.STAR_RATING_ENUM_TYPE_NAME,
                                                                    starRating,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataType.STARS_PROPERTY_NAME);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.REVIEW_PROPERTY_NAME,
                                                                  review,
                                                                  methodName);

        return properties;
    }
}
