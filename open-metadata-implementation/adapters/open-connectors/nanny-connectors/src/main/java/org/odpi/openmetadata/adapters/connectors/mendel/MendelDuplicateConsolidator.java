/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mendel;

import org.odpi.openmetadata.adapters.connectors.mendel.ffdc.MendelAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextClientBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataClassificationDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipEndCardinality;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefAttribute;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefLink;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * MendelDuplicateConsolidator combines a cluster of validated peer duplicates into a single consolidated element,
 * using survivorship rules to decide what the consolidated element contains.
 * <ul>
 *     <li>The properties come from the latest version of the cluster's members.  A property that only an earlier
 *     version supplies is added, so nothing that a member knows about is lost.</li>
 *     <li>The qualified name is derived rather than inherited - the original with the ISO-8601 time of the
 *     merge appended - because a qualified name is unique and the members still hold theirs.</li>
 *     <li>The classifications of all of the members are combined.  Where more than one member carries the same
 *     classification, the one from the latest member wins.</li>
 *     <li>The relationships of all of the members are combined.  A relationship is skipped when adding it would
 *     break the cardinality rules of its type - and where two members conflict on a relationship that the type
 *     only permits one of, the one from the latest member wins.</li>
 * </ul>
 * Wherever the survivorship rules have to choose - two members disagree on the value of a property or of a
 * classification, or the consolidated element can only carry one of two relationships - the losing value is
 * reported to the audit log so that a steward can see what the consolidated element left behind.  The same is
 * done for the content that can not be carried at all: a cluster whose members are of different types can hold
 * properties and classifications that the consolidated element's type does not allow.
 * <br><br>
 * The consolidated element carries the ConsolidatedDuplicate classification with a status of VALIDATED, and is
 * linked to each of its members with a ConsolidatedDuplicateLink relationship.  That combination causes the
 * retrieval processing to return the consolidated element in place of the members.
 */
public class MendelDuplicateConsolidator
{
    private final IntegrationContext integrationContext;
    private final PropertyHelper     propertyHelper;
    private final String             connectorName;
    private final AuditLog           auditLog;

    /**
     * The relationships that describe the duplicate processing itself are not copied to the consolidated element.
     */
    private static final List<String> excludedRelationshipTypes = List.of(OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                          OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeName);

    /**
     * The classifications that describe the duplicate processing itself are not copied to the consolidated element:
     * it is the survivor of the cluster rather than another member of it.  Neither is Anchors, because the
     * consolidated element is created as its own anchor, nor Memento, because a member being archived says nothing
     * about the element that replaces the whole cluster.
     */
    private static final List<String> excludedClassificationTypes = List.of(OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName,
                                                                            OpenMetadataType.CONSOLIDATED_DUPLICATE_CLASSIFICATION.typeName,
                                                                            OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                                            OpenMetadataType.MEMENTO_CLASSIFICATION.typeName);

    /**
     * Cache of the relationship type definitions used to test the cardinality rules.
     */
    private final Map<String, OpenMetadataRelationshipDef> relationshipTypeDefs = new HashMap<>();

    /**
     * Cache of the classification type definitions used to test which elements a classification can be attached to.
     */
    private final Map<String, OpenMetadataClassificationDef> classificationTypeDefs = new HashMap<>();


    /**
     * Constructor.
     *
     * @param integrationContext context giving access to open metadata
     * @param propertyHelper helper for working with element properties
     * @param connectorName name of the calling connector - used in messages
     * @param auditLog logging destination
     */
    public MendelDuplicateConsolidator(IntegrationContext integrationContext,
                                       PropertyHelper     propertyHelper,
                                       String             connectorName,
                                       AuditLog           auditLog)
    {
        this.integrationContext = integrationContext;
        this.propertyHelper     = propertyHelper;
        this.connectorName      = connectorName;
        this.auditLog           = auditLog;
    }


