/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ElementHeader;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ElementOrigin;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ElementType;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.Classification;
import org.odpi.openmetadata.accessservices.datamanager.properties.DataItemSortOrder;
import org.odpi.openmetadata.accessservices.datamanager.properties.KeyPattern;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SoftwareServerCapabilityMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * DataManagerOCFBeanCloner converts OCF Beans to Data Manager OMAS beans.
 */
public class DataManagerOCFBeanCloner
{
    private  DataManagerInstanceHandler instanceHandler;

    /**
     * Constructor
     *
     * @param instanceHandler REST instance handler
     */
    public DataManagerOCFBeanCloner(DataManagerInstanceHandler instanceHandler)
    {
        this.instanceHandler = instanceHandler;
    }


    /**
     * Perform conversion between types.
     *
     * @param ownerCategory data manager version of enum
     * @return OCF version of enum
     */
    OwnerType getOwnerType(OwnerCategory ownerCategory)
    {
        OwnerType ownerType = OwnerType.OTHER;

        if (ownerCategory == OwnerCategory.USER_ID)
        {
            ownerType = OwnerType.USER_ID;
        }
        else if (ownerCategory == OwnerCategory.PROFILE_ID)
        {
            ownerType = OwnerType.PROFILE_ID;
        }

        return ownerType;
    }


    /**
     * Perform conversion between types.
     *
     * @param ownerType OCF version of enum
     * @return data manager version of enum
     */
    private OwnerCategory getOwnerCategory(OwnerType ownerType)
    {
        OwnerCategory ownerCategory = OwnerCategory.OTHER;

        if (ownerType == OwnerType.USER_ID)
        {
            ownerCategory = OwnerCategory.USER_ID;
        }
        else if (ownerType == OwnerType.PROFILE_ID)
        {
            ownerCategory = OwnerCategory.PROFILE_ID;
        }

        return ownerCategory;
    }


    /**
     * Perform conversion between types.
     *
     * @param ocfClassifications classifications used by the data manager OMAS
     * @return classifications used by the OCF
     */
    private List<Classification> getDataManagerClassifications(List<org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification> ocfClassifications)
    {
        List<Classification> dataManagerClassifications = null;

        if (ocfClassifications != null)
        {
            dataManagerClassifications = new ArrayList<>();

            for (org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification ocfClassification : ocfClassifications)
            {
                if (ocfClassification != null)
                {
                    Classification dataManagerClassification = this.getDataManagerClassification(ocfClassification);

                    if (dataManagerClassification != null)
                    {
                        dataManagerClassifications.add(dataManagerClassification);
                    }
                }
            }

            if (dataManagerClassifications.isEmpty())
            {
                dataManagerClassifications = null;
            }
        }

        return dataManagerClassifications;
    }


    /**
     * Perform conversion between types.
     *
     * @param ocfClassification classification used by the OCF
     * @return classification used by the data manager OMAS
     */
    private Classification getDataManagerClassification(org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification ocfClassification)
    {
        if (ocfClassification != null)
        {
            Classification dataManagerClassification = new Classification();

            dataManagerClassification.setClassificationName(ocfClassification.getClassificationName());
            dataManagerClassification.setClassificationProperties(ocfClassification.getClassificationProperties());

            return dataManagerClassification;
        }

        return null;
    }


    /**
     * Retrieve the requested classification from the retrieved list, copying the other classifications into a new list.
     * This is used when an element that is stored as a classification is actually returned as a first class object.
     *
     * @param requestedClassificationName name of the desired classification
     * @param retrievedClassifications list of classification retrieved from the repository
     * @return requested classification or null if it is not present
     */
    private Classification extractClassification(String               requestedClassificationName,
                                                 List<Classification> retrievedClassifications)
    {
        Classification       requestedClassification = null;

        if (retrievedClassifications != null)
        {
            for (Classification retrievedClassification : retrievedClassifications)
            {
                if (retrievedClassification != null)
                {
                    if (requestedClassificationName.equals(retrievedClassification.getClassificationName()))
                    {
                        requestedClassification = retrievedClassification;
                    }
                }
            }
        }

        return requestedClassification;
    }


    /**
     * Remove the requested classification from the retrieved list.
     * This is used when an element that is stored as a classification is actually returned as a first class object.
     *
     * @param requestedClassificationName name of the desired classification
     * @param retrievedClassifications list of classification retrieved from the repository
     * @return requested classification or null if it is not present
     */
    private List<Classification> removeClassification(String               requestedClassificationName,
                                                      List<Classification> retrievedClassifications)
    {
        List<Classification>     newClassifications = null;

        if (retrievedClassifications != null)
        {
            newClassifications = new ArrayList<>();

            for (Classification retrievedClassification : retrievedClassifications)
            {
                if (retrievedClassification != null)
                {
                    if (! requestedClassificationName.equals(retrievedClassification.getClassificationName()))
                    {
                        newClassifications.add(retrievedClassification);
                    }
                }
            }

            if (newClassifications.isEmpty())
            {
                newClassifications = null;
            }
        }

        return newClassifications;
    }


