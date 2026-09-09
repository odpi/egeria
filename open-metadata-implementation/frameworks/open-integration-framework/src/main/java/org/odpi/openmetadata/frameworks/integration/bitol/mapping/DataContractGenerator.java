/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaMap;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractEnumValue;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractLogicalTypeOptions;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractPricing;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractQualityRule;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractRelationship;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractRole;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSLAProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaObject;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractServer;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.PartOfRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.LinkedDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ConfidentialityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CriticalityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.DataQualityRuleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ServiceLevelObjectiveProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.AssociatedSecurityListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ConfidentialityLevel;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CriticalityLevel;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * DataContractGenerator builds an Open Data Contract Standard (ODCS) document from an Agreement in open metadata.  It is
 * the reverse of DataContractMapper: the agreement's data structures become schema objects, their data fields become
 * properties (with nested fields and array items, classifications and linked fields), the data quality rules governing
 * a structure or field become its quality checks, the service level objective governing the agreement becomes the SLA
 * properties, the endpoints listed as resources become servers, and the security roles associated with the agreement's
 * access control become the access roles.
 */
public class DataContractGenerator extends BitolGeneratorBase
{
    private static final String SCHEMA_SEGMENT = "Schema";
    private static final String ITEMS_SEGMENT  = "items";
    private static final String MAP_KEY_SEGMENT = "key";
    private static final String MAP_VALUE_SEGMENT = "value";


    /**
     * Constructor.
     *
     * @param context connector context providing access to the open metadata clients
     */
    public DataContractGenerator(ConnectorContextBase context)
    {
        super(context);
    }


    /**
     * Generate the ODCS document for an agreement.
     *
     * @param agreementGUID unique identifier of the Agreement element
     * @return document
     * @throws InvalidParameterException the element is not an agreement
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    public DataContract generateDataContract(String agreementGUID) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        OpenMetadataRootElement agreement = context.getCollectionClient().getCollectionByGUID(agreementGUID, context.getCollectionClient().getGetOptions());

        return generateDataContract(agreement);
    }


    /**
     * Generate the ODCS document for an agreement that has already been retrieved with its related elements.
     *
     * @param agreement Agreement element
     * @return document
     * @throws InvalidParameterException the element is not an agreement
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    public DataContract generateDataContract(OpenMetadataRootElement agreement) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "generateDataContract";

        if ((agreement == null) || (! propertyHelper.isTypeOf(agreement.getElementHeader(), OpenMetadataType.AGREEMENT.typeName)))
        {
            throw new InvalidParameterException(OIFErrorCode.WRONG_ELEMENT_TYPE_FOR_BITOL_DOCUMENT.getMessageDefinition(getGUID(agreement),
                                                                                                                methodName,
                                                                                                                getTypeName(agreement),
                                                                                                                BitolDocument.DATA_CONTRACT_KIND,
                                                                                                                OpenMetadataType.AGREEMENT.typeName),
                                                this.getClass().getName(),
                                                methodName,
                                                "agreement");
        }

        DataContract dataContract = new DataContract();

        fillFundamentals(agreement, dataContract);

        if (dataContract.getVersion() == null)
        {
            dataContract.setVersion("1.0.0");
        }
        if (dataContract.getStatus() == null)
        {
            dataContract.setStatus("active");
        }

        if (agreement.getProperties() instanceof AgreementProperties properties)
        {
            Map<String, String> additionalProperties = properties.getAdditionalProperties();

            if ((properties.getPurpose() != null) && (dataContract.getDescription() != null))
            {
                dataContract.getDescription().setPurpose(properties.getPurpose());
            }

            dataContract.setDataProduct(getBitolValue(additionalProperties, "dataProduct"));
            dataContract.setContractCreatedTs(getBitolValue(additionalProperties, "contractCreatedTs"));
            dataContract.setPrice(getPricing(additionalProperties));
        }

        dataContract.setSupport(getSupportChannels(agreement));
        dataContract.setTeam(getTeam(agreement));
        dataContract.setServers(getServers(agreement));
        dataContract.setSchema(getSchema(agreement));

        fillGovernance(agreement, dataContract);

        return dataContract;
    }


    /**
     * Return the pricing recorded on the agreement.
     *
     * @param additionalProperties additional properties of the agreement
     * @return pricing or null
     */
    private DataContractPricing getPricing(Map<String, String> additionalProperties)
    {
        String amount = getBitolValue(additionalProperties, "priceAmount");

        if (amount == null)
        {
            return null;
        }

        DataContractPricing pricing = new DataContractPricing();

        try
        {
            pricing.setPriceAmount(Double.valueOf(amount));
        }
        catch (NumberFormatException notNumeric)
        {
            return null;
        }

        pricing.setPriceCurrency(getBitolValue(additionalProperties, "priceCurrency"));
        pricing.setPriceUnit(getBitolValue(additionalProperties, "priceUnit"));

        return pricing;
    }


