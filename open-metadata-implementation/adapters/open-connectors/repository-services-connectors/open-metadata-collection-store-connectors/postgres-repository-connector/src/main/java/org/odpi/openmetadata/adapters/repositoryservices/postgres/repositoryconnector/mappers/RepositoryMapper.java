/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers;


import org.odpi.openmetadata.adapters.connectors.resource.jdbc.mappers.BaseMapper;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryColumn;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.*;

/**
 * Common routines used by the mappers
 */
public class RepositoryMapper extends BaseMapper
{
    final OMRSRepositoryHelper repositoryHelper;
    final String               repositoryName;


    /**
     * Constructor for base mapper.
     *
     * @param repositoryHelper repository helper
     * @param repositoryName repository name
     */
    public RepositoryMapper(OMRSRepositoryHelper repositoryHelper,
                            String               repositoryName)
    {
        super(repositoryName);

        this.repositoryHelper = repositoryHelper;
        this.repositoryName   = repositoryName;
    }


    /**
     * Return the instance provenance type enum.
     *
     * @param instanceTableRow row from database
     * @return enum
     * @throws RepositoryErrorException problem mapping value
     */
    private InstanceProvenanceType getInstanceProvenanceType(Map<String, JDBCDataValue> instanceTableRow) throws RepositoryErrorException
    {
        final String methodName = "getInstanceProvenanceType";

        String instanceProvenanceTypeName = super.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_PROVENANCE_TYPE.getColumnName(),
                                                                              instanceTableRow,
                                                                              true);

        for (InstanceProvenanceType instanceProvenanceType : InstanceProvenanceType.values())
        {
            if (instanceProvenanceType.getName().equals(instanceProvenanceTypeName))
            {
                return instanceProvenanceType;
            }
        }

        throw new RepositoryErrorException(PostgresErrorCode.INVALID_REPOSITORY_VALUE.getMessageDefinition(repositoryName,
                                                                                                           RepositoryColumn.INSTANCE_PROVENANCE_TYPE.getColumnName(),
                                                                                                           methodName,
                                                                                                           this.getClass().getName(),
                                                                                                           instanceTableRow.toString()),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Return the instance status enum.
     *
     * @param columnName column name
     * @param instanceTableRow row from database
     * @param isRequired is this value required
     * @return enum
     * @throws RepositoryErrorException problem mapping value
     */
    private InstanceStatus getInstanceStatus(String columnName, Map<String, JDBCDataValue> instanceTableRow,
                                             boolean                                       isRequired) throws RepositoryErrorException
    {
        final String methodName = "getInstanceStatus";

        String instanceStatusName = super.getStringPropertyFromColumn(columnName, instanceTableRow, isRequired);

        for (InstanceStatus instanceStatus : InstanceStatus.values())
        {
            if (instanceStatus.getName().equals(instanceStatusName))
            {
                return instanceStatus;
            }
        }

        if (isRequired)
        {
            throw new RepositoryErrorException(PostgresErrorCode.INVALID_REPOSITORY_VALUE.getMessageDefinition(repositoryName,
                                                                                                               columnName,
                                                                                                               methodName,
                                                                                                               this.getClass().getName(),
                                                                                                               instanceTableRow.toString()),
                                               this.getClass().getName(),
                                               methodName);
        }

        return null;
    }


    /**
     * Fill in an instance audit header from the values retrieved from the database.
     *
     * @param instanceAuditHeader header to fill in
     * @param instanceTableRow values from the database
     * @throws RepositoryErrorException mapping problem or missing values
     */
    protected void fillInstanceAuditHeader(InstanceAuditHeader        instanceAuditHeader,
                                           Map<String, JDBCDataValue> instanceTableRow) throws RepositoryErrorException
    {
        final String methodName = "fillInstanceAuditHeader";

        String typeName = super.getStringPropertyFromColumn(RepositoryColumn.TYPE_NAME.getColumnName(),
                                                            instanceTableRow,
                                                            true);

        String[] typeNameParts = typeName.split(":");

        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeNameParts[1]);

        if (typeDef != null)
        {
            try
            {
                instanceAuditHeader.setType(repositoryHelper.getNewInstanceType(repositoryName, typeDef));
            }
            catch (TypeErrorException typeErrorException)
            {
                throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                               typeErrorException.getClass().getName(),
                                                                                                               methodName,
                                                                                                               typeErrorException.getMessage()),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   typeErrorException);
            }

