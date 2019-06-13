package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SchemaTypePropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.SchemaTypeConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ComplexSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


public class DataEngineSchemaTypeHandler {
    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private SchemaTypeHandler schemaTypeHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     */
    public DataEngineSchemaTypeHandler(String serviceName, String serverName,
                                       InvalidParameterHandler invalidParameterHandler,
                                       RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;

        schemaTypeHandler = new SchemaTypeHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                repositoryHelper);
    }


    public String createSchemaType(String userId, String serverName, String schemaTypeGUID) throws
                                                                                            InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException {
        final String methodName = "createSchemaType";

        List<SchemaAttribute> schemaAttributes = schemaTypeHandler.getSchemaAttributes(userId, schemaTypeGUID,
                0, 0, methodName);

        EntityDetail schemaTypeEntityDetail = repositoryHandler.getEntityByGUID(userId, schemaTypeGUID,
                "guid", "SchemaType", methodName);

        SchemaTypeConverter schemaTypeConverter = new SchemaTypeConverter(schemaTypeEntityDetail,
                schemaAttributes.size(), repositoryHelper, serverName);
        SchemaType schemaType = schemaTypeConverter.getBean();

        //TODO add method to build qualifiedName & displayName for the new schema
        String schemaTypeQualifiedName = schemaTypeGUID + "::" + "dataEngine" + "::";

        SchemaType newSchemaType = this.createSchemaType(schemaTypeQualifiedName, schemaType);

        List<SchemaAttribute> newSchemaAttributes = this.createSchemaAttributes(newSchemaType.getQualifiedName(),
                schemaAttributes);

        return schemaTypeHandler.saveSchemaType(userId, newSchemaType, newSchemaAttributes, methodName);
    }

    private List<SchemaAttribute> createSchemaAttributes(String parentSchemaQualifiedName,
                                                         List<SchemaAttribute> schemaAttributes) {

        List<SchemaAttribute> newSchemaAttributes = new ArrayList<>();

        for (SchemaAttribute schemaAttribute : schemaAttributes) {
            SchemaAttribute newSchemaAttribute = new SchemaAttribute();

            newSchemaAttribute.setQualifiedName(parentSchemaQualifiedName + ":Column:" + schemaAttribute.getAttributeName());

            newSchemaAttribute.setAttributeName(schemaAttribute.getAttributeName());
            newSchemaAttribute.setCardinality(schemaAttribute.getCardinality());
            newSchemaAttribute.setElementPosition(schemaAttribute.getElementPosition());
            newSchemaAttribute.setType(schemaAttribute.getType());
            newSchemaAttribute.setAdditionalProperties(schemaAttribute.getAdditionalProperties());
            newSchemaAttribute.setExtendedProperties(schemaAttribute.getExtendedProperties());

            newSchemaAttributes.add(newSchemaAttribute);
        }

        return newSchemaAttributes;
    }

    private SchemaType createSchemaType(String parentSchemaQualifiedName, SchemaType schemaType) {

        ComplexSchemaType newSchemaType = new ComplexSchemaType();

        newSchemaType.setQualifiedName(parentSchemaQualifiedName + schemaType.getDisplayName());
        newSchemaType.setDisplayName(parentSchemaQualifiedName + schemaType.getDisplayName());
        newSchemaType.setAuthor(schemaType.getAuthor());
        newSchemaType.setEncodingStandard(schemaType.getEncodingStandard());
        newSchemaType.setUsage(schemaType.getUsage());
        newSchemaType.setAdditionalProperties(schemaType.getAdditionalProperties());
        newSchemaType.setExtendedProperties(schemaType.getExtendedProperties());
        newSchemaType.setType(schemaType.getType());

        return newSchemaType;
}

    public void addLineageMappingRelationship(String userId, String schemaTypeGUID, String newSchemaTypeGUID) throws
                                                                                                              org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException {
        final String methodName = "addLineageMappingRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(newSchemaTypeGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        repositoryHandler.createRelationship(userId, SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_GUID,
                schemaTypeGUID, newSchemaTypeGUID, null, methodName);
    }
}
