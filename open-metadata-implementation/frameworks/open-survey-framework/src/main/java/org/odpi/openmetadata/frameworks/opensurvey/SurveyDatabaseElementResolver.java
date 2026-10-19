/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SurveyDatabaseElementResolver finds the catalogued element that a database survey annotation describes, so
 * that the annotation's AssociatedAnnotation relationship links to the schema, table or column it measures
 * rather than to the surveyed asset.
 * <br><br>
 * The catalogued elements are found by following relationships down from the surveyed asset and matching on the
 * name the database uses: resourceName for a database, which the database templates set to the database name
 * (or displayName for a database catalogued without a resourceName), and displayName for schemas, tables and
 * columns, which the JDBC cataloguer sets to their names.  This means
 * it does not depend on how the qualified names of the catalogued elements were formed.  The path followed is:
 * <ul>
 *     <li>SoftwareServer - SupportedSoftwareCapability - capability - CapabilityAssetUse - RelationalDatabase
 *         (only when the surveyed asset is a database server)</li>
 *     <li>RelationalDatabase - DataSetContent - DeployedDatabaseSchema</li>
 *     <li>DeployedDatabaseSchema - Schema - schema type - AttributeForSchema - RelationalTable</li>
 *     <li>RelationalTable - NestedSchemaAttribute - RelationalColumn</li>
 * </ul>
 * If the element an annotation describes has not been catalogued, the annotation is linked to the surveyed
 * asset.  A survey produces its annotations database by database, schema by schema and table by table, so only
 * the children of the most recently used database, schema and table are cached.
 */
public class SurveyDatabaseElementResolver
{
    private static final int pageSize = 100;

    private final OpenMetadataStore openMetadataStore;
    private final String            surveyedAssetGUID;
    private final boolean           surveyedAssetIsServer;
    private final PropertyHelper    propertyHelper = new PropertyHelper();

    private Map<String, String> databases = null;

    private String              schemasLoadedFor = null;
    private Map<String, String> schemas          = null;

    private String              tablesLoadedFor = null;
    private Map<String, String> tables          = null;

    private String              columnsLoadedFor = null;
    private Map<String, String> columns          = null;


    /**
     * Constructor.
     *
     * @param openMetadataStore access to the metadata
     * @param surveyedAssetGUID unique identifier of the surveyed asset
     * @param surveyedAssetIsServer true if the surveyed asset is a database server; false if it is a database
     */
    private SurveyDatabaseElementResolver(OpenMetadataStore openMetadataStore,
                                          String            surveyedAssetGUID,
                                          boolean           surveyedAssetIsServer)
    {
        this.openMetadataStore     = openMetadataStore;
        this.surveyedAssetGUID     = surveyedAssetGUID;
        this.surveyedAssetIsServer = surveyedAssetIsServer;
    }


    /**
     * Return a resolver for a survey of a single database.  The database annotations are linked to the
     * surveyed asset, and the database name in each annotation is ignored.
     *
     * @param openMetadataStore access to the metadata
     * @param databaseGUID unique identifier of the surveyed database asset
     * @return resolver
     */
    public static SurveyDatabaseElementResolver forDatabase(OpenMetadataStore openMetadataStore,
                                                            String            databaseGUID)
    {
        return new SurveyDatabaseElementResolver(openMetadataStore, databaseGUID, false);
    }


    /**
     * Return a resolver for a survey of a database server, whose annotations cover many databases.
     *
     * @param openMetadataStore access to the metadata
     * @param serverGUID unique identifier of the surveyed software server asset
     * @return resolver
     */
    public static SurveyDatabaseElementResolver forServer(OpenMetadataStore openMetadataStore,
                                                          String            serverGUID)
    {
        return new SurveyDatabaseElementResolver(openMetadataStore, serverGUID, true);
    }