    /*
     * ==========================================================================================================
     * Schema
     */

    /**
     * Return the data structures that are items of the agreement as schema objects.
     *
     * @param agreement agreement element
     * @return list of schema objects or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private List<DataContractSchemaObject> getSchema(OpenMetadataRootElement agreement) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        if (agreement.getAgreementItems() == null)
        {
            return null;
        }

        List<DataContractSchemaObject> schema = new ArrayList<>();

        for (RelatedMetadataElementSummary item : agreement.getAgreementItems())
        {
            if ((item != null) && (item.getRelatedElement() != null) &&
                (propertyHelper.isTypeOf(item.getRelatedElement().getElementHeader(), OpenMetadataType.DATA_STRUCTURE.typeName)))
            {
                OpenMetadataRootElement structure = context.getDataStructureClient().getDataStructureByGUID(item.getRelatedElement().getElementHeader().getGUID(),
                                                                                                             context.getDataStructureClient().getGetOptions());

                if ((structure != null) && (structure.getProperties() instanceof DataStructureProperties structureProperties))
                {
                    DataContractSchemaObject schemaObject         = new DataContractSchemaObject();
                    Map<String, String>      additionalProperties = structureProperties.getAdditionalProperties();

                    schemaObject.setId(getBitolValue(additionalProperties, "id"));
                    schemaObject.setName(getSchemaElementName(structureProperties, structureProperties.getNamePatterns()));
                    schemaObject.setBusinessName(structureProperties.getDisplayName());
                    schemaObject.setPhysicalName(structureProperties.getNamespacePath());
                    schemaObject.setPhysicalType(getBitolValue(additionalProperties, "physicalType"));
                    schemaObject.setLogicalType((getBitolValue(additionalProperties, "logicalType") != null) ? getBitolValue(additionalProperties, "logicalType") : "object");
                    schemaObject.setDescription(structureProperties.getDescription());
                    schemaObject.setDataGranularityDescription(getBitolValue(additionalProperties, "dataGranularityDescription"));
                    schemaObject.setTags(getTags(structure));
                    schemaObject.setAuthoritativeDefinitions(getAuthoritativeDefinitions(structure));
                    schemaObject.setCustomProperties(getCustomProperties(additionalProperties));
                    schemaObject.setQuality(getQualityRules(structure));
                    schemaObject.setDeprecated(getDeprecated(additionalProperties));
                    schemaObject.setSynonyms(getSynonyms(additionalProperties));
                    schemaObject.setContext(getContext(additionalProperties));

                    List<DataContractSchemaProperty> properties    = new ArrayList<>();
                    List<DataContractRelationship>   relationships = new ArrayList<>();

                    if (structure.getContainsDataFields() != null)
                    {
                        for (RelatedMetadataElementSummary member : sortByPosition(structure.getContainsDataFields()))
                        {
                            if ((member != null) && (member.getRelatedElement() != null))
                            {
                                DataContractSchemaProperty property = getSchemaProperty(member.getRelatedElement().getElementHeader().getGUID(), schemaObject.getName(), relationships);

                                if (property != null)
                                {
                                    properties.add(property);
                                }
                            }
                        }
                    }

                    schemaObject.setProperties(properties.isEmpty() ? null : properties);
                    schemaObject.setRelationships(relationships.isEmpty() ? null : relationships);

                    schema.add(schemaObject);
                }
            }
        }

        return schema.isEmpty() ? null : schema;
    }


    /**
     * Return the technical name of a schema element: the first name pattern (the cataloguer stores the ODCS name
     * first), otherwise the display name.
     *
     * @param properties element properties
     * @param namePatterns name patterns
     * @return name
     */
    private String getSchemaElementName(ReferenceableProperties properties,
                                        List<String>            namePatterns)
    {
        if ((namePatterns != null) && (! namePatterns.isEmpty()) && (namePatterns.get(0) != null))
        {
            return namePatterns.get(0);
        }

        return properties.getDisplayName();
    }