    /**
     * Consolidate a cluster of validated peer duplicates into a single element.  Nothing happens if the cluster has
     * already been consolidated.
     *
     * @param members the elements in the cluster
     *
     * @throws Exception the consolidation failed - reported by the caller
     */
    public void consolidateCluster(List<OpenMetadataElementStub> members) throws Exception
    {
        final String methodName = "consolidateCluster";

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        /*
         * Retrieve the full elements because the stubs in the duplicate links carry no properties or versions.
         */
        List<OpenMetadataElement> clusterMembers = new ArrayList<>();

        for (OpenMetadataElementStub member : members)
        {
            if (member != null)
            {
                if (this.isAlreadyConsolidated(member.getGUID()))
                {
                    return;
                }

                OpenMetadataElement clusterMember = openMetadataStore.getMetadataElementByGUID(member.getGUID(), this.getRawGetOptions());

                if (clusterMember != null)
                {
                    clusterMembers.add(clusterMember);
                }
            }
        }

        if (clusterMembers.size() < 2)
        {
            return;
        }

        /*
         * Order the members so that the latest is first.  This is the order that the survivorship rules work in:
         * the latest value of anything wins, and the earlier members only fill in the gaps.
         */
        clusterMembers.sort(Comparator.comparing(this::getLastUpdateTime, Comparator.nullsLast(Comparator.reverseOrder())));

        OpenMetadataElement latestMember = clusterMembers.get(0);

        String consolidatedElementGUID = this.createConsolidatedElement(latestMember, clusterMembers);

        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

        for (OpenMetadataElement clusterMember : clusterMembers)
        {
            /*
             * The purpose-built call is used rather than creating the relationship directly, so that the
             * stewardship API owns how a consolidated element is tied to the elements it was built from.
             */
            classificationExplorerClient.linkConsolidatedDuplicateToSourceElement(consolidatedElementGUID,
                                                                                   clusterMember.getElementGUID(),
                                                                                   null,
                                                                                   this.getRawMakeAnchorOptions(classificationExplorerClient));
        }

        this.combineRelationships(consolidatedElementGUID, clusterMembers);

        auditLog.logMessage(methodName,
                            MendelAuditCode.DUPLICATES_CONSOLIDATED.getMessageDefinition(connectorName,
                                                                                         consolidatedElementGUID,
                                                                                         Integer.toString(clusterMembers.size()),
                                                                                         latestMember.getType().getTypeName()));
    }


    /**
     * Determine whether an element is already linked to a consolidated element.
     *
     * @param elementGUID unique identifier of the element to test
     * @return boolean flag
     *
     * @throws Exception the retrieval failed - reported by the caller
     */
    private boolean isAlreadyConsolidated(String elementGUID) throws Exception
    {
        RelatedMetadataElementList consolidatedLinks = integrationContext.getOpenMetadataStore().getRelatedMetadataElements(elementGUID,
                                                                                                                             0,
                                                                                                                             OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeName,
                                                                                                                             this.getRawQueryOptions(0));

        return (consolidatedLinks != null) && (consolidatedLinks.getElementList() != null) && (! consolidatedLinks.getElementList().isEmpty());
    }