    /**
     * Return the unique identifier of the catalogued element that the annotation describes, or the surveyed
     * asset if that element has not been catalogued.
     *
     * @param annotation annotation and the names of the resource it describes
     * @return element unique identifier
     * @throws InvalidParameterException a parameter is invalid
     * @throws UserNotAuthorizedException the connector is not able to access the elements
     * @throws PropertyServerException a problem accessing the metadata store
     */
    public String getDescribedElementGUID(DatabaseElementAnnotation annotation) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        String databaseGUID = this.getDatabaseGUID(annotation.databaseName());

        if (databaseGUID == null)
        {
            return surveyedAssetGUID;
        }

        if (annotation.schemaName() == null)
        {
            return databaseGUID;
        }

        String schemaGUID = this.getSchemaGUID(databaseGUID, annotation.schemaName());

        if (schemaGUID == null)
        {
            return surveyedAssetGUID;
        }

        if (annotation.tableName() == null)
        {
            return schemaGUID;
        }

        String tableGUID = this.getTableGUID(schemaGUID, annotation.tableName());

        if (tableGUID == null)
        {
            return surveyedAssetGUID;
        }

        if (annotation.columnName() == null)
        {
            return tableGUID;
        }

        String columnGUID = this.getColumnGUID(tableGUID, annotation.columnName());

        if (columnGUID == null)
        {
            return surveyedAssetGUID;
        }