    /**
     * Build a schema property from a data field, including its nested fields, array items and quality checks.  Any
     * linked data fields are added to the relationships list of the owning schema object.
     *
     * @param dataFieldGUID field to read
     * @param objectName name of the owning schema object (for relationship references)
     * @param relationships list to add property level relationships to
     * @return property or null if the field could not be read
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private DataContractSchemaProperty getSchemaProperty(String                         dataFieldGUID,
                                                         String                         objectName,
                                                         List<DataContractRelationship> relationships) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        OpenMetadataRootElement field = context.getDataFieldClient().getDataFieldByGUID(dataFieldGUID, context.getDataFieldClient().getGetOptions());

        if ((field == null) || (! (field.getProperties() instanceof DataFieldProperties fieldProperties)))
        {
            return null;
        }

        DataContractSchemaProperty property             = new DataContractSchemaProperty();
        Map<String, String>        additionalProperties = fieldProperties.getAdditionalProperties();

        property.setId(getBitolValue(additionalProperties, "id"));
        property.setName(getSchemaElementName(fieldProperties, fieldProperties.getNamePatterns()));
        property.setBusinessName(fieldProperties.getDisplayName());
        property.setDescription(fieldProperties.getDescription());
        property.setPhysicalName(getBitolValue(additionalProperties, "physicalName"));
        property.setLogicalType((getBitolValue(additionalProperties, "logicalType") != null) ? getBitolValue(additionalProperties, "logicalType") : fieldProperties.getDataType());
        property.setPhysicalType((getBitolValue(additionalProperties, "physicalType") != null) ? getBitolValue(additionalProperties, "physicalType") : fieldProperties.getDataType());
        property.setRequired(! fieldProperties.getIsNullable());
        property.setUnique(! fieldProperties.getAllowsDuplicateValues());
        property.setPartitioned(fieldProperties.getIsPartitionKey());
        property.setPartitionKeyPosition(fieldProperties.getIsPartitionKey() ? fieldProperties.getPartitionKeyPosition() : -1);
        property.setClassification(getClassification(field.getElementHeader().getConfidentiality(), additionalProperties));
        property.setEncryptedName(getBitolValue(additionalProperties, "encryptedName"));
        property.setTransformLogic(getBitolValue(additionalProperties, "transformLogic"));
        property.setTransformDescription(getBitolValue(additionalProperties, "transformDescription"));
        property.setTags(getTags(field));
        property.setAuthoritativeDefinitions(getAuthoritativeDefinitions(field));
        property.setCustomProperties(getCustomProperties(additionalProperties));
        property.setQuality(getQualityRules(field));
        property.setSemanticType(getBitolValue(additionalProperties, "semanticType"));
        property.setEnumValues(fromJSONList(getBitolValue(additionalProperties, "enum"), DataContractEnumValue.class));
        property.setDeprecated(getDeprecated(additionalProperties));
        property.setSynonyms(getSynonyms(additionalProperties));

        ElementClassification primaryKey = field.getElementHeader().getPrimaryKey();

        property.setPrimaryKey(primaryKey != null);

        String primaryKeyPosition = getBitolValue(additionalProperties, "primaryKeyPosition");

        if (primaryKeyPosition != null)
        {
            property.setPrimaryKeyPosition(parseInt(primaryKeyPosition));
        }
        else
        {
            property.setPrimaryKeyPosition(-1);
        }

        property.setCriticalDataElement(isCritical(field.getElementHeader().getCriticality(), additionalProperties));

        String transformSourceObjects = getBitolValue(additionalProperties, "transformSourceObjects");

        if (transformSourceObjects != null)
        {
            property.setTransformSourceObjects(List.of(transformSourceObjects.split(",\\s*")));
        }

        String examples = getBitolValue(additionalProperties, "examples");

        if (examples != null)
        {
            property.setExamples(new ArrayList<>(List.of(examples.split(",\\s*"))));
        }

        property.setLogicalTypeOptions(getLogicalTypeOptions(fieldProperties, additionalProperties));

        /*
         * Relationships to other fields (only the "to" direction is recorded on the property).
         */
        if (field.getLinkedToDataFields() != null)
        {
            for (RelatedMetadataElementSummary linked : field.getLinkedToDataFields())
            {
                if ((linked != null) && (linked.getRelatedElement() != null))
                {
                    DataContractRelationship relationship = new DataContractRelationship();

                    relationship.setType("foreignKey");
                    relationship.setTo(List.of(getFieldReference(linked.getRelatedElement().getProperties())));

                    if (linked.getRelationshipProperties() instanceof LinkedDataFieldProperties linkProperties)
                    {
                        if (linkProperties.getRelationshipTypeName() != null)
                        {
                            relationship.setType(linkProperties.getRelationshipTypeName());
                        }
                        relationship.setId(getBitolValue(linkProperties.getAdditionalProperties(), "id"));
                        relationship.setCustomProperties(getCustomProperties(linkProperties.getAdditionalProperties()));
                    }

                    if (property.getRelationships() == null)
                    {
                        property.setRelationships(new ArrayList<>());
                    }

                    property.getRelationships().add(relationship);
                }
            }
        }