    /**
     * Create the consolidated element from the merged properties and classifications of the cluster's members.
     *
     * @param latestMember the most recently updated member - it supplies the type of the consolidated element
     * @param clusterMembers the members of the cluster, latest first
     * @return unique identifier of the new consolidated element
     *
     * @throws Exception the creation failed - reported by the caller
     */
    private String createConsolidatedElement(OpenMetadataElement       latestMember,
                                             List<OpenMetadataElement> clusterMembers) throws Exception
    {
        final String methodName = "createConsolidatedElement";

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        ElementProperties consolidatedProperties = this.getConsolidatedProperties(latestMember, clusterMembers);

        /*
         * The qualified name is the one exception to taking the latest member's value.  A qualified name is
         * unique, and the members are still there holding theirs, so the consolidated element is given a
         * derived one: the original with the time of the merge appended.  Without this the repository
         * rejects the new element as a duplicate of the very elements it is consolidating.
         */
        String originalQualifiedName = propertyHelper.getStringProperty(connectorName,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        consolidatedProperties,
                                                                        methodName);

        if (originalQualifiedName != null)
        {
            /*
             * The time is written in ISO-8601 rather than the JVM's default date format: it sorts
             * chronologically as text, carries no spaces or locale-dependent timezone abbreviation, and can
             * be parsed by anything that later needs to know when the merge happened.
             */
            consolidatedProperties = propertyHelper.addStringProperty(consolidatedProperties,
                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                       originalQualifiedName + "_merged:" + Instant.now());
        }

        /*
         * The consolidated element stands in for its members, so it carries their classifications too.
         */
        Map<String, NewElementProperties> initialClassifications = this.getConsolidatedClassifications(latestMember, clusterMembers);

        /*
         * The consolidated element must carry a validated ConsolidatedDuplicate classification, otherwise the
         * retrieval processing ignores it and continues to return the members separately.  It is added last so
         * that it can not be displaced by anything picked up from the members.
         */
        ElementProperties classificationProperties = propertyHelper.addIntProperty(null,
                                                                                   OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                                   StatusIdentifier.VALIDATED.getOrdinal());

        classificationProperties = propertyHelper.addStringProperty(classificationProperties,
                                                                    OpenMetadataProperty.SOURCE.name,
                                                                    connectorName);

        initialClassifications.put(OpenMetadataType.CONSOLIDATED_DUPLICATE_CLASSIFICATION.typeName,
                                   new NewElementProperties(classificationProperties));

        NewElementOptions newElementOptions = new NewElementOptions(openMetadataStore.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);
        newElementOptions.setForDuplicateProcessing(true);

        return openMetadataStore.createMetadataElementInStore(latestMember.getType().getTypeName(),
                                                              newElementOptions,
                                                              initialClassifications,
                                                              new NewElementProperties(consolidatedProperties),
                                                              null);
    }


    /**
     * Merge the properties of the cluster's members.  The members are processed latest first, so the latest
     * member's value of a property wins and the earlier members only fill in the properties that it does not
     * supply.  Where an earlier member disagrees with the value that has already been taken, or supplies a
     * property that the consolidated element's type does not define, the value that is dropped is reported to
     * the audit log.
     *
     * @param latestMember the most recently updated member - it supplies the type of the consolidated element
     * @param clusterMembers the members of the cluster, latest first
     * @return merged properties
     *
     * @throws Exception the type of the consolidated element could not be retrieved - reported by the caller
     */
    private ElementProperties getConsolidatedProperties(OpenMetadataElement       latestMember,
                                                        List<OpenMetadataElement> clusterMembers) throws Exception
    {
        final String methodName = "getConsolidatedProperties";

        String consolidatedTypeName = latestMember.getType().getTypeName();

        /*
         * A null answer means that nothing is known about the type's properties, in which case everything the
         * members supply is passed on and the repository has the final say.
         */
        Set<String> validPropertyNames = this.getValidPropertyNames(integrationContext.getOpenMetadataTypesClient().getTypeDefByName(true,
                                                                                                                                     false,
                                                                                                                                     consolidatedTypeName));

        ElementProperties consolidatedProperties = new ElementProperties();

        /*
         * The property values that have been taken are tracked here rather than read back from the properties
         * being built: getPropertyValueMap() answers null while the set is empty, and returns a copy once it
         * is not, so it can neither be tested nor added to directly.  The member that supplied each value is
         * kept alongside it so that a later conflict can name both sides.
         */
        Map<String, PropertyValue> valuesTaken    = new HashMap<>();
        Map<String, String>        valueSuppliers = new HashMap<>();

        for (OpenMetadataElement clusterMember : clusterMembers)
        {
            if (clusterMember.getElementProperties() != null)
            {
                Map<String, PropertyValue> memberProperties = clusterMember.getElementProperties().getPropertyValueMap();

                if (memberProperties != null)
                {
                    for (Map.Entry<String, PropertyValue> memberProperty : memberProperties.entrySet())
                    {
                        String        propertyName  = memberProperty.getKey();
                        PropertyValue propertyValue = memberProperty.getValue();

                        if ((validPropertyNames != null) && (! validPropertyNames.contains(propertyName)))
                        {
                            /*
                             * The consolidated element takes its type from the latest member, so a property that
                             * only an earlier member's type defines has nowhere to go.  Storing it anyway would
                             * have the repository reject the whole consolidation.
                             */
                            auditLog.logMessage(methodName,
                                                MendelAuditCode.INCOMPATIBLE_PROPERTY.getMessageDefinition(connectorName,
                                                                                                           propertyName,
                                                                                                           this.getValueAsString(propertyValue),
                                                                                                           clusterMember.getElementGUID(),
                                                                                                           consolidatedTypeName));
                        }
                        else if (! valuesTaken.containsKey(propertyName))
                        {
                            valuesTaken.put(propertyName, propertyValue);
                            valueSuppliers.put(propertyName, clusterMember.getElementGUID());

                            consolidatedProperties.setProperty(propertyName, propertyValue);
                        }
                        else if (! Objects.equals(valuesTaken.get(propertyName), propertyValue))
                        {
                            /*
                             * The qualified name is left out of this check.  The members of a cluster are
                             * expected to disagree on it - and the consolidated element takes neither value,
                             * since it is given a derived one - so reporting it as a conflict is just noise.
                             */
                            if (! OpenMetadataProperty.QUALIFIED_NAME.name.equals(propertyName))
                            {
                                auditLog.logMessage(methodName,
                                                    MendelAuditCode.CONFLICTING_PROPERTY.getMessageDefinition(connectorName,
                                                                                                              this.getValueAsString(propertyValue),
                                                                                                              clusterMember.getElementGUID(),
                                                                                                              propertyName,
                                                                                                              valueSuppliers.get(propertyName),
                                                                                                              this.getValueAsString(valuesTaken.get(propertyName))));
                            }
                        }
                    }
                }
            }
        }

        return consolidatedProperties;
    }