            instanceAuditHeader.setInstanceProvenanceType(this.getInstanceProvenanceType(instanceTableRow));
            instanceAuditHeader.setMetadataCollectionId(super.getStringPropertyFromColumn(RepositoryColumn.METADATA_COLLECTION_GUID.getColumnName(), instanceTableRow, true));
            instanceAuditHeader.setMetadataCollectionName(super.getStringPropertyFromColumn(RepositoryColumn.METADATA_COLLECTION_NAME.getColumnName(), instanceTableRow, false));
            instanceAuditHeader.setReplicatedBy(super.getStringPropertyFromColumn(RepositoryColumn.REPLICATED_BY.getColumnName(), instanceTableRow, false));
            instanceAuditHeader.setInstanceLicense(super.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_LICENCE.getColumnName(), instanceTableRow, false));
            instanceAuditHeader.setCreatedBy(super.getStringPropertyFromColumn(RepositoryColumn.CREATED_BY.getColumnName(), instanceTableRow, true));
            instanceAuditHeader.setUpdatedBy(super.getStringPropertyFromColumn(RepositoryColumn.UPDATED_BY.getColumnName(), instanceTableRow, false));
            instanceAuditHeader.setMaintainedBy(super.getStringArrayPropertyFromColumn(RepositoryColumn.MAINTAINED_BY.getColumnName(), instanceTableRow, false));
            instanceAuditHeader.setCreateTime(super.getDatePropertyFromColumn(RepositoryColumn.CREATE_TIME.getColumnName(), instanceTableRow, true));
            if (instanceAuditHeader.getUpdatedBy() != null)
            {
                /*
                 * If this is the first version of the instance, we store the created time in the updated time column to simplify date based searches.
                 * Therefore, we only retrieve the update time if updatedBy is set.
                 */
                instanceAuditHeader.setUpdateTime(super.getDatePropertyFromColumn(RepositoryColumn.UPDATE_TIME.getColumnName(), instanceTableRow, false));
            }
            instanceAuditHeader.setVersion(super.getLongPropertyFromColumn(RepositoryColumn.VERSION.getColumnName(), instanceTableRow, true));
            instanceAuditHeader.setStatus(this.getInstanceStatus(RepositoryColumn.CURRENT_STATUS.getColumnName(), instanceTableRow, true));
            instanceAuditHeader.setStatusOnDelete(this.getInstanceStatus(RepositoryColumn.STATUS_ON_DELETE.getColumnName(), instanceTableRow, false));
            instanceAuditHeader.setMappingProperties(super.getSerializableMapPropertyFromColumn(RepositoryColumn.MAPPING_PROPERTIES.getColumnName(), instanceTableRow, false));
        }
        else
        {
            throw new RepositoryErrorException(PostgresErrorCode.INVALID_REPOSITORY_VALUE.getMessageDefinition(repositoryName,
                                                                                                               RepositoryColumn.TYPE_NAME.getColumnName(),
                                                                                                               methodName,
                                                                                                               this.getClass().getName(),
                                                                                                               instanceTableRow.toString()),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * Fill in an instance header from the values retrieved from the database.
     *
     * @param instanceHeader header to fill in
     * @param instanceTableRow values from the database
     * @throws RepositoryErrorException mapping problem or missing values
     */
    protected void fillInstanceHeader(InstanceHeader             instanceHeader,
                                      Map<String, JDBCDataValue> instanceTableRow) throws RepositoryErrorException
    {
        fillInstanceAuditHeader(instanceHeader, instanceTableRow);

        instanceHeader.setGUID((super.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(), instanceTableRow, true)));
        instanceHeader.setInstanceURL((super.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_URL.getColumnName(), instanceTableRow, false)));
        instanceHeader.setReIdentifiedFromGUID((super.getStringPropertyFromColumn(RepositoryColumn.REIDENTIFIED_FROM_GUID.getColumnName(), instanceTableRow, false)));
    }


    /**
     * Convert the tables retrieved from the database into an instance properties object.  It may contain
     * effectivity dates and/or attributes.  The rows supplied are assumed to be for the correct version.
     *
     * @param instanceTableRow row from the entity, relationship/classification
     * @param instancePropertiesTableRows rows for each version of each attribute
     * @param uniqueOnly should only unique properties be returned?
     * @return instance properties object or null
     * @throws RepositoryErrorException there was a mapping problem
     */
    private InstanceProperties getInstanceProperties(Map<String, JDBCDataValue>       instanceTableRow,
                                                     List<Map<String, JDBCDataValue>> instancePropertiesTableRows,
                                                     boolean                          uniqueOnly) throws RepositoryErrorException
    {
        InstanceProperties instanceProperties = new InstanceProperties();

        if (instanceTableRow != null)
        {
            instanceProperties.setEffectiveFromTime(super.getDatePropertyFromColumn(RepositoryColumn.EFFECTIVE_FROM_TIME.getColumnName(), instanceTableRow, false));
            instanceProperties.setEffectiveToTime(super.getDatePropertyFromColumn(RepositoryColumn.EFFECTIVE_TO_TIME.getColumnName(), instanceTableRow, false));
        }

        Map<String, List<Map<String, JDBCDataValue>>> attributeRows = new HashMap<>();

        if (instancePropertiesTableRows != null)
        {
            for (Map<String, JDBCDataValue> row : instancePropertiesTableRows)
            {
                if (row != null)
                {
                    boolean rowIsUnique = super.getBooleanPropertyFromColumn(RepositoryColumn.IS_UNIQUE_ATTRIBUTE.getColumnName(), row, true);

                    if (rowIsUnique || !uniqueOnly)
                    {
                        String                           attributeName = super.getStringPropertyFromColumn(RepositoryColumn.ATTRIBUTE_NAME.getColumnName(), row, true);
                        List<Map<String, JDBCDataValue>> attributeRow  = attributeRows.get(attributeName);
                        if (attributeRow == null)
                        {
                            attributeRow = new ArrayList<>();
                        }

                        attributeRow.add(row);
                        attributeRows.put(attributeName, attributeRow);
                    }
                }
            }
        }

        for (String attributeName : attributeRows.keySet())
        {
            instanceProperties.setProperty(attributeName, this.getInstancePropertyValue(attributeName, attributeRows.get(attributeName)));
        }

        if ((instanceProperties.getPropertyCount() > 0 ) ||
                (instanceProperties.getEffectiveFromTime() != null) ||
                (instanceProperties.getEffectiveToTime() != null))
        {
            return instanceProperties;
        }

        return null;
    }


    /**
     * Retrieve the value for a collection property.
     *
     * @param attributeName toplevel name of the attribute
     * @param attributeRows list of rows representing this value
     * @return a single property value
     * @throws RepositoryErrorException there was a mapping problem
     */
    private InstancePropertyValue getInstancePropertyValue(String                           attributeName,
                                                           List<Map<String, JDBCDataValue>> attributeRows) throws RepositoryErrorException
    {
        final String methodName = "getInstancePropertyValue";

        if ((attributeRows != null) && (! attributeRows.isEmpty()))
        {
            if (attributeRows.size() == 1)
            {
                return this.getInstancePropertyValue(attributeName, attributeRows.get(0), new ArrayList<>());
            }
            else
            {
                Map<String, JDBCDataValue>       principleRow = null;
                List<Map<String, JDBCDataValue>> additionalRows = new ArrayList<>();

                for (Map<String, JDBCDataValue> attributeRow : attributeRows)
                {
                    String propertyName = this.getStringPropertyFromColumn(RepositoryColumn.PROPERTY_NAME.getColumnName(), attributeRow, true);

                    if (attributeName.equals(propertyName))
                    {
                        if (principleRow == null)
                        {
                            principleRow = attributeRow;
                        }
                        else
                        {
                            throw new RepositoryErrorException(PostgresErrorCode.INVALID_REPOSITORY_VALUE.getMessageDefinition(repositoryName,
                                                                                                                               RepositoryColumn.PROPERTY_NAME.getColumnName(),
                                                                                                                               methodName,
                                                                                                                               this.getClass().getName(),
                                                                                                                               attributeRows.toString()),
                                                               this.getClass().getName(),
                                                               methodName);
                        }
                    }
                    else if (propertyName.startsWith(attributeName))
                    {
                        additionalRows.add(attributeRow);
                    }
                    else
                    {
                        throw new RepositoryErrorException(PostgresErrorCode.INVALID_REPOSITORY_VALUE.getMessageDefinition(repositoryName,
                                                                                                                           RepositoryColumn.PROPERTY_NAME.getColumnName(),
                                                                                                                           methodName,
                                                                                                                           this.getClass().getName(),
                                                                                                                           attributeRows.toString()),
                                                           this.getClass().getName(),
                                                           methodName);
                    }
                }

                return this.getInstancePropertyValue(attributeName, principleRow, additionalRows);
            }
        }

        return null;
    }


    /**
     * Return an instance property value based on a principle row and optional nested rows.
     *
     * @param attributeName name of the attribute
     * @param principleRow database row to transform for top level instance properties
     * @param additionalRows additional rows for nested instance properties
     * @return instance property value
     * @throws RepositoryErrorException there was a mapping problem
     */
    private InstancePropertyValue getInstancePropertyValue(String                           attributeName,
                                                           Map<String, JDBCDataValue>       principleRow,
                                                           List<Map<String, JDBCDataValue>> additionalRows) throws RepositoryErrorException
    {
        final String methodName = "getInstancePropertyValue";

        String propertyCategory = super.getStringPropertyFromColumn(RepositoryColumn.PROPERTY_CATEGORY.getColumnName(), principleRow, true);
        String attributeTypeName = super.getStringPropertyFromColumn(RepositoryColumn.ATTRIBUTE_TYPE_NAME.getColumnName(), principleRow, true);

        AttributeTypeDef attributeTypeDef = repositoryHelper.getAttributeTypeDefByName(repositoryName, attributeTypeName);

        for (InstancePropertyCategory instancePropertyCategory : InstancePropertyCategory.values())
        {
            if (instancePropertyCategory.getName().equals(propertyCategory))
            {
                switch (instancePropertyCategory)
                {
                    case ENUM ->
                    {
                        return getEnumPropertyValue(attributeTypeDef,
                                                    super.getStringPropertyFromColumn(RepositoryColumn.PROPERTY_VALUE.getColumnName(),
                                                                                      principleRow,
                                                                                      true));
                    }
                    case STRUCT ->
                    {
                        return getStructPropertyValue(attributeName, attributeTypeDef, additionalRows);
                    }
                    case MAP ->
                    {
                        return getMapPropertyValue(attributeName, attributeTypeDef, additionalRows);
                    }
                    case ARRAY ->
                    {
                        return getArrayPropertyValue(attributeName, attributeTypeDef, additionalRows);
                    }
                }
            }
        }

        for (PrimitiveDefCategory primitiveDefCategory : PrimitiveDefCategory.values())
        {
            if (primitiveDefCategory.getName().equals(propertyCategory))
            {
                String propertyValue = super.getStringPropertyFromColumn(RepositoryColumn.PROPERTY_VALUE.getColumnName(), principleRow, true);

                switch (primitiveDefCategory)
                {
                    case OM_PRIMITIVE_TYPE_UNKNOWN, OM_PRIMITIVE_TYPE_STRING ->
                    {
                        return this.getPrimitivePropertyValue(attributeTypeDef, primitiveDefCategory, propertyValue);
                    }
                    case OM_PRIMITIVE_TYPE_BOOLEAN ->
                    {
                        return this.getPrimitivePropertyValue(attributeTypeDef, primitiveDefCategory, Boolean.parseBoolean(propertyValue));
                    }
                    case OM_PRIMITIVE_TYPE_BYTE ->
                    {
                        return this.getPrimitivePropertyValue(attributeTypeDef, primitiveDefCategory, Byte.parseByte(propertyValue));
                    }
                    case OM_PRIMITIVE_TYPE_CHAR ->
                    {
                        return this.getPrimitivePropertyValue(attributeTypeDef, primitiveDefCategory, propertyValue.charAt(0));
                    }
                    case OM_PRIMITIVE_TYPE_SHORT ->
                    {
                        return this.getPrimitivePropertyValue(attributeTypeDef, primitiveDefCategory, Short.parseShort(propertyValue));
                    }
                    case OM_PRIMITIVE_TYPE_INT ->
                    {
                        return this.getPrimitivePropertyValue(attributeTypeDef, primitiveDefCategory, Integer.parseInt(propertyValue));
                    }
                    case OM_PRIMITIVE_TYPE_LONG ->
                    {
                        return this.getPrimitivePropertyValue(attributeTypeDef, primitiveDefCategory, Long.parseLong(propertyValue));
                    }
                    case OM_PRIMITIVE_TYPE_FLOAT ->
                    {
                        return this.getPrimitivePropertyValue(attributeTypeDef, primitiveDefCategory, Float.parseFloat(propertyValue));
                    }
                    case OM_PRIMITIVE_TYPE_DOUBLE, OM_PRIMITIVE_TYPE_BIGINTEGER, OM_PRIMITIVE_TYPE_BIGDECIMAL ->
                    {
                        return this.getPrimitivePropertyValue(attributeTypeDef, primitiveDefCategory, Double.parseDouble(propertyValue));
                    }
                    case OM_PRIMITIVE_TYPE_DATE ->
                    {
                        return this.getPrimitivePropertyValue(attributeTypeDef, primitiveDefCategory, new Date(Long.parseLong(propertyValue)));
                    }
                }
            }
        }

        throw new RepositoryErrorException(PostgresErrorCode.INVALID_REPOSITORY_VALUE.getMessageDefinition(repositoryName,
                                                                                                           RepositoryColumn.PROPERTY_VALUE.getColumnName(),
                                                                                                           methodName,
                                                                                                           this.getClass().getName(),
                                                                                                           principleRow.toString()),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Create an array instance property value from database rows
     *
     * @param attributeName name of the attribute
     * @param typeDef property's type definition
     * @param additionalRows additional rows for nested instance properties
     * @return array property value
     * @throws RepositoryErrorException there was a mapping problem
     */
    private ArrayPropertyValue getArrayPropertyValue(String                           attributeName,
                                                     AttributeTypeDef                 typeDef,
                                                     List<Map<String, JDBCDataValue>> additionalRows) throws RepositoryErrorException
    {
        ArrayPropertyValue arrayPropertyValue = new ArrayPropertyValue();

        arrayPropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
        arrayPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.ARRAY);
        arrayPropertyValue.setTypeGUID(typeDef.getGUID());
        arrayPropertyValue.setTypeName(typeDef.getName());
        arrayPropertyValue.setArrayCount(additionalRows.size());

        arrayPropertyValue.setArrayValues(getNestedProperties(attributeName, additionalRows));

        return arrayPropertyValue;
    }


    /**
     * Create a map instance property value from database rows.
     *
     * @param attributeName name of the attribute
     * @param attributeTypeDef property's type definition
     * @param additionalRows additional rows for nested instance properties
     * @return map property value
     * @throws RepositoryErrorException there was a mapping problem
     */
    private MapPropertyValue getMapPropertyValue(String                           attributeName,
                                                 AttributeTypeDef                 attributeTypeDef,
                                                 List<Map<String, JDBCDataValue>> additionalRows) throws RepositoryErrorException
    {
        MapPropertyValue mapPropertyValue = new MapPropertyValue();

        mapPropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
        mapPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.MAP);
        mapPropertyValue.setTypeGUID(attributeTypeDef.getGUID());
        mapPropertyValue.setTypeName(attributeTypeDef.getName());

        mapPropertyValue.setMapValues(getNestedProperties(attributeName, additionalRows));

        return mapPropertyValue;
    }


    /**
     * Create a struct instance property value from database rows.
     *
     * @param attributeName name of the attribute
     * @param attributeTypeDef property's type definition
     * @param additionalRows additional rows for nested instance properties
     * @return struct property value
     */
    private StructPropertyValue getStructPropertyValue(String                           attributeName,
                                                       AttributeTypeDef                 attributeTypeDef,
                                                       List<Map<String, JDBCDataValue>> additionalRows) throws RepositoryErrorException
    {
        StructPropertyValue structPropertyValue = new StructPropertyValue();

        structPropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
        structPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.STRUCT);
        structPropertyValue.setTypeGUID(attributeTypeDef.getGUID());
        structPropertyValue.setTypeName(attributeTypeDef.getName());

        structPropertyValue.setAttributes(getNestedProperties(attributeName, additionalRows));

        return structPropertyValue;
    }


    /**
     * Create an InstanceProperties object for nested collection properties.
     *
     * @param attributeName and of the collection attribute
     * @param additionalRows rows for the nested properties
     * @return InstanceProperties
     * @throws RepositoryErrorException mapping problem
     */
    private InstanceProperties getNestedProperties(String                           attributeName,
                                                   List<Map<String, JDBCDataValue>> additionalRows) throws RepositoryErrorException
    {
        List<Map<String, JDBCDataValue>> nestedRows = new ArrayList<>();

        for (Map<String, JDBCDataValue> row : additionalRows)
        {
            Map<String, JDBCDataValue> nestedRow = new HashMap<>(row);

            PropertyNameStruct propertyNameStruct = new PropertyNameStruct(attributeName,
                                                                           super.getStringPropertyFromColumn(RepositoryColumn.PROPERTY_NAME.getColumnName(),
                                                                                                             row,
                                                                                                             true));

            nestedRow.put(RepositoryColumn.ATTRIBUTE_NAME.getColumnName(), new JDBCDataValue(propertyNameStruct.getNextParentAttributeName(), ColumnType.STRING.getJdbcType()));
            nestedRow.put(RepositoryColumn.PROPERTY_NAME.getColumnName(), new JDBCDataValue(propertyNameStruct.getNestedPropertyName(), ColumnType.STRING.getJdbcType()));

            nestedRows.add(nestedRow);
        }

        return this.getInstanceProperties(null, nestedRows, false);
    }


    /**
     * Manage the structure of a property name
     */
    class PropertyNameStruct
    {
        String nestedPropertyName;
        String nextParentAttributeName;

        /**
         * Constructor
         *
         * @param parentAttributeName parent attribute name
         * @param fullPropertyName full property name (parentAttributeName.nestedAttributeName.nestedPropertyName)
         * @throws RepositoryErrorException mapping problem parsing full property name
         */
        public PropertyNameStruct(String parentAttributeName, String fullPropertyName) throws RepositoryErrorException
        {
            final String methodName = "PropertyNameStruct";

            String[] propertyNameParts = fullPropertyName.split(":");

            if (propertyNameParts.length < 2)
            {
                throw new RepositoryErrorException(PostgresErrorCode.INVALID_REPOSITORY_VALUE.getMessageDefinition(repositoryName,
                                                                                                                   RepositoryColumn.PROPERTY_NAME.getColumnName(),
                                                                                                                   methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   fullPropertyName),
                                                   this.getClass().getName(),
                                                   methodName);
            }
            else if (! parentAttributeName.equals(propertyNameParts[0]))
            {
                throw new RepositoryErrorException(PostgresErrorCode.INVALID_REPOSITORY_VALUE.getMessageDefinition(repositoryName,
                                                                                                                   RepositoryColumn.PROPERTY_NAME.getColumnName(),
                                                                                                                   methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   fullPropertyName),
                                                   this.getClass().getName(),
                                                   methodName);
            }

            boolean firstPartOfName = true;
            StringBuilder stringBuilder = new StringBuilder();

            this.nextParentAttributeName = propertyNameParts[1];
            for (int i=1; i<propertyNameParts.length; i++)
            {
                if (firstPartOfName)
                {
                    firstPartOfName = false;
                }
                else
                {
                    stringBuilder.append(":");
                }

                stringBuilder.append(propertyNameParts[i]);
            }

            this.nestedPropertyName = stringBuilder.toString();
        }


        /**
         * Return the nested property name
         *
         * @return string
         */
        public String getNestedPropertyName()
        {
            return nestedPropertyName;
        }


        /**
         * Return the next parent attribute name.
         *
         * @return string
         */
        public String getNextParentAttributeName()
        {
            return nextParentAttributeName;
        }
    }


    /**
     * Create a primitive property from a database row.
     *
     * @param attributeTypeDef property's type definition
     * @param primitiveTypeCategory value type
     * @param primitiveValue value
     * @return primitive property value
     */
    private PrimitivePropertyValue getPrimitivePropertyValue(AttributeTypeDef     attributeTypeDef,
                                                             PrimitiveDefCategory primitiveTypeCategory,
                                                             Object               primitiveValue)
    {
        PrimitivePropertyValue omrsPropertyValue = new PrimitivePropertyValue();

        omrsPropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.PRIMITIVE);
        omrsPropertyValue.setTypeGUID(attributeTypeDef.getGUID());
        omrsPropertyValue.setTypeName(attributeTypeDef.getName());
        omrsPropertyValue.setPrimitiveDefCategory(primitiveTypeCategory);
        omrsPropertyValue.setPrimitiveValue(primitiveValue);

        return omrsPropertyValue;
    }


    /**
     * Create an Enum instance property value from a database row.
     *
     * @param attributeTypeDef  property's type definition
     * @param symbolicName enum value
     * @return OMRS property value
     */
    private EnumPropertyValue getEnumPropertyValue(AttributeTypeDef attributeTypeDef,
                                                   String           symbolicName) throws RepositoryErrorException
    {
        final String methodName = "getEnumPropertyValue";

        if (attributeTypeDef instanceof EnumDef enumDef)
        {
            List<EnumElementDef> enumElementDefs = enumDef.getElementDefs();

            if ((enumElementDefs != null) && (! enumElementDefs.isEmpty()))
            {
                for (EnumElementDef enumElementDef : enumElementDefs)
                {
                    if ((enumElementDef != null) &&
                            (enumElementDef.getValue() != null) &&
                            (symbolicName.equalsIgnoreCase(enumElementDef.getValue())))
                    {
                        EnumPropertyValue enumPropertyValue = new EnumPropertyValue();

                        enumPropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
                        enumPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.ENUM);
                        enumPropertyValue.setTypeGUID(attributeTypeDef.getGUID());
                        enumPropertyValue.setTypeName(attributeTypeDef.getName());
                        enumPropertyValue.setSymbolicName(enumElementDef.getValue());
                        enumPropertyValue.setOrdinal(enumElementDef.getOrdinal());
                        enumPropertyValue.setDescription(enumElementDef.getDescription());

                        return enumPropertyValue;
                    }
                }
            }
        }

        throw new RepositoryErrorException(PostgresErrorCode.INVALID_REPOSITORY_VALUE.getMessageDefinition(repositoryName,
                                                                                                           RepositoryColumn.PROPERTY_VALUE.getColumnName(),
                                                                                                           methodName,
                                                                                                           this.getClass().getName(),
                                                                                                           symbolicName),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Convert the tables retrieved from the database into an instance properties object.  It may contain
     * effectivity dates and/or attributes.
     *
     * @param instanceTableRow row from the entity, relationship/classification
     * @param instancePropertiesTableRows rows for each version of each attribute
     * @return instance properties object or null
     * @throws RepositoryErrorException there was a mapping problem
     */
    protected InstanceProperties getInstanceProperties(Map<String, JDBCDataValue>       instanceTableRow,
                                                       List<Map<String, JDBCDataValue>> instancePropertiesTableRows) throws RepositoryErrorException
    {
        return this.getInstanceProperties(instanceTableRow, instancePropertiesTableRows, false);
    }


    /**
     * Convert the tables retrieved from the database into an instance properties object.  It may contain
     * effectivity dates and/or unique attributes.
     *
     * @param instanceTableRow row from the entity, relationship/classification
     * @param instancePropertiesTableRows rows for each version of each attribute
     * @return instance properties object or null
     * @throws RepositoryErrorException there was a mapping problem
     */
    protected InstanceProperties getUniqueProperties(Map<String, JDBCDataValue>        instanceTableRow,
                                                     List<Map<String, JDBCDataValue>>  instancePropertiesTableRows) throws RepositoryErrorException
    {
        return this.getInstanceProperties(instanceTableRow, instancePropertiesTableRows, true);
    }


    /**
     * Add the values from the header into a table row.
     *
     * @param instanceTableRow row
     * @param instanceGUID unique identifier of instance
     * @param classificationName optional classification name
     * @param effectiveFromTime effective from (optional)
     * @param effectiveToTime effective to (optional)
     * @param versionEndTime time that this version ended (null for the latest version)
     * @param instanceAuditHeader header info
     * @throws RepositoryErrorException there was a mapping problem
     */
    protected void extractValuesFromInstanceAuditHeader(Map<String, JDBCDataValue> instanceTableRow,
                                                        String                     instanceGUID,
                                                        String                     classificationName,
                                                        Date                       effectiveFromTime,
                                                        Date                       effectiveToTime,
                                                        Date                       versionEndTime,
                                                        InstanceAuditHeader        instanceAuditHeader) throws RepositoryErrorException
    {
        super.setUpStringValueInRow(instanceTableRow, instanceGUID, RepositoryColumn.INSTANCE_GUID.getColumnName(), true);
        if (classificationName != null)
        {
            super.setUpStringValueInRow(instanceTableRow, classificationName, RepositoryColumn.CLASSIFICATION_NAME.getColumnName(), true);
        }

        super.setUpLongValueInRow(instanceTableRow, instanceAuditHeader.getVersion(), RepositoryColumn.VERSION.getColumnName());

        super.setUpStringValueInRow(instanceTableRow, instanceAuditHeader.getType().getTypeDefGUID(), RepositoryColumn.TYPE_GUID.getColumnName(), true);
        this.setUpTypeNameInRow(instanceTableRow, instanceAuditHeader.getType().getTypeDefName());
        super.setUpStringValueInRow(instanceTableRow, instanceAuditHeader.getStatus().getName(), RepositoryColumn.CURRENT_STATUS.getColumnName(), true);
        super.setUpStringValueInRow(instanceTableRow, instanceAuditHeader.getInstanceProvenanceType().getName(), RepositoryColumn.INSTANCE_PROVENANCE_TYPE.getColumnName(), true);
        super.setUpStringValueInRow(instanceTableRow, instanceAuditHeader.getMetadataCollectionId(), RepositoryColumn.METADATA_COLLECTION_GUID.getColumnName(), true);
        super.setUpStringValueInRow(instanceTableRow, instanceAuditHeader.getMetadataCollectionName(), RepositoryColumn.METADATA_COLLECTION_NAME.getColumnName(), false);
        super.setUpSerializableMapValueInRow(instanceTableRow,
                                             instanceAuditHeader.getMappingProperties(),
                                             RepositoryColumn.MAPPING_PROPERTIES.getColumnName(),
                                             false);
        super.setUpDateValueInRow(instanceTableRow, effectiveFromTime, RepositoryColumn.EFFECTIVE_FROM_TIME.getColumnName(), false);
        super.setUpDateValueInRow(instanceTableRow, effectiveToTime, RepositoryColumn.EFFECTIVE_TO_TIME.getColumnName(), false);

        super.setUpStringValueInRow(instanceTableRow, instanceAuditHeader.getReplicatedBy(), RepositoryColumn.REPLICATED_BY.getColumnName(), false);
        super.setUpStringValueInRow(instanceTableRow, instanceAuditHeader.getCreatedBy(), RepositoryColumn.CREATED_BY.getColumnName(), true);
        super.setUpStringValueInRow(instanceTableRow, instanceAuditHeader.getUpdatedBy(), RepositoryColumn.UPDATED_BY.getColumnName(), false);
        super.setUpStringArrayValueInRow(instanceTableRow, instanceAuditHeader.getMaintainedBy(), RepositoryColumn.MAINTAINED_BY.getColumnName(), false);
        super.setUpDateValueInRow(instanceTableRow, instanceAuditHeader.getCreateTime(), RepositoryColumn.CREATE_TIME.getColumnName(), true);
        super.setUpDateValueInRow(instanceTableRow, instanceAuditHeader.getUpdateTime(), RepositoryColumn.UPDATE_TIME.getColumnName(), false);
        if (instanceAuditHeader.getUpdateTime() == null)
        {
            /*
             * This is the first version of the instance so use the created time.  This means date based searches can use the update time.
             */
            super.setUpDateValueInRow(instanceTableRow, instanceAuditHeader.getCreateTime(), RepositoryColumn.VERSION_START_TIME.getColumnName(), true);
        }
        else
        {
            super.setUpDateValueInRow(instanceTableRow, instanceAuditHeader.getUpdateTime(), RepositoryColumn.VERSION_START_TIME.getColumnName(), true);
        }
        super.setUpDateValueInRow(instanceTableRow, versionEndTime, RepositoryColumn.VERSION_END_TIME.getColumnName(), false);
        if (instanceAuditHeader.getStatusOnDelete() != null)
        {
            super.setUpStringValueInRow(instanceTableRow, instanceAuditHeader.getStatusOnDelete().getName(), RepositoryColumn.STATUS_ON_DELETE.getColumnName(), false);
        }
        else
        {
            super.setUpStringValueInRow(instanceTableRow, null, RepositoryColumn.STATUS_ON_DELETE.getColumnName(), false);
        }
    }


    /**
     * Convert a type name into a fully qualified type name.
     *
     * @param instanceTableRow table row
     * @param typeName name of type
     * @throws RepositoryErrorException mapping problem
     */
    private void setUpTypeNameInRow(Map<String, JDBCDataValue> instanceTableRow,
                                    String                     typeName) throws RepositoryErrorException
    {
        List<TypeDefLink> superTypes = repositoryHelper.getSuperTypes(repositoryName, typeName);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(":");
        stringBuilder.append(typeName);

        if (superTypes != null)
        {
            for (TypeDefLink typeDefLink : superTypes)
            {
                stringBuilder.append(":");
                stringBuilder.append(typeDefLink.getName());
            }
        }

        stringBuilder.append(":");

        super.setUpStringValueInRow(instanceTableRow, stringBuilder.toString(), RepositoryColumn.TYPE_NAME.getColumnName(), true);
    }


    /**
     * Add the values from the header into a table row.
     *
     * @param instanceTableRow row
     * @param instanceGUID unique identifier of the instance
     * @param classificationName optional classification name
     * @param effectiveFromTime effective from (optional)
     * @param effectiveToTime effective to (optional)
     * @param versionEndTime time that this version ended (null for the latest version)
     * @param instanceHeader header info
     * @throws RepositoryErrorException there was a mapping problem
     */
    protected void extractValuesFromInstanceHeader(Map<String, JDBCDataValue> instanceTableRow,
                                                   String                     classificationName,
                                                   Date                       effectiveFromTime,
                                                   Date                       effectiveToTime,
                                                   Date                       versionEndTime,
                                                   InstanceHeader             instanceHeader) throws RepositoryErrorException
    {
        final String methodName = "extractValuesFromInstanceHeader";
        final String parameterName = "instanceTableRow";

        if (instanceTableRow != null)
        {
            extractValuesFromInstanceAuditHeader(instanceTableRow,
                                                 instanceHeader.getGUID(),
                                                 classificationName,
                                                 effectiveFromTime,
                                                 effectiveToTime,
                                                 versionEndTime,
                                                 instanceHeader);

            super.setUpStringValueInRow(instanceTableRow, instanceHeader.getInstanceURL(), RepositoryColumn.INSTANCE_URL.getColumnName(), false);
            super.setUpStringValueInRow(instanceTableRow, instanceHeader.getInstanceLicense(), RepositoryColumn.INSTANCE_LICENCE.getColumnName(), false);
            super.setUpStringValueInRow(instanceTableRow, instanceHeader.getReIdentifiedFromGUID(), RepositoryColumn.REIDENTIFIED_FROM_GUID.getColumnName(), false);
        }
        else
        {
            throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition(parameterName,
                                                                                                            methodName,
                                                                                                            this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * Extract the nested attributes from the instance properties structure as table rows for the properties table.
     *
     * @param instanceGUID unique identifier of entity/relationship
     * @param classificationName optional classification name
     * @param version version number of this version of the properties
     * @param typeName unique name of the type for this property
     * @param instanceProperties instance properties (maybe null)
     * @param parentPropertyName parent property name (for nested collection properties) or null
     * @return row information
     * @throws RepositoryErrorException there was a mapping problem
     */
    List<Map<String, JDBCDataValue>> extractValuesFromInstanceProperties(String             instanceGUID,
                                                                         String             classificationName,
                                                                         long               version,
                                                                         String             typeName,
                                                                         InstanceProperties instanceProperties,
                                                                         String             parentPropertyName,
                                                                         String             parentAttributeName) throws RepositoryErrorException
    {
        if ((instanceProperties != null) && (instanceProperties.getPropertyCount() > 0))
        {
            List<Map<String, JDBCDataValue>> attributeRows = new ArrayList<>();
            List<String> uniqueAttributeNames = repositoryHelper.getUniqueAttributesList(repositoryName, typeName);

            for (String propertyName : instanceProperties.getInstanceProperties().keySet())
            {
                InstancePropertyValue instancePropertyValue = instanceProperties.getPropertyValue(propertyName);
                String                qualifiedPropertyName;
                String                attributeName;

                if (parentPropertyName == null)
                {
                    qualifiedPropertyName = propertyName;
                }
                else
                {
                    qualifiedPropertyName = parentPropertyName + ":" + propertyName;
                }

                if (parentAttributeName == null)
                {
                    attributeName = propertyName;
                }
                else
                {
                    attributeName = parentAttributeName;
                }

                /*
                 * Set up the row for this property value
                 */
                Map<String, JDBCDataValue> attributeRow = new HashMap<>();

                super.setUpStringValueInRow(attributeRow, instanceGUID, RepositoryColumn.INSTANCE_GUID.getColumnName(), true);
                if (classificationName != null)
                {
                    super.setUpStringValueInRow(attributeRow, classificationName, RepositoryColumn.CLASSIFICATION_NAME.getColumnName(), true);
                }
                super.setUpLongValueInRow(attributeRow, version, RepositoryColumn.VERSION.getColumnName());
                super.setUpStringValueInRow(attributeRow, qualifiedPropertyName, RepositoryColumn.PROPERTY_NAME.getColumnName(), true);
                super.setUpStringValueInRow(attributeRow, attributeName, RepositoryColumn.ATTRIBUTE_NAME.getColumnName(), true);

                if (instancePropertyValue instanceof PrimitivePropertyValue primitivePropertyValue)
                {
                    if (primitivePropertyValue.getPrimitiveValue() != null)
                    {
                        super.setUpStringValueInRow(attributeRow, primitivePropertyValue.getPrimitiveValue().toString(), RepositoryColumn.PROPERTY_VALUE.getColumnName(), true);
                    }
                    else
                    {
                        super.setUpStringValueInRow(attributeRow, null, RepositoryColumn.PROPERTY_VALUE.getColumnName(), false);
                    }
                    super.setUpStringValueInRow(attributeRow, primitivePropertyValue.getPrimitiveDefCategory().getName(), RepositoryColumn.PROPERTY_CATEGORY.getColumnName(), true);
                }
                else if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                {
                    super.setUpStringValueInRow(attributeRow, enumPropertyValue.getSymbolicName(), RepositoryColumn.PROPERTY_VALUE.getColumnName(), true);
                    super.setUpStringValueInRow(attributeRow, instancePropertyValue.getInstancePropertyCategory().getName(), RepositoryColumn.PROPERTY_CATEGORY.getColumnName(), true);
                }
                else
                {
                    super.setUpStringValueInRow(attributeRow, null, RepositoryColumn.PROPERTY_VALUE.getColumnName(), false);
                    super.setUpStringValueInRow(attributeRow, instancePropertyValue.getInstancePropertyCategory().getName(), RepositoryColumn.PROPERTY_CATEGORY.getColumnName(), true);
                }

                if (uniqueAttributeNames.contains(propertyName))
                {
                    super.setUpBooleanValueInRow(attributeRow, true, RepositoryColumn.IS_UNIQUE_ATTRIBUTE.getColumnName());
                }
                else
                {
                    super.setUpBooleanValueInRow(attributeRow, false, RepositoryColumn.IS_UNIQUE_ATTRIBUTE.getColumnName());
                }

                super.setUpStringValueInRow(attributeRow, instancePropertyValue.getTypeGUID(), RepositoryColumn.ATTRIBUTE_TYPE_GUID.getColumnName(), false);
                super.setUpStringValueInRow(attributeRow, instancePropertyValue.getTypeName(), RepositoryColumn.ATTRIBUTE_TYPE_NAME.getColumnName(), false);

                /*
                 * Set up nested rows
                 */
                if (instancePropertyValue instanceof ArrayPropertyValue arrayPropertyValue)
                {
                    List<Map<String, JDBCDataValue>> arrayRows = extractValuesFromInstanceProperties(instanceGUID,
                                                                                                     classificationName,
                                                                                                     version,
                                                                                                     typeName,
                                                                                                     arrayPropertyValue.getArrayValues(),
                                                                                                     qualifiedPropertyName,
                                                                                                     attributeName);

                    if (arrayRows != null)
                    {
                        attributeRows.addAll(arrayRows);
                    }
                }
                else if (instancePropertyValue instanceof MapPropertyValue mapPropertyValue)
                {
                    List<Map<String, JDBCDataValue>> mapRows = extractValuesFromInstanceProperties(instanceGUID,
                                                                                                   classificationName,
                                                                                                   version,
                                                                                                   typeName,
                                                                                                   mapPropertyValue.getMapValues(),
                                                                                                   qualifiedPropertyName,
                                                                                                   attributeName);

                    if (mapRows != null)
                    {
                        attributeRows.addAll(mapRows);
                    }
                }
                else if (instancePropertyValue instanceof StructPropertyValue structPropertyValue)
                {
                    List<Map<String, JDBCDataValue>> structRows = extractValuesFromInstanceProperties(instanceGUID,
                                                                                                      classificationName,
                                                                                                      version,
                                                                                                      typeName,
                                                                                                      structPropertyValue.getAttributes(),
                                                                                                      qualifiedPropertyName,
                                                                                                      attributeName);

                    if (structRows != null)
                    {
                        attributeRows.addAll(structRows);
                    }
                }

                attributeRows.add(attributeRow);
            }

            if (! attributeRows.isEmpty())
            {
                return attributeRows;
            }
        }

        return null;
    }
}