    /**
     * Return a file system element from a SoftwareServerCapability OCF bean.
     *
     * @param capability ocf bean
     * @param userId calling user
     * @param serverName active server
     * @param methodName calling method
     *
     * @return FileSystemElement
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    FileSystemElement getFileSystemFromCapability(SoftwareServerCapability capability,
                                                  String                   userId,
                                                  String                   serverName,
                                                  String                   methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        FileSystemElement fileSystem = null;

        if (capability != null)
        {
            fileSystem = new FileSystemElement();

            setReferenceableProperties(fileSystem, capability);

            fileSystem.setDisplayName(capability.getDisplayName());

            // todo
        }

        return fileSystem;
    }


    /**
     * Return a file folder element from an Asset OCF bean.
     *
     * @param asset ocf bean
     * @param userId calling user
     * @param serverName active server
     * @param methodName calling method
     *
     * @return FileFolderElement
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    FileFolderElement getFileFolderFromAsset(Asset   asset,
                                             String  userId,
                                             String  serverName,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        FileFolderElement fileFolder = null;

        if (asset != null)
        {
            fileFolder = new FileFolderElement();

            setReferenceableProperties(fileFolder, asset);

            fileFolder.setDisplayName(asset.getDisplayName());

            // todo
        }

        return fileFolder;
    }


    /**
     * Return a data file element from an Asset OCF bean.
     *
     * @param asset ocf bean
     * @param userId calling user
     * @param serverName active server
     * @param methodName calling method
     *
     * @return DataFileElement
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    DataFileElement getDataFileFromAsset(Asset  asset,
                                         String userId,
                                         String serverName,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        DataFileElement element = null;

        if (asset != null)
        {
            element = new DataFileElement();

            setReferenceableProperties(element, asset);

            element.setDisplayName(asset.getDisplayName());

            Map<String, Object>  extendedProperties = asset.getExtendedProperties();
            if (extendedProperties != null)
            {
                if (extendedProperties.get(AssetMapper.FILE_TYPE_PROPERTY_NAME) != null)
                {
                    element.setFileType(extendedProperties.get(AssetMapper.FILE_TYPE_PROPERTY_NAME).toString());
                }
            }

            // todo
        }

        return element;
    }


    /**
     * Retrieve the list of Data Manager OMAS database elements from a list of OCF Asset Beans.
     *
     * @param assets OCF Asset bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<DatabaseElement> getDatabasesFromAssets(List<Asset> assets,
                                                 String      userId,
                                                 String      serverName,
                                                 String      methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        List<DatabaseElement> databaseElements = null;

        if (assets != null)
        {
            databaseElements = new ArrayList<>();

            for (Asset asset: assets)
            {
                databaseElements.add(this.getDatabaseFromAsset(asset, userId, serverName, methodName));
            }
        }

        return databaseElements;
    }


    /**
     * Retrieve the Data Manager OMAS database element from an OCF Bean.
     *
     * @param asset OCF Asset bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    DatabaseElement getDatabaseFromAsset(Asset  asset,
                                         String userId,
                                         String serverName,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        DatabaseElement element = null;

        if (asset != null)
        {
            element = new DatabaseElement();

            element.setElementHeader(this.getElementHeaderFromBean(asset, serverName));
            this.setAssetProperties(element, asset);

            Map<String, Object>  extendedProperties = asset.getExtendedProperties();
            if (extendedProperties != null)
            {
                if (extendedProperties.get(AssetMapper.CREATE_TIME_PROPERTY_NAME) != null)
                {
                    element.setCreateTime((Date)extendedProperties.get(AssetMapper.CREATE_TIME_PROPERTY_NAME));
                }
                if (extendedProperties.get(AssetMapper.MODIFIED_TIME_PROPERTY_NAME) != null)
                {
                    element.setModifiedTime((Date)extendedProperties.get(AssetMapper.MODIFIED_TIME_PROPERTY_NAME));
                }
                if (extendedProperties.get(AssetMapper.ENCODING_TYPE_PROPERTY_NAME) != null)
                {
                    element.setEncodingType(extendedProperties.get(AssetMapper.ENCODING_TYPE_PROPERTY_NAME).toString());
                }
                if (extendedProperties.get(AssetMapper.ENCODING_LANGUAGE_PROPERTY_NAME) != null)
                {
                    element.setEncodingLanguage(extendedProperties.get(AssetMapper.ENCODING_LANGUAGE_PROPERTY_NAME).toString());
                }
                if (extendedProperties.get(AssetMapper.ENCODING_DESCRIPTION_PROPERTY_NAME) != null)
                {
                    element.setEncodingDescription(extendedProperties.get(AssetMapper.ENCODING_DESCRIPTION_PROPERTY_NAME).toString());
                }

                if (extendedProperties.get(AssetMapper.DATABASE_TYPE_PROPERTY_NAME) != null)
                {
                    element.setDatabaseType(extendedProperties.get(AssetMapper.DATABASE_TYPE_PROPERTY_NAME).toString());
                }
                if (extendedProperties.get(AssetMapper.DATABASE_VERSION_PROPERTY_NAME) != null)
                {
                    element.setDatabaseVersion(extendedProperties.get(AssetMapper.DATABASE_VERSION_PROPERTY_NAME).toString());
                }
                if (extendedProperties.get(AssetMapper.DATABASE_INSTANCE_PROPERTY_NAME) != null)
                {
                    element.setDatabaseInstance(extendedProperties.get(AssetMapper.DATABASE_INSTANCE_PROPERTY_NAME).toString());
                }
                if (extendedProperties.get(AssetMapper.DATABASE_IMPORTED_FROM_PROPERTY_NAME) != null)
                {
                    element.setDatabaseImportedFrom(extendedProperties.get(AssetMapper.DATABASE_IMPORTED_FROM_PROPERTY_NAME).toString());
                }
            }

            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            element.setVendorProperties(handler.getVendorProperties(userId,
                                                                    asset.getGUID(),
                                                                    asset.getQualifiedName(),
                                                                    methodName));
        }

        return element;
    }


    /**
     * Retrieve the list of Data Manager OMAS database schema elements from a list of OCF Asset Beans.
     *
     * @param assets OCF Asset bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database schema metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<DatabaseSchemaElement> getDatabaseSchemasFromAssets(List<Asset> assets,
                                                             String      userId,
                                                             String      serverName,
                                                             String      methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        List<DatabaseSchemaElement> databaseSchemaElements = null;

        if (assets != null)
        {
            databaseSchemaElements = new ArrayList<>();

            for (Asset asset: assets)
            {
                databaseSchemaElements.add(this.getDatabaseSchemaFromAsset(asset, userId, serverName, methodName));
            }
        }

        return databaseSchemaElements;
    }


    /**
     * Retrieve the Data Manager OMAS database schema element from an OCF Bean.
     *
     * @param asset OCF Asset bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database schema metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    DatabaseSchemaElement getDatabaseSchemaFromAsset(Asset  asset,
                                                     String userId,
                                                     String serverName,
                                                     String methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        DatabaseSchemaElement element = null;

        if (asset != null)
        {
            element = new DatabaseSchemaElement();

            element.setElementHeader(this.getElementHeaderFromBean(asset, serverName));
            this.setAssetProperties(element, asset);

            element.setExtendedProperties(asset.getExtendedProperties());

            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            element.setVendorProperties(handler.getVendorProperties(userId,
                                                                    asset.getGUID(),
                                                                    asset.getQualifiedName(),
                                                                    methodName));
        }

        return element;
    }


    /**
     * Set up the common properties for an asset.
     *
     * @param assetPropertiesBean target Data Manager OMAS bean
     * @param assetBean OCF bean
     */
    private void setAssetProperties(AssetProperties   assetPropertiesBean,
                                    Asset             assetBean)
    {
        if (assetPropertiesBean != null)
        {
            setReferenceableProperties(assetPropertiesBean, assetBean);

            assetPropertiesBean.setDisplayName(assetBean.getDisplayName());
            assetPropertiesBean.setDescription(assetBean.getDescription());
            assetPropertiesBean.setOwner(assetBean.getOwner());
            assetPropertiesBean.setOwnerCategory(this.getOwnerCategory(assetBean.getOwnerType()));
            assetPropertiesBean.setZoneMembership(assetBean.getZoneMembership());
            assetPropertiesBean.setOrigin(assetBean.getOrigin());
            assetPropertiesBean.setLatestChange(assetBean.getLatestChange());
        }
    }