    /**
     * Merge the classifications of the cluster's members.  Only one classification of each type can be attached
     * to an element, so the members are processed latest first and the latest member's version of a
     * classification wins.  A classification that is dropped - because a later member has already supplied one
     * with different properties, or because it can not be attached to the consolidated element's type - is
     * reported to the audit log.
     *
     * @param latestMember the most recently updated member - it supplies the type of the consolidated element
     * @param clusterMembers the members of the cluster, latest first
     * @return the classifications to create the consolidated element with
     *
     * @throws Exception a classification type could not be retrieved - reported by the caller
     */
    private Map<String, NewElementProperties> getConsolidatedClassifications(OpenMetadataElement       latestMember,
                                                                             List<OpenMetadataElement> clusterMembers) throws Exception
    {
        final String methodName = "getConsolidatedClassifications";

        Map<String, NewElementProperties> consolidatedClassifications = new HashMap<>();

        /*
         * The classifications that have been taken are tracked in their original form so that the properties of
         * a later one can be compared with them, alongside the member that supplied each one so that a conflict
         * can name both sides.
         */
        Map<String, AttachedClassification> classificationsTaken    = new HashMap<>();
        Map<String, String>                 classificationSuppliers = new HashMap<>();

        for (OpenMetadataElement clusterMember : clusterMembers)
        {
            if (clusterMember.getClassifications() != null)
            {
                for (AttachedClassification classification : clusterMember.getClassifications())
                {
                    if ((classification == null) || (classification.getClassificationName() == null))
                    {
                        continue;
                    }

                    String classificationName = classification.getClassificationName();

                    if (excludedClassificationTypes.contains(classificationName))
                    {
                        continue;
                    }

                    AttachedClassification classificationTaken = classificationsTaken.get(classificationName);

                    if (classificationTaken == null)
                    {
                        if (! this.isValidClassification(classificationName, latestMember))
                        {
                            auditLog.logMessage(methodName,
                                                MendelAuditCode.INCOMPATIBLE_CLASSIFICATION.getMessageDefinition(connectorName,
                                                                                                                 classificationName,
                                                                                                                 clusterMember.getElementGUID(),
                                                                                                                 latestMember.getType().getTypeName()));
                            continue;
                        }

                        classificationsTaken.put(classificationName, classification);
                        classificationSuppliers.put(classificationName, clusterMember.getElementGUID());

                        consolidatedClassifications.put(classificationName,
                                                         this.getClassificationProperties(classification, clusterMember.getElementGUID()));
                    }
                    else if (! this.samePropertyValues(classificationTaken.getClassificationProperties(),
                                                        classification.getClassificationProperties()))
                    {
                        auditLog.logMessage(methodName,
                                            MendelAuditCode.CONFLICTING_CLASSIFICATION.getMessageDefinition(connectorName,
                                                                                                             classificationName,
                                                                                                             this.getPropertiesAsString(classification.getClassificationProperties()),
                                                                                                             clusterMember.getElementGUID(),
                                                                                                             classificationSuppliers.get(classificationName),
                                                                                                             this.getPropertiesAsString(classificationTaken.getClassificationProperties())));
                    }
                }
            }
        }

        return consolidatedClassifications;
    }


