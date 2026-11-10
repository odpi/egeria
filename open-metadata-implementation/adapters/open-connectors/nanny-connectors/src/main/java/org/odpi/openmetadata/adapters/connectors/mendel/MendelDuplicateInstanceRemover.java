/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mendel;

import org.odpi.openmetadata.adapters.connectors.mendel.ffdc.MendelAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingBase;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * MendelDuplicateInstanceRemover removes the exact duplicates that a process creates when it adds the same thing
 * more than once - typically a connector that re-links or re-attaches everything each time it catalogues a
 * document.  Unlike the peer duplicates that the rest of Mendel manages, these instances are identical apart from
 * their unique identifiers and audit headers, so there is nothing to decide: the oldest is kept and the rest are
 * deleted using the server's default delete method.
 * <ul>
 *     <li>Duplicate relationships: relationships of the same type that connect the same two elements and have the
 *     same properties and effectivity dates.</li>
 *     <li>Duplicate attached elements: elements of the same type with the same properties, attached to the same
 *     element (at end 1) through the same type of relationship (with the same relationship properties), and
 *     anchored to that element.  The anchor test means only elements that belong to the element are removed - a
 *     shared element is never deleted.  Deleting the element also deletes its attachment relationship.</li>
 * </ul>
 * The comparison is strict: every property must match.  Elements that carry a unique name, such as a
 * Referenceable's qualifiedName, therefore never match each other.
 */
public class MendelDuplicateInstanceRemover extends AuditLoggingBase
{
    /**
     * The number of examples of duplicate groups included in the report.
     */
    private static final int maxExamples = 5;

    private final IntegrationContext integrationContext;
    private final PropertyHelper     propertyHelper;
    private final String             connectorName;
    private final boolean            reportOnly;


    /**
     * A set of identical instances: the one to keep and the ones to delete.
     *
     * @param kept the oldest instance, which is kept
     * @param duplicates the other instances, which are deleted
     * @param <T> type of instance
     */
    record DuplicateGroup<T>(T kept, List<T> duplicates) {}


    /**
     * Constructor.
     *
     * @param integrationContext context giving access to open metadata
     * @param propertyHelper helper for working with element properties
     * @param connectorName name of the calling connector - used in messages
     * @param reportOnly report the duplicates rather than delete them
     * @param auditLog logging destination
     */
    public MendelDuplicateInstanceRemover(IntegrationContext integrationContext,
                                          PropertyHelper     propertyHelper,
                                          String             connectorName,
                                          boolean            reportOnly,
                                          AuditLog           auditLog)
    {
        super(auditLog);

        this.integrationContext = integrationContext;
        this.propertyHelper     = propertyHelper;
        this.connectorName      = connectorName;
        this.reportOnly         = reportOnly;
    }


    /**
     * Remove the duplicate relationships of each of the requested types.  A failure with one type is logged and
     * the next type is processed.
     *
     * @param relationshipTypeNames relationship types to process
     */
    public void removeDuplicateRelationships(List<String> relationshipTypeNames)
    {
        final String methodName = "removeDuplicateRelationships";

        for (String relationshipTypeName : relationshipTypeNames)
        {
            try
            {
                List<DuplicateGroup<OpenMetadataRelationship>> groups = findDuplicates(this.getRelationships(relationshipTypeName),
                                                                                       MendelDuplicateInstanceRemover::getRelationshipKey);

                if (! groups.isEmpty())
                {
                    if (! reportOnly)
                    {
                        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

                        for (DuplicateGroup<OpenMetadataRelationship> group : groups)
                        {
                            for (OpenMetadataRelationship duplicate : group.duplicates())
                            {
                                openMetadataStore.deleteRelationshipInStore(duplicate.getRelationshipGUID());
                            }
                        }
                    }

                    this.logResult(methodName,
                                   groups,
                                   "relationships",
                                   relationshipTypeName,
                                   group -> group.kept().getRelationshipGUID() + " (" + getGUID(group.kept().getElementAtEnd1()) +
                                           " -> " + getGUID(group.kept().getElementAtEnd2()) + ") has " + group.duplicates().size() + " duplicates");
                }
            }
            catch (Exception error)
            {
                logExceptionRecord(methodName,
                                   MendelAuditCode.DUPLICATE_REMOVAL_FAILED.getMessageDefinition(connectorName,
                                                                                                 relationshipTypeName,
                                                                                                 error.getClass().getName(),
                                                                                                 error.getMessage()),
                                   error);
            }
        }
    }


