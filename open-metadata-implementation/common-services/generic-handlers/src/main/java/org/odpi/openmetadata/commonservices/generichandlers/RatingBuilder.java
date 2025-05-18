/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.StarRating;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
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
        super(OpenMetadataType.RATING.typeGUID,
              OpenMetadataType.RATING.typeName,
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
                                                                    OpenMetadataProperty.STARS.name,
                                                                    StarRating.getOpenTypeGUID(),
                                                                    StarRating.getOpenTypeName(),
                                                                    starRating,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.STARS.name);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.REVIEW.name,
                                                                  review,
                                                                  methodName);

        return properties;
    }
}
