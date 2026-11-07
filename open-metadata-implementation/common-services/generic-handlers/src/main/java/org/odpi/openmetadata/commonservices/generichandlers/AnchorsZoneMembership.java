/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The zoneMembership property of an element's Anchors classification holds the governance zones that decide who
 * may see the element: the zones of its own ZoneMembership classification if it has one, and otherwise the zones of
 * its anchor.  This matches the security connector, which judges an anchored element on its own zones when it has
 * any and on its anchor's zones when it does not.  Keeping the effective zones on the element itself means that a
 * query can filter on them without reaching across to the anchor.
 * <br><br>
 * These helpers work on the classifications only; the handlers decide when to apply them.
 */
class AnchorsZoneMembership
{
    private static final String methodName = "AnchorsZoneMembership";


    /**
     * Return the zones listed in a ZoneMembership or Anchors classification.
     *
     * @param classification classification (may be null)
     * @param repositoryHelper helper
     * @param serviceName calling service
     * @return list of zones, or null if there are none
     */
    static List<String> getZones(Classification       classification,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName)
    {
        if ((classification != null) && (classification.getProperties() != null))
        {
            List<String> zones = repositoryHelper.getStringArrayProperty(serviceName,
                                                                         OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                         classification.getProperties(),
                                                                         methodName);

            if ((zones != null) && (! zones.isEmpty()))
            {
                return zones;
            }
        }

        return null;
    }


    /**
     * Return the named classification from an entity, or null.
     *
     * @param entity entity to search
     * @param classificationName name of the classification
     * @return classification or null
     */
    static Classification getClassification(EntitySummary entity,
                                            String        classificationName)
    {
        if ((entity != null) && (entity.getClassifications() != null))
        {
            for (Classification classification : entity.getClassifications())
            {
                if ((classification != null) && (classificationName.equals(classification.getName())))
                {
                    return classification;
                }
            }
        }

        return null;
    }


    /**
     * Return a copy of the Anchors properties with zoneMembership set to the supplied zones.  No zones removes
     * the property.
     *
     * @param anchorsProperties current properties (may be null)
     * @param zones new zones
     * @param repositoryHelper helper
     * @param serviceName calling service
     * @return new properties
     */
    static InstanceProperties setZones(InstanceProperties   anchorsProperties,
                                       List<String>         zones,
                                       OMRSRepositoryHelper repositoryHelper,
                                       String               serviceName)
    {
        InstanceProperties newProperties = (anchorsProperties == null) ? new InstanceProperties() : new InstanceProperties(anchorsProperties);

        newProperties.setProperty(OpenMetadataProperty.ZONE_MEMBERSHIP.name, null);

        if ((zones != null) && (! zones.isEmpty()))
        {
            newProperties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                              newProperties,
                                                                              OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                              zones,
                                                                              methodName);
        }