    /**
     * Retrieve the list of Data Manager OMAS database table elements from a list of OCF SchemaAttribute Beans.
     *
     * @param schemaAttributes OCF SchemaAttribute beans
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database table metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<DatabaseTableElement> getDatabaseTablesFromAttributes(List<SchemaAttribute> schemaAttributes,
                                                               String                userId,
                                                               String                serverName,
                                                               String                methodName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        List<DatabaseTableElement> databaseTableElements = null;

        if (schemaAttributes != null)
        {
            databaseTableElements = new ArrayList<>();

            for (SchemaAttribute schemaAttribute: schemaAttributes)
            {
                databaseTableElements.add(this.getDatabaseTableFromAttribute(schemaAttribute, userId, serverName, methodName));
            }
        }

        return databaseTableElements;
    }


    /**
     * Retrieve the Data Manager OMAS database table element from an OCF Bean.
     *
     * @param schemaAttribute OCF SchemaAttribute bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database table metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    DatabaseTableElement getDatabaseTableFromAttribute(SchemaAttribute schemaAttribute,
                                                       String          userId,
                                                       String          serverName,
                                                       String          methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        DatabaseTableElement element = null;

        if (schemaAttribute != null)
        {
            element = new DatabaseTableElement();

            element.setElementHeader(this.getElementHeaderFromBean(schemaAttribute, serverName));
            this.setSchemaAttributeProperties(element, schemaAttribute);

            element.setExtendedProperties(schemaAttribute.getExtendedProperties());

            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            element.setVendorProperties(handler.getVendorProperties(userId,
                                                                    schemaAttribute.getGUID(),
                                                                    schemaAttribute.getQualifiedName(),
                                                                    methodName));
        }

        return element;
    }


    /**
     * Retrieve the list of Data Manager OMAS database table elements from a list of OCF SchemaAttribute Beans.
     *
     * @param schemaAttributes OCF SchemaAttribute beans
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database view metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<DatabaseViewElement> getDatabaseViewsFromAttributes(List<SchemaAttribute> schemaAttributes,
                                                             String                userId,
                                                             String                serverName,
                                                             String                methodName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        List<DatabaseViewElement> databaseViewElements = null;

        if (schemaAttributes != null)
        {
            databaseViewElements = new ArrayList<>();

            for (SchemaAttribute schemaAttribute: schemaAttributes)
            {
                databaseViewElements.add(this.getDatabaseViewFromAttribute(schemaAttribute, userId, serverName, methodName));
            }
        }

        return databaseViewElements;
    }


    /**
     * Retrieve the Data Manager OMAS database table element from an OCF Bean.
     *
     * @param schemaAttribute OCF SchemaAttribute bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database view metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    DatabaseViewElement getDatabaseViewFromAttribute(SchemaAttribute schemaAttribute,
                                                     String          userId,
                                                     String          serverName,
                                                     String          methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        DatabaseViewElement element = null;

        if (schemaAttribute != null)
        {
            element = new DatabaseViewElement();

            ElementHeader elementHeader = this.getElementHeaderFromBean(schemaAttribute, serverName);

            Classification relationalViewClassification = this.extractClassification(SchemaElementMapper.RELATIONAL_VIEW_CLASSIFICATION_TYPE_NAME,
                                                                                     elementHeader.getClassifications());

            if (relationalViewClassification != null)
            {
                elementHeader.setClassifications(this.removeClassification(SchemaElementMapper.RELATIONAL_VIEW_CLASSIFICATION_TYPE_NAME,
                                                                           elementHeader.getClassifications()));

                Map<String, Object>  properties = relationalViewClassification.getClassificationProperties();
                if (properties != null)
                {
                    Object expressionObject = properties.get(SchemaElementMapper.EXPRESSION_PROPERTY_NAME);
                    if (expressionObject != null)
                    {
                        element.setExpression(expressionObject.toString());
                    }
                }
            }

            element.setElementHeader(elementHeader);

            this.setSchemaAttributeProperties(element, schemaAttribute);
            element.setExtendedProperties(schemaAttribute.getExtendedProperties());

            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            element.setVendorProperties(handler.getVendorProperties(userId,
                                                                    schemaAttribute.getGUID(),
                                                                    schemaAttribute.getQualifiedName(),
                                                                    methodName));
        }

        return element;
    }


    /**
     * Retrieve the list of Data Manager OMAS database column elements from a list of OCF SchemaAttribute Beans.
     *
     * @param schemaAttributes OCF SchemaAttribute beans
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database column metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<DatabaseColumnElement> getDatabaseColumnsFromAttributes(List<SchemaAttribute> schemaAttributes,
                                                                 String                userId,
                                                                 String                serverName,
                                                                 String                methodName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        List<DatabaseColumnElement> databaseColumnElements = null;

        if (schemaAttributes != null)
        {
            databaseColumnElements = new ArrayList<>();

            for (SchemaAttribute schemaAttribute: schemaAttributes)
            {
                databaseColumnElements.add(this.getDatabaseColumnFromAttribute(schemaAttribute, userId, serverName, methodName));
            }
        }

        return databaseColumnElements;
    }


    /**
     * Retrieve the Data Manager OMAS database column element from an OCF Bean.
     *
     * @param schemaAttribute OCF SchemaAttribute bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database column metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    DatabaseColumnElement getDatabaseColumnFromAttribute(SchemaAttribute schemaAttribute,
                                                         String          userId,
                                                         String          serverName,
                                                         String          methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        DatabaseColumnElement element = null;

        if (schemaAttribute != null)
        {
            element = new DatabaseColumnElement();

            if (schemaAttribute instanceof DerivedSchemaAttribute)
            {
                element.setFormula(((DerivedSchemaAttribute) schemaAttribute).getFormula());
                element.setQueries(this.getDatabaseSchemaQueries(((DerivedSchemaAttribute) schemaAttribute).getQueries()));
            }
            else
            {
                SchemaType attributeType = schemaAttribute.getAttributeType();

                if (attributeType != null)
                {
                    if (attributeType instanceof PrimitiveSchemaType)
                    {
                        PrimitiveSchemaType primitiveSchemaType = (PrimitiveSchemaType)attributeType;

                        element.setDataType(primitiveSchemaType.getDataType());
                        element.setDefaultValue(primitiveSchemaType.getDefaultValue());
                    }
                }
            }

            ElementHeader elementHeader = this.getElementHeaderFromBean(schemaAttribute, serverName);

            this.setSchemaAttributeProperties(element, schemaAttribute);

            element.setExtendedProperties(schemaAttribute.getExtendedProperties());

            Classification primaryKeyClassification = this.extractClassification(SchemaElementMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
                                                                                 elementHeader.getClassifications());

            if (primaryKeyClassification != null)
            {
                elementHeader.setClassifications(this.removeClassification(SchemaElementMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
                                                                            elementHeader.getClassifications()));

                DatabasePrimaryKeyProperties primaryKeyProperties = new DatabasePrimaryKeyProperties();

                Map<String, Object> classificationProperties = primaryKeyClassification.getClassificationProperties();

                if (classificationProperties != null)
                {
                    Object primaryKeyName = classificationProperties.get(SchemaElementMapper.PRIMARY_KEY_NAME_PROPERTY_NAME);

                    if (primaryKeyName != null)
                    {
                        primaryKeyProperties.setName(primaryKeyName.toString());
                    }

                    Object primaryKeyPattern = classificationProperties.get(SchemaElementMapper.PRIMARY_KEY_PATTERN_PROPERTY_NAME);

                    if (primaryKeyPattern != null)
                    {
                        primaryKeyProperties.setKeyPattern(this.getKeyPattern((org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern) primaryKeyPattern));
                    }
                }

                element.setPrimaryKeyProperties(primaryKeyProperties);
            }

            element.setElementHeader(elementHeader);

            List<SchemaAttributeRelationship>  relationships = schemaAttribute.getAttributeRelationships();
            if (relationships != null)
            {
                for (SchemaAttributeRelationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        if (SchemaElementMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME.equals(relationship.getLinkType()))
                        {
                            element.setReferencedColumnGUID(relationship.getLinkedAttributeGUID());
                            element.setReferencedColumnQualifiedName(relationship.getLinkedAttributeName());

                            DatabaseForeignKeyProperties foreignKeyProperties = new DatabaseForeignKeyProperties();

                            Map<String, Object> linkProperties = relationship.getLinkProperties();
                            if (linkProperties != null)
                            {
                                Object propertyValue = linkProperties.get(SchemaElementMapper.FOREIGN_KEY_NAME_PROPERTY_NAME);

                                if (propertyValue != null)
                                {
                                    foreignKeyProperties.setName(propertyValue.toString());
                                }

                                propertyValue = linkProperties.get(SchemaElementMapper.FOREIGN_KEY_DESCRIPTION_PROPERTY_NAME);

                                if (propertyValue != null)
                                {
                                    foreignKeyProperties.setDescription(propertyValue.toString());
                                }

                                propertyValue = linkProperties.get(SchemaElementMapper.FOREIGN_KEY_CONFIDENCE_PROPERTY_NAME);

                                if (propertyValue != null)
                                {
                                    foreignKeyProperties.setConfidence(Integer.getInteger(propertyValue.toString()));
                                }

                                propertyValue = linkProperties.get(SchemaElementMapper.FOREIGN_KEY_STEWARD_PROPERTY_NAME);

                                if (propertyValue != null)
                                {
                                    foreignKeyProperties.setSteward(propertyValue.toString());
                                }

                                propertyValue = linkProperties.get(SchemaElementMapper.FOREIGN_KEY_SOURCE_PROPERTY_NAME);

                                if (propertyValue != null)
                                {
                                    foreignKeyProperties.setSource(propertyValue.toString());
                                }
                            }

                            element.setForeignKeyProperties(foreignKeyProperties);
                        }
                    }
                }
            }

            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            element.setVendorProperties(handler.getVendorProperties(userId,
                                                                    schemaAttribute.getGUID(),
                                                                    schemaAttribute.getQualifiedName(),
                                                                    methodName));
        }

        return element;
    }


    /**
     * Set up the common properties for a schema attribute.
     *
     * @param schemaAttributePropertiesBean target Data Manager OMAS bean
     * @param schemaAttributeBean OCF bean
     */
    private void setSchemaAttributeProperties(SchemaAttributeProperties   schemaAttributePropertiesBean,
                                              SchemaAttribute             schemaAttributeBean)
    {
        if (schemaAttributePropertiesBean != null)
        {
            setSchemaElementProperties(schemaAttributePropertiesBean, schemaAttributeBean);

            schemaAttributePropertiesBean.setElementPosition(schemaAttributeBean.getElementPosition());
            schemaAttributePropertiesBean.setMinCardinality(schemaAttributeBean.getMinCardinality());
            schemaAttributePropertiesBean.setMaxCardinality(schemaAttributeBean.getMaxCardinality());
            schemaAttributePropertiesBean.setAllowsDuplicateValues(schemaAttributeBean.isAllowsDuplicateValues());
            schemaAttributePropertiesBean.setOrderedValues(schemaAttributeBean.isOrderedValues());
            schemaAttributePropertiesBean.setDefaultValueOverride(schemaAttributeBean.getDefaultValueOverride());
            schemaAttributePropertiesBean.setMinimumLength(schemaAttributeBean.getMinimumLength());
            schemaAttributePropertiesBean.setLength(schemaAttributeBean.getLength());
            schemaAttributePropertiesBean.setSignificantDigits(schemaAttributeBean.getSignificantDigits());
            schemaAttributePropertiesBean.setNullable(schemaAttributeBean.isNullable());
            schemaAttributePropertiesBean.setNativeJavaClass(schemaAttributeBean.getNativeJavaClass());
            schemaAttributePropertiesBean.setAliases(schemaAttributeBean.getAliases());
            schemaAttributePropertiesBean.setSortOrder(this.getDataItemSortOrder(schemaAttributeBean.getSortOrder()));
        }
    }