    /**
     * Remove the duplicate elements attached through each of the requested relationship types.  Only the
     * elements at end 1 that have more than one attachment of the type are retrieved.  A failure with one type
     * is logged and the next type is processed.
     *
     * @param attachmentTypeNames relationship types to process
     */
    public void removeDuplicateAttachments(List<String> attachmentTypeNames)
    {
        final String methodName = "removeDuplicateAttachments";

        for (String attachmentTypeName : attachmentTypeNames)
        {
            try
            {
                Map<String, Integer> attachmentCounts = new LinkedHashMap<>();

                for (OpenMetadataRelationship attachment : this.getRelationships(attachmentTypeName))
                {
                    String parentGUID = getGUID(attachment.getElementAtEnd1());

                    if (parentGUID != null)
                    {
                        attachmentCounts.merge(parentGUID, 1, Integer::sum);
                    }
                }

                List<DuplicateGroup<RelatedMetadataElement>> groups = new ArrayList<>();

                for (Map.Entry<String, Integer> attachmentCount : attachmentCounts.entrySet())
                {
                    if (attachmentCount.getValue() > 1)
                    {
                        String parentGUID = attachmentCount.getKey();

                        List<RelatedMetadataElement> ownedAttachments = new ArrayList<>();

                        for (RelatedMetadataElement attachment : this.getAttachedElements(parentGUID, attachmentTypeName))
                        {
                            if ((attachment.getElement() != null) && (parentGUID.equals(this.getAnchorGUID(attachment.getElement()))))
                            {
                                ownedAttachments.add(attachment);
                            }
                        }

                        groups.addAll(findDuplicates(ownedAttachments, MendelDuplicateInstanceRemover::getAttachmentKey));
                    }
                }

                if (! groups.isEmpty())
                {
                    if (! reportOnly)
                    {
                        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

                        for (DuplicateGroup<RelatedMetadataElement> group : groups)
                        {
                            for (RelatedMetadataElement duplicate : group.duplicates())
                            {
                                openMetadataStore.deleteMetadataElementInStore(duplicate.getElement().getElementGUID(), false);
                            }
                        }
                    }

                    this.logResult(methodName,
                                   groups,
                                   "attached elements",
                                   attachmentTypeName,
                                   group -> group.kept().getElement().getElementGUID() + " " + getPropertiesKey(group.kept().getElement().getElementProperties()) +
                                           " has " + group.duplicates().size() + " duplicates");
                }
            }
            catch (Exception error)
            {
                logExceptionRecord(methodName,
                                   MendelAuditCode.DUPLICATE_REMOVAL_FAILED.getMessageDefinition(connectorName,
                                                                                                 attachmentTypeName,
                                                                                                 error.getClass().getName(),
                                                                                                 error.getMessage()),
                                   error);
            }
        }
    }


    /**
     * Report what was found, and what was deleted unless only reporting.
     *
     * @param methodName calling method
     * @param groups duplicate groups
     * @param instanceDescription what sort of instance is duplicated
     * @param typeName relationship type name
     * @param describeGroup function describing one group for the examples
     * @param <T> type of instance
     */
    private <T> void logResult(String                           methodName,
                               List<DuplicateGroup<T>>          groups,
                               String                           instanceDescription,
                               String                           typeName,
                               Function<DuplicateGroup<T>, String> describeGroup)
    {
        int duplicateCount = countDuplicates(groups);

        if (reportOnly)
        {
            List<String> examples = new ArrayList<>();

            for (DuplicateGroup<T> group : groups.stream().sorted(Comparator.comparingInt((DuplicateGroup<T> group) -> group.duplicates().size()).reversed()).limit(maxExamples).toList())
            {
                examples.add(describeGroup.apply(group));
            }

            logRecord(methodName,
                      MendelAuditCode.DUPLICATES_REPORTED.getMessageDefinition(connectorName,
                                                                               Integer.toString(duplicateCount),
                                                                               instanceDescription,
                                                                               Integer.toString(groups.size()),
                                                                               typeName,
                                                                               String.join("; ", examples)));
        }
        else
        {
            logRecord(methodName,
                      MendelAuditCode.DUPLICATES_REMOVED.getMessageDefinition(connectorName,
                                                                              Integer.toString(duplicateCount),
                                                                              instanceDescription,
                                                                              Integer.toString(groups.size()),
                                                                              typeName));
        }
    }