        /*
         * Nested fields and array items.
         */
        if (field.getNestedDataFields() != null)
        {
            List<DataContractSchemaProperty> nestedProperties = new ArrayList<>();

            for (RelatedMetadataElementSummary nested : sortByPosition(field.getNestedDataFields()))
            {
                if ((nested != null) && (nested.getRelatedElement() != null))
                {
                    String role = null;

                    if (nested.getRelatedElement().getProperties() instanceof ReferenceableProperties nestedProperties1)
                    {
                        role = getNestedRole(nestedProperties1.getQualifiedName());
                    }

                    DataContractSchemaProperty nestedProperty = getSchemaProperty(nested.getRelatedElement().getElementHeader().getGUID(), objectName, relationships);

                    if (nestedProperty != null)
                    {
                        if (ITEMS_SEGMENT.equals(role))
                        {
                            nestedProperty.setName(null);
                            property.setItems(nestedProperty);
                        }
                        else if ((MAP_KEY_SEGMENT.equals(role)) || (MAP_VALUE_SEGMENT.equals(role)))
                        {
                            if (property.getMap() == null)
                            {
                                property.setMap(new DataContractSchemaMap());
                            }

                            nestedProperty.setName(null);

                            if (MAP_KEY_SEGMENT.equals(role))
                            {
                                property.getMap().setKey(nestedProperty);
                            }
                            else
                            {
                                property.getMap().setValue(nestedProperty);
                            }
                        }
                        else
                        {
                            nestedProperties.add(nestedProperty);
                        }
                    }
                }
            }

            property.setProperties(nestedProperties.isEmpty() ? null : nestedProperties);
        }

