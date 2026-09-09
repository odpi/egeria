/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractLogicalTypeOptions;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractQualityRule;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractRelationship;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractRole;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSLAProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaObject;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractServer;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.GovernanceDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.LinkedDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.NestedDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementItemProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DataSharingAgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ConfidentialityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CriticalityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.DataQualityRuleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ServiceLevelObjectiveProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.PrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.AssociatedSecurityListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityAccessControlProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ConfidentialityLevel;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CriticalityLevel;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * DataContractMapper catalogues an Open Data Contract Standard (ODCS) document as an Agreement in open metadata.
 * <ul>
 *     <li>The document becomes an Agreement classified as a DataSharingAgreement (one element per version, qualifiedName
 *         DataContract::{id}::{version}) whose identifier is also recorded as an ExternalId shared by all versions.</li>
 *     <li>Each schema object becomes a DataStructure that is an item of the agreement; each property becomes a DataField
 *         member of the structure, with nested properties and array items as nested data fields.  Primary key,
 *         classification and critical data element markers become the PrimaryKey, Confidentiality and Criticality
 *         classifications; relationships between properties become LinkedDataField relationships.</li>
 *     <li>Each data quality check becomes a DataQualityRule governance control that governs the structure or field.</li>
 *     <li>The SLA properties become a ServiceLevelObjective that governs the agreement; the description's limitations
 *         and usage become its restrictions and entitlements.</li>
 *     <li>Servers become Endpoints listed as resources of the agreement.  A SecurityAccessControl governs the agreement
 *         and each access role becomes a SecurityRole associated with it, with the access type as the operation name.</li>
 *     <li>The tenant, tags, team, support channels and authoritative definitions are mapped as for data products
 *         (see BitolMapperBase).</li>
 * </ul>
 * Everything that has no home in the open metadata types is kept in additional properties with the prefix "bitol.".
 */
public class DataContractMapper extends BitolMapperBase
{
    private static final String SCHEMA_SEGMENT   = "Schema";
    private static final String ITEMS_SEGMENT    = "items";
    private static final String QUALITY_SEGMENT  = "Quality";
    private static final String SLA_SEGMENT      = "SLA";
    private static final String SERVER_SEGMENT   = "Server";
    private static final String SECURITY_ROLE_SEGMENT  = "SecurityRole";
    private static final String ACCESS_CONTROL_SEGMENT = "AccessControl";

    /**
     * Additional property on the service level objective holding the SLA properties as JSON.
     */
    public static final String SLA_PROPERTIES_JSON = ADDITIONAL_PROPERTY_PREFIX + "slaProperties";


    /**
     * Constructor.
     *
     * @param context connector context providing access to the open metadata clients
     */
    public DataContractMapper(ConnectorContextBase context)
    {
        super(context);
    }


    /**
     * Catalogue a data contract.  Republishing the same version updates the existing elements; a new version creates
     * new elements linked to the same ExternalId.
     *
     * @param dataContract document to catalogue
     * @param source description of where the document came from (recorded on the ExternalId link)
     * @return result describing what was done and anything that could not be fully represented
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    public BitolMappingResult catalogueDataContract(DataContract dataContract,
                                                    String       source) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        if ((dataContract == null) || (dataContract.getId() == null))
        {
            BitolMappingResult skipped = new BitolMappingResult(BitolDocument.DATA_CONTRACT_KIND, null, null, null);

            skipped.addWarning("The data contract has no identifier so it can not be catalogued");

            return skipped;
        }

        String             qualifiedName   = getDocumentQualifiedName(BitolDocument.DATA_CONTRACT_KIND, dataContract.getId(), dataContract.getVersion());
        BitolMappingResult result          = new BitolMappingResult(BitolDocument.DATA_CONTRACT_KIND, dataContract.getId(), dataContract.getVersion(), qualifiedName);
        CollectionClient   agreementClient = context.getCollectionClient(OpenMetadataType.AGREEMENT.typeName);
        String             agreementGUID   = findElementGUID(qualifiedName);

        if (isRetired(dataContract))
        {
            if (agreementGUID != null)
            {
                agreementClient.deleteCollection(agreementGUID, agreementClient.getDeleteOptions(false));

                result.setElementGUID(agreementGUID);
                result.setAction(BitolMappingResult.Action.DELETED);
            }
            else
            {
                result.addWarning("The data contract is retired and was never catalogued, so there is nothing to remove");
            }

            return result;
        }

        /*
         * The agreement itself.
         */
        AgreementProperties properties = getAgreementProperties(dataContract, qualifiedName);

        if (agreementGUID == null)
        {
            agreementGUID = agreementClient.createCollection(new NewElementOptions(agreementClient.getMetadataSourceOptions()),
                                                             null,
                                                             properties,
                                                             null);

            agreementClient.setAgreementAsDataSharingAgreement(agreementGUID, new DataSharingAgreementProperties(), agreementClient.getMetadataSourceOptions());

            result.setAction(BitolMappingResult.Action.CREATED);
        }
        else
        {
            agreementClient.updateCollection(agreementGUID, agreementClient.getUpdateOptions(false), properties);
            result.setAction(BitolMappingResult.Action.UPDATED);
        }

        result.setElementGUID(agreementGUID);

        /*
         * The fundamentals that become separate elements.
         */
        linkExternalId(BitolDocument.DATA_CONTRACT_KIND, dataContract.getId(), agreementGUID, source);
        addSearchKeywords(dataContract.getTags(), agreementGUID);
        linkTenantOrganization(dataContract.getTenant(), agreementGUID);
        linkAuthoritativeDefinitions(dataContract.getAuthoritativeDefinitions(), agreementGUID);

        if (dataContract.getDescription() != null)
        {
            linkAuthoritativeDefinitions(dataContract.getDescription().getAuthoritativeDefinitions(), agreementGUID);
        }

        /*
         * The sections.
         */
        Map<String, String> fieldGUIDsByReference = new HashMap<>();

        linkSchema(dataContract, agreementGUID, qualifiedName, fieldGUIDsByReference, result);
        linkServiceLevelObjective(dataContract, agreementGUID, qualifiedName);
        linkServers(dataContract.getServers(), agreementGUID, qualifiedName);
        linkSecurityRoles(dataContract, agreementGUID, qualifiedName);
        linkSupportChannels(dataContract.getSupport(), agreementGUID, qualifiedName);
        linkTeam(dataContract.getTeam(), agreementGUID, qualifiedName, result);