    /**
     * Retrieve every relationship of a type, as stored rather than combined by the deduplication processing.
     * They are all retrieved before any is deleted so that the deletes do not shift the pages.
     *
     * @param relationshipTypeName type of relationship
     * @return list of relationships (may be empty)
     * @throws Exception problem retrieving the relationships
     */
    private List<OpenMetadataRelationship> getRelationships(String relationshipTypeName) throws Exception
    {
        OpenMetadataStore              openMetadataStore = integrationContext.getOpenMetadataStore();
        List<OpenMetadataRelationship> relationships     = new ArrayList<>();

        int startFrom = 0;

        OpenMetadataRelationshipList page = openMetadataStore.findRelationshipsBetweenMetadataElements(relationshipTypeName,
                                                                                                       null,
                                                                                                       null,
                                                                                                       null,
                                                                                                       null,
                                                                                                       null,
                                                                                                       this.getRawQueryOptions(startFrom));

        while ((page != null) && (page.getRelationships() != null) && (! page.getRelationships().isEmpty()))
        {
            relationships.addAll(page.getRelationships());
            startFrom = startFrom + page.getRelationships().size();

            page = openMetadataStore.findRelationshipsBetweenMetadataElements(relationshipTypeName,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              this.getRawQueryOptions(startFrom));
        }

        return relationships;
    }


    /**
     * Retrieve every element attached to an element (at end 1) through a type of relationship.
     *
     * @param parentGUID element at end 1
     * @param attachmentTypeName type of relationship
     * @return list of attached elements (may be empty)
     * @throws Exception problem retrieving the elements
     */
    private List<RelatedMetadataElement> getAttachedElements(String parentGUID,
                                                             String attachmentTypeName) throws Exception
    {
        OpenMetadataStore            openMetadataStore = integrationContext.getOpenMetadataStore();
        List<RelatedMetadataElement> attachedElements  = new ArrayList<>();

        int startFrom = 0;

        RelatedMetadataElementList page = openMetadataStore.getRelatedMetadataElements(parentGUID, 1, attachmentTypeName, this.getRawQueryOptions(startFrom));

        while ((page != null) && (page.getElementList() != null) && (! page.getElementList().isEmpty()))
        {
            attachedElements.addAll(page.getElementList());
            startFrom = startFrom + page.getElementList().size();

            page = openMetadataStore.getRelatedMetadataElements(parentGUID, 1, attachmentTypeName, this.getRawQueryOptions(startFrom));
        }

        return attachedElements;
    }


    /**
     * Return query options that ask for the instances as they are stored, rather than combined.
     *
     * @param startFrom where to start the page
     * @return query options
     */
    private QueryOptions getRawQueryOptions(int startFrom)
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setForDuplicateProcessing(true);
        queryOptions.setStartFrom(startFrom);
        queryOptions.setPageSize(integrationContext.getMaxPageSize());

