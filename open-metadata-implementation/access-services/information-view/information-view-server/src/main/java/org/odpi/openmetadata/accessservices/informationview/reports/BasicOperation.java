/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.BusinessTerm;
import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.SoftwareServerCapabilitySource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.AddRelationshipException;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.throwAddEntityRelationship;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.throwAddRelationshipException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.throwDeleteRelationshipException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.throwEntityNotFoundException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.throwNoRegistrationDetailsProvided;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.throwSourceNotFoundException;

public abstract class BasicOperation {

    private static final Logger log = LoggerFactory.getLogger(BasicOperation.class);
    protected final OMEntityDao omEntityDao;
    protected final OMRSAuditLog auditLog;
    protected final OMRSRepositoryHelper helper;
    public static final String SEPARATOR = "::";
    protected final EntityReferenceResolver entityReferenceResolver;

    public BasicOperation(OMEntityDao omEntityDao, LookupHelper lookupHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        this.omEntityDao = omEntityDao;
        this.auditLog = auditLog;
        this.helper = helper;
        this.entityReferenceResolver = new EntityReferenceResolver(lookupHelper, omEntityDao);
    }

    /**
     * @param registrationGuid          guid of the softwareServerCapability
     * @param registrationQualifiedName qualifiedName of the softwareServerCapability
     * @return entity of type SoftwareServerCapability retrieved by provided identifiers
     */
    public SoftwareServerCapabilitySource retrieveSoftwareServerCapability(String registrationGuid, String registrationQualifiedName) {
        EntityDetail softwareServerCapability = null;
        if (StringUtils.isEmpty(registrationGuid) && StringUtils.isEmpty(registrationQualifiedName)) {
             throwNoRegistrationDetailsProvided(null, ReportBasicOperation.class.getName());
        }

        String searchProperty = "";
        String searchValue = "";
        try {
            if (!StringUtils.isEmpty(registrationGuid)) {
                searchProperty = Constants.GUID;
                searchValue = registrationGuid;
                softwareServerCapability = omEntityDao.getEntityByGuid(registrationGuid);
            } else {
                searchProperty = Constants.QUALIFIED_NAME;
                searchValue = registrationGuid;
                softwareServerCapability = omEntityDao.getEntity(Constants.SOFTWARE_SERVER_CAPABILITY,
                        registrationQualifiedName, false);

            }
        } catch (RepositoryErrorException | UserNotAuthorizedException | EntityProxyOnlyException | InvalidParameterException
                | EntityNotKnownException | PagingErrorException | FunctionNotSupportedException | PropertyErrorException
                | TypeErrorException e) {
            throwEntityNotFoundException(searchProperty, searchValue, Constants.SOFTWARE_SERVER_CAPABILITY, ReportBasicOperation.class.getName());
        }
        if (softwareServerCapability == null) {
             throwEntityNotFoundException(Constants.QUALIFIED_NAME, registrationQualifiedName, Constants.SOFTWARE_SERVER_CAPABILITY, ReportBasicOperation.class.getName());
        }

        SoftwareServerCapabilitySource softwareServerCapabilitySource = new SoftwareServerCapabilitySource();
        softwareServerCapabilitySource.setGuid(softwareServerCapability.getGUID());
        softwareServerCapabilitySource.setQualifiedName(helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUALIFIED_NAME, softwareServerCapability.getProperties(), "retrieveSoftwareServerCapability"));
        return softwareServerCapabilitySource;
    }


    /**
     *
     * @param userId id of user submitting the request
     * @param schemaAttributeTypeName       - type name for the actual schema type entity to be created
     * @param qualifiedNameForSchemaType    - qualifiedName for schema type entity
     * @param registrationGuid - guid of softwareServerCapability
     * @param registrationQualifiedName - qualified name of software server capability
     * @param schemaAttributeTypeProperties - instance properties for schema attribute
     * @param schemaTypeRelationshipName    - type name for the actual schema type entity to be created
     * @param schemaAttributeGuid           - guid of the schema attribute for which the schema type was created
     * @return entity representing schema type
     */
    protected EntityDetail createSchemaType(String userId, String schemaAttributeTypeName,
                                            String qualifiedNameForSchemaType,
                                            String registrationGuid,
                                            String registrationQualifiedName,
                                            InstanceProperties schemaAttributeTypeProperties,
                                            String schemaTypeRelationshipName,
                                            String schemaAttributeGuid) {
        EntityDetail schemaTypeEntity = null;
        try {
            schemaTypeEntity = omEntityDao.addExternalEntity(userId, schemaAttributeTypeName,
                                                            qualifiedNameForSchemaType,
                                                            registrationGuid,
                                                            registrationQualifiedName,
                                                            schemaAttributeTypeProperties,
                                                            null,
                                                            false);
        } catch (InvalidParameterException | PropertyErrorException | RepositoryErrorException  | FunctionNotSupportedException
                | ClassificationErrorException | UserNotAuthorizedException | TypeErrorException | StatusNotSupportedException e) {
            throwAddEntityRelationship(schemaAttributeTypeName, e, ReportBasicOperation.class.getName());
        }

        try {
            omEntityDao.addExternalRelationship(userId,
                                                schemaTypeRelationshipName,
                                                registrationGuid,
                                                registrationQualifiedName,
                                                schemaAttributeGuid,
                                                schemaTypeEntity.getGUID(),
                                                new InstanceProperties());
        } catch (InvalidParameterException | TypeErrorException | PropertyErrorException | EntityNotKnownException |
                FunctionNotSupportedException | PagingErrorException | UserNotAuthorizedException | RepositoryErrorException
                | StatusNotSupportedException e) {
            throwAddRelationshipException(schemaTypeRelationshipName, e, ReportBasicOperation.class.getName());
        }
        return schemaTypeEntity;
    }


    /**
     * @param userId id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param referenceableEntityGuid guid of the entity representing the derived column
     * @param businessTermGuid        guid of businessTerm
     */
    protected void addBusinessTerm(String userId, String registrationGuid, String registrationQualifiedName, String referenceableEntityGuid, String businessTermGuid)  {
        if (!StringUtils.isEmpty(businessTermGuid)) {
            try {
                omEntityDao.addExternalRelationship(userId,
                                                    Constants.SEMANTIC_ASSIGNMENT,
                                                    registrationGuid,
                                                    registrationQualifiedName,
                                                    referenceableEntityGuid,
                                                    businessTermGuid,
                                                    new InstanceProperties());
            } catch (InvalidParameterException | TypeErrorException | PropertyErrorException | EntityNotKnownException |
                    FunctionNotSupportedException | PagingErrorException | UserNotAuthorizedException | RepositoryErrorException |
                    StatusNotSupportedException e) {
               throwAddRelationshipException(Constants.SEMANTIC_ASSIGNMENT, e, ReportBasicOperation.class.getName());
            }
        }
    }

    /**
     * @param userId id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param derivedColumnEntityGuid the guid of the entity representing the derived column
     * @param sourceColumnGuid        identifier of the source column entity
     * @param queryValue
     */
    protected void addQueryTarget(String userId, String registrationGuid, String registrationQualifiedName, String derivedColumnEntityGuid, String sourceColumnGuid, String queryValue)  {

        InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUERY, queryValue)
                .build();
        try {
            omEntityDao.addExternalRelationship(userId,
                                                Constants.SCHEMA_QUERY_IMPLEMENTATION,
                                                registrationGuid,
                                                registrationQualifiedName,
                                                derivedColumnEntityGuid,
                                                sourceColumnGuid,
                                                schemaQueryImplProperties);
        } catch (InvalidParameterException | TypeErrorException | PropertyErrorException | EntityNotKnownException |
                FunctionNotSupportedException | PagingErrorException | UserNotAuthorizedException | RepositoryErrorException |
                StatusNotSupportedException e) {
           throwAddRelationshipException(Constants.SCHEMA_QUERY_IMPLEMENTATION, e, ReportBasicOperation.class.getName());
        }
    }

    /**
     *
     * @param userId - id of user submitting the request
     * @param qualifiedNameForType - qualified name of the schema attribute
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param schemaAttributeGuid  schema attribute entity guid
     * @param schemaAttributeType  schema attribute type entity
     * @param properties           properties for the type entity
     * @return entity describing the schema type
     */
    protected EntityDetail addSchemaType(String userId, String qualifiedNameForType, String registrationGuid,
                                         String registrationQualifiedName, String schemaAttributeGuid,
                                         String schemaAttributeType, InstanceProperties properties) {

        InstanceProperties typeProperties;
        typeProperties = helper.addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, properties, Constants.QUALIFIED_NAME, qualifiedNameForType, "addSchemaType");
        EntityDetail schemaTypeEntity = createSchemaType(userId, schemaAttributeType, qualifiedNameForType, registrationGuid, registrationQualifiedName, typeProperties,
                Constants.SCHEMA_ATTRIBUTE_TYPE, schemaAttributeGuid);
        return schemaTypeEntity;
    }


    /**
     *
     * @param userId id of user submitting the request - userId of the user submitting the request
     * @param assetGuid                  -       guid of the entity describing the asset
     * @param qualifiedNameForComplexSchemaType qualified name for complex schema type
     * @param registrationGuid - guid of softwareServerCapability
     * @param registrationQualifiedName - qualified name of software server capability
     * @param schemaTypeName - name os schema type
     * @param complexSchemaTypeProperties       properties of the complex schema type
     * @return entity describing the asset schema type
     */
    protected EntityDetail addAssetSchemaType(String userId, String assetGuid, String qualifiedNameForComplexSchemaType,
                                              String registrationGuid, String registrationQualifiedName, String schemaTypeName, InstanceProperties complexSchemaTypeProperties) {
        EntityDetail complexSchemaTypeEntity = null;
        try {
            complexSchemaTypeEntity = omEntityDao.addExternalEntity(userId,
                                                            schemaTypeName,
                                                            qualifiedNameForComplexSchemaType,
                                                            registrationGuid,
                                                            registrationQualifiedName,
                                                            complexSchemaTypeProperties,
                                                            null,
                                                            false);
        } catch (InvalidParameterException | StatusNotSupportedException | PropertyErrorException | TypeErrorException | FunctionNotSupportedException  | ClassificationErrorException | UserNotAuthorizedException | RepositoryErrorException e) {
           throwAddEntityRelationship(schemaTypeName, e, ReportBasicOperation.class.getName());
        }

        try {
            omEntityDao.addExternalRelationship(userId,
                                                Constants.ASSET_SCHEMA_TYPE,
                                                registrationGuid,
                                                registrationQualifiedName,
                                                assetGuid,
                                                complexSchemaTypeEntity.getGUID(),
                                                new InstanceProperties());
        } catch (InvalidParameterException | TypeErrorException | PropertyErrorException | EntityNotKnownException | FunctionNotSupportedException | PagingErrorException | UserNotAuthorizedException | RepositoryErrorException | StatusNotSupportedException e) {
            throwAddRelationshipException(Constants.ASSET_SCHEMA_TYPE, e, ReportBasicOperation.class.getName());
        }
        return complexSchemaTypeEntity;
    }

    /**
     * @param existingRelationships relationships to be deleted
     */
    protected void deleteRelationships(List<Relationship> existingRelationships) {
        if (existingRelationships != null && !existingRelationships.isEmpty()) {
            for (Relationship relationship : existingRelationships) {
                try {
                    omEntityDao.purgeRelationship(relationship);
                } catch (RepositoryErrorException | UserNotAuthorizedException | InvalidParameterException | RelationshipNotDeletedException | RelationshipNotKnownException | FunctionNotSupportedException e) {
                    throwDeleteRelationshipException(relationship, null, BasicOperation.class.getName());
                }
            }
        } else {
            log.debug("No relationships to delete.");
        }
    }


    /**
     * Create relationships of type SEMANTIC_ASSIGNMENT between the business terms and the entity representing the column
     * @param userId - id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param  businessTerms - list of business terms
     * @param derivedColumnEntity - entity describing the derived column
     */
    public void addSemanticAssignments(String userId, String registrationGuid, String registrationQualifiedName, List<BusinessTerm> businessTerms, EntityDetail derivedColumnEntity){
        if(businessTerms != null && !businessTerms.isEmpty()) {
            businessTerms.stream().forEach(bt -> {
                addSemanticAssignment(userId, registrationGuid, registrationQualifiedName, bt, derivedColumnEntity);
            });
        }
    }

    /**
     *
     * @param userId - id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param bt - business term to link to derived column
     * @param derivedColumnEntity - entity describing the column
     */
    public void addSemanticAssignment(String userId, String registrationGuid, String registrationQualifiedName, BusinessTerm bt, EntityDetail derivedColumnEntity)  {
        String businessTermGuid;
        try {
            businessTermGuid = entityReferenceResolver.getBusinessTermGuid(bt);
            if (!StringUtils.isEmpty(businessTermGuid)) {
                omEntityDao.addExternalRelationship(userId,
                                                    Constants.SEMANTIC_ASSIGNMENT,
                                                    registrationGuid,
                                                    registrationQualifiedName,
                                                    derivedColumnEntity.getGUID(),
                                                    businessTermGuid,
                                                    new InstanceProperties());
            }
        } catch (UserNotAuthorizedException | FunctionNotSupportedException | InvalidParameterException | RepositoryErrorException | PropertyErrorException | TypeErrorException | PagingErrorException | StatusNotSupportedException | EntityNotKnownException e) {
            InformationViewErrorCode errorCode = InformationViewErrorCode.ADD_RELATIONSHIP_EXCEPTION;
            throw new AddRelationshipException(errorCode.getHttpErrorCode(), ReportUpdater.class.getName(),
                    errorCode.getFormattedErrorMessage(Constants.SEMANTIC_ASSIGNMENT, e.getMessage()),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }
    }


    /**
     * @param userId id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param sources list of sources describing the report column
     * @param derivedColumnEntity entity describing the derived column
     */
    public void addQueryTargets(String userId, String registrationGuid, String registrationQualifiedName, List<Source> sources, EntityDetail derivedColumnEntity) {
        if(sources!=null && !sources.isEmpty()) {
            for (Source source : sources) {
                String sourceColumnGUID = entityReferenceResolver.getSourceGuid(source);
                if (!StringUtils.isEmpty(sourceColumnGUID)) {
                    log.debug("source {} for entity {} found.", source, derivedColumnEntity.getGUID());
                    addQueryTarget(userId, registrationGuid, registrationQualifiedName, derivedColumnEntity.getGUID(), sourceColumnGUID, "");
                } else {
                    throwSourceNotFoundException(source, null, ReportBasicOperation.class.getName());
                }
            }
        }
    }


    /**
     * @param userId - id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param columnGuid guid of the column
     * @param businessTerm business term to link or update to column
     * @param existingAssignments list of existing relationships of type semantic assignment
     */
    protected void createOrUpdateSemanticAssignment(String userId, String registrationGuid,
                                                    String registrationQualifiedName,
                                                    String columnGuid,
                                                    BusinessTerm businessTerm,
                                                    List<Relationship> existingAssignments) {
        try {
            String businessTermAssignedToColumnGuid = entityReferenceResolver.getBusinessTermGuid(businessTerm);
            List<Relationship> matchingRelationship = new ArrayList<>();
            if (existingAssignments != null && !existingAssignments.isEmpty()) {
                matchingRelationship = existingAssignments.stream().filter(e -> e.getEntityTwoProxy().getGUID().equals(businessTermAssignedToColumnGuid)).collect(Collectors.toList());
                deleteRelationships(existingAssignments.stream().filter(e -> !e.getEntityTwoProxy().getGUID().equals(businessTermAssignedToColumnGuid)).collect(Collectors.toList()));
            }

            if ((matchingRelationship == null || matchingRelationship.isEmpty()) && !StringUtils.isEmpty(businessTermAssignedToColumnGuid)) {
                omEntityDao.addExternalRelationship(userId,
                        Constants.SEMANTIC_ASSIGNMENT,
                        registrationGuid,
                        registrationQualifiedName,
                        columnGuid,
                        businessTermAssignedToColumnGuid,
                        new InstanceProperties());
            }
        } catch (UserNotAuthorizedException | FunctionNotSupportedException | InvalidParameterException | RepositoryErrorException | PropertyErrorException | TypeErrorException | PagingErrorException | StatusNotSupportedException | EntityNotKnownException e) {
            throwAddRelationshipException(Constants.SEMANTIC_ASSIGNMENT, e, ReportUpdater.class.getName());
        }
    }


    protected String buildQualifiedNameForSchemaType(String qualifiedNameForParent, String schemaType, ReportElement element) {
        return QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, schemaType, element.getName() + Constants.TYPE_SUFFIX);
    }


    /**
     *
     * @param sources list of sources to be linked to column
     * @param columnGuid guid of column
     * @throws UserNotAuthorizedException
     * @throws FunctionNotSupportedException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     * @throws TypeErrorException
     * @throws PagingErrorException
     * @throws StatusNotSupportedException
     * @throws EntityNotKnownException
     * @throws RelationshipNotKnownException
     * @throws RelationshipNotDeletedException
     */
    protected void createOrUpdateSchemaQueryImplementation(List<Source> sources, String columnGuid) throws
                                                                                                    UserNotAuthorizedException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    InvalidParameterException,
                                                                                                    RepositoryErrorException,
                                                                                                    PropertyErrorException,
                                                                                                    TypeErrorException,
                                                                                                    PagingErrorException,
                                                                                                    StatusNotSupportedException,
                                                                                                    EntityNotKnownException,
                                                                                                    RelationshipNotKnownException,
                                                                                                    RelationshipNotDeletedException {

        List<Relationship> relationships = omEntityDao.getRelationships(Constants.SCHEMA_QUERY_IMPLEMENTATION, columnGuid);
        List<String> relationshipsToRemove = new ArrayList<>();
        if (relationships != null && !relationships.isEmpty()) {
            relationshipsToRemove = relationships.stream().map(e -> e.getEntityTwoProxy().getGUID()).collect(Collectors.toList());
        }
        for (Source source : sources) {
            String sourceColumnGuid = entityReferenceResolver.getSourceGuid(source);
            if (!StringUtils.isEmpty(sourceColumnGuid)) {
                log.info("source {} found.", source);
                if (relationshipsToRemove != null && relationshipsToRemove.contains(sourceColumnGuid)) {
                    log.info("Relationship already exists and is valid");
                    relationshipsToRemove.remove(sourceColumnGuid);
                } else {
                    InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                            .withStringProperty(Constants.QUERY, "")
                            .build();
                    omEntityDao.addRelationship(Constants.SCHEMA_QUERY_IMPLEMENTATION,
                            columnGuid,
                            sourceColumnGuid,
                            schemaQueryImplProperties);
                }
            } else {
                log.error(MessageFormat.format("source column not found, unable to add relationship {0} between column {1} and source {2}", Constants.SCHEMA_QUERY_IMPLEMENTATION, columnGuid, source.toString()));
            }

            if (relationships != null && !relationships.isEmpty() && relationshipsToRemove != null && !relationshipsToRemove.isEmpty()) {
                for (Relationship relationship : relationships) {
                    if (relationshipsToRemove.contains(relationship.getGUID())) {
                        omEntityDao.purgeRelationship(relationship);
                    }
                }
            }
        }
    }

    /**
     *
     *
     * @param userId - id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param businessTerms list of business terms  to link to column
     * @param columnGuid guid of column
     * @throws UserNotAuthorizedException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     * @throws TypeErrorException
     * @throws PagingErrorException
     */
    protected void createOrUpdateSemanticAssignments(String userId, String registrationGuid,
                                                     String registrationQualifiedName,
                                                     List<BusinessTerm> businessTerms,
                                                     String columnGuid) throws UserNotAuthorizedException,
                                                                               EntityNotKnownException,
                                                                               FunctionNotSupportedException,
                                                                               InvalidParameterException,
                                                                               RepositoryErrorException,
                                                                               PropertyErrorException,
                                                                               TypeErrorException,
                                                                               PagingErrorException{
        List<Relationship> existingAssignments = omEntityDao.getRelationships(Constants.SEMANTIC_ASSIGNMENT, columnGuid);
        if (businessTerms == null || businessTerms.isEmpty()) {
            deleteRelationships(existingAssignments);
        } else {
            businessTerms.stream().forEach( bt -> createOrUpdateSemanticAssignment(userId, registrationGuid, registrationQualifiedName, columnGuid, bt, existingAssignments));
        }
    }

}