        return newProperties;
    }


    /**
     * Does the classification hold an empty list of zones?  An element with no zones is open to everyone, and
     * the server records that by leaving zoneMembership unset.  An empty list means the same thing to the
     * security checks, but the search filter can not tell it apart from a list of zones the caller can not read,
     * so it hides the element.  The server never writes one, but an open metadata archive can.
     *
     * @param classification classification to test
     * @return true if zoneMembership is set to an empty list
     */
    static boolean holdsEmptyZoneList(Classification classification)
    {
        if ((classification != null) && (classification.getProperties() != null))
        {
            InstancePropertyValue zones = classification.getProperties().getPropertyValue(OpenMetadataProperty.ZONE_MEMBERSHIP.name);

            return (zones instanceof ArrayPropertyValue arrayValue) &&
                    ((arrayValue.getArrayCount() == 0) || (arrayValue.getArrayValues() == null) ||
                     (arrayValue.getArrayValues().getPropertyCount() == 0));
        }

        return false;
    }


    /**
     * Are the two lists of zones the same?  Order and duplicates do not matter, and no zones is the same as an
     * empty list.
     *
     * @param zones1 first list
     * @param zones2 second list
     * @return boolean
     */
    static boolean sameZones(List<String> zones1,
                             List<String> zones2)
    {
        HashSet<String> set1 = (zones1 == null) ? new HashSet<>() : new HashSet<>(zones1);
        HashSet<String> set2 = (zones2 == null) ? new HashSet<>() : new HashSet<>(zones2);

        return set1.equals(set2);
    }


    /**
     * Make the Anchors classification of a new element carry the element's own zones when it has a
     * ZoneMembership classification.  An element without one keeps the zones its Anchors classification was
     * given, which are its anchor's.
     *
     * @param classifications classifications for the new element
     * @param repositoryHelper helper
     * @param serviceName calling service
     * @return classifications with the Anchors classification aligned (a copy if it changed)
     */
    static List<Classification> alignNewClassifications(List<Classification> classifications,
                                                        OMRSRepositoryHelper repositoryHelper,
                                                        String               serviceName)
    {
        if (classifications == null)
        {
            return null;
        }

        Classification anchors        = null;
        Classification zoneMembership = null;

        for (Classification classification : classifications)
        {
            if (classification != null)
            {
                if (OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(classification.getName()))
                {
                    anchors = classification;
                }
                else if (OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName.equals(classification.getName()))
                {
                    zoneMembership = classification;
                }
            }
        }

        if ((anchors == null) || (zoneMembership == null))
        {
            return classifications;
        }

        List<String> ownZones = getZones(zoneMembership, repositoryHelper, serviceName);

        if (sameZones(ownZones, getZones(anchors, repositoryHelper, serviceName)))
        {
            return classifications;
        }

        Classification alignedAnchors = new Classification(anchors);

        alignedAnchors.setProperties(setZones(anchors.getProperties(), ownZones, repositoryHelper, serviceName));

        List<Classification> results = new ArrayList<>();

        for (Classification classification : classifications)
        {
            results.add(classification == anchors ? alignedAnchors : classification);
        }

        return results;
    }


    /**
     * Return the classification search that matches every element with an Anchors classification.
     *
     * @return classification conditions
     */
    static SearchClassifications getHasAnchorsSearch()
    {
        ClassificationCondition hasAnchors = new ClassificationCondition();

        hasAnchors.setName(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);

        SearchClassifications searchClassifications = new SearchClassifications();

        searchClassifications.setConditions(List.of(hasAnchors));
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        return searchClassifications;
    }


    /**
     * Add to a search the condition that leaves out the elements the user cannot read because of their zones:
     * those whose Anchors classification lists zones, none of which is readable.  An element whose Anchors
     * classification lists no zones, or that has no Anchors classification, is left in, and is checked when it is
     * returned like every other element.
     * <br><br>
     * The condition is expressed as a NONE search on the classifications.  A search that already has classification
     * conditions can only be combined with it if they are also NONE; otherwise the search is returned unchanged
     * and the elements are filtered only as they are returned.
     *
     * @param searchClassifications the caller's classification conditions (may be null)
     * @param readableZones the zones the user could read (null means no filter)
     * @return classification conditions to use
     */
    static SearchClassifications addReadableZonesFilter(SearchClassifications searchClassifications,
                                                        List<String>          readableZones)
    {
        if (readableZones == null)
        {
            return searchClassifications;
        }

        List<ClassificationCondition> conditions = new ArrayList<>();

        if ((searchClassifications != null) && (searchClassifications.getConditions() != null) && (! searchClassifications.getConditions().isEmpty()))
        {
            if (searchClassifications.getMatchCriteria() != MatchCriteria.NONE)
            {
                return searchClassifications;
            }

            conditions.addAll(searchClassifications.getConditions());
        }

        conditions.add(getUnreadableZonesCondition(readableZones));

        SearchClassifications filteredClassifications = new SearchClassifications();

        filteredClassifications.setConditions(conditions);
        filteredClassifications.setMatchCriteria(MatchCriteria.NONE);

        return filteredClassifications;
    }


    /**
     * Return the condition matching an Anchors classification that lists zones, none of which is readable:
     * zoneMembership is set, and NONE of (zoneMembership IN readableZones).
     *
     * @param readableZones zones the user could read
     * @return classification condition
     */
    private static ClassificationCondition getUnreadableZonesCondition(List<String> readableZones)
    {
        ArrayPropertyValue zoneList   = new ArrayPropertyValue();
        InstanceProperties zoneValues = new InstanceProperties();
        int                index      = 0;

        for (String zoneName : readableZones)
        {
            if (zoneName != null)
            {
                PrimitivePropertyValue zoneValue = new PrimitivePropertyValue();

                zoneValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                zoneValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                zoneValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
                zoneValue.setPrimitiveValue(zoneName);

                zoneValues.setProperty(Integer.toString(index), zoneValue);
                index++;
            }
        }

        zoneList.setArrayCount(index);
        zoneList.setArrayValues(zoneValues);

        PropertyCondition inReadableZones = new PropertyCondition();

        inReadableZones.setProperty(OpenMetadataProperty.ZONE_MEMBERSHIP.name);
        inReadableZones.setOperator(PropertyComparisonOperator.IN);
        inReadableZones.setValue(zoneList);

        SearchProperties notInReadableZones = new SearchProperties();

        notInReadableZones.setConditions(List.of(inReadableZones));
        notInReadableZones.setMatchCriteria(MatchCriteria.NONE);

        PropertyCondition noReadableZone = new PropertyCondition();

        noReadableZone.setNestedConditions(notInReadableZones);

        PropertyCondition hasZones = new PropertyCondition();

        hasZones.setProperty(OpenMetadataProperty.ZONE_MEMBERSHIP.name);
        hasZones.setOperator(PropertyComparisonOperator.NOT_NULL);

        SearchProperties matchProperties = new SearchProperties();

        matchProperties.setConditions(List.of(hasZones, noReadableZone));
        matchProperties.setMatchCriteria(MatchCriteria.ALL);

        ClassificationCondition classificationCondition = new ClassificationCondition();

        classificationCondition.setName(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
        classificationCondition.setMatchProperties(matchProperties);

        return classificationCondition;
    }
}
