/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.BusinessTerm;
import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.SoftwareServerCapabilitySource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.*;

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
        EntityDetail softwareServerCapability;
        if (StringUtils.isEmpty(registrationGuid) && StringUtils.isEmpty(registrationQualifiedName)) {
            throw buildNoRegistrationDetailsProvided(null, ReportBasicOperation.class.getName());
        }
        if (!StringUtils.isEmpty(registrationGuid)) {
            softwareServerCapability = Optional.ofNullable(omEntityDao.getEntityByGuid(registrationGuid))
                                                .orElseThrow(() -> buildEntityNotFoundException(Constants.GUID, registrationGuid,
                                                                                                Constants.SOFTWARE_SERVER_CAPABILITY,
                                                                                                ReportBasicOperation.class.getName()));
        }else {
            softwareServerCapability = Optional.ofNullable(omEntityDao.getEntity(Constants.SOFTWARE_SERVER_CAPABILITY,
                                                                                registrationQualifiedName,
                                                                                false))
                                                .orElseThrow(() -> buildEntityNotFoundException(Constants.QUALIFIED_NAME,
                                                        registrationQualifiedName,
                                                        Constants.SOFTWARE_SERVER_CAPABILITY,
                                                        ReportBasicOperation.class.getName()));
        }
        return buildSoftwareServerCapabilitySource(softwareServerCapability);
    }

    private SoftwareServerCapabilitySource buildSoftwareServerCapabilitySource(EntityDetail softwareServerCapability) {
        SoftwareServerCapabilitySource softwareServerCapabilitySource = new SoftwareServerCapabilitySource();
        softwareServerCapabilitySource.setGuid(softwareServerCapability.getGUID());
        softwareServerCapabilitySource.setQualifiedName(helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.QUALIFIED_NAME, softwareServerCapability.getProperties(),
                "retrieveSoftwareServerCapability"));
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
        EntityDetail schemaTypeEntity;
        schemaTypeEntity = omEntityDao.addExternalEntity(userId,
                                                            schemaAttributeTypeName,
                                                            qualifiedNameForSchemaType,
                                                            registrationGuid,
                                                            registrationQualifiedName,
                                                            schemaAttributeTypeProperties,
                                                            null,
                                                            false);
        omEntityDao.addExternalRelationship(userId,
                                            schemaTypeRelationshipName,
                                            registrationGuid,
                                            registrationQualifiedName,
                                            schemaAttributeGuid,
                                            schemaTypeEntity.getGUID(),
                                            new InstanceProperties());
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
                omEntityDao.addExternalRelationship(userId,
                                                    Constants.SEMANTIC_ASSIGNMENT,
                                                    registrationGuid,
                                                    registrationQualifiedName,
                                                    referenceableEntityGuid,
                                                    businessTermGuid,
                                                    new InstanceProperties());

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
            omEntityDao.addExternalRelationship(userId,
                                                Constants.SCHEMA_QUERY_IMPLEMENTATION,
                                                registrationGuid,
                                                registrationQualifiedName,
                                                derivedColumnEntityGuid,
                                                sourceColumnGuid,
                                                schemaQueryImplProperties);

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
        EntityDetail complexSchemaTypeEntity = omEntityDao.addExternalEntity(userId,
                                                                            schemaTypeName,
                                                                            qualifiedNameForComplexSchemaType,
                                                                            registrationGuid,
                                                                            registrationQualifiedName,
                                                                            complexSchemaTypeProperties,
                                                                            null,
                                                                            false);

        omEntityDao.addExternalRelationship(userId,
                                            Constants.ASSET_SCHEMA_TYPE,
                                            registrationGuid,
                                            registrationQualifiedName,
                                            assetGuid,
                                            complexSchemaTypeEntity.getGUID(),
                                            new InstanceProperties());

        return complexSchemaTypeEntity;
    }



    /**
     * Create relationships of type SEMANTIC_ASSIGNMENT between the business terms and the entity representing the
     * column
     * @param userId - id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param  businessTerms - list of business terms
     * @param derivedColumnEntity - entity describing the derived column
     */
    public void addSemanticAssignments(String userId, String registrationGuid, String registrationQualifiedName, List<BusinessTerm> businessTerms, EntityDetail derivedColumnEntity){
        if(!CollectionUtils.isEmpty(businessTerms)) {
            businessTerms.forEach(bt -> addSemanticAssignment(userId, registrationGuid, registrationQualifiedName, bt, derivedColumnEntity));
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
        businessTermGuid = entityReferenceResolver.getBusinessTermGuid(bt);
        omEntityDao.addExternalRelationship(userId,
                                            Constants.SEMANTIC_ASSIGNMENT,
                                            registrationGuid,
                                            registrationQualifiedName,
                                            derivedColumnEntity.getGUID(),
                                            businessTermGuid,
                                            new InstanceProperties());
    }


    /**
     * @param userId id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param sources list of sources describing the report column
     * @param derivedColumnEntity entity describing the derived column
     */
    public void addQueryTargets(String userId, String registrationGuid, String registrationQualifiedName, List<Source> sources, EntityDetail derivedColumnEntity) {
        Optional.ofNullable(sources)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .forEach(s -> {String sourceColumnGUID = entityReferenceResolver.resolveSourceGuid(s);
                              addQueryTarget(userId, registrationGuid, registrationQualifiedName, derivedColumnEntity.getGUID(), sourceColumnGUID, "");});

    }


    /**
     * @param userId - id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param columnGuid guid of the column
     * @param businessTerm business term to link or update to column
     * @param existingAssignments list of existing relationships of type semantic assignment
     */
    protected Relationship createOrUpdateSemanticAssignment(String userId, String registrationGuid,
                                                    String registrationQualifiedName,
                                                    String columnGuid,
                                                    BusinessTerm businessTerm,
                                                    List<Relationship> existingAssignments) {
            String businessTermAssignedToColumnGuid = entityReferenceResolver.getBusinessTermGuid(businessTerm);
            return Optional.ofNullable(existingAssignments).map(Collection::stream)
                                                            .orElseGet(Stream::empty)
                                                            .filter(e -> e.getEntityTwoProxy().getGUID().equals(businessTermAssignedToColumnGuid))
                                                            .findFirst()
                                                            .orElseGet(() -> omEntityDao.addExternalRelationship(userId,
                                                                                                                Constants.SEMANTIC_ASSIGNMENT,
                                                                                                                registrationGuid,
                                                                                                                registrationQualifiedName,
                                                                                                                columnGuid,
                                                                                                                businessTermAssignedToColumnGuid,
                                                                                                                new InstanceProperties()));
    }


    protected String buildQualifiedNameForSchemaType(String qualifiedNameForParent, String schemaType,
                                                     ReportElement element) {
        return QualifiedNameUtils.buildQualifiedName(qualifiedNameForParent, schemaType, element.getName() + Constants.TYPE_SUFFIX);
    }


    /**
     *
     * @param sources list of sources to be linked to column
     * @param columnGuid guid of column
     */
    protected void createOrUpdateSchemaQueryImplementation(List<Source> sources, String columnGuid)  {

        List<Relationship> existingRelationships = omEntityDao.getRelationships(Constants.SCHEMA_QUERY_IMPLEMENTATION, columnGuid);
        List<String> validRelationships = sources.stream().map(source -> addQueryTarget(columnGuid, source,
                existingRelationships).getGUID()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(existingRelationships) ) {
            existingRelationships.stream().filter(r -> !validRelationships.contains(r.getGUID())).forEach(omEntityDao::purgeRelationship);
        }
    }

    private Relationship addQueryTarget(String columnGuid, Source source, List<Relationship> existingRelationships) {
        String sourceColumnGuid = entityReferenceResolver.resolveSourceGuid(source);
        if (!StringUtils.isEmpty(sourceColumnGuid)) {
            log.debug("source {} found.", source);
            return Optional.ofNullable(existingRelationships)
                                                            .map(Collection::stream)
                                                            .orElseGet(Stream::empty)
                                                            .filter(r -> r.getEntityTwoProxy().getGUID().equals(sourceColumnGuid) ||
                                                                    r.getEntityOneProxy().getGUID().equals(sourceColumnGuid))
                                                            .findFirst()
                                                            .orElseGet(() ->{
                                                                InstanceProperties schemaQueryImplProperties =
                                                                        new EntityPropertiesBuilder().withStringProperty(Constants.QUERY, "")
                                                                                .build();
                                                                return omEntityDao.addRelationship(Constants.SCHEMA_QUERY_IMPLEMENTATION,
                                                                                                columnGuid,
                                                                                                sourceColumnGuid,
                                                                                                schemaQueryImplProperties);});
        }
        log.error(MessageFormat.format("source column not found, unable to add relationship {0} between column " +
                "{1} and source {2}", Constants.SCHEMA_QUERY_IMPLEMENTATION, columnGuid, source.toString()));
        throw buildAddRelationshipException(Constants.SCHEMA_QUERY_IMPLEMENTATION, null, this.getClass().getName());
    }

    /**
     *
     *
     * @param userId - id of user submitting the request
     * @param registrationGuid - guid of software server capability
     * @param registrationQualifiedName  - qualified name of software server capability
     * @param businessTerms list of business terms  to link to column
     * @param columnGuid guid of column
     */
    protected void createOrUpdateSemanticAssignments(String userId, String registrationGuid,
                                                     String registrationQualifiedName,
                                                     List<BusinessTerm> businessTerms,
                                                     String columnGuid) {
        List<Relationship> existingAssignments = omEntityDao.getRelationships(Constants.SEMANTIC_ASSIGNMENT, columnGuid);
        if (CollectionUtils.isEmpty(businessTerms)) {
            omEntityDao.purgeRelationships(existingAssignments);
        } else {
            businessTerms.forEach( bt -> createOrUpdateSemanticAssignment(userId, registrationGuid, registrationQualifiedName, columnGuid, bt, existingAssignments));
        }
    }
}