    /**
     * Return the properties to create one of the members' classifications with.  A property that the
     * classification's type does not define - which happens when the member was created against a different
     * version of the open metadata types - is dropped and reported, rather than being allowed to fail the
     * whole consolidation.
     *
     * @param classification the member's classification
     * @param memberGUID unique identifier of the member that supplied it - used in messages
     * @return properties or null if the classification has none
     *
     * @throws Exception the classification type could not be retrieved - reported by the caller
     */
    private NewElementProperties getClassificationProperties(AttachedClassification classification,
                                                             String                 memberGUID) throws Exception
    {
        final String methodName = "getClassificationProperties";

        if (classification.getClassificationProperties() == null)
        {
            return null;
        }

        Map<String, PropertyValue> memberProperties = classification.getClassificationProperties().getPropertyValueMap();

        if (memberProperties == null)
        {
            return null;
        }

        Set<String> validPropertyNames = this.getValidPropertyNames(this.getClassificationTypeDef(classification.getClassificationName()));

        if (validPropertyNames == null)
        {
            return new NewElementProperties(classification.getClassificationProperties());
        }

        ElementProperties classificationProperties = new ElementProperties();

        for (Map.Entry<String, PropertyValue> memberProperty : memberProperties.entrySet())
        {
            if (validPropertyNames.contains(memberProperty.getKey()))
            {
                classificationProperties.setProperty(memberProperty.getKey(), memberProperty.getValue());
            }
            else
            {
                auditLog.logMessage(methodName,
                                    MendelAuditCode.INCOMPATIBLE_CLASSIFICATION_PROPERTY.getMessageDefinition(connectorName,
                                                                                                              memberProperty.getKey(),
                                                                                                              this.getValueAsString(memberProperty.getValue()),
                                                                                                              classification.getClassificationName(),
                                                                                                              memberGUID));
            }
        }

        return new NewElementProperties(classificationProperties);
    }


    /**
     * Determine whether a classification can be attached to the consolidated element.  The members of a cluster
     * are not necessarily all of the same type - a steward can validate a duplicate link between elements of
     * different types - so a classification that is valid for one member is not necessarily valid for the type
     * that the consolidated element takes from the latest member.
     *
     * @param classificationName name of the classification
     * @param latestMember the member that supplies the type of the consolidated element
     * @return boolean flag - true means the classification can be attached
     *
     * @throws Exception the type could not be retrieved - reported by the caller
     */
    private boolean isValidClassification(String              classificationName,
                                          OpenMetadataElement latestMember) throws Exception
    {
        OpenMetadataClassificationDef classificationDef = this.getClassificationTypeDef(classificationName);

        if ((classificationDef == null) || (classificationDef.getValidEntityDefs() == null) || (classificationDef.getValidEntityDefs().isEmpty()))
        {
            /*
             * Nothing is known about where this classification can be attached, so it is passed on and the
             * repository has the final say.
             */
            return true;
        }

        List<String> validEntityTypeNames = new ArrayList<>();

        for (OpenMetadataTypeDefLink validEntityDef : classificationDef.getValidEntityDefs())
        {
            if ((validEntityDef != null) && (validEntityDef.getName() != null))
            {
                validEntityTypeNames.add(validEntityDef.getName());
            }
        }

        return propertyHelper.isTypeOf(latestMember, validEntityTypeNames);
    }


    /**
     * Retrieve the definition of a classification type, caching it for the other members that carry it.
     *
     * @param classificationName name of the classification type
     * @return type definition or null if it is not a classification type
     *
     * @throws Exception the type could not be retrieved - reported by the caller
     */
    private OpenMetadataClassificationDef getClassificationTypeDef(String classificationName) throws Exception
    {
        if (classificationTypeDefs.containsKey(classificationName))
        {
            return classificationTypeDefs.get(classificationName);
        }

        OpenMetadataTypeDef           typeDef           = integrationContext.getOpenMetadataTypesClient().getTypeDefByName(true, false, classificationName);
        OpenMetadataClassificationDef classificationDef = null;

        if (typeDef instanceof OpenMetadataClassificationDef retrievedClassificationDef)
        {
            classificationDef = retrievedClassificationDef;
        }

        classificationTypeDefs.put(classificationName, classificationDef);

        return classificationDef;
    }


