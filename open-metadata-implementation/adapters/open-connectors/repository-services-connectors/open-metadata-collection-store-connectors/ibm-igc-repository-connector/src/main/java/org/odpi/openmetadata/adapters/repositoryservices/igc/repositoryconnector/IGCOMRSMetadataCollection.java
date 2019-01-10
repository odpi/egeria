/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector;

import com.fasterxml.jackson.databind.JsonNode;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCVersionEnum;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchSorting;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.*;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.EntityMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.ReferenceableMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.PropertyMappingSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.model.OMRSStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;

/**
 * Provides the OMRSMetadataCollection implementation for IBM InfoSphere Information Governance Catalog ("IGC").
 */
public class IGCOMRSMetadataCollection extends OMRSMetadataCollectionBase {

    private static final Logger log = LoggerFactory.getLogger(IGCOMRSMetadataCollection.class);

    public static final String MAPPING_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.";
    public static final String DEFAULT_IGC_TYPE = "main_object";
    public static final String DEFAULT_IGC_TYPE_DISPLAY_NAME = "Main Object";

    public static final String GENERATED_TYPE_PREFIX = "__|";
    public static final String GENERATED_TYPE_POSTFIX = "|__";

    private IGCRestClient igcRestClient;

    private HashSet<ImplementedMapping> implementedMappings = new HashSet<>();

    private XMLOutputFactory xmlOutputFactory;