    /**
     * Set up the common properties for a schema element.
     *
     * @param schemaElementPropertiesBean target Data Manager OMAS bean
     * @param schemaElementBean OCF bean
     */
    private void setSchemaElementProperties(SchemaElementProperties   schemaElementPropertiesBean,
                                            SchemaElement             schemaElementBean)
    {
        if (schemaElementPropertiesBean != null)
        {
            setReferenceableProperties(schemaElementPropertiesBean, schemaElementBean);

            schemaElementPropertiesBean.setDisplayName(schemaElementBean.getDisplayName());
            schemaElementPropertiesBean.setDescription(schemaElementBean.getDescription());
            schemaElementPropertiesBean.setDeprecated(schemaElementBean.isDeprecated());

        }
    }


    /**
     * Set up the common properties for a referencable (ignoring extendedProperties and vendor properties).
     *
     * @param referenceablePropertiesBean Data Manager OMAS bean
     * @param referenceableBean OCF bean
     */
    private void setReferenceableProperties(ReferenceableProperties   referenceablePropertiesBean,
                                            Referenceable             referenceableBean)
    {
        if (referenceableBean != null)
        {
            referenceablePropertiesBean.setQualifiedName(referenceableBean.getQualifiedName());
            referenceablePropertiesBean.setAdditionalProperties(referenceableBean.getAdditionalProperties());

            if (referenceableBean.getType() != null)
            {
                referenceablePropertiesBean.setTypeName(referenceableBean.getType().getElementTypeName());
            }
        }
    }