        return property;
    }


    /**
     * Return the role of a nested field from the last segment of its qualified name: items, key or value for the
     * fields the cataloguer creates for array items and map keys and values, otherwise null.
     *
     * @param qualifiedName qualified name of the nested field
     * @return role or null
     */
    private static String getNestedRole(String qualifiedName)
    {
        if (qualifiedName != null)
        {
            for (String role : new String[]{ITEMS_SEGMENT, MAP_KEY_SEGMENT, MAP_VALUE_SEGMENT})
            {
                if (qualifiedName.endsWith(BitolMapperBase.SEPARATOR + role))
                {
                    return role;
                }
            }
        }

        return null;
    }


    /**
     * Build the shorthand reference (object.property) for a linked field from its qualified name.  The cataloguer
     * builds field qualified names as contract::Schema::object::property.
     *
     * @param properties properties of the linked field
     * @return reference
     */
    private String getFieldReference(Object properties)
    {
        if (properties instanceof ReferenceableProperties referenceable)
        {
            String qualifiedName = referenceable.getQualifiedName();

            if (qualifiedName != null)
            {
                int schemaIndex = qualifiedName.indexOf(BitolMapperBase.SEPARATOR + SCHEMA_SEGMENT + BitolMapperBase.SEPARATOR);

                if (schemaIndex >= 0)
                {
                    String   remainder = qualifiedName.substring(schemaIndex + SCHEMA_SEGMENT.length() + (2 * BitolMapperBase.SEPARATOR.length()));
                    String[] parts     = remainder.split(BitolMapperBase.SEPARATOR);

                    if (parts.length >= 2)
                    {
                        return parts[0] + "." + parts[parts.length - 1];
                    }
                }
            }

            return (referenceable.getDisplayName() != null) ? referenceable.getDisplayName() : qualifiedName;
        }

        return null;
    }


    /**
     * Return the ODCS classification for a field: the value recorded by the cataloguer, otherwise the name of the
     * confidentiality level.
     *
     * @param confidentiality confidentiality classification (may be null)
     * @param additionalProperties additional properties of the field
     * @return classification string or null
     */
    private String getClassification(ElementClassification confidentiality,
                                     Map<String, String>   additionalProperties)
    {
        String recorded = getBitolValue(additionalProperties, "classification");

        if (recorded != null)
        {
            return recorded;
        }

        if ((confidentiality != null) && (confidentiality.getClassificationProperties() instanceof ConfidentialityProperties properties))
        {
            for (ConfidentialityLevel level : ConfidentialityLevel.values())
            {
                if (level.getOrdinal() == properties.getConfidentialityLevel())
                {
                    return level.getDisplayName().toLowerCase(java.util.Locale.ROOT);
                }
            }
        }

        return null;
    }


    /**
     * Return whether the field is a critical data element.
     *
     * @param criticality criticality classification (may be null)
     * @param additionalProperties additional properties of the field
     * @return boolean or null when not critical
     */
    private Boolean isCritical(ElementClassification criticality,
                               Map<String, String>   additionalProperties)
    {
        if ("true".equals(getBitolValue(additionalProperties, "criticalDataElement")))
        {
            return Boolean.TRUE;
        }

        if ((criticality != null) && (criticality.getClassificationProperties() instanceof CriticalityProperties properties))
        {
            return properties.getCriticalityLevel() >= CriticalityLevel.CRITICAL.getOrdinal() ? Boolean.TRUE : null;
        }

        return null;
    }


    /**
     * Rebuild the logical type options from the field's lengths and the recorded option values.
     *
     * @param fieldProperties field properties
     * @param additionalProperties additional properties of the field
     * @return options or null if none are set
     */
    private DataContractLogicalTypeOptions getLogicalTypeOptions(DataFieldProperties fieldProperties,
                                                                 Map<String, String> additionalProperties)
    {
        DataContractLogicalTypeOptions options = new DataContractLogicalTypeOptions();
        boolean                        anySet  = false;

        if (fieldProperties.getMinimumLength() > 0)
        {
            options.setMinLength(fieldProperties.getMinimumLength());
            anySet = true;
        }
        if (fieldProperties.getLength() > 0)
        {
            options.setMaxLength(fieldProperties.getLength());
            anySet = true;
        }

        String value;

        if ((value = getBitolValue(additionalProperties, "format")) != null)          { options.setFormat(value); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "pattern")) != null)         { options.setPattern(value); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "minimum")) != null)         { options.setMinimum(value); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "exclusiveMinimum")) != null){ options.setExclusiveMinimum(value); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "maximum")) != null)         { options.setMaximum(value); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "exclusiveMaximum")) != null){ options.setExclusiveMaximum(value); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "defaultTimezone")) != null) { options.setDefaultTimezone(value); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "timezone")) != null)        { options.setTimezone(Boolean.valueOf(value)); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "uniqueItems")) != null)     { options.setUniqueItems(Boolean.valueOf(value)); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "minItems")) != null)        { options.setMinItems(parseInt(value)); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "maxItems")) != null)        { options.setMaxItems(parseInt(value)); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "minProperties")) != null)   { options.setMinProperties(parseInt(value)); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "maxProperties")) != null)   { options.setMaxProperties(parseInt(value)); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "requiredProperties")) != null) { options.setRequired(List.of(value.split(",\\s*"))); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "dimensions")) != null)      { options.setDimensions(parseInt(value)); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "elementType")) != null)     { options.setElementType(value); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "distanceMetric")) != null)  { options.setDistanceMetric(value); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "normalized")) != null)      { options.setNormalized(Boolean.valueOf(value)); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "embeddingModel")) != null)  { options.setEmbeddingModel(value); anySet = true; }
        if ((value = getBitolValue(additionalProperties, "embeddingModelVersion")) != null) { options.setEmbeddingModelVersion(value); anySet = true; }

        return anySet ? options : null;
    }


    /**
     * Parse an integer recorded as a string.
     *
     * @param value string
     * @return integer or null if not numeric
     */
    private static Integer parseInt(String value)
    {
        try
        {
            return Integer.valueOf(value.trim());
        }
        catch (NumberFormatException notNumeric)
        {
            return null;
        }
    }


    /*
     * ==========================================================================================================
     * Quality
     */

    /**
     * Return the data quality rules governing an element as quality checks.
     *
     * @param element structure or field with its related elements
     * @return list of checks or null
     */
    private List<DataContractQualityRule> getQualityRules(OpenMetadataRootElement element)
    {
        if (element.getGovernedBy() == null)
        {
            return null;
        }

        List<DataContractQualityRule> qualityRules = new ArrayList<>();

        for (RelatedMetadataElementSummary governedBy : element.getGovernedBy())
        {
            if ((governedBy != null) && (governedBy.getRelatedElement() != null) &&
                (governedBy.getRelatedElement().getProperties() instanceof DataQualityRuleProperties rule))
            {
                DataContractQualityRule qualityRule          = new DataContractQualityRule();
                Map<String, String>     additionalProperties = rule.getAdditionalProperties();

                qualityRule.setId(getBitolValue(additionalProperties, "id"));
                qualityRule.setName(rule.getDisplayName());
                qualityRule.setDescription(rule.getDescription());
                qualityRule.setType(rule.getCheckType());
                qualityRule.setDimension(rule.getQualityDimension());
                qualityRule.setMetric(rule.getMetric());
                qualityRule.setSeverity(rule.getSeverity());
                qualityRule.setBusinessImpact(rule.getBusinessImpact());
                qualityRule.setMethod(rule.getMethod());
                qualityRule.setUnit(rule.getUnits());
                qualityRule.setSchedule(rule.getSchedule());
                qualityRule.setScheduler(rule.getScheduler());
                qualityRule.setEngine(rule.getQualityEngine());

                if ("sql".equalsIgnoreCase(rule.getCheckType()))
                {
                    qualityRule.setQuery(rule.getExpression());
                }
                else if (rule.getExpression() != null)
                {
                    qualityRule.setImplementation(rule.getExpression());
                }

                setComparison(qualityRule, rule.getComparisonOperator(), rule.getThresholdValues());

                if (additionalProperties != null)
                {
                    Map<String, Object> arguments = new java.util.LinkedHashMap<>();
                    String              prefix    = BitolMapperBase.ADDITIONAL_PROPERTY_PREFIX + "argument.";

                    for (Map.Entry<String, String> entry : additionalProperties.entrySet())
                    {
                        if (entry.getKey().startsWith(prefix))
                        {
                            arguments.put(entry.getKey().substring(prefix.length()), entry.getValue());
                        }
                    }

                    if (! arguments.isEmpty())
                    {
                        qualityRule.setArguments(arguments);
                    }
                }

                qualityRule.setCustomProperties(getCustomProperties(additionalProperties));
                qualityRules.add(qualityRule);
            }
        }

        return qualityRules.isEmpty() ? null : qualityRules;
    }


    /**
     * Set the comparison operator field of a quality check from the stored operator and thresholds.
     *
     * @param qualityRule check to fill
     * @param operator operator name (may be null)
     * @param thresholds threshold values (may be null)
     */
    static void setComparison(DataContractQualityRule qualityRule,
                              String                  operator,
                              List<String>            thresholds)
    {
        if ((operator == null) || (thresholds == null) || (thresholds.isEmpty()))
        {
            return;
        }

        switch (operator)
        {
            case "mustBe" -> qualityRule.setMustBe(toNumberOrString(thresholds.get(0)));
            case "mustNotBe" -> qualityRule.setMustNotBe(toNumberOrString(thresholds.get(0)));
            case "mustBeGreaterThan" -> qualityRule.setMustBeGreaterThan(toNumber(thresholds.get(0)));
            case "mustBeGreaterOrEqualTo" -> qualityRule.setMustBeGreaterOrEqualTo(toNumber(thresholds.get(0)));
            case "mustBeLessThan" -> qualityRule.setMustBeLessThan(toNumber(thresholds.get(0)));
            case "mustBeLessOrEqualTo" -> qualityRule.setMustBeLessOrEqualTo(toNumber(thresholds.get(0)));
            case "mustBeBetween" -> qualityRule.setMustBeBetween(toNumbers(thresholds));
            case "mustNotBeBetween" -> qualityRule.setMustNotBeBetween(toNumbers(thresholds));
            default -> { /* unknown operator - leave unset */ }
        }
    }


    /**
     * Convert a stored threshold to a number.
     *
     * @param value string
     * @return number (integer where possible) or null
     */
    static Number toNumber(String value)
    {
        try
        {
            return Long.valueOf(value.trim());
        }
        catch (NumberFormatException notLong)
        {
            try
            {
                return Double.valueOf(value.trim());
            }
            catch (NumberFormatException notDouble)
            {
                return null;
            }
        }
    }


    /**
     * Convert a stored value to a number where possible, otherwise keep it as a string.
     *
     * @param value string
     * @return number or string
     */
    static Object toNumberOrString(String value)
    {
        Number number = toNumber(value);

        return (number != null) ? number : value;
    }


    /**
     * Convert stored thresholds to numbers.
     *
     * @param values strings
     * @return numbers
     */
    private static List<Number> toNumbers(List<String> values)
    {
        List<Number> numbers = new ArrayList<>();

        for (String value : values)
        {
            Number number = toNumber(value);

            if (number != null)
            {
                numbers.add(number);
            }
        }

        return numbers;
    }


    /*
     * ==========================================================================================================
     * Governance: SLA and access roles
     */

    /**
     * Fill the SLA properties and the access roles from the governance definitions that govern the agreement.
     *
     * @param agreement agreement element
     * @param dataContract document to fill
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void fillGovernance(OpenMetadataRootElement agreement,
                                DataContract            dataContract) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        if (agreement.getGovernedBy() == null)
        {
            return;
        }

        for (RelatedMetadataElementSummary governedBy : agreement.getGovernedBy())
        {
            if ((governedBy != null) && (governedBy.getRelatedElement() != null))
            {
                if (governedBy.getRelatedElement().getProperties() instanceof ServiceLevelObjectiveProperties slo)
                {
                    fillServiceLevels(slo, dataContract);
                }
                else if (propertyHelper.isTypeOf(governedBy.getRelatedElement().getElementHeader(), OpenMetadataType.SECURITY_ACCESS_CONTROL.typeName))
                {
                    fillAccessRoles(governedBy.getRelatedElement().getElementHeader().getGUID(), dataContract);
                }
            }
        }
    }


    /**
     * Fill the SLA properties from a service level objective.  The cataloguer keeps the original SLA properties as
     * JSON so they come back exactly; an objective created in Egeria contributes its obligations as SLA properties.
     *
     * @param slo service level objective properties
     * @param dataContract document to fill
     */
    private void fillServiceLevels(ServiceLevelObjectiveProperties slo,
                                   DataContract                    dataContract)
    {
        String json = getBitolValue(slo.getAdditionalProperties(), "slaProperties");

        if (json != null)
        {
            try
            {
                dataContract.setSlaProperties(BitolDocumentFormatter.fromJSONFragmentList(json, DataContractSLAProperty.class));
            }
            catch (IOException error)
            {
                json = null;
            }
        }

        if ((json == null) && (slo.getObligations() != null))
        {
            List<DataContractSLAProperty> slaProperties = new ArrayList<>();

            for (Map.Entry<String, String> obligation : slo.getObligations().entrySet())
            {
                DataContractSLAProperty slaProperty = new DataContractSLAProperty();

                slaProperty.setProperty(obligation.getKey());
                slaProperty.setValue(obligation.getValue());
                slaProperties.add(slaProperty);
            }

            dataContract.setSlaProperties(slaProperties.isEmpty() ? null : slaProperties);
        }

        if (dataContract.getDescription() != null)
        {
            if ((dataContract.getDescription().getLimitations() == null) && (slo.getRestrictions() != null))
            {
                dataContract.getDescription().setLimitations(slo.getRestrictions().get("limitations"));
            }
            if ((dataContract.getDescription().getUsage() == null) && (slo.getEntitlements() != null))
            {
                dataContract.getDescription().setUsage(slo.getEntitlements().get("usage"));
            }
        }
    }


    /**
     * Fill the access roles from the security roles associated with the agreement's access control.  Roles recorded
     * against a server are attached to that server; the rest are contract-level roles.
     *
     * @param accessControlGUID access control
     * @param dataContract document to fill
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void fillAccessRoles(String       accessControlGUID,
                                 DataContract dataContract) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        OpenMetadataRootElement accessControl = context.getGovernanceDefinitionClient().getGovernanceDefinitionByGUID(accessControlGUID,
                                                                                                                        context.getGovernanceDefinitionClient().getGetOptions());

        if ((accessControl == null) || (accessControl.getAssociatedSecurityLists() == null))
        {
            return;
        }

        List<DataContractRole> contractRoles = new ArrayList<>();

        for (RelatedMetadataElementSummary association : accessControl.getAssociatedSecurityLists())
        {
            if ((association != null) && (association.getRelatedElement() != null) &&
                (association.getRelatedElement().getProperties() instanceof SecurityListProperties securityRole))
            {
                DataContractRole    role                 = new DataContractRole();
                Map<String, String> additionalProperties = securityRole.getAdditionalProperties();

                role.setId(getBitolValue(additionalProperties, "id"));
                role.setRole((securityRole.getDisplayName() != null) ? securityRole.getDisplayName() : securityRole.getIdentifier());
                role.setDescription(securityRole.getDescription());
                role.setFirstLevelApprovers(getBitolValue(additionalProperties, "firstLevelApprovers"));
                role.setSecondLevelApprovers(getBitolValue(additionalProperties, "secondLevelApprovers"));
                role.setCustomProperties(getCustomProperties(additionalProperties));

                if (association.getRelationshipProperties() instanceof AssociatedSecurityListProperties associationProperties)
                {
                    role.setAccess(associationProperties.getOperationName());
                }
                if (role.getAccess() == null)
                {
                    role.setAccess(getBitolValue(additionalProperties, "access"));
                }

                String serverName = getBitolValue(additionalProperties, "server");

                if (serverName != null)
                {
                    DataContractServer server = findServer(dataContract, serverName);

                    if (server != null)
                    {
                        if (server.getRoles() == null)
                        {
                            server.setRoles(new ArrayList<>());
                        }

                        server.getRoles().add(role);
                        continue;
                    }
                }

                contractRoles.add(role);
            }
        }

        dataContract.setRoles(contractRoles.isEmpty() ? null : contractRoles);
    }


    /**
     * Find a server in the document by name.
     *
     * @param dataContract document
     * @param serverName name
     * @return server or null
     */
    private DataContractServer findServer(DataContract dataContract,
                                          String       serverName)
    {
        if (dataContract.getServers() != null)
        {
            for (DataContractServer server : dataContract.getServers())
            {
                if (serverName.equals(server.getServer()))
                {
                    return server;
                }
            }
        }

        return null;
    }


    /*
     * ==========================================================================================================
     * Servers
     */

    /**
     * Return the endpoints listed as resources of the agreement as servers.
     *
     * @param agreement agreement element
     * @return list of servers or null
     */
    private List<DataContractServer> getServers(OpenMetadataRootElement agreement)
    {
        if (agreement.getResourceList() == null)
        {
            return null;
        }

        List<DataContractServer> servers = new ArrayList<>();

        for (RelatedMetadataElementSummary resource : agreement.getResourceList())
        {
            if ((resource != null) && (resource.getRelatedElement() != null) &&
                (resource.getRelatedElement().getProperties() instanceof EndpointProperties endpoint))
            {
                DataContractServer  server               = new DataContractServer();
                Map<String, String> additionalProperties = endpoint.getAdditionalProperties();

                server.setId(getBitolValue(additionalProperties, "id"));
                server.setServer(endpoint.getDisplayName());
                server.setDescription(endpoint.getDescription());
                server.setType((getBitolValue(additionalProperties, "type") != null) ? getBitolValue(additionalProperties, "type") : endpoint.getProtocol());
                server.setEnvironment(getBitolValue(additionalProperties, "environment"));
                server.setAccount(getBitolValue(additionalProperties, "account"));
                server.setCatalog(getBitolValue(additionalProperties, "catalog"));
                server.setDatabase(getBitolValue(additionalProperties, "database"));
                server.setDataset(getBitolValue(additionalProperties, "dataset"));
                server.setSchema(getBitolValue(additionalProperties, "schema"));
                server.setHost(getBitolValue(additionalProperties, "host"));
                server.setLocation(getBitolValue(additionalProperties, "location"));
                server.setEndpointUrl(getBitolValue(additionalProperties, "endpointUrl"));
                server.setPath(getBitolValue(additionalProperties, "path"));
                server.setFormat(getBitolValue(additionalProperties, "format"));
                server.setDelimiter(getBitolValue(additionalProperties, "delimiter"));
                server.setProject(getBitolValue(additionalProperties, "project"));
                server.setRegion(getBitolValue(additionalProperties, "region"));
                server.setRegionName(getBitolValue(additionalProperties, "regionName"));
                server.setServiceName(getBitolValue(additionalProperties, "serviceName"));
                server.setStagingDir(getBitolValue(additionalProperties, "stagingDir"));
                server.setWarehouse(getBitolValue(additionalProperties, "warehouse"));
                server.setStream(getBitolValue(additionalProperties, "stream"));
                server.setEncoding(getBitolValue(additionalProperties, "encoding"));
                server.setWorkgroup(getBitolValue(additionalProperties, "workgroup"));
                server.setCatalogUrl(getBitolValue(additionalProperties, "catalogUrl"));
                server.setNamespace(getBitolValue(additionalProperties, "namespace"));

                String port = getBitolValue(additionalProperties, "port");

                if (port != null)
                {
                    /*
                     * A port is normally a number, but may be a variable reference such as ${DB_PORT}.
                     */
                    Integer portNumber = parseInt(port);

                    server.setPort((portNumber != null) ? portNumber : port);
                }

                if ((server.getHost() == null) && (server.getLocation() == null) && (server.getEndpointUrl() == null) && (server.getPath() == null))
                {
                    /*
                     * An endpoint created in Egeria: use its network address as the location.
                     */
                    server.setLocation(endpoint.getNetworkAddress());
                }

                if (server.getType() == null)
                {
                    server.setType("custom");
                }

                server.setCustomProperties(getCustomProperties(additionalProperties));

                if (server.getServer() != null)
                {
                    servers.add(server);
                }
            }
        }

        return servers.isEmpty() ? null : servers;
    }

    /**
     * Order related data fields by the position recorded on their MemberDataField/NestedDataField relationship, so the
     * generated schema lists the properties in the order the contract declared them.  Fields without a position keep
     * their retrieval order after the positioned ones.
     *
     * @param related related fields (may be null)
     * @return ordered copy
     */
    static List<RelatedMetadataElementSummary> sortByPosition(List<RelatedMetadataElementSummary> related)
    {
        List<RelatedMetadataElementSummary> ordered = new ArrayList<>();

        if (related != null)
        {
            ordered.addAll(related);
            ordered.sort(Comparator.comparingInt(DataContractGenerator::getPosition));
        }

        return ordered;
    }


    /**
     * Return the position recorded on a field's relationship.
     *
     * @param related related field
     * @return position, or Integer.MAX_VALUE when none is recorded
     */
    private static int getPosition(RelatedMetadataElementSummary related)
    {
        if ((related != null) && (related.getRelationshipProperties() instanceof PartOfRelationshipProperties partOf) && (partOf.getPosition() > 0))
        {
            return partOf.getPosition();
        }

        return Integer.MAX_VALUE;
    }
}
