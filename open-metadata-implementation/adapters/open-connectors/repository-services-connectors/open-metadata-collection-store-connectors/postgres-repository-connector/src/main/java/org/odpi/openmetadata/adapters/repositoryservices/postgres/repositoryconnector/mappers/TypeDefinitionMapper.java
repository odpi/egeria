/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.mappers.BaseMapper;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryColumn;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryTable;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TypeDefinitionMapper converts between a type definition - a TypeDef or an AttributeTypeDef - and a row in the
 * type_definition table.  The type definition itself is stored as JSON; the other columns describe it so the
 * row can be found and ordered without parsing the JSON.
 * <br><br>
 * The JSON is written and read through the abstract classes TypeDef and AttributeTypeDef rather than the class
 * of the bean.  Their Jackson annotations add a "class" property that names the subclass, which is what allows
 * an EntityDef, RelationshipDef, ClassificationDef or EnumDef to be restored as itself.
 */
public class TypeDefinitionMapper extends BaseMapper
{
    /**
     * Value of the type_category column for a TypeDef.
     */
    public static final String TYPE_DEF_CATEGORY = "TypeDef";

    /**
     * Value of the type_category column for an AttributeTypeDef.
     */
    public static final String ATTRIBUTE_TYPE_DEF_CATEGORY = "AttributeTypeDef";