    /**
     * Return the list of unique identifiers for the glossary terms linked to the metadata element.
     *
     * @param ocfMeanings OCF bean representing the meanings
     * @return list of GUIDs
     */
    private List<String>  getMeaningGUIDs(List<Meaning>   ocfMeanings)
    {
        List<String> meaningGUIDs = null;

        if ((ocfMeanings != null) && (!ocfMeanings.isEmpty()))
        {
            meaningGUIDs = new ArrayList<>();

            for (Meaning ocfMeaning : ocfMeanings)
            {
                if (ocfMeaning != null)
                {
                    meaningGUIDs.add(ocfMeaning.getGUID());
                }
            }
        }

        return meaningGUIDs;
    }


    /**
     * Create the metadata element header for the metadata element just extracted from the metadata repository.
     *
     * @param bean metadata from repository
     * @param serverName local server name
     * @return metadata element header
     */
    private ElementHeader getElementHeaderFromBean(Referenceable   bean,
                                                   String          serverName)
    {
        ElementHeader elementHeader = null;

        if (bean != null)
        {
            elementHeader = new ElementHeader();

            elementHeader.setGUID(bean.getGUID());
            elementHeader.setOrigin(getElementOriginFromBean(bean, serverName));
            elementHeader.setType(this.getElementTypeFromBean(bean));

            elementHeader.setMeanings(this.getMeaningGUIDs(bean.getMeanings()));
            elementHeader.setClassifications(this.getDataManagerClassifications(bean.getClassifications()));
        }

        return elementHeader;
    }