        return result;
    }


    /**
     * Build the properties of the Agreement from the document's fundamentals.
     *
     * @param dataContract document
     * @param qualifiedName qualified name for this version
     * @return properties
     */
    private AgreementProperties getAgreementProperties(DataContract dataContract,
                                                       String       qualifiedName)
    {
        AgreementProperties properties = new AgreementProperties();

        fillFundamentals(dataContract, properties, qualifiedName);

        if (dataContract.getDescription() != null)
        {
            properties.setPurpose(dataContract.getDescription().getPurpose());
        }

        Map<String, String> additionalProperties = properties.getAdditionalProperties();

        if (dataContract.getDataProduct() != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "dataProduct", dataContract.getDataProduct());
        }
        if (dataContract.getContractCreatedTs() != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "contractCreatedTs", dataContract.getContractCreatedTs());
        }
        if (dataContract.getPrice() != null)
        {
            if (dataContract.getPrice().getPriceAmount() != null)
            {
                additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "priceAmount", dataContract.getPrice().getPriceAmount().toString());
            }
            if (dataContract.getPrice().getPriceCurrency() != null)
            {
                additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "priceCurrency", dataContract.getPrice().getPriceCurrency());
            }
            if (dataContract.getPrice().getPriceUnit() != null)
            {
                additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "priceUnit", dataContract.getPrice().getPriceUnit());
            }
        }

        return properties;
    }


    /*
     * ==========================================================================================================
     * Schema
     */

    /**
     * Represent the schema objects as DataStructures (items of the agreement) and their properties as DataFields.
     *
     * @param dataContract document
     * @param agreementGUID agreement
     * @param contractQualifiedName agreement qualified name
     * @param fieldGUIDsByReference map that is filled with the GUID of each field, keyed by its shorthand reference (object.property)
     * @param result result to record warnings in
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkSchema(DataContract        dataContract,
                            String              agreementGUID,
                            String              contractQualifiedName,
                            Map<String, String> fieldGUIDsByReference,
                            BitolMappingResult  result) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        if (dataContract.getSchema() == null)
        {
            return;
        }

        Map<String, String> structureGUIDsByName = new HashMap<>();

        /*
         * First pass: the structures and their fields.
         */
        for (DataContractSchemaObject schemaObject : dataContract.getSchema())
        {
            if ((schemaObject != null) && (schemaObject.getName() != null))
            {
                String structureQualifiedName = contractQualifiedName + SEPARATOR + SCHEMA_SEGMENT + SEPARATOR + schemaObject.getName();
                String structureGUID          = findElementGUID(structureQualifiedName);

                DataStructureProperties properties = new DataStructureProperties();

                properties.setQualifiedName(structureQualifiedName);
                properties.setDisplayName((schemaObject.getBusinessName() != null) ? schemaObject.getBusinessName() : schemaObject.getName());
                properties.setDescription(schemaObject.getDescription());
                properties.setNamespacePath(schemaObject.getPhysicalName());
                properties.setNamePatterns(getNamePatterns(schemaObject.getName(), schemaObject.getPhysicalName()));
                properties.setVersionIdentifier(dataContract.getVersion());

                Map<String, String> additionalProperties = new HashMap<>();

                putIfPresent(additionalProperties, "id", schemaObject.getId());
                putIfPresent(additionalProperties, "physicalType", schemaObject.getPhysicalType());
                putIfPresent(additionalProperties, "logicalType", schemaObject.getLogicalType());
                putIfPresent(additionalProperties, "dataGranularityDescription", schemaObject.getDataGranularityDescription());
                addCustomProperties(schemaObject.getCustomProperties(), additionalProperties);
                properties.setAdditionalProperties(additionalProperties);

                if (structureGUID == null)
                {
                    NewElementOptions newElementOptions = new NewElementOptions(context.getDataStructureClient().getMetadataSourceOptions());

                    newElementOptions.setAnchorGUID(agreementGUID);
                    newElementOptions.setIsOwnAnchor(false);
                    newElementOptions.setParentGUID(agreementGUID);
                    newElementOptions.setParentRelationshipTypeName(OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName);
                    newElementOptions.setParentAtEnd1(true);

                    AgreementItemProperties itemProperties = new AgreementItemProperties();

                    itemProperties.setAgreementItemId(schemaObject.getName());

                    structureGUID = context.getDataStructureClient().createDataStructure(newElementOptions, null, properties, itemProperties);
                }
                else
                {
                    context.getDataStructureClient().updateDataStructure(structureGUID, context.getDataStructureClient().getUpdateOptions(false), properties);
                }

                structureGUIDsByName.put(schemaObject.getName(), structureGUID);

                addSearchKeywords(schemaObject.getTags(), structureGUID);
                linkAuthoritativeDefinitions(schemaObject.getAuthoritativeDefinitions(), structureGUID);
                linkQualityRules(schemaObject.getQuality(), structureGUID, structureQualifiedName, agreementGUID, result);

                if (schemaObject.getProperties() != null)
                {
                    int position = 0;

                    for (DataContractSchemaProperty property : schemaObject.getProperties())
                    {
                        if ((property != null) && (property.getName() != null))
                        {
                            position++;

                            String fieldGUID = linkDataField(property,
                                                             agreementGUID,
                                                             structureGUID,
                                                             OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                                             structureQualifiedName + SEPARATOR + property.getName(),
                                                             dataContract.getVersion(),
                                                             position,
                                                             result);

                            fieldGUIDsByReference.put(schemaObject.getName() + "." + property.getName(), fieldGUID);
                        }
                    }
                }
            }
        }

        /*
         * Second pass: the relationships, once every field exists.
         */
        for (DataContractSchemaObject schemaObject : dataContract.getSchema())
        {
            if ((schemaObject != null) && (schemaObject.getName() != null))
            {
                if (schemaObject.getRelationships() != null)
                {
                    for (DataContractRelationship relationship : schemaObject.getRelationships())
                    {
                        linkRelationship(relationship, null, fieldGUIDsByReference, result);
                    }
                }

                if (schemaObject.getProperties() != null)
                {
                    for (DataContractSchemaProperty property : schemaObject.getProperties())
                    {
                        if ((property != null) && (property.getName() != null) && (property.getRelationships() != null))
                        {
                            for (DataContractRelationship relationship : property.getRelationships())
                            {
                                linkRelationship(relationship, schemaObject.getName() + "." + property.getName(), fieldGUIDsByReference, result);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Create or update a data field, its classifications, quality rules and nested fields.
     *
     * @param property property from the document
     * @param agreementGUID anchor
     * @param parentGUID structure or parent field
     * @param parentRelationshipTypeName MemberDataField for a structure, NestedDataField for a parent field
     * @param qualifiedName qualified name for the field
     * @param version version of the contract
     * @param position 1-based position of the property within its parent, recorded on the relationship so the order of
     *                 the schema can be regenerated
     * @param result result to record warnings in
     * @return unique identifier of the field
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private String linkDataField(DataContractSchemaProperty property,
                                 String                     agreementGUID,
                                 String                     parentGUID,
                                 String                     parentRelationshipTypeName,
                                 String                     qualifiedName,
                                 String                     version,
                                 int                        position,
                                 BitolMappingResult         result) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        String              fieldGUID  = findElementGUID(qualifiedName);
        DataFieldProperties properties = getDataFieldProperties(property, qualifiedName, version);

        Map<String, ClassificationProperties> classifications = getFieldClassifications(property);

        if (fieldGUID == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(context.getDataFieldClient().getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(agreementGUID);
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(parentGUID);
            newElementOptions.setParentRelationshipTypeName(parentRelationshipTypeName);
            newElementOptions.setParentAtEnd1(true);

            if (OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName.equals(parentRelationshipTypeName))
            {
                MemberDataFieldProperties memberProperties = new MemberDataFieldProperties();

                memberProperties.setPosition(position);

                fieldGUID = context.getDataFieldClient().createDataField(newElementOptions, classifications, properties, memberProperties);
            }
            else
            {
                NestedDataFieldProperties nestedProperties = new NestedDataFieldProperties();

                nestedProperties.setPosition(position);

                fieldGUID = context.getDataFieldClient().createDataField(newElementOptions, classifications, properties, nestedProperties);
            }
        }
        else
        {
            context.getDataFieldClient().updateDataField(fieldGUID, context.getDataFieldClient().getUpdateOptions(false), properties);

            /*
             * Refresh the classifications.
             */
            for (ClassificationProperties classification : classifications.values())
            {
                if (classification instanceof PrimaryKeyProperties primaryKeyProperties)
                {
                    context.getSchemaAttributeClient().addPrimaryKeyClassification(fieldGUID,
                                                                                   primaryKeyProperties,
                                                                                   context.getSchemaAttributeClient().getMetadataSourceOptions());
                }
                else if (classification instanceof ConfidentialityProperties confidentialityProperties)
                {
                    context.getClassificationExplorerClient().setConfidentialityClassification(fieldGUID,
                                                                                               confidentialityProperties,
                                                                                               context.getClassificationExplorerClient().getMetadataSourceOptions());
                }
                else if (classification instanceof CriticalityProperties criticalityProperties)
                {
                    context.getClassificationExplorerClient().setCriticalityClassification(fieldGUID,
                                                                                           criticalityProperties,
                                                                                           context.getClassificationExplorerClient().getMetadataSourceOptions());
                }
            }
        }

        addSearchKeywords(property.getTags(), fieldGUID);
        linkAuthoritativeDefinitions(property.getAuthoritativeDefinitions(), fieldGUID);
        linkQualityRules(property.getQuality(), fieldGUID, qualifiedName, agreementGUID, result);

        /*
         * Nested structure and array items.
         */
        if (property.getProperties() != null)
        {
            int nestedPosition = 0;

            for (DataContractSchemaProperty nestedProperty : property.getProperties())
            {
                if ((nestedProperty != null) && (nestedProperty.getName() != null))
                {
                    nestedPosition++;

                    linkDataField(nestedProperty,
                                  agreementGUID,
                                  fieldGUID,
                                  OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                  qualifiedName + SEPARATOR + nestedProperty.getName(),
                                  version,
                                  nestedPosition,
                                  result);
                }
            }
        }

        if (property.getItems() != null)
        {
            DataContractSchemaProperty items = property.getItems();

            if (items.getName() == null)
            {
                items.setName(property.getName() + "[" + ITEMS_SEGMENT + "]");
            }

            linkDataField(items,
                          agreementGUID,
                          fieldGUID,
                          OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                          qualifiedName + SEPARATOR + ITEMS_SEGMENT,
                          version,
                          1,
                          result);
        }

        return fieldGUID;
    }


    /**
     * Build the properties of a DataField from a schema property.
     *
     * @param property property from the document
     * @param qualifiedName qualified name for the field
     * @param version version of the contract
     * @return properties
     */
    DataFieldProperties getDataFieldProperties(DataContractSchemaProperty property,
                                               String                     qualifiedName,
                                               String                     version)
    {
        DataFieldProperties properties = new DataFieldProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName((property.getBusinessName() != null) ? property.getBusinessName() : property.getName());
        properties.setDescription(property.getDescription());
        properties.setVersionIdentifier(version);
        properties.setNamePatterns(getNamePatterns(property.getName(), property.getPhysicalName()));
        properties.setDataType((property.getLogicalType() != null) ? property.getLogicalType() : property.getPhysicalType());
        properties.setIsNullable(! Boolean.TRUE.equals(property.getRequired()));
        properties.setAllowsDuplicateValues(! Boolean.TRUE.equals(property.getUnique()));
        properties.setIsPartitionKey(Boolean.TRUE.equals(property.getPartitioned()));

        if ((property.getPartitionKeyPosition() != null) && (property.getPartitionKeyPosition() > 0))
        {
            properties.setPartitionKeyPosition(property.getPartitionKeyPosition());
        }

        Map<String, String> additionalProperties = new HashMap<>();

        putIfPresent(additionalProperties, "id", property.getId());
        putIfPresent(additionalProperties, "physicalName", property.getPhysicalName());
        putIfPresent(additionalProperties, "physicalType", property.getPhysicalType());
        putIfPresent(additionalProperties, "logicalType", property.getLogicalType());
        putIfPresent(additionalProperties, "classification", property.getClassification());
        putIfPresent(additionalProperties, "encryptedName", property.getEncryptedName());
        putIfPresent(additionalProperties, "transformLogic", property.getTransformLogic());
        putIfPresent(additionalProperties, "transformDescription", property.getTransformDescription());

        if ((property.getPrimaryKeyPosition() != null) && (property.getPrimaryKeyPosition() > 0))
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "primaryKeyPosition", property.getPrimaryKeyPosition().toString());
        }
        if (property.getTransformSourceObjects() != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "transformSourceObjects", String.join(", ", property.getTransformSourceObjects()));
        }
        if (property.getExamples() != null)
        {
            List<String> examples = new ArrayList<>();

            for (Object example : property.getExamples())
            {
                examples.add(String.valueOf(example));
            }

            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "examples", String.join(", ", examples));
        }
        if (Boolean.TRUE.equals(property.getCriticalDataElement()))
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "criticalDataElement", "true");
        }

        DataContractLogicalTypeOptions options = property.getLogicalTypeOptions();

        if (options != null)
        {
            if (options.getMinLength() != null)
            {
                properties.setMinimumLength(options.getMinLength());
            }
            if (options.getMaxLength() != null)
            {
                properties.setLength(options.getMaxLength());
            }

            putIfPresent(additionalProperties, "format", options.getFormat());
            putIfPresent(additionalProperties, "pattern", options.getPattern());
            putIfPresent(additionalProperties, "minimum", toStringValue(options.getMinimum()));
            putIfPresent(additionalProperties, "exclusiveMinimum", toStringValue(options.getExclusiveMinimum()));
            putIfPresent(additionalProperties, "maximum", toStringValue(options.getMaximum()));
            putIfPresent(additionalProperties, "exclusiveMaximum", toStringValue(options.getExclusiveMaximum()));
            putIfPresent(additionalProperties, "multipleOf", toStringValue(options.getMultipleOf()));
            putIfPresent(additionalProperties, "timezone", toStringValue(options.getTimezone()));
            putIfPresent(additionalProperties, "defaultTimezone", options.getDefaultTimezone());
            putIfPresent(additionalProperties, "minProperties", toStringValue(options.getMinProperties()));
            putIfPresent(additionalProperties, "maxProperties", toStringValue(options.getMaxProperties()));
            putIfPresent(additionalProperties, "minItems", toStringValue(options.getMinItems()));
            putIfPresent(additionalProperties, "maxItems", toStringValue(options.getMaxItems()));
            putIfPresent(additionalProperties, "uniqueItems", toStringValue(options.getUniqueItems()));

            if (options.getRequired() != null)
            {
                additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "requiredProperties", String.join(", ", options.getRequired()));
            }
            if (options.getAdditionalProperties() != null)
            {
                for (Map.Entry<String, Object> entry : options.getAdditionalProperties().entrySet())
                {
                    additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }

        addCustomProperties(property.getCustomProperties(), additionalProperties);
        properties.setAdditionalProperties(additionalProperties);

        return properties;
    }


    /**
     * Build the classifications for a data field: PrimaryKey, Confidentiality (from the classification value) and
     * Criticality (for a critical data element).
     *
     * @param property property from the document
     * @return map of classification type name to properties (empty if none)
     */
    Map<String, ClassificationProperties> getFieldClassifications(DataContractSchemaProperty property)
    {
        Map<String, ClassificationProperties> classifications = new LinkedHashMap<>();

        if (Boolean.TRUE.equals(property.getPrimaryKey()))
        {
            PrimaryKeyProperties primaryKey = new PrimaryKeyProperties();

            primaryKey.setDisplayName(property.getName());

            classifications.put(OpenMetadataType.PRIMARY_KEY_CLASSIFICATION.typeName, primaryKey);
        }

        ConfidentialityLevel confidentialityLevel = getConfidentialityLevel(property.getClassification());

        if (confidentialityLevel != null)
        {
            ConfidentialityProperties confidentiality = new ConfidentialityProperties();

            confidentiality.setConfidentialityLevel(confidentialityLevel.getOrdinal());
            confidentiality.setSource(BitolDocument.DATA_CONTRACT_KIND);
            confidentiality.setNotes(property.getClassification());

            classifications.put(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName, confidentiality);
        }

        if (Boolean.TRUE.equals(property.getCriticalDataElement()))
        {
            CriticalityProperties criticality = new CriticalityProperties();

            criticality.setCriticalityLevel(CriticalityLevel.CRITICAL.getOrdinal());
            criticality.setSource(BitolDocument.DATA_CONTRACT_KIND);
            criticality.setNotes("Critical data element in the data contract");

            classifications.put(OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName, criticality);
        }

        return classifications;
    }


    /**
     * Map a free-text classification from a data contract onto a confidentiality level.
     *
     * @param classification value from the document (may be null)
     * @return level or null if the value is not recognized
     */
    static ConfidentialityLevel getConfidentialityLevel(String classification)
    {
        if (classification == null)
        {
            return null;
        }

        return switch (classification.trim().toLowerCase(Locale.ROOT))
        {
            case "public", "unclassified", "open" -> ConfidentialityLevel.UNCLASSIFIED;
            case "internal", "internal use only" -> ConfidentialityLevel.INTERNAL;
            case "confidential", "private" -> ConfidentialityLevel.CONFIDENTIAL;
            case "sensitive", "pii", "personal" -> ConfidentialityLevel.SENSITIVE;
            case "restricted", "secret", "highly confidential" -> ConfidentialityLevel.RESTRICTED;
            default -> null;
        };
    }


    /**
     * Link two fields named in a relationship.  Property level relationships have an implicit "from"; schema level
     * relationships list the from and to references pairwise (composite keys).
     *
     * @param relationship relationship from the document
     * @param implicitFrom reference of the property that owns the relationship (null at the schema level)
     * @param fieldGUIDsByReference GUIDs of the fields in this contract keyed by object.property
     * @param result result to record warnings in
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkRelationship(DataContractRelationship relationship,
                                  String                   implicitFrom,
                                  Map<String, String>      fieldGUIDsByReference,
                                  BitolMappingResult       result) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        if ((relationship == null) || (relationship.getTo() == null))
        {
            return;
        }

        List<String> fromReferences = relationship.getFrom();

        if ((fromReferences == null) || (fromReferences.isEmpty()))
        {
            if (implicitFrom == null)
            {
                result.addWarning("A schema level relationship has no from reference and was not linked");
                return;
            }

            fromReferences = List.of(implicitFrom);
        }

        int pairs = Math.min(fromReferences.size(), relationship.getTo().size());

        for (int i = 0; i < pairs; i++)
        {
            String fromGUID = resolveFieldReference(fromReferences.get(i), fieldGUIDsByReference);
            String toGUID   = resolveFieldReference(relationship.getTo().get(i), fieldGUIDsByReference);

            if ((fromGUID != null) && (toGUID != null))
            {
                LinkedDataFieldProperties linkProperties = new LinkedDataFieldProperties();

                linkProperties.setDisplayName((relationship.getType() != null) ? relationship.getType() : "foreignKey");
                linkProperties.setRelationshipTypeName(relationship.getType());

                Map<String, String> additionalProperties = new HashMap<>();
                addCustomProperties(relationship.getCustomProperties(), additionalProperties);

                if (! additionalProperties.isEmpty())
                {
                    linkProperties.setAdditionalProperties(additionalProperties);
                }

                context.getDataFieldClient().linkLinkedDataField(fromGUID,
                                                                 toGUID,
                                                                 context.getDataFieldClient().getMakeAnchorOptions(false),
                                                                 linkProperties);
            }
            else
            {
                result.addWarning("Relationship from " + fromReferences.get(i) + " to " + relationship.getTo().get(i) + " could not be linked because one of the fields is not catalogued");
            }
        }
    }


    /**
     * Resolve a property reference to the GUID of a data field.  A shorthand reference (object.property) names a
     * field in this contract; a fully qualified reference (contractId.object.property) names a field in the latest
     * catalogued version of another contract.
     *
     * @param reference reference from the document
     * @param fieldGUIDsByReference GUIDs of the fields in this contract keyed by object.property
     * @return guid or null if the field is not known
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private String resolveFieldReference(String              reference,
                                         Map<String, String> fieldGUIDsByReference) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        if (reference == null)
        {
            return null;
        }

        String localGUID = fieldGUIDsByReference.get(reference);

        if (localGUID != null)
        {
            return localGUID;
        }

        String[] parts = reference.split("\\.");

        if (parts.length >= 3)
        {
            /*
             * contractId.object.property - the contract id may itself contain no dots (UUIDs do not).
             */
            String contractId = parts[0];
            String object     = parts[parts.length - 2];
            String property   = parts[parts.length - 1];

            OpenMetadataRootElement agreement = getLatestVersion(findElementsByDocumentId(BitolDocument.DATA_CONTRACT_KIND, contractId));

            if ((agreement != null) && (agreement.getProperties() instanceof AgreementProperties agreementProperties))
            {
                return findElementGUID(agreementProperties.getQualifiedName() + SEPARATOR + SCHEMA_SEGMENT + SEPARATOR + object + SEPARATOR + property);
            }
        }

        return null;
    }


    /*
     * ==========================================================================================================
     * Quality
     */

    /**
     * Represent the data quality checks of a structure or field as DataQualityRule governance controls that govern it.
     *
     * @param qualityRules checks from the document (may be null)
     * @param elementGUID structure or field
     * @param elementQualifiedName qualified name of the structure or field
     * @param agreementGUID anchor
     * @param result result to record warnings in
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkQualityRules(List<DataContractQualityRule> qualityRules,
                                  String                        elementGUID,
                                  String                        elementQualifiedName,
                                  String                        agreementGUID,
                                  BitolMappingResult            result) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        if (qualityRules == null)
        {
            return;
        }

        GovernanceDefinitionClient ruleClient = context.getGovernanceDefinitionClient(OpenMetadataType.DATA_QUALITY_RULE.typeName);
        int                        index      = 0;

        for (DataContractQualityRule qualityRule : qualityRules)
        {
            index++;

            if (qualityRule != null)
            {
                String ruleName = qualityRule.getId();

                if (ruleName == null)
                {
                    ruleName = (qualityRule.getName() != null) ? qualityRule.getName() : Integer.toString(index);
                }

                String qualifiedName = elementQualifiedName + SEPARATOR + QUALITY_SEGMENT + SEPARATOR + ruleName;
                String ruleGUID      = findElementGUID(qualifiedName);

                DataQualityRuleProperties properties = getDataQualityRuleProperties(qualityRule, qualifiedName, elementQualifiedName);

                if (ruleGUID == null)
                {
                    NewElementOptions newElementOptions = new NewElementOptions(ruleClient.getMetadataSourceOptions());

                    newElementOptions.setAnchorGUID(agreementGUID);
                    newElementOptions.setIsOwnAnchor(false);

                    ruleGUID = ruleClient.createGovernanceDefinition(newElementOptions, null, properties, null);

                    ruleClient.addGovernanceDefinitionToElement(elementGUID,
                                                                ruleGUID,
                                                                ruleClient.getMakeAnchorOptions(false),
                                                                new GovernedByProperties());
                }
                else
                {
                    ruleClient.updateGovernanceDefinition(ruleGUID, ruleClient.getUpdateOptions(false), properties);
                }

                addSearchKeywords(qualityRule.getTags(), ruleGUID);
                linkAuthoritativeDefinitions(qualityRule.getAuthoritativeDefinitions(), ruleGUID);

                if ((qualityRule.getType() != null) && (qualityRule.getImplementation() == null) && (qualityRule.getQuery() == null) &&
                    (qualityRule.getMetric() == null) && (qualityRule.getRule() == null) && (! "text".equalsIgnoreCase(qualityRule.getType())))
                {
                    result.addWarning("Quality check " + ruleName + " on " + elementQualifiedName + " has type " + qualityRule.getType() + " but no metric, query or implementation");
                }
            }
        }
    }


    /**
     * Build the properties of a DataQualityRule from a quality check.
     *
     * @param qualityRule check from the document
     * @param qualifiedName qualified name for the rule
     * @param elementQualifiedName qualified name of the element being checked (used in the scope)
     * @return properties
     */
    DataQualityRuleProperties getDataQualityRuleProperties(DataContractQualityRule qualityRule,
                                                           String                  qualifiedName,
                                                           String                  elementQualifiedName)
    {
        DataQualityRuleProperties properties = new DataQualityRuleProperties();

        String displayName = qualityRule.getName();

        if (displayName == null)
        {
            displayName = (qualityRule.getMetric() != null) ? qualityRule.getMetric() : (qualityRule.getRule() != null) ? qualityRule.getRule() : "Data quality check";
        }

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(displayName);
        properties.setDescription(qualityRule.getDescription());
        properties.setSummary(qualityRule.getDescription());
        properties.setScope(elementQualifiedName);
        properties.setQualityDimension(qualityRule.getDimension());
        properties.setCheckType(qualityRule.getType());
        properties.setMetric((qualityRule.getMetric() != null) ? qualityRule.getMetric() : qualityRule.getRule());
        properties.setSeverity(qualityRule.getSeverity());
        properties.setBusinessImpact(qualityRule.getBusinessImpact());
        properties.setMethod(qualityRule.getMethod());
        properties.setUnits(qualityRule.getUnit());
        properties.setSchedule(qualityRule.getSchedule());
        properties.setScheduler(qualityRule.getScheduler());
        properties.setQualityEngine(qualityRule.getEngine());

        if (qualityRule.getQuery() != null)
        {
            properties.setExpression(qualityRule.getQuery());
        }
        else if (qualityRule.getImplementation() != null)
        {
            properties.setExpression(qualityRule.getImplementation().toString());
        }

        Map.Entry<String, List<String>> comparison = getComparison(qualityRule);

        if (comparison != null)
        {
            properties.setComparisonOperator(comparison.getKey());
            properties.setThresholdValues(comparison.getValue());
        }

        Map<String, String> additionalProperties = new HashMap<>();

        putIfPresent(additionalProperties, "id", qualityRule.getId());

        if (qualityRule.getArguments() != null)
        {
            for (Map.Entry<String, Object> argument : qualityRule.getArguments().entrySet())
            {
                additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "argument." + argument.getKey(), String.valueOf(argument.getValue()));
            }
        }

        addCustomProperties(qualityRule.getCustomProperties(), additionalProperties);
        properties.setAdditionalProperties(additionalProperties);

        return properties;
    }


    /**
     * Extract the comparison operator and threshold values from a quality check.  The standard expects only one of
     * the operators to be set; the first one found (in the order of the standard) is used.
     *
     * @param qualityRule check from the document
     * @return operator name and threshold values, or null if no operator is set
     */
    static Map.Entry<String, List<String>> getComparison(DataContractQualityRule qualityRule)
    {
        if (qualityRule.getMustBe() != null)
        {
            return Map.entry("mustBe", List.of(String.valueOf(qualityRule.getMustBe())));
        }
        if (qualityRule.getMustNotBe() != null)
        {
            return Map.entry("mustNotBe", List.of(String.valueOf(qualityRule.getMustNotBe())));
        }
        if (qualityRule.getMustBeGreaterThan() != null)
        {
            return Map.entry("mustBeGreaterThan", List.of(qualityRule.getMustBeGreaterThan().toString()));
        }
        if (qualityRule.getMustBeGreaterOrEqualTo() != null)
        {
            return Map.entry("mustBeGreaterOrEqualTo", List.of(qualityRule.getMustBeGreaterOrEqualTo().toString()));
        }
        if (qualityRule.getMustBeLessThan() != null)
        {
            return Map.entry("mustBeLessThan", List.of(qualityRule.getMustBeLessThan().toString()));
        }
        if (qualityRule.getMustBeLessOrEqualTo() != null)
        {
            return Map.entry("mustBeLessOrEqualTo", List.of(qualityRule.getMustBeLessOrEqualTo().toString()));
        }
        if (qualityRule.getMustBeBetween() != null)
        {
            return Map.entry("mustBeBetween", toStrings(qualityRule.getMustBeBetween()));
        }
        if (qualityRule.getMustNotBeBetween() != null)
        {
            return Map.entry("mustNotBeBetween", toStrings(qualityRule.getMustNotBeBetween()));
        }

        return null;
    }


    /**
     * Convert a list of numbers to strings.
     *
     * @param numbers list of numbers
     * @return list of strings
     */
    private static List<String> toStrings(List<Number> numbers)
    {
        List<String> strings = new ArrayList<>();

        for (Number number : numbers)
        {
            strings.add(String.valueOf(number));
        }

        return strings;
    }


    /*
     * ==========================================================================================================
     * Service level agreement
     */

    /**
     * Represent the SLA properties, and the limitations and usage from the description, as a ServiceLevelObjective
     * that governs the agreement.  Each SLA property becomes an obligation of the provider; the limitations become
     * restrictions and the usage an entitlement of the consumer.
     *
     * @param dataContract document
     * @param agreementGUID agreement
     * @param contractQualifiedName agreement qualified name
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkServiceLevelObjective(DataContract dataContract,
                                           String       agreementGUID,
                                           String       contractQualifiedName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        boolean hasSLA         = (dataContract.getSlaProperties() != null) && (! dataContract.getSlaProperties().isEmpty());
        boolean hasLimitations = (dataContract.getDescription() != null) && (dataContract.getDescription().getLimitations() != null);
        boolean hasUsage       = (dataContract.getDescription() != null) && (dataContract.getDescription().getUsage() != null);

        if ((! hasSLA) && (! hasLimitations) && (! hasUsage))
        {
            return;
        }

        GovernanceDefinitionClient sloClient     = context.getGovernanceDefinitionClient(OpenMetadataType.SERVICE_LEVEL_OBJECTIVE.typeName);
        String                     qualifiedName = contractQualifiedName + SEPARATOR + SLA_SEGMENT;
        String                     sloGUID       = findElementGUID(qualifiedName);

        ServiceLevelObjectiveProperties properties = new ServiceLevelObjectiveProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName("Service level agreement for " + ((dataContract.getName() != null) ? dataContract.getName() : dataContract.getId()));
        properties.setSummary("Service levels promised by the data contract");
        properties.setScope(contractQualifiedName);
        properties.setVersionIdentifier(dataContract.getVersion());

        if (hasSLA)
        {
            Map<String, String> obligations = new LinkedHashMap<>();

            for (DataContractSLAProperty slaProperty : dataContract.getSlaProperties())
            {
                if ((slaProperty != null) && (slaProperty.getProperty() != null))
                {
                    String key = slaProperty.getProperty();

                    if (slaProperty.getElement() != null)
                    {
                        key = key + " [" + slaProperty.getElement() + "]";
                    }
                    if (slaProperty.getDriver() != null)
                    {
                        key = key + " (" + slaProperty.getDriver() + ")";
                    }

                    int suffix = 2;
                    String uniqueKey = key;
                    while (obligations.containsKey(uniqueKey))
                    {
                        uniqueKey = key + " #" + suffix++;
                    }

                    StringBuilder value = new StringBuilder(String.valueOf(slaProperty.getValue()));

                    if (slaProperty.getValueExt() != null)
                    {
                        value.append(" / ").append(slaProperty.getValueExt());
                    }
                    if (slaProperty.getUnit() != null)
                    {
                        value.append(" ").append(slaProperty.getUnit());
                    }
                    if (slaProperty.getDescription() != null)
                    {
                        value.append(" - ").append(slaProperty.getDescription());
                    }
                    if (slaProperty.getSchedule() != null)
                    {
                        value.append(" [").append(slaProperty.getScheduler() != null ? slaProperty.getScheduler() + ": " : "").append(slaProperty.getSchedule()).append("]");
                    }

                    obligations.put(uniqueKey, value.toString());
                }
            }

            properties.setObligations(obligations);

            /*
             * The obligations are for people; keep the SLA properties themselves so the document can be regenerated exactly.
             */
            try
            {
                Map<String, String> additionalProperties = new HashMap<>();

                additionalProperties.put(SLA_PROPERTIES_JSON, BitolDocumentFormatter.toJSONFragment(dataContract.getSlaProperties()));
                properties.setAdditionalProperties(additionalProperties);
            }
            catch (IOException error)
            {
                /*
                 * Nothing lost that matters - the obligations still describe the SLA.
                 */
            }
        }

        if (hasLimitations)
        {
            properties.setRestrictions(Map.of("limitations", dataContract.getDescription().getLimitations()));
        }
        if (hasUsage)
        {
            properties.setEntitlements(Map.of("usage", dataContract.getDescription().getUsage()));
        }

        if (sloGUID == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(sloClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(agreementGUID);
            newElementOptions.setIsOwnAnchor(false);

            sloGUID = sloClient.createGovernanceDefinition(newElementOptions, null, properties, null);

            sloClient.addGovernanceDefinitionToElement(agreementGUID, sloGUID, sloClient.getMakeAnchorOptions(false), new GovernedByProperties());
        }
        else
        {
            sloClient.updateGovernanceDefinition(sloGUID, sloClient.getUpdateOptions(false), properties);
        }
    }


    /*
     * ==========================================================================================================
     * Servers and roles
     */

    /**
     * Represent the servers where the data resides as Endpoints listed as resources of the agreement.
     *
     * @param servers servers from the document (may be null)
     * @param agreementGUID agreement
     * @param contractQualifiedName agreement qualified name
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkServers(List<DataContractServer> servers,
                             String                   agreementGUID,
                             String                   contractQualifiedName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        if (servers == null)
        {
            return;
        }

        for (DataContractServer server : servers)
        {
            if ((server != null) && (server.getServer() != null))
            {
                String qualifiedName = contractQualifiedName + SEPARATOR + SERVER_SEGMENT + SEPARATOR + server.getServer();
                String endpointGUID  = findElementGUID(qualifiedName);

                EndpointProperties properties = new EndpointProperties();

                properties.setQualifiedName(qualifiedName);
                properties.setDisplayName(server.getServer());
                properties.setDescription(server.getDescription());
                properties.setProtocol(server.getType());
                properties.setNetworkAddress(getServerAddress(server));

                Map<String, String> additionalProperties = new HashMap<>();

                putIfPresent(additionalProperties, "id", server.getId());
                putIfPresent(additionalProperties, "type", server.getType());
                putIfPresent(additionalProperties, "environment", server.getEnvironment());
                putIfPresent(additionalProperties, "account", server.getAccount());
                putIfPresent(additionalProperties, "catalog", server.getCatalog());
                putIfPresent(additionalProperties, "database", server.getDatabase());
                putIfPresent(additionalProperties, "dataset", server.getDataset());
                putIfPresent(additionalProperties, "schema", server.getSchema());
                putIfPresent(additionalProperties, "host", server.getHost());
                putIfPresent(additionalProperties, "port", toStringValue(server.getPort()));
                putIfPresent(additionalProperties, "location", server.getLocation());
                putIfPresent(additionalProperties, "endpointUrl", server.getEndpointUrl());
                putIfPresent(additionalProperties, "path", server.getPath());
                putIfPresent(additionalProperties, "format", server.getFormat());
                putIfPresent(additionalProperties, "delimiter", server.getDelimiter());
                putIfPresent(additionalProperties, "project", server.getProject());
                putIfPresent(additionalProperties, "region", server.getRegion());
                putIfPresent(additionalProperties, "regionName", server.getRegionName());
                putIfPresent(additionalProperties, "serviceName", server.getServiceName());
                putIfPresent(additionalProperties, "stagingDir", server.getStagingDir());
                putIfPresent(additionalProperties, "warehouse", server.getWarehouse());
                putIfPresent(additionalProperties, "stream", server.getStream());

                if (server.getAdditionalProperties() != null)
                {
                    for (Map.Entry<String, Object> entry : server.getAdditionalProperties().entrySet())
                    {
                        additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }

                if (server.getRoles() != null)
                {
                    List<String> roleNames = new ArrayList<>();

                    for (DataContractRole role : server.getRoles())
                    {
                        if ((role != null) && (role.getRole() != null))
                        {
                            roleNames.add(role.getRole() + ((role.getAccess() != null) ? " (" + role.getAccess() + ")" : ""));
                        }
                    }

                    if (! roleNames.isEmpty())
                    {
                        additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "roles", String.join(", ", roleNames));
                    }
                }

                addCustomProperties(server.getCustomProperties(), additionalProperties);
                properties.setAdditionalProperties(additionalProperties);

                if (endpointGUID == null)
                {
                    NewElementOptions newElementOptions = new NewElementOptions(context.getEndpointClient().getMetadataSourceOptions());

                    newElementOptions.setAnchorGUID(agreementGUID);
                    newElementOptions.setIsOwnAnchor(false);
                    newElementOptions.setParentGUID(agreementGUID);
                    newElementOptions.setParentRelationshipTypeName(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName);
                    newElementOptions.setParentAtEnd1(true);

                    context.getEndpointClient().createEndpoint(newElementOptions, null, properties, new ResourceListProperties());
                }
                else
                {
                    context.getEndpointClient().updateEndpoint(endpointGUID, context.getEndpointClient().getUpdateOptions(false), properties);
                }
            }
        }
    }


    /**
     * Build a network address for a server from whichever location properties it carries.
     *
     * @param server server from the document
     * @return address or null
     */
    static String getServerAddress(DataContractServer server)
    {
        if (server.getLocation() != null)
        {
            return server.getLocation();
        }
        if (server.getEndpointUrl() != null)
        {
            return server.getEndpointUrl();
        }
        if (server.getHost() != null)
        {
            return (server.getPort() != null) ? server.getHost() + ":" + server.getPort() : server.getHost();
        }
        if (server.getPath() != null)
        {
            return server.getPath();
        }

        return null;
    }


    /**
     * Represent the access roles of the contract using the security definitions: one SecurityAccessControl governs the
     * agreement, and each ODCS role becomes a SecurityRole that the control associates with the role's access type as the
     * operation name.  Roles declared on a server are represented the same way, with the server recorded on the role.
     *
     * @param dataContract document
     * @param agreementGUID agreement
     * @param contractQualifiedName agreement qualified name
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkSecurityRoles(DataContract dataContract,
                                   String       agreementGUID,
                                   String       contractQualifiedName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        boolean hasContractRoles = (dataContract.getRoles() != null) && (! dataContract.getRoles().isEmpty());
        boolean hasServerRoles   = false;

        if (dataContract.getServers() != null)
        {
            for (DataContractServer server : dataContract.getServers())
            {
                if ((server != null) && (server.getRoles() != null) && (! server.getRoles().isEmpty()))
                {
                    hasServerRoles = true;
                }
            }
        }

        if ((! hasContractRoles) && (! hasServerRoles))
        {
            return;
        }

        /*
         * The access control for the contract.
         */
        GovernanceDefinitionClient controlClient        = context.getGovernanceDefinitionClient(OpenMetadataType.SECURITY_ACCESS_CONTROL.typeName);
        String                     controlQualifiedName = contractQualifiedName + SEPARATOR + ACCESS_CONTROL_SEGMENT;
        String                     controlGUID          = findElementGUID(controlQualifiedName);

        SecurityAccessControlProperties controlProperties = new SecurityAccessControlProperties();

        controlProperties.setQualifiedName(controlQualifiedName);
        controlProperties.setDisplayName("Access to " + ((dataContract.getName() != null) ? dataContract.getName() : dataContract.getId()));
        controlProperties.setSummary("The security roles that grant access to the data described by the data contract");
        controlProperties.setScope(contractQualifiedName);
        controlProperties.setVersionIdentifier(dataContract.getVersion());

        if (controlGUID == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(controlClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(agreementGUID);
            newElementOptions.setIsOwnAnchor(false);

            controlGUID = controlClient.createGovernanceDefinition(newElementOptions, null, controlProperties, null);

            controlClient.addGovernanceDefinitionToElement(agreementGUID, controlGUID, controlClient.getMakeAnchorOptions(false), new GovernedByProperties());
        }
        else
        {
            controlClient.updateGovernanceDefinition(controlGUID, controlClient.getUpdateOptions(false), controlProperties);
        }

        /*
         * The roles.
         */
        if (hasContractRoles)
        {
            for (DataContractRole role : dataContract.getRoles())
            {
                linkSecurityRole(role, null, controlClient, controlGUID, agreementGUID, contractQualifiedName);
            }
        }

        if (hasServerRoles)
        {
            for (DataContractServer server : dataContract.getServers())
            {
                if ((server != null) && (server.getRoles() != null))
                {
                    for (DataContractRole role : server.getRoles())
                    {
                        linkSecurityRole(role, server.getServer(), controlClient, controlGUID, agreementGUID, contractQualifiedName);
                    }
                }
            }
        }
    }


    /**
     * Create or update the SecurityRole for one ODCS role and associate it with the access control.
     *
     * @param role role from the document
     * @param serverName server the role applies to (null for a contract-level role)
     * @param controlClient client for the access control
     * @param controlGUID access control
     * @param agreementGUID anchor
     * @param contractQualifiedName agreement qualified name
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkSecurityRole(DataContractRole           role,
                                  String                     serverName,
                                  GovernanceDefinitionClient controlClient,
                                  String                     controlGUID,
                                  String                     agreementGUID,
                                  String                     contractQualifiedName) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        if ((role == null) || (role.getRole() == null))
        {
            return;
        }

        CollectionClient roleClient    = context.getCollectionClient(OpenMetadataType.SECURITY_ROLE.typeName);
        String           qualifiedName = contractQualifiedName + SEPARATOR + SECURITY_ROLE_SEGMENT + SEPARATOR + ((serverName != null) ? serverName + SEPARATOR : "") + role.getRole();
        String           roleGUID      = findElementGUID(qualifiedName);

        SecurityRoleProperties properties = new SecurityRoleProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(role.getRole());
        properties.setDescription(role.getDescription());
        properties.setIdentifier(role.getRole());

        Map<String, String> additionalProperties = new HashMap<>();

        putIfPresent(additionalProperties, "id", role.getId());
        putIfPresent(additionalProperties, "access", role.getAccess());
        putIfPresent(additionalProperties, "firstLevelApprovers", role.getFirstLevelApprovers());
        putIfPresent(additionalProperties, "secondLevelApprovers", role.getSecondLevelApprovers());
        putIfPresent(additionalProperties, "server", serverName);
        addCustomProperties(role.getCustomProperties(), additionalProperties);
        properties.setAdditionalProperties(additionalProperties);

        if (roleGUID == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(roleClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(agreementGUID);
            newElementOptions.setIsOwnAnchor(false);

            roleGUID = roleClient.createCollection(newElementOptions, null, properties, null);

            AssociatedSecurityListProperties association = new AssociatedSecurityListProperties();

            association.setOperationName(role.getAccess());

            controlClient.linkAssociatedSecurityList(controlGUID, roleGUID, controlClient.getMakeAnchorOptions(false), association);
        }
        else
        {
            roleClient.updateCollection(roleGUID, roleClient.getUpdateOptions(false), properties);
        }
    }


    /*
     * ==========================================================================================================
     * Helpers
     */

    /**
     * Build the name patterns for a schema element from its logical and physical names.
     *
     * @param name logical name
     * @param physicalName physical name (may be null)
     * @return list of distinct names, or null if both are null
     */
    static List<String> getNamePatterns(String name,
                                        String physicalName)
    {
        List<String> namePatterns = new ArrayList<>();

        if (name != null)
        {
            namePatterns.add(name);
        }
        if ((physicalName != null) && (! physicalName.equals(name)))
        {
            namePatterns.add(physicalName);
        }

        return namePatterns.isEmpty() ? null : namePatterns;
    }


    /**
     * Add a value to the additional properties under the bitol prefix if it is present.
     *
     * @param additionalProperties map to add to
     * @param name property name (without prefix)
     * @param value value (may be null)
     */
    private static void putIfPresent(Map<String, String> additionalProperties,
                                     String              name,
                                     String              value)
    {
        if (value != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + name, value);
        }
    }
}