        return columnGUID;
    }


    /**
     * Return the catalogued database with the requested name.  For a database survey, this is the surveyed asset.
     *
     * @param databaseName name of the database
     * @return unique identifier or null if not catalogued
     * @throws InvalidParameterException a parameter is invalid
     * @throws UserNotAuthorizedException the connector is not able to access the elements
     * @throws PropertyServerException a problem accessing the metadata store
     */
    private String getDatabaseGUID(String databaseName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        if (! surveyedAssetIsServer)
        {
            return surveyedAssetGUID;
        }

        if (databases == null)
        {
            databases = new HashMap<>();

            for (OpenMetadataElement capability : this.getRelatedElements(surveyedAssetGUID,
                                                                          OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                                                          OpenMetadataType.SOFTWARE_CAPABILITY.typeName))
            {
                this.addElementsByName(databases,
                                       capability.getElementGUID(),
                                       OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                       OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                       OpenMetadataProperty.RESOURCE_NAME.name,
                                       OpenMetadataProperty.DISPLAY_NAME.name);
            }
        }

        return databases.get(databaseName);
    }


    /**
     * Return the catalogued schema with the requested name within a database.
     *
     * @param databaseGUID unique identifier of the database
     * @param schemaName name of the schema
     * @return unique identifier or null if not catalogued
     * @throws InvalidParameterException a parameter is invalid
     * @throws UserNotAuthorizedException the connector is not able to access the elements
     * @throws PropertyServerException a problem accessing the metadata store
     */
    private String getSchemaGUID(String databaseGUID,
                                 String schemaName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        if (! databaseGUID.equals(schemasLoadedFor))
        {
            schemas = new HashMap<>();

            this.addElementsByName(schemas,
                                   databaseGUID,
                                   OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                   OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                   null);

            schemasLoadedFor = databaseGUID;
        }

        return schemas.get(schemaName);
    }


    /**
     * Return the catalogued table or view with the requested name within a schema.
     *
     * @param schemaGUID unique identifier of the schema asset
     * @param tableName name of the table or view
     * @return unique identifier or null if not catalogued
     * @throws InvalidParameterException a parameter is invalid
     * @throws UserNotAuthorizedException the connector is not able to access the elements
     * @throws PropertyServerException a problem accessing the metadata store
     */
    private String getTableGUID(String schemaGUID,
                                String tableName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        if (! schemaGUID.equals(tablesLoadedFor))
        {
            tables = new HashMap<>();

            for (OpenMetadataElement schemaType : this.getRelatedElements(schemaGUID,
                                                                          OpenMetadataType.SCHEMA_RELATIONSHIP.typeName,
                                                                          OpenMetadataType.SCHEMA_TYPE.typeName))
            {
                this.addElementsByName(tables,
                                       schemaType.getElementGUID(),
                                       OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                       OpenMetadataType.RELATIONAL_TABLE.typeName,
                                       OpenMetadataProperty.DISPLAY_NAME.name,
                                       null);
            }

            tablesLoadedFor = schemaGUID;
        }

        return tables.get(tableName);
    }


    /**
     * Return the catalogued column with the requested name within a table.
     *
     * @param tableGUID unique identifier of the table
     * @param columnName name of the column
     * @return unique identifier or null if not catalogued
     * @throws InvalidParameterException a parameter is invalid
     * @throws UserNotAuthorizedException the connector is not able to access the elements
     * @throws PropertyServerException a problem accessing the metadata store
     */
    private String getColumnGUID(String tableGUID,
                                 String columnName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        if (! tableGUID.equals(columnsLoadedFor))
        {
            columns = new HashMap<>();

            this.addElementsByName(columns,
                                   tableGUID,
                                   OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                   OpenMetadataType.RELATIONAL_COLUMN.typeName,
                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                   null);

            columnsLoadedFor = tableGUID;
        }

        return columns.get(columnName);
    }


    /**
     * Add the elements of the requested type that are linked to the starting element to a map of name to
     * unique identifier.
     *
     * @param elementsByName map to add to
     * @param startingGUID element to start from
     * @param relationshipTypeName relationship to follow
     * @param elementTypeName type of the elements to return
     * @param namePropertyName property that holds the name the database uses for the element
     * @param fallbackPropertyName property to use when the element has no value for namePropertyName (may be null)
     * @throws InvalidParameterException a parameter is invalid
     * @throws UserNotAuthorizedException the connector is not able to access the elements
     * @throws PropertyServerException a problem accessing the metadata store
     */
    private void addElementsByName(Map<String, String> elementsByName,
                                   String              startingGUID,
                                   String              relationshipTypeName,
                                   String              elementTypeName,
                                   String              namePropertyName,
                                   String              fallbackPropertyName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "addElementsByName";

        for (OpenMetadataElement element : this.getRelatedElements(startingGUID, relationshipTypeName, elementTypeName))
        {
            String name = propertyHelper.getStringProperty(surveyedAssetGUID,
                                                           namePropertyName,
                                                           element.getElementProperties(),
                                                           methodName);

            if ((name == null) && (fallbackPropertyName != null))
            {
                name = propertyHelper.getStringProperty(surveyedAssetGUID,
                                                        fallbackPropertyName,
                                                        element.getElementProperties(),
                                                        methodName);
            }

            if (name != null)
            {
                elementsByName.put(name, element.getElementGUID());
            }
        }
    }


    /**
     * Return all elements of the requested type that are linked to the starting element by the relationship,
     * whichever end they are at.
     *
     * @param startingGUID element to start from
     * @param relationshipTypeName relationship to follow
     * @param elementTypeName type of the elements to return (subtypes are included)
     * @return list of elements (may be empty)
     * @throws InvalidParameterException a parameter is invalid
     * @throws UserNotAuthorizedException the connector is not able to access the elements
     * @throws PropertyServerException a problem accessing the metadata store
     */
    private List<OpenMetadataElement> getRelatedElements(String startingGUID,
                                                         String relationshipTypeName,
                                                         String elementTypeName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        List<OpenMetadataElement> results = new ArrayList<>();

        int startFrom = 0;

        while (true)
        {
            RelatedMetadataElementList page = openMetadataStore.getRelatedMetadataElements(startingGUID,
                                                                                           0,
                                                                                           relationshipTypeName,
                                                                                           startFrom,
                                                                                           pageSize);

            if ((page == null) || (page.getElementList() == null))
            {
                break;
            }

            for (RelatedMetadataElement relatedElement : page.getElementList())
            {
                if ((relatedElement != null) &&
                        (relatedElement.getElement() != null) &&
                        (propertyHelper.isTypeOf(relatedElement.getElement(), elementTypeName)))
                {
                    results.add(relatedElement.getElement());
                }
            }

            if (page.getElementList().size() < pageSize)
            {
                break;
            }

            startFrom = startFrom + pageSize;
        }

        return results;
    }
}