    /**
     * Set up the Data Manager ElementOrigin from the OCF bean.
     *
     * @param bean OCF bean
     * @param serverName local server name
     * @return Data Manager OMAS version
     */
    private ElementOrigin getElementOriginFromBean(Referenceable   bean,
                                                   String          serverName)
    {
        ElementOrigin elementOrigin = null;

        if (bean != null)
        {
            elementOrigin = new ElementOrigin();

            elementOrigin.setSourceServer(serverName);

            if (bean.getType() != null)
            {
                elementOrigin.setOriginCategory(this.getElementOriginCategory(bean.getType().getElementOrigin()));
                elementOrigin.setHomeMetadataCollectionId(bean.getType().getElementHomeMetadataCollectionId());
                elementOrigin.setHomeMetadataCollectionName(bean.getType().getElementHomeMetadataCollectionName());
                elementOrigin.setLicense(bean.getType().getElementLicense());
            }
        }

        return elementOrigin;
    }


    /**
     * Return a Data Manager OMAS ElementOriginCategory from an OCF ElementOrigin.
     *
     * @param ocfElementOrigin enum from OCF
     * @return Data Manager OMAS version
     */
    private ElementOriginCategory getElementOriginCategory(org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin ocfElementOrigin)
    {
        if (ocfElementOrigin != null)
        {
            switch (ocfElementOrigin)
            {
                case LOCAL_COHORT:
                    return ElementOriginCategory.LOCAL_COHORT;

                case EXPORT_ARCHIVE:
                    return ElementOriginCategory.EXPORT_ARCHIVE;

                case CONTENT_PACK:
                    return ElementOriginCategory.CONTENT_PACK;

                case DEREGISTERED_REPOSITORY:
                    return ElementOriginCategory.DEREGISTERED_REPOSITORY;

                case CONFIGURATION:
                    return ElementOriginCategory.CONFIGURATION;

                case EXTERNAL_SOURCE:
                    return ElementOriginCategory.EXTERNAL_SOURCE;
            }
        }

        return ElementOriginCategory.UNKNOWN;
    }


