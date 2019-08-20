/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.RatingMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * RatingBuilder is able to build the properties for a Rating entity.
 */
public class RatingBuilder extends RootBuilder
{
    private StarRating starRating;
    private String     review;
    private boolean    isPublic;
    private String     anchorGUID;

    /**
     * Constructor.
     *
     * @param starRating stars parameter
     * @param review review comments
     * @param isPublic should this feedback be shareable?
     * @param anchorGUID unique identifier of the anchor entity
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public RatingBuilder(StarRating           starRating,
                         String               review,
                         boolean              isPublic,
                         String               anchorGUID,
                         OMRSRepositoryHelper repositoryHelper,
                         String               serviceName,
                         String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);

        this.starRating = starRating;
        this.review = review;
        this.isPublic = isPublic;
        this.anchorGUID = anchorGUID;
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
                                                             RatingMapper.IS_PUBLIC_PROPERTY_NAME,
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
    public InstanceProperties getEntityInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (starRating != null)
        {
            properties = this.addStarRatingPropertyToInstance(properties,
                                                              starRating,
                                                              methodName);
        }

        if (review != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      RatingMapper.REVIEW_PROPERTY_NAME,
                                                                      review,
                                                                      methodName);
        }

        if (anchorGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      RatingMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                      anchorGUID,
                                                                      methodName);
        }

        return properties;
    }



    /**
     * Set up a property value for the StarRating enum property.
     *
     * @param properties  current properties
     * @param starRating  enum value
     * @param methodName  calling method
     * @return  InstanceProperties object with the enum value added
     */
    private InstanceProperties addStarRatingPropertyToInstance(InstanceProperties  properties,
                                                               StarRating          starRating,
                                                               String              methodName)
    {
        int                ordinal = 99;
        String             symbolicName = null;
        String             description = null;

        final int    element1Ordinal         = 0;
        final String element1Value           = "NotRecommended";
        final String element1Description     = "This content is not recommended.";

        final int    element2Ordinal         = 1;
        final String element2Value           = "OneStar";
        final String element2Description     = "One star rating.";

        final int    element3Ordinal         = 2;
        final String element3Value           = "TwoStar";
        final String element3Description     = "Two star rating.";

        final int    element4Ordinal         = 3;
        final String element4Value           = "ThreeStar";
        final String element4Description     = "Three star rating.";

        final int    element5Ordinal         = 4;
        final String element5Value           = "FourStar";
        final String element5Description     = "Four star rating.";

        final int    element6Ordinal         = 5;
        final String element6Value           = "FiveStar";
        final String element6Description     = "Five star rating.";

        switch (starRating)
        {
            case NOT_RECOMMENDED:
                ordinal = element1Ordinal;
                symbolicName = element1Value;
                description = element1Description;
                break;

            case ONE_STAR:
                ordinal = element2Ordinal;
                symbolicName = element2Value;
                description = element2Description;
                break;

            case TWO_STARS:
                ordinal = element3Ordinal;
                symbolicName = element3Value;
                description = element3Description;
                break;

            case THREE_STARS:
                ordinal = element4Ordinal;
                symbolicName = element4Value;
                description = element4Description;
                break;

            case FOUR_STARS:
                ordinal = element5Ordinal;
                symbolicName = element5Value;
                description = element5Description;
                break;

            case FIVE_STARS:
                ordinal = element6Ordinal;
                symbolicName = element6Value;
                description = element6Description;
                break;
        }

        return repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                          properties,
                                                          RatingMapper.STARS_PROPERTY_NAME,
                                                          ordinal,
                                                          symbolicName,
                                                          description,
                                                          methodName);
    }
}