        return queryOptions;
    }


    /**
     * Return the anchor of an element from its Anchors classification.
     *
     * @param element element to test
     * @return anchor GUID or null if the element is not anchored
     */
    private String getAnchorGUID(OpenMetadataElement element)
    {
        final String methodName = "getAnchorGUID";

        if (element.getClassifications() != null)
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if ((classification != null) && (OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(classification.getClassificationName())))
                {
                    return propertyHelper.getStringProperty(connectorName,
                                                            OpenMetadataProperty.ANCHOR_GUID.name,
                                                            classification.getClassificationProperties(),
                                                            methodName);
                }
            }
        }

        return null;
    }


    /**
     * Group identical instances and return the groups with more than one member.  The oldest member of each group
     * is kept; members with the same creation time are ordered by their unique identifier, so the same one is kept
     * whichever order the repository returns them in.
     *
     * @param instances instances to compare
     * @param getKey function returning the value that identical instances share
     * @return duplicate groups (may be empty)
     * @param <T> type of instance
     */
    static <T extends ElementControlHeader> List<DuplicateGroup<T>> findDuplicates(List<T>             instances,
                                                                                    Function<T, String> getKey)
    {
        Map<String, List<T>> identicalInstances = new LinkedHashMap<>();

        for (T instance : instances)
        {
            identicalInstances.computeIfAbsent(getKey.apply(instance), key -> new ArrayList<>()).add(instance);
        }

        Comparator<T> oldestFirst = Comparator.comparing((T instance) -> getCreateTime(instance), Comparator.nullsLast(Comparator.naturalOrder()))
                                              .thenComparing(MendelDuplicateInstanceRemover::getInstanceGUID, Comparator.nullsLast(Comparator.naturalOrder()));

        List<DuplicateGroup<T>> groups = new ArrayList<>();

        for (List<T> identical : identicalInstances.values())
        {
            if (identical.size() > 1)
            {
                List<T> sorted = new ArrayList<>(identical);

                sorted.sort(oldestFirst);
                groups.add(new DuplicateGroup<>(sorted.get(0), sorted.subList(1, sorted.size())));
            }
        }

        return groups;
    }


    /**
     * Return the total number of instances to delete.
     *
     * @param groups duplicate groups
     * @return count
     * @param <T> type of instance
     */
    static <T> int countDuplicates(List<DuplicateGroup<T>> groups)
    {
        int count = 0;

        for (DuplicateGroup<T> group : groups)
        {
            count = count + group.duplicates().size();
        }

        return count;
    }


    /**
     * Return the value that identical relationships share: type, ends, effectivity dates and properties.
     *
     * @param relationship relationship
     * @return key
     */
    static String getRelationshipKey(OpenMetadataRelationship relationship)
    {
        String typeName = getTypeName(relationship);

        if ((relationship.getRelationshipType() != null) && (relationship.getRelationshipType().getTypeName() != null))
        {
            typeName = relationship.getRelationshipType().getTypeName();
        }

        return typeName + "|" +
                getGUID(relationship.getElementAtEnd1()) + "|" +
                getGUID(relationship.getElementAtEnd2()) + "|" +
                getTime(relationship.getEffectiveFromTime()) + "|" +
                getTime(relationship.getEffectiveToTime()) + "|" +
                getPropertiesKey(relationship.getRelationshipProperties());
    }


    /**
     * Return the value that identical attached elements share: the attachment relationship's type, effectivity
     * dates and properties, and the element's type, effectivity dates and properties.  The element at end 1 is
     * the same for all of the elements compared, so it is not part of the key.
     *
     * @param attachment attached element and the relationship that attaches it
     * @return key
     */
    static String getAttachmentKey(RelatedMetadataElement attachment)
    {
        OpenMetadataElement element = attachment.getElement();

        return getTypeName(attachment) + "|" +
                getTime(attachment.getEffectiveFromTime()) + "|" +
                getTime(attachment.getEffectiveToTime()) + "|" +
                getPropertiesKey(attachment.getRelationshipProperties()) + "|" +
                getTypeName(element) + "|" +
                getTime(element.getEffectiveFromTime()) + "|" +
                getTime(element.getEffectiveToTime()) + "|" +
                getPropertiesKey(element.getElementProperties());
    }


    /**
     * Return the properties in a stable order.
     *
     * @param properties properties (may be null)
     * @return string form
     */
    private static String getPropertiesKey(ElementProperties properties)
    {
        if ((properties == null) || (properties.getPropertiesAsStrings() == null))
        {
            return "{}";
        }

        return new TreeMap<>(properties.getPropertiesAsStrings()).toString();
    }


    /**
     * Return the type name of an instance.
     *
     * @param instance instance
     * @return type name or null
     */
    private static String getTypeName(ElementControlHeader instance)
    {
        if ((instance != null) && (instance.getType() != null))
        {
            return instance.getType().getTypeName();
        }

        return null;
    }


    /**
     * Return the unique identifier of an instance - the relationship, or the attached element.
     *
     * @param instance instance
     * @return GUID or null
     */
    private static String getInstanceGUID(ElementControlHeader instance)
    {
        if (instance instanceof OpenMetadataRelationship relationship)
        {
            return relationship.getRelationshipGUID();
        }
        else if ((instance instanceof RelatedMetadataElement attachment) && (attachment.getElement() != null))
        {
            return attachment.getElement().getElementGUID();
        }

        return null;
    }


    /**
     * Return the creation time of an instance - the relationship, or the attached element.
     *
     * @param instance instance
     * @return creation time or null
     */
    private static Date getCreateTime(ElementControlHeader instance)
    {
        ElementControlHeader header = instance;

        if ((instance instanceof RelatedMetadataElement attachment) && (attachment.getElement() != null))
        {
            header = attachment.getElement();
        }

        if (header.getVersions() != null)
        {
            return header.getVersions().getCreateTime();
        }

        return null;
    }


    /**
     * Return the unique identifier of a relationship end.
     *
     * @param end element stub
     * @return GUID or null
     */
    private static String getGUID(OpenMetadataElementStub end)
    {
        if (end != null)
        {
            return end.getGUID();
        }

        return null;
    }


    /**
     * Return a time as a number so that it compares reliably.
     *
     * @param time date (may be null)
     * @return string form
     */
    private static String getTime(Date time)
    {
        if (time != null)
        {
            return Long.toString(time.getTime());
        }

        return "";
    }
}