    /**
     * @param parentConnector      connector that this metadata collection supports.
     *                             The connector has the information to call the metadata repository.
     * @param repositoryName       name of this repository.
     * @param repositoryHelper     helper that provides methods to repository connectors and repository event mappers
     *                             to build valid type definitions (TypeDefs), entities and relationships.
     * @param repositoryValidator  validator class for checking open metadata repository objects and parameters
     * @param metadataCollectionId unique identifier for the repository
     */
    public IGCOMRSMetadataCollection(IGCOMRSRepositoryConnector parentConnector,
                                     String repositoryName,
                                     OMRSRepositoryHelper repositoryHelper,
                                     OMRSRepositoryValidator repositoryValidator,
                                     String metadataCollectionId) {
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);
        this.igcRestClient = parentConnector.getIGCRestClient();
        upsertOMRSBundleZip();
        this.igcRestClient.registerPOJO(OMRSStub.class);
        this.xmlOutputFactory = XMLOutputFactory.newInstance();
    }

    /**
     * Generates a zip file for the OMRS OpenIGC bundle, needed to enable change tracking for the event mapper.
     */
    private void upsertOMRSBundleZip() {

        ClassPathResource bundleResource = new ClassPathResource("/bundleOMRS");
        try {
            File bundle = this.igcRestClient.createOpenIgcBundleFile(bundleResource.getFile());
            if (bundle != null) {
                this.igcRestClient.upsertOpenIgcBundle("OMRS", bundle.getAbsolutePath());
            } else {
                log.error("Unable to generate OMRS bundle.");
            }
        } catch (IOException e) {
            log.error("Unable to open bundle resource for OMRS.", e);
        }

    }

    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * (Currently only full type definitions (TypeDefs) are implemented.)
     *
     * @param userId unique identifier for requesting user.
     * @return TypeDefGalleryResponse List of different categories of type definitions.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDefGallery getAllTypes(String   userId) throws RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String                       methodName = "getAllTypes";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery typeDefGallery = new TypeDefGallery();
        ArrayList<TypeDef> typeDefs = new ArrayList<>();
        for (ImplementedMapping implementedMapping : implementedMappings) {
            typeDefs.add(implementedMapping.getTypeDef());
        }
        typeDefGallery.setTypeDefs(typeDefs);

        return typeDefGallery;
    }

    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the name is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDef getTypeDefByName(String    userId,
                                    String    name) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName = "getTypeDefByName";
        final String  nameParameterName = "name";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeName(repositoryName, nameParameterName, name, methodName);

        /*
         * Perform operation
         */
        TypeDef typeDef = null;
        for (ImplementedMapping implementedMapping : implementedMappings) {
            if (implementedMapping.getTypeDef().getName().equals(name)) {
                typeDef = implementedMapping.getTypeDef();
                break;
            }
        }
        if (typeDef == null) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    name,
                    "unknown",
                    nameParameterName,
                    methodName,
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return typeDef;

    }

    /**
     * Verify that a definition of a TypeDef is either new or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDef TypeDef structure describing the TypeDef to test.
     * @return boolean true means the TypeDef matches the local definition; false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public boolean verifyTypeDef(String  userId,
                                 TypeDef typeDef) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotSupportedException,
            TypeDefConflictException,
            InvalidTypeDefException,
            UserNotAuthorizedException
    {
        final String  methodName           = "verifyTypeDef";
        final String  typeDefParameterName = "typeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, typeDef, methodName);

        // TODO: just need to handle AttributeTypeDefs now?

        TypeDefCategory typeDefCategory = typeDef.getCategory();
        String omrsTypeDefName = typeDef.getName();
        log.debug("Looking for mapping for {} of type {}", omrsTypeDefName, typeDefCategory.getName());

        boolean bMapperExists = false;

        // See if we have a Mapper defined for the class -- if so, it's implemented
        StringBuilder sbMapperClassname = new StringBuilder();
        sbMapperClassname.append(MAPPING_PKG);
        switch(typeDefCategory) {
            case RELATIONSHIP_DEF:
                sbMapperClassname.append("relationships.");
                break;
            case CLASSIFICATION_DEF:
                sbMapperClassname.append("classifications.");
                break;
            default:
                sbMapperClassname.append("entities.");
                break;
        }
        sbMapperClassname.append(omrsTypeDefName);
        sbMapperClassname.append("Mapper");

        try {
            Class mappingClass = Class.forName(sbMapperClassname.toString());
            bMapperExists = (mappingClass != null);
            log.debug(" ... found mapping class: {}", mappingClass);

            ImplementedMapping implementedMapping = new ImplementedMapping(
                    typeDef,
                    mappingClass,
                    (IGCOMRSRepositoryConnector)parentConnector,
                    userId
            );
            implementedMapping.registerPOJOs(this.igcRestClient);
            implementedMappings.add(implementedMapping);

        } catch (ClassNotFoundException e) {
            log.info("Unable to find Mapper for {}", omrsTypeDefName);
        }

        return bMapperExists;

    }

    /**
     * Return a requested relationship. Note that currently this will only work for relationships known to
     * (originated within) IGC.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @return a relationship structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the metadata collection does not have a relationship with
     *                                         the requested GUID stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship getRelationship(String    userId,
                                        String    guid) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName = "getRelationship";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Process operation
         */

        // Translate the key properties of the GUID into IGC-retrievables
        String proxyOneRid = RelationshipMapping.getProxyOneGUIDFromRelationshipGUID(guid);
        String proxyTwoRid = RelationshipMapping.getProxyTwoGUIDFromRelationshipGUID(guid);
        String omrsRelationshipName = RelationshipMapping.getRelationshipTypeFromRelationshipGUID(guid);

        String proxyOneIgcRid = proxyOneRid;
        String proxyTwoIgcRid = proxyTwoRid;

        if (isGeneratedGUID(proxyOneRid)) {
            proxyOneIgcRid = getRidFromGeneratedId(proxyOneRid);
        }
        if (isGeneratedGUID(proxyTwoRid)) {
            proxyTwoIgcRid = getRidFromGeneratedId(proxyTwoRid);
        }

        log.debug("Looking up relationship: {}", guid);

        // Should not need to translate from proxyone / proxytwo to alternative assets, as the RIDs provided
        // in the relationship GUID should already be pointing to the correct assets
        String relationshipLevelRid = (proxyOneRid.equals(proxyTwoRid)) ? proxyOneRid : null;
        Reference proxyOne;
        Reference proxyTwo;
        RelationshipMapping relationshipMapping;
        if (relationshipLevelRid != null) {
            Reference relationshipAsset = igcRestClient.getAssetRefById(proxyOneIgcRid);
            String relationshipAssetType = relationshipAsset.getType();
            relationshipMapping = getRelationshipMapper(
                    omrsRelationshipName,
                    relationshipAssetType,
                    relationshipAssetType
            );
            // Only need to translate if the RIDs are relationship-level RIDs
            proxyOne = relationshipMapping.getProxyOneAssetFromAsset(relationshipAsset, igcRestClient).get(0);
            proxyTwo = relationshipMapping.getProxyTwoAssetFromAsset(relationshipAsset, igcRestClient).get(0);
        } else {
            proxyOne = igcRestClient.getAssetRefById(proxyOneIgcRid);
            proxyTwo = igcRestClient.getAssetRefById(proxyTwoIgcRid);
            relationshipMapping = getRelationshipMapper(
                    omrsRelationshipName,
                    proxyOne.getType(),
                    proxyTwo.getType()
            );
        }

        Relationship found = null;

        if (relationshipMapping != null) {
            try {

                RelationshipDef omrsRelationshipDef = (RelationshipDef) getTypeDefByName(userId, omrsRelationshipName);
                // Since the ordering should be set by the GUID we're lookup up anyway, we'll simply set the property
                // to one of the proxyOne properties
                String igcPropertyName = relationshipMapping.getProxyOneMapping().getIgcRelationshipProperties().get(0);
                log.debug(" ... using property: {}", igcPropertyName);
                found = RelationshipMapping.getMappedRelationship(
                        (IGCOMRSRepositoryConnector)parentConnector,
                        relationshipMapping,
                        omrsRelationshipDef,
                        proxyOne,
                        proxyTwo,
                        igcPropertyName,
                        userId,
                        relationshipLevelRid
                );

            } catch (TypeDefNotKnownException e) {
                OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                        omrsRelationshipName,
                        guid,
                        guidParameterName,
                        methodName,
                        repositoryName);
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        } else {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    omrsRelationshipName,
                    guid,
                    guidParameterName,
                    methodName,
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return found;

    }

    /**
     * Return the relationships for a specific entity. Note that currently this will only work for relationships known
     * to (originated within) IGC, and that not all parameters are (yet) implemented.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus Must be null (relationship status is not implemented for IGC).
     * @param asOfTime Must be null (history not implemented for IGC).
     * @param sequencingProperty Must be null (there are no properties on IGC relationships).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize -- the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return Relationships list.  Null means no relationships associated with the entity.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws PropertyErrorException the sequencing property is not valid for the attached classifications.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<Relationship> getRelationshipsForEntity(String                     userId,
                                                        String                     entityGUID,
                                                        String                     relationshipTypeGUID,
                                                        int                        fromRelationshipElement,
                                                        List<InstanceStatus>       limitResultsByStatus,
                                                        Date                       asOfTime,
                                                        String                     sequencingProperty,
                                                        SequencingOrder            sequencingOrder,
                                                        int                        pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "getRelationshipsForEntity";
        final String  guidParameterName = "entityGUID";
        final String  typeGUIDParameter = "relationshipTypeGUID";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, entityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameter, relationshipTypeGUID, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Perform operation
         */
        ArrayList<Relationship> alRelationships = new ArrayList<>();

        // Immediately throw unimplemented exception if trying to limit by status or retrieve historical view
        if (limitResultsByStatus != null
                || asOfTime != null
                || sequencingProperty != null
                || (sequencingOrder != null
                    &&
                    (sequencingOrder.equals(SequencingOrder.PROPERTY_ASCENDING)
                    || sequencingOrder.equals(SequencingOrder.PROPERTY_DESCENDING)))
        ) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {

            // 0. see if the entityGUID has a prefix (indicating a generated type)
            String rid = getRidFromGeneratedId(entityGUID);
            String prefix = getPrefixFromGeneratedId(entityGUID);

            // 1. retrieve entity from IGC by GUID (RID)
            Reference asset = this.igcRestClient.getAssetRefById(rid);

            // Ensure the entity actually exists (if not, throw error to that effect)
            if (asset == null) {
                OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                        this.getClass().getName(),
                        repositoryName);
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            } else {

                ReferenceableMapper referenceMapper = getMapperForParameters(asset, prefix, userId);

                if (referenceMapper != null) {
                    // 2. Apply the mapping to the object, and retrieve the resulting relationships
                    alRelationships.addAll(
                            referenceMapper.getOMRSRelationships(
                                    relationshipTypeGUID,
                                    fromRelationshipElement,
                                    sequencingOrder,
                                    pageSize)
                    );
                } else {
                    OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN_FOR_INSTANCE;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                            prefix + asset.getType(),
                            "IGC asset",
                            methodName,
                            repositoryName);
                    throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }

            }

        }

        return alRelationships.isEmpty() ? null : alRelationships;

    }

    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties List of entity properties to match to (null means match on entityTypeGUID only).
     * @param matchCriteria Enum defining how the properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus Must be null (relationship status is not implemented for IGC).
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Must be null (history not implemented for IGC).
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection.
     *
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<EntityDetail> findEntitiesByProperty(String                    userId,
                                                      String                    entityTypeGUID,
                                                      InstanceProperties        matchProperties,
                                                      MatchCriteria             matchCriteria,
                                                      int                       fromEntityElement,
                                                      List<InstanceStatus>      limitResultsByStatus,
                                                      List<String>              limitResultsByClassification,
                                                      Date                      asOfTime,
                                                      String                    sequencingProperty,
                                                      SequencingOrder           sequencingOrder,
                                                      int                       pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName                   = "findEntitiesByProperty";
        final String  matchCriteriaParameterName   = "matchCriteria";
        final String  matchPropertiesParameterName = "matchProperties";
        final String  typeGUIDParameterName        = "entityTypeGUID";
        final String  asOfTimeParameter            = "asOfTime";
        final String  pageSizeParameter            = "pageSize";

        // TODO: need to walk the hierarchy of types and ensure we do a search across all subtypes of (as well as
        //  base type received), eg. searching for 'Asset' should also search for RelationalTable, RelationalColumn,
        //  Database, etc, etc
        //  - also need search by classification for demo

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameterName, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName,
                matchCriteriaParameterName,
                matchPropertiesParameterName,
                matchCriteria,
                matchProperties,
                methodName);

        /*
         * Perform operation
         */
        ArrayList<EntityDetail> entityDetails = new ArrayList<>();

        // Immediately throw unimplemented exception if trying to limit by status or retrieve historical view
        if (limitResultsByStatus != null || asOfTime != null) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {

            IGCSearch igcSearch = new IGCSearch();

            ImplementedMapping mapping = getMappingForEntityType(entityTypeGUID);
            addTypeToSearch(mapping, igcSearch);

            /* We need to first retrieve the mapping so we know how to translate
             * the provided OMRS property names to IGC property names */
            PropertyMappingSet propertyMappingSet = getEntityPropertiesFromMapping(mapping, userId);

            /* Provided there is a mapping, build up a list of IGC-specific properties
             * and search criteria, based on the values of the InstanceProperties provided */
            ArrayList<String> properties = new ArrayList<>();
            IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet();

            Iterator iPropertyNames = matchProperties.getPropertyNames();
            while (propertyMappingSet != null && iPropertyNames.hasNext()) {
                String omrsPropertyName = (String) iPropertyNames.next();
                InstancePropertyValue value = matchProperties.getPropertyValue(omrsPropertyName);
                addSearchConditionFromValue(
                        igcSearchConditionSet,
                        omrsPropertyName,
                        properties,
                        propertyMappingSet,
                        value
                );
            }

            IGCSearchSorting igcSearchSorting = null;
            if (sequencingProperty == null && sequencingOrder != null) {
                igcSearchSorting = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);
            }

            if (matchCriteria != null) {
                switch(matchCriteria) {
                    case ALL:
                        igcSearchConditionSet.setMatchAnyCondition(false);
                        break;
                    case ANY:
                        igcSearchConditionSet.setMatchAnyCondition(true);
                        break;
                    case NONE:
                        igcSearchConditionSet.setMatchAnyCondition(false);
                        igcSearchConditionSet.setNegateAll(true);
                        break;
                }
            }

            igcSearch.addProperties(properties);
            igcSearch.addConditions(igcSearchConditionSet);

            setPagingForSearch(igcSearch, fromEntityElement, pageSize);

            if (igcSearchSorting != null) {
                igcSearch.addSortingCriteria(igcSearchSorting);
            }

            processResults(this.igcRestClient.search(igcSearch), entityDetails, pageSize, userId);

        }

        return entityDetails.isEmpty() ? null : entityDetails;

    }

    /**
     * Retrieve any mapping that exists for the provided entityTypeGUID (or null if there are none).
     *
     * @param entityTypeGUID the OMRS entityTypeGUID for which to find a mapping
     * @return EntityMapping
     */
    private ImplementedMapping getMappingForEntityType(String entityTypeGUID) {
        ImplementedMapping mapping = null;
        for (ImplementedMapping implementedMapping : implementedMappings) {
            if (entityTypeGUID.equals(implementedMapping.getTypeDef().getGUID())) {
                mapping = implementedMapping;
            }
        }
        return mapping;
    }

    /**
     * Retrieve a mapping from IGC property name to the OMRS relationship type it represents.
     *
     * @param assetType the IGC asset type for which to find mappings
     * @param userId the userId making the request
     * @return Map<String, RelationshipMapping> - keyed by IGC asset type with values of the RelationshipMappings
     */
    public Map<String, List<RelationshipMapping>> getIgcPropertiesToRelationshipMappings(String assetType, String userId) {

        HashMap<String, List<RelationshipMapping>> map = new HashMap<>();

        List<ReferenceableMapper> mappers = getMappers(assetType, userId);
        for (ReferenceableMapper mapper : mappers) {
            List<RelationshipMapping> relationshipMappings = mapper.getRelationshipMappers();
            for (RelationshipMapping relationshipMapping : relationshipMappings) {
                if (relationshipMapping.getProxyOneMapping().matchesAssetType(assetType)) {
                    List<String> relationshipNamesOne = relationshipMapping.getProxyOneMapping().getIgcRelationshipProperties();
                    for (String relationshipName : relationshipNamesOne) {
                        if (!map.containsKey(relationshipName)) {
                            map.put(relationshipName, new ArrayList<>());
                        }
                        if (!map.get(relationshipName).contains(relationshipMapping)) {
                            map.get(relationshipName).add(relationshipMapping);
                        }
                    }
                }
                if (relationshipMapping.getProxyTwoMapping().matchesAssetType(assetType)) {
                    List<String> relationshipNamesTwo = relationshipMapping.getProxyTwoMapping().getIgcRelationshipProperties();
                    for (String relationshipName : relationshipNamesTwo) {
                        if (!map.containsKey(relationshipName)) {
                            map.put(relationshipName, new ArrayList<>());
                        }
                        if (!map.get(relationshipName).contains(relationshipMapping)) {
                            map.get(relationshipName).add(relationshipMapping);
                        }
                    }
                }
            }
        }

        return map;

    }

    /**
     * Add the type to search based on the provided mapping.
     *
     * @param mapping the mapping on which to base the search
     * @param igcSearch the IGC search object to which to add the criteria
     */
    private void addTypeToSearch(ImplementedMapping mapping, IGCSearch igcSearch) {
        if (mapping == null) {
            // If no TypeDef was provided, run against all types
            igcSearch.addType(DEFAULT_IGC_TYPE);
        } else {
            igcSearch.addType(mapping.getIgcAssetType());
        }
    }

    /**
     * Retrieve the property mappings from the mapping.
     *
     * @param mapping the mapping from which to retrieve property mappings
     * @param userId the userId making the request
     * @return PropertyMappingSet
     */
    private PropertyMappingSet getEntityPropertiesFromMapping(ImplementedMapping mapping, String userId) {
        PropertyMappingSet propertyMappingSet = null;
        if (mapping != null) {
            EntityMapping entityMapping = mapping.getEntityMapping();
            if (entityMapping != null) {
                propertyMappingSet = entityMapping.getPropertyMappings();
            }
        }
        return propertyMappingSet;
    }

    /**
     * Setup paging properties of the IGC search.
     *
     * @param igcSearch the IGC search object to which to add the criteria
     * @param beginAt the starting index for results
     * @param pageSize the number of results to include in each page
     */
    private void setPagingForSearch(IGCSearch igcSearch, int beginAt, int pageSize) {
        if (pageSize > 0) {
            /* Only set pageSize if it has been provided; otherwise we'll end up defaulting to IGC's
             * minimal pageSize of 10 (so will need to make many calls to get all pages) */
            igcSearch.setPageSize(pageSize);
        } else {
            /* So if none has been specified, we'll set a large pageSize to be able to more efficiently
             * retrieve all pages of results */
            igcSearch.setPageSize(parentConnector.getMaxPageSize());
        }
        igcSearch.setBeginAt(beginAt);
    }

    /**
     * Process the search results into the provided list of EntityDetail objects.
     *
     * @param results the IGC search results
     * @param entityDetails the list of EntityDetails to append
     * @param pageSize the number of results per page (0 for all results)
     * @param userId the user making the request
     */
    private void processResults(ReferenceList results,
                                List<EntityDetail> entityDetails,
                                int pageSize,
                                String userId) throws RepositoryErrorException {

        if (pageSize == 0) {
            // If the provided pageSize was 0, we need to retrieve ALL pages of results...
            results.getAllPages(this.igcRestClient);
        }

        for (Reference reference : results.getItems()) {
            /* Only proceed with retrieving the EntityDetail if the type from IGC is not explicitly
             * a 'main_object' (as these are non-API-accessible asset types in IGC like column analysis master,
             * etc and will simply result in 400-code Bad Request messages from the API) */
            if (!reference.getType().equals(DEFAULT_IGC_TYPE)) {
                EntityDetail ed = null;

                List<ReferenceableMapper> mappers = getMappers(reference.getType(), userId);
                for (ReferenceableMapper mapper : mappers) {
                    log.debug("processResults with mapper: {}", mapper.getClass().getCanonicalName());
                    if (mapper.igcRidNeedsPrefix()) {
                        log.debug(" ... prefix required, getEntityDetail with: {}", mapper.getIgcRidPrefix() + reference.getId());
                        ed = getEntityDetail(userId, mapper.getIgcRidPrefix() + reference.getId(), reference);
                    } else {
                        log.debug(" ... no prefix required, getEntityDetail with: {}", reference.getId());
                        ed = getEntityDetail(userId, reference.getId(), reference);
                    }
                    if (ed != null) {
                        entityDetails.add(ed);
                    }
                }
            }
        }

        // If we haven't filled a page of results (because we needed to skip some above), recurse...
        if (results.hasMorePages() && entityDetails.size() < pageSize) {
            results.getNextPage(this.igcRestClient);
            processResults(results, entityDetails, pageSize, userId);
        }

    }

    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String expression contained in any of the property values within the entities
     *                       of the supplied type.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus Must be null (status is not implemented for IGC).
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     *                                     (currently not implemented for IGC)
     * @param asOfTime Must be null (history not implemented for IGC).
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     *                           (currently not implemented for IGC)
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<EntityDetail> findEntitiesByPropertyValue(String                userId,
                                                           String                entityTypeGUID,
                                                           String                searchCriteria,
                                                           int                   fromEntityElement,
                                                           List<InstanceStatus>  limitResultsByStatus,
                                                           List<String>          limitResultsByClassification,
                                                           Date                  asOfTime,
                                                           String                sequencingProperty,
                                                           SequencingOrder       sequencingOrder,
                                                           int                   pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "findEntitiesByPropertyValue";
        final String  searchCriteriaParameterName = "searchCriteria";
        final String  typeGUIDParameter = "entityTypeGUID";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateSearchCriteria(repositoryName, searchCriteriaParameterName, searchCriteria, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameter, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Process operation
         */
        ArrayList<EntityDetail> entityDetails = new ArrayList<>();

        // Immediately throw unimplemented exception if trying to limit by status or retrieve historical view
        if (limitResultsByStatus != null || asOfTime != null) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {

            // TODO:
            //  - POST'd search to IGC doesn't work on latest v11.7.0.2+ using long_description; suggestion
            //    to instead use "searchText" (TBD; may need to drop 'long_description' from v11.7 search in
            //    meantime)
            //  - using "searchText" requires using "searchProperties" (no "where" conditions) -- but does not
            //    work with 'main_object', must be used with a specific asset type

            IGCSearch igcSearch = new IGCSearch();

            ImplementedMapping mapping = getMappingForEntityType(entityTypeGUID);
            PropertyMappingSet propertyMappingSet = getEntityPropertiesFromMapping(mapping, userId);
            addTypeToSearch(mapping, igcSearch);

            String[] properties = null;
            if (propertyMappingSet != null) {
                properties = propertyMappingSet.getAllMappedIgcProperties().toArray(new String[0]);
            } else {
                /* Since IGC requires the set of properties against which to search,
                 * if no type has been provided we'll use the generic set of the following
                 * properties for the search (common to all objects) */
                properties = new String[] {
                        "name",
                        "short_description",
                        "long_description"
                };
            }

            IGCSearchSorting igcSearchSorting = null;
            if (sequencingProperty == null && sequencingOrder != null) {
                igcSearchSorting = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);
            }

            IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet();
            igcSearchConditionSet.setMatchAnyCondition(true);
            for (String property : properties) {
                igcSearchConditionSet.addCondition(new IGCSearchCondition(
                        property,
                        "like %{0}%",
                        searchCriteria
                ));
            }

            igcSearch.addConditions(igcSearchConditionSet);

            setPagingForSearch(igcSearch, fromEntityElement, pageSize);

            if (igcSearchSorting != null) {
                igcSearch.addSortingCriteria(igcSearchSorting);
            }

            processResults(this.igcRestClient.search(igcSearch), entityDetails, pageSize, userId);

        }

        return entityDetails.isEmpty() ? null : entityDetails;

    }

    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail getEntityDetail(String userId,
                                        String guid) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityProxyOnlyException,
            UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */

        // Lookup the basic asset based on the RID (strip off prefix (indicating a generated type), if there)
        String rid = getRidFromGeneratedId(guid);
        Reference asset = this.igcRestClient.getAssetRefById(rid);

        return getEntityDetail(userId, guid, asset);

    }

    /**
     * Return the header, classifications and properties of a specific entity, using the provided IGC asset.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param asset the IGC asset for which an EntityDetail should be constructed.
     * @return EntityDetail structure.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     */
    public EntityDetail getEntityDetail(String userId, String guid, Reference asset) throws RepositoryErrorException {

        final String  methodName        = "getEntityDetail";

        log.debug("getEntityDetail with guid = {}", guid);

        EntityDetail detail = null;
        String prefix = getPrefixFromGeneratedId(guid);

        // If we could not find any asset by the provided guid, throw an ENTITY_NOT_KNOWN exception
        if (asset == null) {
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid,
                    methodName,
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else if (asset.getType().equals(DEFAULT_IGC_TYPE)) {
            /* If the asset type returned has an IGC-listed type of 'main_object', it isn't one that the REST API
             * of IGC supports (eg. a data rule detail object, a column analysis master object, etc)...
             * Trying to further process it will result in failed REST API requests; so we should skip these objects */
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_ENTITY_FROM_STORE;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid,
                    repositoryName,
                    methodName,
                    asset.toString());
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {

            // Otherwise, retrieve the mapping dynamically based on the type of asset
            ReferenceableMapper referenceMapper = getMapperForParameters(asset, prefix, userId);

            if (referenceMapper != null) {
                // 2. Apply the mapping to the object, and retrieve the resulting EntityDetail
                detail = referenceMapper.getOMRSEntityDetail();
            } else {
                OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN_FOR_INSTANCE;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                        prefix + asset.getType(),
                        "IGC asset",
                        methodName,
                        repositoryName);
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }

        }

        return detail;

    }

    /**
     * Retrieves the Mapper that can be used for the provided parameters (or null if none exists).
     *
     * @param igcObject the IGC asset
     * @param prefix the prefix used for the asset (if any; null otherwise)
     * @param userId the user making the request
     * @return
     */
    public ReferenceableMapper getMapperForParameters(Reference igcObject, String prefix, String userId) {

        String igcAssetType = igcObject.getType();

        log.debug("Looking for mapper for type {} with prefix {}", igcAssetType, prefix);

        List<ReferenceableMapper> mappers = getMappers(igcAssetType, userId);

        ReferenceableMapper candidateMapper = null;
        for (int i = 0; i < mappers.size(); i++) {
            candidateMapper = mappers.get(i);
            String mapperPrefix = candidateMapper.getIgcRidPrefix();
            if ( (prefix == null && mapperPrefix == null)
                    || (prefix != null && mapperPrefix != null && mapperPrefix.equals(prefix)) ) {
                // If we didn't receive any prefix and this Mapper doesn't use a prefix, use it
                // or if we did receive a prefix and it matches this Mapper's prefix, use it
                break;
            }
            // Otherwise keep looping until we find one that meets above criteria (or we run out of options)
        }

        ReferenceableMapper referenceMapper = null;
        if (candidateMapper != null) {
            log.debug("Found mapper class: {}", candidateMapper.getClass().getCanonicalName());
            // Translate the provided asset to a base asset type for the mapper, if needed
            // (if not needed the 'getBaseIgcAssetFromAlternative' is effecitively a NOOP and gives back same object)
            referenceMapper = candidateMapper.initialize(
                    candidateMapper.getBaseIgcAssetFromAlternative(igcObject),
                    igcObject);
        } else {
            log.debug("No mapper class found!");
        }
        return referenceMapper;

    }

    /**
     * Retrieves the classes to use for mapping the provided IGC asset type to an OMRS entity.
     *
     * @param igcAssetType the name of the IGC asset type
     * @return List<Class>
     */
    public List<ReferenceableMapper> getMappers(String igcAssetType, String userId) {
        ArrayList<ReferenceableMapper> mappers = new ArrayList<>();
        for (ImplementedMapping mapping : implementedMappings) {
            String mappingAssetType = mapping.getIgcAssetType();
            // For now skip over the default Referenceable mapping, if no other candidate is found we will add
            // this default mapping back in below
            if (mappingAssetType != null && !mappingAssetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                if (mapping.matchesAssetType(igcAssetType)) {
                    log.debug(" ... adding candidate: {}", mapping.getEntityMapping().getClass().getCanonicalName());
                    mappers.add((ReferenceableMapper) mapping.getEntityMapping());
                } else {
                    for (String otherType : mapping.getOtherIgcAssetTypes()) {
                        if (igcAssetType.equals(otherType)) {
                            log.debug(" ... adding other candidate: {}", mapping.getEntityMapping().getClass().getCanonicalName());
                            mappers.add((ReferenceableMapper) mapping.getEntityMapping());
                        }
                    }
                }
            }
        }
        if (mappers.isEmpty()) {
            try {
                ReferenceableMapper defaultMapper = (ReferenceableMapper) ImplementedMapping.getEntityMapper(
                        Class.forName(MAPPING_PKG + "entities.ReferenceableMapper"),
                        (IGCOMRSRepositoryConnector)parentConnector,
                        userId
                );
                mappers.add(defaultMapper);
            } catch (ClassNotFoundException e) {
                log.error("Unable to find default ReferenceableMapper class: " + MAPPING_PKG + "entities.ReferenceableMapper", e);
            }
        }
        return mappers;
    }

    /**
     * Retrieves a RelationshipMapping by OMRS relationship type from those that are listed as implemented.
     *
     * @param omrsRelationshipType the name of the OMRS relationship type for which to retrieve a mapping
     * @return RelationshipMapping
     */
    public RelationshipMapping getRelationshipMapper(String omrsRelationshipType,
                                                     String proxyOneType,
                                                     String proxyTwoType) {
        RelationshipMapping found = null;
        for (ImplementedMapping mapping : implementedMappings) {
            RelationshipMapping candidate = mapping.getRelationshipMapping();
            // If the mapping has a relationship mapping, and its type equals the one we're looking for,
            // set it to found and short-circuit out of the loop
            if (candidate != null) {
                if (matchingRelationshipMapper(candidate, omrsRelationshipType, proxyOneType, proxyTwoType)) {
                    found = candidate;
                    break;
                } else if (candidate.hasSubTypes()) {
                    for (RelationshipMapping subMapping : candidate.getSubTypes()) {
                        if (matchingRelationshipMapper(subMapping, omrsRelationshipType, proxyOneType, proxyTwoType)) {
                            found = subMapping;
                            break;
                        }
                    }
                } else if (candidate.hasRelationshipLevelAsset()) {
                    String relationshipLevelAssetType = candidate.getRelationshipLevelIgcAsset();
                    if (proxyOneType.equals(relationshipLevelAssetType) && proxyTwoType.equals(relationshipLevelAssetType)) {
                        found = candidate;
                        break;
                    }
                }

            }
        }
        return found;
    }

    /**
     * Indicates whether the provided relationship mapping matches the provided criteria.
     *
     * @param candidate the relationship mapping to check
     * @param omrsRelationshipType the OMRS relationship type to confirm
     * @param proxyOneType the asset type of endpoint 1 of the relationship to confirm
     * @param proxyTwoType the asset type of endpoint 2 of the relationship to confirm
     * @return
     */
    private boolean matchingRelationshipMapper(RelationshipMapping candidate,
                                               String omrsRelationshipType,
                                               String proxyOneType,
                                               String proxyTwoType) {
        return (candidate != null
                && candidate.getOmrsRelationshipType().equals(omrsRelationshipType)
                && candidate.getProxyOneMapping().matchesAssetType(proxyOneType)
                && candidate.getProxyTwoMapping().matchesAssetType(proxyTwoType));
    }

    /**
     * Retrieves the OMRS TypeDefs that are mapped to by the provided IGC asset display name.
     *
     * @param igcAssetName the display name of the IGC asset type
     * @return List<TypeDef>
     */
    public List<TypeDef> getTypeDefsForAssetName(String igcAssetName) {
        ArrayList<TypeDef> typeDefs = new ArrayList<>();
        for (ImplementedMapping mapping : implementedMappings) {
            if (igcAssetName.equals(mapping.getIgcAssetTypeDisplayName())) {
                typeDefs.add(mapping.getTypeDef());
            }
        }
        return typeDefs;
    }

    /**
     * Retrieves the IGC asset type from the provided IGC asset display name (only for those assets that have
     * a mapping implemented). If none is found, will return null.
     *
     * @param igcAssetName the display name of the IGC asset type
     * @return String
     */
    public String getIgcAssetTypeForAssetName(String igcAssetName) {
        String assetType = null;
        for (ImplementedMapping mapping : implementedMappings) {
            if (igcAssetName.equals(mapping.getIgcAssetTypeDisplayName())) {
                assetType = mapping.getIgcAssetType();
                break;
            }
        }
        return assetType;
    }

    /**
     * Adds the provided value to the search criteria for IGC.
     *
     * @param igcSearchConditionSet the search conditions to which to add the criteria
     * @param omrsPropertyName the OMRS property name to search
     * @param igcProperties the list of IGC properties to which to add for inclusion in the IGC search
     * @param propertyMappingSet the property mappings
     * @param value the value for which to search
     */
    public void addSearchConditionFromValue(IGCSearchConditionSet igcSearchConditionSet,
                                            String omrsPropertyName,
                                            List<String> igcProperties,
                                            PropertyMappingSet propertyMappingSet,
                                            InstancePropertyValue value) {

        String igcPropertyName = propertyMappingSet.getIgcPropertyName(omrsPropertyName);
        if (igcPropertyName != null) {
            igcProperties.add(igcPropertyName);
            InstancePropertyCategory category = value.getInstancePropertyCategory();
            switch (category) {
                case PRIMITIVE:
                    PrimitivePropertyValue actualValue = (PrimitivePropertyValue) value;
                    PrimitiveDefCategory primitiveType = actualValue.getPrimitiveDefCategory();
                    switch (primitiveType) {
                        case OM_PRIMITIVE_TYPE_BOOLEAN:
                        case OM_PRIMITIVE_TYPE_BYTE:
                        case OM_PRIMITIVE_TYPE_CHAR:
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "=",
                                    actualValue.getPrimitiveValue().toString()
                            ));
                            break;
                        case OM_PRIMITIVE_TYPE_SHORT:
                        case OM_PRIMITIVE_TYPE_INT:
                        case OM_PRIMITIVE_TYPE_LONG:
                        case OM_PRIMITIVE_TYPE_FLOAT:
                        case OM_PRIMITIVE_TYPE_DOUBLE:
                        case OM_PRIMITIVE_TYPE_BIGINTEGER:
                        case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "=",
                                    actualValue.getPrimitiveValue().toString()
                            ));
                            break;
                        case OM_PRIMITIVE_TYPE_DATE:
                            Date date = (Date) actualValue.getPrimitiveValue();
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "=",
                                    "" + date.getTime()
                            ));
                            break;
                        case OM_PRIMITIVE_TYPE_STRING:
                        default:
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "like %{0}%",
                                    actualValue.getPrimitiveValue().toString()
                            ));
                            break;
                    }
                    break;
                case ENUM:
                    igcSearchConditionSet.addCondition(new IGCSearchCondition(
                            igcPropertyName,
                            "=",
                            ((EnumPropertyValue) value).getSymbolicName()
                    ));
                    break;
                case STRUCT:
                    Map<String, InstancePropertyValue> structValues = ((StructPropertyValue) value).getAttributes().getInstanceProperties();
                    for (Map.Entry<String, InstancePropertyValue> nextEntry : structValues.entrySet()) {
                        addSearchConditionFromValue(
                                igcSearchConditionSet,
                                nextEntry.getKey(),
                                igcProperties,
                                propertyMappingSet,
                                nextEntry.getValue()
                        );
                    }
                    break;
                case MAP:
                    Map<String, InstancePropertyValue> mapValues = ((MapPropertyValue) value).getMapValues().getInstanceProperties();
                    for (Map.Entry<String, InstancePropertyValue> nextEntry : mapValues.entrySet()) {
                        addSearchConditionFromValue(
                                igcSearchConditionSet,
                                nextEntry.getKey(),
                                igcProperties,
                                propertyMappingSet,
                                nextEntry.getValue()
                        );
                    }
                    break;
                case ARRAY:
                    Map<String, InstancePropertyValue> arrayValues = ((ArrayPropertyValue) value).getArrayValues().getInstanceProperties();
                    for (Map.Entry<String, InstancePropertyValue> nextEntry : arrayValues.entrySet()) {
                        addSearchConditionFromValue(
                                igcSearchConditionSet,
                                igcPropertyName,
                                igcProperties,
                                propertyMappingSet,
                                nextEntry.getValue()
                        );
                    }
                    break;
                default:
                    // Do nothing
                    break;
            }
        }

    }

    /**
     * Retrieves the RID from a generated GUID (or the GUID if it is not generated).
     *
     * @param guid the guid to translate
     * @return String
     */
    public static final String getRidFromGeneratedId(String guid) {
        if (isGeneratedGUID(guid)) {
            return guid
                    .substring(guid.indexOf(GENERATED_TYPE_POSTFIX) + GENERATED_TYPE_POSTFIX.length());
        } else {
            return guid;
        }
    }

    /**
     * Retrieves the generated prefix from a generated GUID (or null if the GUID is not generated).
     *
     * @param guid the guid from which to retrieve the prefix
     * @return String
     */
    public static final String getPrefixFromGeneratedId(String guid) {
        if (isGeneratedGUID(guid)) {
            return guid
                    .substring(0, guid.indexOf(GENERATED_TYPE_POSTFIX) + GENERATED_TYPE_POSTFIX.length());
        } else {
            return null;
        }
    }

    /**
     * Indicates whether the provided GUID was generated (true) or not (false).
     *
     * @param guid the guid to test
     * @return boolean
     */
    public static final boolean isGeneratedGUID(String guid) {
        return guid.startsWith(GENERATED_TYPE_PREFIX);
    }

    /**
     * Generates a unique type prefix for RIDs based on the provided moniker.
     *
     * @param moniker a repeatable way by which to refer to the type
     * @return String
     */
    public static final String generateTypePrefix(String moniker) {
        return GENERATED_TYPE_PREFIX + moniker + GENERATED_TYPE_POSTFIX;
    }

    /**
     * Retrieve an OMRS asset stub (shadow copy of last version of an asset) for the provided asset.
     * If there is no existing stub, will return null.
     *
     * @param asset the asset for which to retrieve the OMRS stub
     * @return OMRSStub
     */
    public OMRSStub getOMRSStubForAsset(Reference asset) {

        // We need to translate the provided asset into a unique name for the stub
        String stubName = getStubNameFromAsset(asset);
        IGCSearchCondition condition = new IGCSearchCondition(
                "name",
                "=",
                stubName
        );
        String[] properties = new String[]{ "$sourceRID", "$sourceType", "$payload" };
        IGCSearchConditionSet conditionSet = new IGCSearchConditionSet(condition);
        IGCSearch igcSearch = new IGCSearch("$OMRS-Stub", properties, conditionSet);
        ReferenceList results = igcRestClient.search(igcSearch);
        OMRSStub stub = null;
        if (results.getPaging().getNumTotal() > 0) {
            if (results.getPaging().getNumTotal() > 1) {
                log.warn("Found multiple stubs for asset, taking only the first: {}", stubName);
            }
            stub = (OMRSStub) results.getItems().get(0);
        } else {
            log.info("No stub found for asset: {}", stubName);
        }
        return stub;

    }

    /**
     * Update (or create if it does not already exist) the OMRS asset stub for the provided asset.
     * (Note that this method assumes you have already retrieved the full asset being provided.)
     *
     * @param asset the asset for which to upsert the OMRS stub
     * @return String the Repository ID (RID) of the OMRS stub
     */
    public String upsertOMRSStubForAsset(Reference asset) {

        String stubName = getStubNameFromAsset(asset);

        // Get the full asset details as a singular JSON payload
        String payload = igcRestClient.getValueAsJSON(asset);

        // Construct the asset XML document, including the full asset payload
        StringWriter stringWriter = new StringWriter();
        try {

            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");

            xmlStreamWriter.writeStartElement("doc");
            xmlStreamWriter.writeNamespace("xmlns", "http://www.ibm.com/iis/flow-doc");

            xmlStreamWriter.writeStartElement("assets");
            xmlStreamWriter.writeStartElement("asset");

            xmlStreamWriter.writeAttribute("class", "$OMRS-Stub");
            xmlStreamWriter.writeAttribute("repr", stubName);
            xmlStreamWriter.writeAttribute("ID", "stub1");

            addAttributeToAssetXML(xmlStreamWriter, "name", stubName);
            addAttributeToAssetXML(xmlStreamWriter, "$sourceType", asset.getType());
            addAttributeToAssetXML(xmlStreamWriter, "$sourceRID", asset.getId());
            addAttributeToAssetXML(xmlStreamWriter, "$payload", payload);

            xmlStreamWriter.writeEndElement(); // </asset>
            xmlStreamWriter.writeEndElement(); // </assets>

            xmlStreamWriter.writeStartElement("importAction");
            xmlStreamWriter.writeAttribute("completeAssetIDs", "stub1");
            xmlStreamWriter.writeEndElement(); // </importAction>

            xmlStreamWriter.writeEndElement(); // </doc>

            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();

        } catch (XMLStreamException e) {
            log.error("Unable to write XML stream: {}", asset, e);
        }

        String stubXML = stringWriter.getBuffer().toString();
        log.debug("Constructed XML for stub: {}", stubXML);

        // Upsert using the constructed asset XML
        JsonNode results = igcRestClient.upsertOpenIgcAsset(stubXML);

        return results.path("stub1").asText();

    }

    /**
     * Adds the provided attribute to the asset XML being constructed.
     *
     * @param xmlStreamWriter the asset XML being constructed
     * @param attrName the name of the attribute to add
     * @param attrValue the value of the attribute
     * @throws XMLStreamException
     */
    private void addAttributeToAssetXML(XMLStreamWriter xmlStreamWriter, String attrName, String attrValue) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("attribute");
        xmlStreamWriter.writeAttribute("name", attrName);
        xmlStreamWriter.writeAttribute("value", attrValue);
        xmlStreamWriter.writeEndElement(); // </attribute>
    }

    /**
     * Delete the OMRS asset stub for the provided asset details (cannot require the asset itself since it has
     * already been removed).
     *
     * @param rid the Repository ID (RID) of the asset for which to delete the OMRS stub
     * @param assetType the IGC asset type of the asset for which to delete the OMRS stub
     * @return boolean - true on successful deletion, false otherwise
     */
    public boolean deleteOMRSStubForAsset(String rid, String assetType) {

        String stubName = getStubNameForAsset(rid, assetType);

        // Construct the asset XML document, including the full asset payload
        StringWriter stringWriter = new StringWriter();
        try {

            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");

            xmlStreamWriter.writeStartElement("doc");
            xmlStreamWriter.writeNamespace("xmlns", "http://www.ibm.com/iis/flow-doc");

            xmlStreamWriter.writeStartElement("assets");
            xmlStreamWriter.writeStartElement("asset");

            xmlStreamWriter.writeAttribute("class", "$OMRS-Stub");
            xmlStreamWriter.writeAttribute("repr", stubName);
            xmlStreamWriter.writeAttribute("ID", "stub1");

            addAttributeToAssetXML(xmlStreamWriter, "name", stubName);

            xmlStreamWriter.writeEndElement(); // </asset>
            xmlStreamWriter.writeEndElement(); // </assets>

            xmlStreamWriter.writeStartElement("assetsToDelete");
            xmlStreamWriter.writeCharacters("stub1");
            xmlStreamWriter.writeEndElement(); // </assetsToDelete>

            xmlStreamWriter.writeEndElement(); // </doc>

            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();

        } catch (XMLStreamException e) {
            log.error("Unable to write XML stream.", e);
        }

        String stubXML = stringWriter.getBuffer().toString();
        log.debug("Constructed XML for stub deletion: {}", stubXML);

        // Delete using the constructed asset XML
        return igcRestClient.deleteOpenIgcAsset(stubXML);

    }

    /**
     * Construct the unique name for the OMRS stub based on the provided asset.
     *
     * @param asset the asset for which to construct the unique OMRS stub name
     * @return String
     */
    public static String getStubNameFromAsset(Reference asset) {
        return getStubNameForAsset(asset.getId(), asset.getType());
    }

    /**
     * Construct the unique name for the OMRS stub based on the provided asset information.
     *
     * @param rid the Repository ID (RID) of the asset for which to construct the unique OMRS stub name
     * @param assetType the asset type (REST form) of the asset for which to construct the unique OMRS stub name
     * @return String
     */
    public static String getStubNameForAsset(String rid, String assetType) {
        return assetType + "_" + rid;
    }

    /**
     * Retrieve all of the asset details, including all relationships, from the RID.
     * <br><br>
     * Note that this is quite a heavy operation, relying on multiple REST calls, to build up what could be a very
     * large object; to simply retrieve the details without all relationships, see getAssetDetails.
     *
     * @param rid the Repository ID (RID) of the asset for which to retrieve all details
     * @return Reference - the object including all of its details and relationships
     */
    public Reference getFullAssetDetails(String rid) {

        // Start by retrieving the asset header, so we can introspect the class itself
        Reference assetRef = igcRestClient.getAssetRefById(rid);
        Reference fullAsset = null;

        if (assetRef != null) {

            // Introspect the full list of properties from the POJO of the asset
            Class pojoClass = igcRestClient.getPOJOForType(assetRef.getType());
            if (pojoClass != null) {
                List<String> allProps = Reference.getAllPropertiesFromPOJO(pojoClass);

                // Retrieve all asset properties, via search, as this will allow larger page
                // retrievals (and therefore be overall more efficient) than going by the GET of the asset
                fullAsset = assetRef.getAssetWithSubsetOfProperties(
                        igcRestClient,
                        allProps.toArray(new String[0]),
                        igcRestClient.getDefaultPageSize()
                );

                if (fullAsset != null) {

                    // Iterate through all the paged properties and retrieve all pages for each
                    List<String> allPaged = Reference.getPagedRelationalPropertiesFromPOJO(pojoClass);
                    for (String pagedProperty : allPaged) {
                        ReferenceList pagedValue = (ReferenceList) fullAsset.getPropertyByName(pagedProperty);
                        if (pagedValue != null) {
                            pagedValue.getAllPages(igcRestClient);
                        }
                    }

                    // Set the asset as fully retrieved, so we do not attempt to retrieve parts of it again
                    fullAsset.setFullyRetrieved();

                }
            } else {
                log.debug("No registered POJO for asset type {} -- returning basic reference.", assetRef.getType());
                fullAsset = assetRef;
            }

        } else {
            log.info("Unable to retrieve any asset with RID {} -- assume it was deleted.", rid);
        }

        return fullAsset;

    }

}