    /*
     * A row written by a later version of Egeria may carry properties that this version's beans do not have.
     * They are skipped rather than failing the restore, since the properties this version understands are
     * still a usable type definition.  The ObjectMapper is thread-safe once configured, so one is shared.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final String repositoryName;
    private final String typeGUID;
    private final String typeName;
    private final String typeCategory;
    private final long   typeVersion;
    private final String typeDefinitionJSON;
    private final Date   firstStoredTime;
    private final Date   lastStoredTime;


    /**
     * Constructor used to store a TypeDef.
     *
     * @param repositoryName name of this repository
     * @param typeDef type definition to store
     * @param firstStoredTime time when the type definition was first stored - kept from any previous version
     * @param lastStoredTime time when this version is stored
     * @throws RepositoryErrorException the type definition cannot be converted to JSON
     */
    public TypeDefinitionMapper(String  repositoryName,
                                TypeDef typeDef,
                                Date    firstStoredTime,
                                Date    lastStoredTime) throws RepositoryErrorException
    {
        super(repositoryName);

        final String methodName = "TypeDefinitionMapper(TypeDef)";

        this.repositoryName  = repositoryName;
        this.typeGUID        = typeDef.getGUID();
        this.typeName        = typeDef.getName();
        this.typeCategory    = TYPE_DEF_CATEGORY;
        this.typeVersion     = typeDef.getVersion();
        this.firstStoredTime = firstStoredTime;
        this.lastStoredTime  = lastStoredTime;

        try
        {
            this.typeDefinitionJSON = objectMapper.writerFor(TypeDef.class).writeValueAsString(typeDef);
        }
        catch (JsonProcessingException error)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNSTORABLE_TYPE_DEFINITION.getMessageDefinition(repositoryName,
                                                                                                                 typeName,
                                                                                                                 typeGUID,
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }


    /**
     * Constructor used to store an AttributeTypeDef.
     *
     * @param repositoryName name of this repository
     * @param attributeTypeDef attribute type definition to store
     * @param firstStoredTime time when the attribute type definition was first stored - kept from any previous version
     * @param lastStoredTime time when this version is stored
     * @throws RepositoryErrorException the attribute type definition cannot be converted to JSON
     */
    public TypeDefinitionMapper(String           repositoryName,
                                AttributeTypeDef attributeTypeDef,
                                Date             firstStoredTime,
                                Date             lastStoredTime) throws RepositoryErrorException
    {
        super(repositoryName);

        final String methodName = "TypeDefinitionMapper(AttributeTypeDef)";

        this.repositoryName  = repositoryName;
        this.typeGUID        = attributeTypeDef.getGUID();
        this.typeName        = attributeTypeDef.getName();
        this.typeCategory    = ATTRIBUTE_TYPE_DEF_CATEGORY;
        this.typeVersion     = attributeTypeDef.getVersion();
        this.firstStoredTime = firstStoredTime;
        this.lastStoredTime  = lastStoredTime;

        try
        {
            this.typeDefinitionJSON = objectMapper.writerFor(AttributeTypeDef.class).writeValueAsString(attributeTypeDef);
        }
        catch (JsonProcessingException error)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNSTORABLE_TYPE_DEFINITION.getMessageDefinition(repositoryName,
                                                                                                                 typeName,
                                                                                                                 typeGUID,
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }


    /**
     * Constructor used when receiving a row from the type_definition table.
     *
     * @param repositoryName name of this repository
     * @param typeDefinitionRow row from the database
     * @throws RepositoryErrorException a required value is missing from the row
     */
    public TypeDefinitionMapper(String                     repositoryName,
                                Map<String, JDBCDataValue> typeDefinitionRow) throws RepositoryErrorException
    {
        super(repositoryName);

        this.repositoryName     = repositoryName;
        this.typeGUID           = super.getStringPropertyFromColumn(RepositoryColumn.TYPE_GUID.getColumnName(), typeDefinitionRow, true);
        this.typeName           = super.getStringPropertyFromColumn(RepositoryColumn.TYPE_NAME.getColumnName(), typeDefinitionRow, true);
        this.typeCategory       = super.getStringPropertyFromColumn(RepositoryColumn.TYPE_CATEGORY.getColumnName(), typeDefinitionRow, true);
        this.typeVersion        = super.getLongPropertyFromColumn(RepositoryColumn.TYPE_VERSION.getColumnName(), typeDefinitionRow, true);
        this.typeDefinitionJSON = super.getStringPropertyFromColumn(RepositoryColumn.TYPE_DEFINITION.getColumnName(), typeDefinitionRow, true);
        this.firstStoredTime    = super.getDatePropertyFromColumn(RepositoryColumn.FIRST_STORED_TIME.getColumnName(), typeDefinitionRow, true);
        this.lastStoredTime     = super.getDatePropertyFromColumn(RepositoryColumn.LAST_STORED_TIME.getColumnName(), typeDefinitionRow, true);
    }


    /**
     * Return the unique identifier of the stored type definition.
     *
     * @return guid
     */
    public String getTypeGUID()
    {
        return typeGUID;
    }


    /**
     * Return the unique name of the stored type definition.
     *
     * @return name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return whether the row holds a TypeDef or an AttributeTypeDef - see TYPE_DEF_CATEGORY and
     * ATTRIBUTE_TYPE_DEF_CATEGORY.
     *
     * @return category name
     */
    public String getTypeCategory()
    {
        return typeCategory;
    }


    /**
     * Return the version number of the stored type definition.
     *
     * @return long
     */
    public long getTypeVersion()
    {
        return typeVersion;
    }


    /**
     * Return the time when the type definition was first stored.
     *
     * @return date
     */
    public Date getFirstStoredTime()
    {
        return firstStoredTime;
    }


    /**
     * Return the time when this version of the type definition was stored.
     *
     * @return date
     */
    public Date getLastStoredTime()
    {
        return lastStoredTime;
    }


    /**
     * Return whether the row holds a TypeDef (rather than an AttributeTypeDef).
     *
     * @return boolean
     */
    public boolean isTypeDef()
    {
        return TYPE_DEF_CATEGORY.equals(typeCategory);
    }


    /**
     * Return whether the row holds an AttributeTypeDef (rather than a TypeDef).
     *
     * @return boolean
     */
    public boolean isAttributeTypeDef()
    {
        return ATTRIBUTE_TYPE_DEF_CATEGORY.equals(typeCategory);
    }


    /**
     * Restore the stored TypeDef from its JSON.
     *
     * @return type definition
     * @throws RepositoryErrorException the JSON does not describe a TypeDef
     */
    public TypeDef getTypeDef() throws RepositoryErrorException
    {
        final String methodName = "getTypeDef";

        try
        {
            return objectMapper.readValue(typeDefinitionJSON, TypeDef.class);
        }
        catch (JsonProcessingException error)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNREADABLE_TYPE_DEFINITION.getMessageDefinition(repositoryName,
                                                                                                                 typeName,
                                                                                                                 typeGUID,
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }


    /**
     * Restore the stored AttributeTypeDef from its JSON.
     *
     * @return attribute type definition
     * @throws RepositoryErrorException the JSON does not describe an AttributeTypeDef
     */
    public AttributeTypeDef getAttributeTypeDef() throws RepositoryErrorException
    {
        final String methodName = "getAttributeTypeDef";

        try
        {
            return objectMapper.readValue(typeDefinitionJSON, AttributeTypeDef.class);
        }
        catch (JsonProcessingException error)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNREADABLE_TYPE_DEFINITION.getMessageDefinition(repositoryName,
                                                                                                                 typeName,
                                                                                                                 typeGUID,
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }


    /**
     * Return the row to insert into the type_definition table.
     *
     * @return row
     * @throws RepositoryErrorException a required value is missing
     */
    public Map<String, JDBCDataValue> getTypeDefinitionRow() throws RepositoryErrorException
    {
        Map<String, JDBCDataValue> typeDefinitionRow = new HashMap<>();

        super.setUpStringValueInRow(typeDefinitionRow, typeGUID, RepositoryColumn.TYPE_GUID.getColumnName(), true);
        super.setUpStringValueInRow(typeDefinitionRow, typeName, RepositoryColumn.TYPE_NAME.getColumnName(), true);
        super.setUpStringValueInRow(typeDefinitionRow, typeCategory, RepositoryColumn.TYPE_CATEGORY.getColumnName(), true);
        super.setUpLongValueInRow(typeDefinitionRow, typeVersion, RepositoryColumn.TYPE_VERSION.getColumnName());
        super.setUpStringValueInRow(typeDefinitionRow, typeDefinitionJSON, RepositoryColumn.TYPE_DEFINITION.getColumnName(), true);
        super.setUpDateValueInRow(typeDefinitionRow, firstStoredTime, RepositoryColumn.FIRST_STORED_TIME.getColumnName(), true);
        super.setUpDateValueInRow(typeDefinitionRow, lastStoredTime, RepositoryColumn.LAST_STORED_TIME.getColumnName(), true);

        return typeDefinitionRow;
    }


    /**
     * Standard toString method.  The JSON is left out since it can be long.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "TypeDefinitionMapper{" +
                "table=" + RepositoryTable.TYPE_DEFINITION.getTableName() +
                ", typeGUID='" + typeGUID + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeCategory='" + typeCategory + '\'' +
                ", typeVersion=" + typeVersion +
                ", firstStoredTime=" + firstStoredTime +
                ", lastStoredTime=" + lastStoredTime +
                '}';
    }
}
