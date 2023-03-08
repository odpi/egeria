/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

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
        super(OpenMetadataAPIMapper.RATING_TYPE_GUID,
              OpenMetadataAPIMapper.RATING_TYPE_NAME,
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
                                                             OpenMetadataAPIMapper.IS_PUBLIC_PROPERTY_NAME,
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
                                                                    OpenMetadataAPIMapper.STARS_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.STAR_RATING_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.STAR_RATING_ENUM_TYPE_NAME,
                                                                    starRating,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataAPIMapper.STARS_PROPERTY_NAME);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.REVIEW_PROPERTY_NAME,
                                                                  review,
                                                                  methodName);

        return properties;
    }
}