    /**
     * Create a Data Manager OMAS Element Type from an OCF bean.
     *
     * @param bean OCF bean
     * @return new element type
     */
    private ElementType getElementTypeFromBean(Referenceable  bean)
    {
        ElementType elementType = null;

        if ((bean != null) && (bean.getType() != null))
        {
            elementType = new ElementType();

            elementType.setTypeId(bean.getType().getElementTypeId());
            elementType.setTypeName(bean.getType().getElementTypeName());
            elementType.setTypeDescription(bean.getType().getElementTypeDescription());
            elementType.setTypeVersion(bean.getType().getElementTypeVersion());
            elementType.setSuperTypeNames(bean.getType().getElementSuperTypeNames());
        }

        return elementType;
    }


    /**
     * Return a Data Manager OMAS DataItemSourceOrder from an OCF DataItemSourceOrder.
     *
     * @param ocfSortOrder enum from OCF
     * @return Data Manager OMAS version
     */
    private DataItemSortOrder getDataItemSortOrder(org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder ocfSortOrder)
    {
        if (ocfSortOrder != null)
        {
            switch (ocfSortOrder)
            {
                case ASCENDING:
                    return DataItemSortOrder.ASCENDING;

                case DESCENDING:
                    return DataItemSortOrder.DESCENDING;

                case UNSORTED:
                    return DataItemSortOrder.UNSORTED;
            }
        }

        return DataItemSortOrder.UNKNOWN;
    }


    /**
     * Return a OCF DataItemSourceOrder from a Data Manager OMAS DataItemSourceOrder.
     *
     * @param dataItemSortOrder enum from Data Manager OMAS
     * @return Data Manager OMAS version
     */
    org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder getOCFSortOrder(DataItemSortOrder dataItemSortOrder)
    {
        if (dataItemSortOrder != null)
        {
            switch (dataItemSortOrder)
            {
                case ASCENDING:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder.ASCENDING;

                case DESCENDING:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder.DESCENDING;

                case UNSORTED:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder.UNSORTED;
            }
        }

        return org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder.UNKNOWN;
    }