    /**
     * Return the names of the properties that a type defines, including the ones it inherits from its
     * supertypes.
     *
     * @param typeDef the type definition to read - it must have been retrieved with inherited attributes
     * @return property names, or null if the type's properties are not known
     */
    private Set<String> getValidPropertyNames(OpenMetadataTypeDef typeDef)
    {
        if ((typeDef == null) || (typeDef.getAttributeDefinitions() == null) || (typeDef.getAttributeDefinitions().isEmpty()))
        {
            return null;
        }

        Set<String> validPropertyNames = new HashSet<>();

        for (OpenMetadataTypeDefAttribute attributeDefinition : typeDef.getAttributeDefinitions())
        {
            if ((attributeDefinition != null) && (attributeDefinition.getAttributeName() != null))
            {
                validPropertyNames.add(attributeDefinition.getAttributeName());
            }
        }

        return validPropertyNames;
    }


    /**
     * Determine whether two sets of properties hold the same values.  The comparison goes through the property
     * value maps because an empty set of properties and no properties at all mean the same thing here.
     *
     * @param propertiesOne first set of properties
     * @param propertiesTwo second set of properties
     * @return boolean flag
     */
    private boolean samePropertyValues(ElementProperties propertiesOne,
                                       ElementProperties propertiesTwo)
    {
        Map<String, PropertyValue> valuesOne = (propertiesOne == null) ? null : propertiesOne.getPropertyValueMap();
        Map<String, PropertyValue> valuesTwo = (propertiesTwo == null) ? null : propertiesTwo.getPropertyValueMap();

        return Objects.equals(valuesOne, valuesTwo);
    }


    /**
     * Return a property value in a form that can be inserted into an audit log message.
     *
     * @param propertyValue value to convert
     * @return printable value
     */
    private String getValueAsString(PropertyValue propertyValue)
    {
        if (propertyValue == null)
        {
            return "<null>";
        }

        return propertyValue.valueAsString();
    }


    /**
     * Return a set of properties in a form that can be inserted into an audit log message.
     *
     * @param properties properties to convert
     * @return printable properties
     */
    private String getPropertiesAsString(ElementProperties properties)
    {
        if (properties == null)
        {
            return "<none>";
        }

        Map<String, String> propertiesAsStrings = properties.getPropertiesAsStrings();

        if (propertiesAsStrings == null)
        {
            return "<none>";
        }

        return propertiesAsStrings.toString();
    }


    /**
     * Copy the relationships of the cluster's members onto the consolidated element.  The members are processed
     * latest first, so where the cardinality rules only allow one relationship, it is the latest member's
     * relationship that is used.
     *
     * @param consolidatedElementGUID unique identifier of the consolidated element
     * @param clusterMembers the members of the cluster, latest first
     *
     * @throws Exception the retrieval or creation failed - reported by the caller
     */
    private void combineRelationships(String                    consolidatedElementGUID,
                                      List<OpenMetadataElement> clusterMembers) throws Exception
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        /*
         * These record what has already been attached to the consolidated element, so that the cardinality rules
         * are applied across all of the members rather than one member at a time.
         */
        Set<String> attachedPairs        = new HashSet<>();
        Set<String> singletonEndsUsed    = new HashSet<>();

        Set<String> memberGUIDs = new HashSet<>();

        for (OpenMetadataElement member : clusterMembers)
        {
            memberGUIDs.add(member.getElementGUID());
        }