    /**
     * Return a list of OCF SchemaImplementationQuery from a list of Data Manager OMAS DatabaseQueryProperties.
     *
     * @param databaseQueries bean from Data Manager OMAS
     * @return Data Manager OMAS version
     */
    List<SchemaImplementationQuery> getOCFDerivedSchemaQueries(List<DatabaseQueryProperties> databaseQueries)
    {
        List<SchemaImplementationQuery> derivedSchemaQueries = null;

        if (databaseQueries != null)
        {
            derivedSchemaQueries = new ArrayList<>();

            for (DatabaseQueryProperties queryProperties : databaseQueries)
            {
                if (queryProperties != null)
                {
                    SchemaImplementationQuery ocfQueryProperties = new SchemaImplementationQuery();

                    ocfQueryProperties.setQueryId(queryProperties.getQueryId());
                    ocfQueryProperties.setQuery(queryProperties.getQuery());
                    ocfQueryProperties.setQueryTargetGUID(queryProperties.getQueryTargetGUID());

                    derivedSchemaQueries.add(ocfQueryProperties);
                }
            }

            if (derivedSchemaQueries.isEmpty())
            {
                derivedSchemaQueries = null;
            }
        }

        return derivedSchemaQueries;
    }


    /**
     * Return a list of Data Manager OMAS DatabaseQueryProperties from a list of OCF SchemaImplementationQuery.
     *
     * @param derivedSchemaQueries bean from Data Manager OMAS
     * @return Data Manager OMAS version
     */
    List<DatabaseQueryProperties> getDatabaseSchemaQueries(List<SchemaImplementationQuery> derivedSchemaQueries)
    {
        List<DatabaseQueryProperties> databaseQueryPropertiesList = null;

        if (derivedSchemaQueries != null)
        {
            databaseQueryPropertiesList = new ArrayList<>();

            for (SchemaImplementationQuery derivedSchemaQueryProperties : derivedSchemaQueries)
            {
                if (derivedSchemaQueryProperties != null)
                {
                    DatabaseQueryProperties databaseQueryProperties = new DatabaseQueryProperties();

                    databaseQueryProperties.setQueryId(derivedSchemaQueryProperties.getQueryId());
                    databaseQueryProperties.setQuery(derivedSchemaQueryProperties.getQuery());
                    databaseQueryProperties.setQueryTargetGUID(derivedSchemaQueryProperties.getQueryTargetGUID());

                    databaseQueryPropertiesList.add(databaseQueryProperties);
                }
            }

            if (databaseQueryPropertiesList.isEmpty())
            {
                databaseQueryPropertiesList = null;
            }
        }

        return databaseQueryPropertiesList;
    }


    /**
     * Return a Data Manager OMAS KeyPattern from an OCF KeyPattern.
     *
     * @param ocfKeyPattern enum from OCF
     * @return Data Manager OMAS version
     */
    private KeyPattern getKeyPattern(org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern ocfKeyPattern)
    {
        if (ocfKeyPattern != null)
        {
            switch (ocfKeyPattern)
            {
                case LOCAL_KEY:
                    return KeyPattern.LOCAL_KEY;

                case RECYCLED_KEY:
                    return KeyPattern.RECYCLED_KEY;

                case NATURAL_KEY:
                    return KeyPattern.NATURAL_KEY;

                case MIRROR_KEY:
                    return KeyPattern.MIRROR_KEY;

                case AGGREGATE_KEY:
                    return KeyPattern.AGGREGATE_KEY;

                case CALLERS_KEY:
                    return KeyPattern.CALLERS_KEY;

                case STABLE_KEY:
                    return KeyPattern.STABLE_KEY;
            }
        }

        return KeyPattern.OTHER;
    }


    /**
     * Return an OCF KeyPattern from a Data Manager OMAS KeyPattern.
     *
     * @param keyPattern enum from OCF
     * @return Data Manager OMAS version
     */
    org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern getOCFKeyPattern(KeyPattern keyPattern)
    {
        if (keyPattern != null)
        {
            switch (keyPattern)
            {
                case LOCAL_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.LOCAL_KEY;

                case RECYCLED_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.RECYCLED_KEY;

                case NATURAL_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.NATURAL_KEY;

                case MIRROR_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.MIRROR_KEY;

                case AGGREGATE_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.AGGREGATE_KEY;

                case CALLERS_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.CALLERS_KEY;

                case STABLE_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.STABLE_KEY;
            }
        }

        return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.OTHER;
    }

}