        for (OpenMetadataElement clusterMember : clusterMembers)
        {
            int startFrom = 0;

            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(clusterMember.getElementGUID(),
                                                                                                       0,
                                                                                                       null,
                                                                                                       this.getRawQueryOptions(startFrom));

            while ((relatedElements != null) && (relatedElements.getElementList() != null) && (! relatedElements.getElementList().isEmpty()))
            {
                for (RelatedMetadataElement relatedElement : relatedElements.getElementList())
                {
                    this.combineRelationship(consolidatedElementGUID,
                                             clusterMember.getElementGUID(),
                                             relatedElement,
                                             memberGUIDs,
                                             attachedPairs,
                                             singletonEndsUsed);
                }

                startFrom = startFrom + integrationContext.getMaxPageSize();

                relatedElements = openMetadataStore.getRelatedMetadataElements(clusterMember.getElementGUID(),
                                                                                0,
                                                                                null,
                                                                                this.getRawQueryOptions(startFrom));
            }
        }
    }


    /**
     * Copy one of a member's relationships onto the consolidated element, if the cardinality rules allow it.
     *
     * @param consolidatedElementGUID unique identifier of the consolidated element
     * @param memberGUID unique identifier of the member whose relationship this is - used in messages
     * @param relatedElement the member's relationship
     * @param memberGUIDs the members of the cluster - relationships between members are not copied
     * @param attachedPairs the pairs of elements already linked by each type of relationship
     * @param singletonEndsUsed the relationship types that have used up their single permitted end
     *
     * @throws Exception the creation failed - reported by the caller
     */
    private void combineRelationship(String                 consolidatedElementGUID,
                                     String                 memberGUID,
                                     RelatedMetadataElement relatedElement,
                                     Set<String>            memberGUIDs,
                                     Set<String>            attachedPairs,
                                     Set<String>            singletonEndsUsed) throws Exception
    {
        final String methodName = "combineRelationship";

        if ((relatedElement == null) || (relatedElement.getType() == null) || (relatedElement.getElement() == null))
        {
            return;
        }

        String relationshipTypeName = relatedElement.getType().getTypeName();
        String otherElementGUID     = relatedElement.getElement().getElementGUID();

        /*
         * The duplicate processing relationships are not copied, and neither are the relationships between the
         * members of this cluster - they describe the members' relationship to each other, not to the world.
         */
        if ((excludedRelationshipTypes.contains(relationshipTypeName)) || (memberGUIDs.contains(otherElementGUID)))
        {
            return;
        }

        /*
         * The consolidated element takes the place of the member, so it sits at the end of the relationship that
         * the member was at.  getElementAtEnd1() describes the *other* element, so the member - and therefore the
         * consolidated element - is at the opposite end.
         */
        boolean consolidatedElementAtEnd1 = ! relatedElement.getElementAtEnd1();

        String end1GUID = consolidatedElementAtEnd1 ? consolidatedElementGUID : otherElementGUID;
        String end2GUID = consolidatedElementAtEnd1 ? otherElementGUID : consolidatedElementGUID;

        /*
         * Never create the same relationship between the same two elements twice.
         */
        String pairKey = relationshipTypeName + "::" + end1GUID + "::" + end2GUID;

        if (! attachedPairs.add(pairKey))
        {
            return;
        }

        /*
         * Where the type only permits one relationship at the consolidated element's end, the first one wins.
         * The members are processed latest first, so that is the latest member's relationship.
         */
        if (this.isSingletonEnd(relationshipTypeName, consolidatedElementAtEnd1))
        {
            String singletonKey = relationshipTypeName + "::" + (consolidatedElementAtEnd1 ? "1" : "2");

            if (! singletonEndsUsed.add(singletonKey))
            {
                auditLog.logMessage(methodName,
                                    MendelAuditCode.CONFLICTING_RELATIONSHIP.getMessageDefinition(connectorName,
                                                                                                  relationshipTypeName,
                                                                                                  memberGUID,
                                                                                                  otherElementGUID,
                                                                                                  consolidatedElementGUID));
                return;
            }
        }

        integrationContext.getOpenMetadataStore().createRelatedElementsInStore(relationshipTypeName,
                                                                                end1GUID,
                                                                                end2GUID,
                                                                                this.getRawMakeAnchorOptions(integrationContext.getOpenMetadataStore()),
                                                                                this.getRelationshipProperties(relatedElement));
    }


    /**
     * Determine whether a relationship type only permits the element at the requested end to have one relationship
     * of this type.  The cardinality of an end describes how many elements can be attached at that end, so the
     * limit on the element at end one is the cardinality recorded on end two, and the other way around.
     *
     * @param relationshipTypeName name of the relationship type
     * @param atEnd1 is the element of interest at end one?
     * @return boolean flag - true means only one relationship of this type is permitted
     *
     * @throws Exception the type could not be retrieved - reported by the caller
     */
    private boolean isSingletonEnd(String  relationshipTypeName,
                                   boolean atEnd1) throws Exception
    {
        OpenMetadataRelationshipDef relationshipDef = this.getRelationshipTypeDef(relationshipTypeName);

        if (relationshipDef == null)
        {
            return false;
        }

        if (atEnd1)
        {
            return (relationshipDef.getEndDef2() != null) &&
                    (relationshipDef.getEndDef2().getAttributeCardinality() == OpenMetadataRelationshipEndCardinality.AT_MOST_ONE);
        }

        return (relationshipDef.getEndDef1() != null) &&
                (relationshipDef.getEndDef1().getAttributeCardinality() == OpenMetadataRelationshipEndCardinality.AT_MOST_ONE);
    }


    /**
     * Retrieve the definition of a relationship type, caching it for the other relationships of the same type.
     *
     * @param relationshipTypeName name of the relationship type
     * @return type definition or null if it is not a relationship type
     *
     * @throws Exception the type could not be retrieved - reported by the caller
     */
    private OpenMetadataRelationshipDef getRelationshipTypeDef(String relationshipTypeName) throws Exception
    {
        if (relationshipTypeDefs.containsKey(relationshipTypeName))
        {
            return relationshipTypeDefs.get(relationshipTypeName);
        }

        OpenMetadataTypeDef         typeDef         = integrationContext.getOpenMetadataTypesClient().getTypeDefByName(false, true, relationshipTypeName);
        OpenMetadataRelationshipDef relationshipDef = null;

        if (typeDef instanceof OpenMetadataRelationshipDef retrievedRelationshipDef)
        {
            relationshipDef = retrievedRelationshipDef;
        }

        relationshipTypeDefs.put(relationshipTypeName, relationshipDef);

        return relationshipDef;
    }


    /**
     * Return the properties to use on the copied relationship.
     *
     * @param relatedElement the member's relationship
     * @return properties or null if the relationship has none
     */
    private NewElementProperties getRelationshipProperties(RelatedMetadataElement relatedElement)
    {
        if (relatedElement.getRelationshipProperties() == null)
        {
            return null;
        }

        return new NewElementProperties(relatedElement.getRelationshipProperties());
    }


    /**
     * Return the options to use when writing a relationship, asking for the elements named at each end to be
     * taken literally rather than resolved to whatever their duplicates combine into.
     * <br>
     * Without this, a member of a cluster that is already being combined is resolved on the way in: the link
     * meant for the second member is created against the surviving element instead, and once the consolidated
     * element is the survivor the link degenerates into one from that element to itself.
     *
     * @param client client whose metadata source options are being built on
     * @return options
     */
    private MakeAnchorOptions getRawMakeAnchorOptions(ConnectorContextClientBase client)
    {
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(client.getMetadataSourceOptions());

        makeAnchorOptions.setForDuplicateProcessing(true);

        return makeAnchorOptions;
    }


    /**
     * Return query options that ask for the duplicates as they are stored, rather than combined.  Reading a
     * combined view here would report a member's relationships as the whole cluster's, so a cluster that has
     * already been consolidated would look as though each member had one link per member.
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
     * Return get options that ask for the element as it is stored, rather than combined with its duplicates.
     *
     * @return get options
     */
    private GetOptions getRawGetOptions()
    {
        GetOptions getOptions = new GetOptions();

        getOptions.setForDuplicateProcessing(true);

        return getOptions;
    }


    /**
     * Return the time that an element was last changed, so that the members of a cluster can be ordered.
     *
     * @param element element to test
     * @return update time, creation time if it has never been updated, or null if neither is available
     */
    private Date getLastUpdateTime(OpenMetadataElement element)
    {
        if (element.getVersions() != null)
        {
            if (element.getVersions().getUpdateTime() != null)
            {
                return element.getVersions().getUpdateTime();
            }

            return element.getVersions().getCreateTime();
        }

        return null;
    }
}
