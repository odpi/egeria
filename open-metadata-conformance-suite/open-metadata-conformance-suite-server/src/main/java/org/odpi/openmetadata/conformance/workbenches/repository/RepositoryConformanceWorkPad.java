/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.repository;

import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryConformanceWorkbenchConfig;
import org.odpi.openmetadata.conformance.beans.*;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.*;


/**
 * RepositoryConformanceWorkPad provides the thread safe place to assemble results from the repository workbench.
 */
public class RepositoryConformanceWorkPad extends OpenMetadataConformanceWorkbenchWorkPad
{
    private static final String workbenchId            = "repository-workbench";
    private static final String workbenchName          = "Open Metadata Repository Test Workbench";
    private static final String workbenchVersionNumber = "V1.1";
    private static final String workbenchDocURL        = "https://egeria.odpi.org/open-metadata-conformance-suite/docs/" + workbenchId;
    private static final String tutType                = "Open Metadata Repository";

    private OMRSAuditLog            auditLog;

    private String                  tutServerName               = null;
    private String                  tutMetadataCollectionId     = null;
    private String                  tutServerType               = null;
    private String                  tutOrganization             = null;
    private int                     maxSearchResults            = 50;

    private OMRSRepositoryConnector tutRepositoryConnector      = null;

    private String                  localMetadataCollectionId   = null;
    private OMRSRepositoryConnector localRepositoryConnector    = null;

    private Map<String, AttributeTypeDef>    supportedAttributeTypeDefsByGUIDFromRESTAPI = new HashMap<>();
    private Map<String, AttributeTypeDef>    supportedAttributeTypeDefsByGUIDFromEvents  = new HashMap<>();
    private Map<String, AttributeTypeDef>    supportedAttributeTypeDefsByName            = new HashMap<>();

    private Map<String, TypeDef>    supportedTypeDefsByGUIDFromRESTAPI = new HashMap<>();
    private Map<String, TypeDef>    supportedTypeDefsByGUIDFromEvents  = new HashMap<>();
    private Map<String, TypeDef>    supportedTypeDefsByName            = new HashMap<>();


    private Map<String, List<String>>              entitySubTypes          = new HashMap<>();
    private Map<String, List<String>>              relationshipEndTypes    = new HashMap<>();
    private Map<String, List<List<String>>>        entityRelationshipTypes = new HashMap<>();
    private Map<String, List<List<EntityDetail>>>  entityInstances         = new HashMap<>();
    private Map<String, List<List<Relationship>>>  relationshipInstances   = new HashMap<>();



    /**
     * Constructor receives key information from the configuration services.
     *
     * @param localServerUserId userId that this server should use on requests
     * @param localServerPassword password that this server should use on requests
     * @param maxPageSize maximum number of elements that can be returned on a single call
     * @param auditLog audit log for administrator messages
     * @param configuration configuration for this work pad/workbench
     */
    public RepositoryConformanceWorkPad(String                                localServerUserId,
                                        String                                localServerPassword,
                                        int                                   maxPageSize,
                                        OMRSAuditLog                          auditLog,
                                        RepositoryConformanceWorkbenchConfig  configuration)
    {
        super(workbenchId,
              workbenchName,
              workbenchVersionNumber,
              workbenchDocURL,
              localServerUserId,
              localServerPassword,
              tutType,
              maxPageSize);

        this.auditLog = auditLog;

        if (configuration != null)
        {
            this.tutServerName = configuration.getTutRepositoryServerName();
            this.maxSearchResults = configuration.getMaxSearchResults();
            super.tutName = this.tutServerName;
        }
    }

    /**
     * Return the audit log for this server.
     *
     * @return audit log object.
     */
    public OMRSAuditLog getAuditLog()
    {
        return auditLog;
    }


    /**
     * Return the name of the server being tested.
     *
     * @return server name
     */
    public String getTutServerName()
    {
        return tutServerName;
    }


    /**
     * Return the maximum number of search results that be processed for tests of the repository under test.
     *
     * @return maximum number of search results to process
     */
    public int getMaxSearchResults()
    {
        return maxSearchResults;
    }

    /**
     * Return the server type of the technology under test.  This is extracted from the registration
     * events.
     *
     * @return string type name
     */
    public synchronized String getTutServerType()
    {
        return tutServerType;
    }


    /**
     * Set up the server type of the technology under test.  This is extracted from the registration
     * events.
     *
     * @param tutServerType string type name
     */
    public synchronized void setTutServerType(String tutServerType)
    {
        this.tutServerType = tutServerType;
    }


    /**
     * Return the owning organization of the technology under test.  This is extracted from the registration
     * event.
     *
     * @return string organization name
     */
    public synchronized String getTutOrganization()
    {
        return tutOrganization;
    }


    /**
     * Set up the owning organization of the technology under test.  This is extracted from the registration
     * event.
     *
     * @param tutOrganization string organization name
     */
    public synchronized void setTutOrganization(String tutOrganization)
    {
        this.tutOrganization = tutOrganization;
    }


    /**
     * Return the metadata collection id of the technology under test (or null if it is not known).
     * This value is populated from the registration events.
     *
     * @return string id
     */
    public synchronized String getTutMetadataCollectionId()
    {
        return tutMetadataCollectionId;
    }


    /**
     * Set up the metadata collection id of the technology under test.
     * This value is populated from the registration events.
     *
     * @param tutMetadataCollectionId string id
     */
    public synchronized void setTutMetadataCollectionId(String tutMetadataCollectionId)
    {
        this.tutMetadataCollectionId = tutMetadataCollectionId;
    }


    /**
     * Set up the supported attributeTypeDefs for validation.  This list comes from the REST API.
     *
     * @param supportedAttributeTypeDefs list of attributeTypeDefs
     */
    public synchronized void setSupportedAttributeTypeDefsFromRESTAPI(List<AttributeTypeDef> supportedAttributeTypeDefs)
    {
        this.supportedAttributeTypeDefsByGUIDFromRESTAPI = new HashMap<>();

        if (supportedAttributeTypeDefs != null)
        {
            for (AttributeTypeDef   attributeTypeDef: supportedAttributeTypeDefs)
            {
                if (attributeTypeDef != null)
                {
                    this.supportedAttributeTypeDefsByGUIDFromRESTAPI.put(attributeTypeDef.getGUID(), attributeTypeDef);
                    this.supportedAttributeTypeDefsByName.put(attributeTypeDef.getName(), attributeTypeDef);
                }
            }
        }
    }


    /**
     * Add a TypeDef to the list of supported type definitions.  This value comes from the events.
     *
     * @param attributeTypeDef type definition object
     */
    public synchronized void addSupportedAttributeTypeDefFromEvent(AttributeTypeDef   attributeTypeDef)
    {
        if (attributeTypeDef != null)
        {
            this.supportedAttributeTypeDefsByGUIDFromEvents.put(attributeTypeDef.getGUID(), attributeTypeDef);
            this.supportedAttributeTypeDefsByName.put(attributeTypeDef.getName(), attributeTypeDef);
        }
    }


    /**
     * Return a type definition object by name (or null if not known)
     *
     * @param name name of type definition object
     * @return attributeTypeDef object
     */
    public synchronized AttributeTypeDef  getAttributeTypeDefByName(String   name)
    {
        return this.supportedAttributeTypeDefsByName.get(name);
    }


    /**
     * Return a specific type definition received from the REST API.
     *
     * @param guid unique identifier of the required type definition
     * @return type definition object or null if not known
     */
    public synchronized AttributeTypeDef  getAttributeTypeDefFromRESTAPI(String   guid)
    {
        return this.supportedAttributeTypeDefsByGUIDFromRESTAPI.get(guid);
    }


    /**
     * Return a specific type definition received in an OMRS event.
     *
     * @param guid unique identifier of the required type definition
     * @return type definition object or null if not known
     */
    public synchronized AttributeTypeDef  getAttributeTypeDefFromEvents(String   guid)
    {
        return this.supportedAttributeTypeDefsByGUIDFromEvents.get(guid);
    }



    /**
     * Set up the supported typeDefs for validation.  This list comes from the REST API.
     *
     * @param supportedTypeDefs list of typeDefs
     */
    public synchronized void setSupportedTypeDefsFromRESTAPI(List<TypeDef> supportedTypeDefs)
    {
        this.supportedTypeDefsByGUIDFromRESTAPI = new HashMap<>();

        if (supportedTypeDefs != null)
        {
            for (TypeDef   typeDef: supportedTypeDefs)
            {
                if (typeDef != null)
                {
                    this.supportedTypeDefsByGUIDFromRESTAPI.put(typeDef.getGUID(), typeDef);
                    this.supportedTypeDefsByName.put(typeDef.getName(), typeDef);
                }
            }
        }
    }


    /**
     * Add a TypeDef to the list of supported type definitions.  This value comes from the events.
     *
     * @param typeDef type definition object
     */
    public synchronized void addSupportedTypeDefFromEvent(TypeDef   typeDef)
    {
        if (typeDef != null)
        {
            this.supportedTypeDefsByGUIDFromEvents.put(typeDef.getGUID(), typeDef);
            this.supportedTypeDefsByName.put(typeDef.getName(), typeDef);
        }
    }


    /**
     * Return a type definition object by name (or null if not known)
     *
     * @param name name of type definition object
     * @return typeDef object
     */
    public synchronized TypeDef  getTypeDefByName(String   name)
    {
        return this.supportedTypeDefsByName.get(name);
    }


    /**
     * Return a specific type definition received from the REST API.
     *
     * @param guid unique identifier of the required type definition
     * @return type definition object or null if not known
     */
    public synchronized TypeDef  getTypeDefFromRESTAPI(String   guid)
    {
        return this.supportedTypeDefsByGUIDFromRESTAPI.get(guid);
    }


    /**
     * Return a specific type definition received in an OMRS event.
     *
     * @param guid unique identifier of the required type definition
     * @return type definition object or null if not known
     */
    public synchronized TypeDef  getTypeDefFromEvents(String   guid)
    {
        return this.supportedTypeDefsByGUIDFromEvents.get(guid);
    }


    /**
     * Return the repository connector for the technology under test.
     *
     * @return OMRSRepositoryConnector
     */
    public synchronized OMRSRepositoryConnector getTutRepositoryConnector()
    {
        return tutRepositoryConnector;
    }


    /**
     * Set up the repository connector for the technology under test.
     *
     * @param tutRepositoryConnector OMRSRepositoryConnector
     */
    public synchronized void setTutRepositoryConnector(OMRSRepositoryConnector tutRepositoryConnector)
    {
        this.tutRepositoryConnector = tutRepositoryConnector;
    }


    /**
     * Return the metadata collection id for the local repository.
     *
     * @return string id
     */
    public synchronized String getLocalMetadataCollectionId()
    {
        return localMetadataCollectionId;
    }


    /**
     * Set up the metadata collection id for the local repository.
     *
     * @param localMetadataCollectionId string id
     */
    public synchronized void setLocalMetadataCollectionId(String localMetadataCollectionId)
    {
        this.localMetadataCollectionId = localMetadataCollectionId;
    }


    /**
     * Return the connector to the local repository.
     *
     * @return OMRSRepositoryConnector
     */
    public synchronized OMRSRepositoryConnector getLocalRepositoryConnector()
    {
        return localRepositoryConnector;
    }


    /**
     * Set up the connector to the local repository.
     *
     * @param localRepositoryConnector access to the local repository (updated to generate events for the
     *                                 technology under test to respond to)
     */
    public synchronized void setLocalRepositoryConnector(OMRSRepositoryConnector localRepositoryConnector)
    {
        this.localRepositoryConnector = localRepositoryConnector;
    }


    /**
     * {@inheritDoc}
     */
    public synchronized List<String> getProfileNames()
    {
        List<String> list = new ArrayList<>();
        RepositoryConformanceProfile[] profiles = RepositoryConformanceProfile.values();
        for (RepositoryConformanceProfile profile : profiles)
        {
            list.add(profile.getProfileName());
        }
        return list;
    }


    /**
     * {@inheritDoc}
     */
    public synchronized OpenMetadataConformanceProfileResults getProfileResults(String profileName)
    {

        RepositoryConformanceProfile[]            profiles     = RepositoryConformanceProfile.values();
        RepositoryConformanceProfileRequirement[] requirements = RepositoryConformanceProfileRequirement.values();

        OpenMetadataConformanceProfileResults  profileResults  = null;

        for (RepositoryConformanceProfile profile : profiles)
        {

            String candidateProfile = profile.getProfileName();
            if (candidateProfile.equals(profileName)) {

                profileResults = new OpenMetadataConformanceProfileResults();
                profileResults.setId(profile.getProfileId());
                profileResults.setName(profileName);
                profileResults.setDocumentationURL(profile.getProfileDocumentationURL());
                profileResults.setDescription(profile.getProfileDescription());
                profileResults.setProfilePriority(profile.getProfilePriority());

                List<OpenMetadataConformanceTestEvidence> profileTestEvidence = new ArrayList<>();

                if (testEvidenceList != null)
                {
                    for (OpenMetadataConformanceTestEvidence testEvidenceItem : testEvidenceList)
                    {
                        if ((testEvidenceItem != null) && (testEvidenceItem.getProfileId().intValue() == profileResults.getId().intValue()))
                        {
                            profileTestEvidence.add(testEvidenceItem);
                        }
                    }
                }

                if (profileTestEvidence.isEmpty())
                {
                    profileResults.setConformanceStatus(OpenMetadataConformanceStatus.UNKNOWN_STATUS);
                }
                else
                {
                    List<OpenMetadataConformanceTestEvidence>       positiveTestEvidence = new ArrayList<>();
                    List<OpenMetadataConformanceTestEvidence>       negativeTestEvidence = new ArrayList<>();

                    profileResults.setConformanceStatus(super.processEvidence(profileTestEvidence,
                            positiveTestEvidence,
                            negativeTestEvidence));

                    List<OpenMetadataConformanceRequirementResults> requirementResultsList = new ArrayList<>();
                    OpenMetadataConformanceRequirementResults       requirementResults;

                    for (RepositoryConformanceProfileRequirement requirement : requirements)
                    {
                        /*
                         * If (and only if) this requirement is relevant to the current profile, process it...
                         */
                        if (requirement.getProfileId().equals(profile.getProfileId()))
                        {
                            requirementResults = new OpenMetadataConformanceRequirementResults();

                            requirementResults.setId(requirement.getRequirementId());
                            requirementResults.setName(requirement.getName());
                            requirementResults.setDescription(requirement.getDescription());
                            requirementResults.setDocumentationURL(requirement.getDocumentationURL());

                            List<OpenMetadataConformanceTestEvidence> requirementTestEvidence = new ArrayList<>();

                            for (OpenMetadataConformanceTestEvidence testEvidenceItem : profileTestEvidence)
                            {
                                if (testEvidenceItem != null)
                                {
                                    if (testEvidenceItem.getRequirementId().intValue() == requirementResults.getId().intValue())
                                    {
                                        requirementTestEvidence.add(testEvidenceItem);
                                    }
                                }
                            }

                            positiveTestEvidence = new ArrayList<>();
                            negativeTestEvidence = new ArrayList<>();

                            requirementResults.setConformanceStatus(super.processEvidence(requirementTestEvidence,
                                    positiveTestEvidence,
                                    negativeTestEvidence));

                            if (!positiveTestEvidence.isEmpty())
                            {
                                requirementResults.setPositiveTestEvidence(positiveTestEvidence);
                            }

                            if (!negativeTestEvidence.isEmpty())
                            {
                                requirementResults.setNegativeTestEvidence(negativeTestEvidence);
                            }

                            requirementResultsList.add(requirementResults);
                        }
                    }

                    profileResults.setRequirementResults(requirementResultsList);
                }
            }

        }

        return profileResults;

    }


    /**
     * Accumulate the evidences for each profile
     *
     * @return the test evidence organized by profile and requirement withing profile
     */
    public synchronized List<OpenMetadataConformanceProfileResults> getProfileResults()
    {
        List<OpenMetadataConformanceProfileResults>  resultsList = new ArrayList<>();

        for (String profileName : getProfileNames())
        {
            OpenMetadataConformanceProfileResults profileResults = getProfileResults(profileName);
            if (profileResults != null)
            {
                resultsList.add(profileResults);
            }
        }

        if (resultsList.isEmpty())
        {
            return null;
        }
        else
        {
            return resultsList;
        }
    }


    /**
     * Accumulate the summarized evidences for each profile
     *
     * @return the summarized test evidence organized by profile and requirement withing profile
     */
    public synchronized  List<OpenMetadataConformanceProfileSummary> getProfileSummaries()
    {
        List<OpenMetadataConformanceProfileSummary>  summaryList = new ArrayList<>();

        RepositoryConformanceProfile[]            profiles     = RepositoryConformanceProfile.values();
        RepositoryConformanceProfileRequirement[] requirements = RepositoryConformanceProfileRequirement.values();

        OpenMetadataConformanceProfileSummary profileSummary = null;

        for (RepositoryConformanceProfile profile : profiles)
        {

            profileSummary = new OpenMetadataConformanceProfileSummary();
            profileSummary.setId(profile.getProfileId());
            profileSummary.setName(profile.getProfileName());
            profileSummary.setDocumentationURL(profile.getProfileDocumentationURL());
            profileSummary.setDescription(profile.getProfileDescription());
            profileSummary.setProfilePriority(profile.getProfilePriority());

            List<OpenMetadataConformanceTestEvidence> profileTestEvidence = new ArrayList<>();

            if (testEvidenceList != null) {
                for (OpenMetadataConformanceTestEvidence testEvidenceItem : testEvidenceList) {
                    if ((testEvidenceItem != null) && (testEvidenceItem.getProfileId().intValue() == profileSummary.getId().intValue())) {
                        profileTestEvidence.add(testEvidenceItem);
                    }
                }
            }

            if (profileTestEvidence.isEmpty()) {
                profileSummary.setConformanceStatus(OpenMetadataConformanceStatus.UNKNOWN_STATUS);
            } else {
                List<OpenMetadataConformanceTestEvidence> positiveTestEvidence = new ArrayList<>();
                List<OpenMetadataConformanceTestEvidence> negativeTestEvidence = new ArrayList<>();

                profileSummary.setConformanceStatus(super.processEvidence(profileTestEvidence,
                        positiveTestEvidence,
                        negativeTestEvidence));

                List<OpenMetadataConformanceRequirementSummary> requirementResultsList = new ArrayList<>();
                OpenMetadataConformanceRequirementSummary requirementSummary;


                for (RepositoryConformanceProfileRequirement requirement : requirements) {
                    requirementSummary = new OpenMetadataConformanceRequirementSummary();

                    requirementSummary.setId(requirement.getRequirementId());
                    requirementSummary.setName(requirement.getName());
                    requirementSummary.setDescription(requirement.getDescription());
                    requirementSummary.setDocumentationURL(requirement.getDocumentationURL());

                    List<OpenMetadataConformanceTestEvidence> requirementTestEvidence = new ArrayList<>();

                    for (OpenMetadataConformanceTestEvidence testEvidenceItem : profileTestEvidence) {
                        if (testEvidenceItem != null) {
                            if (testEvidenceItem.getRequirementId().intValue() == requirementSummary.getId().intValue()) {
                                requirementTestEvidence.add(testEvidenceItem);
                            }
                        }
                    }

                    positiveTestEvidence = new ArrayList<>();
                    negativeTestEvidence = new ArrayList<>();

                    requirementSummary.setConformanceStatus(super.processEvidence(requirementTestEvidence,
                            positiveTestEvidence,
                            negativeTestEvidence));

                    requirementResultsList.add(requirementSummary);
                }

                profileSummary.setRequirementSummary(requirementResultsList);
            }
            summaryList.add(profileSummary);
        }

        if (summaryList.isEmpty())
        {
            return null;
        }
        else
        {
            return summaryList;
        }
    }


    /**
     * Add the specified subtype to the list for the named entity type
     * @param entityTypeName - the name of the entity type
     * @param subTypeName    - name of the entity subtype
     */
     void addEntitySubType(String entityTypeName, String subTypeName)
     {
        List<String> subTypeList = this.entitySubTypes.get(entityTypeName);
        if (subTypeList == null)
        {
            List<String> newList = new ArrayList<>();
            newList.add(subTypeName);
            this.entitySubTypes.put(entityTypeName,newList);
        }
        else
        {
            subTypeList.add(subTypeName);
        }
    }

    /**
     * Return the list of subtypes of the named entity type
     * @param entityTypeName - the name of the entity type
     * @return - the list of subtypes of the entity type
     */
    public List<String> getEntitySubTypes(String entityTypeName)
    {
        List<String> subTypeList = this.entitySubTypes.get(entityTypeName);
        return subTypeList;
    }

    /**
     * Add the specified relationship type to the appropriate end-specific relationship type list, for the entity type specified
     *
     * @param entityTypeName entity type name
     * @param relationshipTypeName relationship type name
     * @param end which relationship end
     */
    void addEntityRelationshipType(String entityTypeName, String relationshipTypeName, int end)
    {
        List<List<String>> bothEndLists = this.entityRelationshipTypes.get(entityTypeName);
        if (bothEndLists == null) {
            List<String> end1List = new ArrayList<>();
            List<String> end2List = new ArrayList<>();
            bothEndLists = new ArrayList<>();
            bothEndLists.add(end1List);
            bothEndLists.add(end2List);
            this.entityRelationshipTypes.put(entityTypeName,bothEndLists);
        }
        if (end == 1) {
            List<String> end1List = bothEndLists.get(0);
            end1List.add(relationshipTypeName);
        }
        else if (end == 2) {
            List<String> end1List = bothEndLists.get(1);
            end1List.add(relationshipTypeName);
        }
    }


    /**
     * Return the list of end types for the named relationship type.
     *
     * @param entityTypeName requested type
     * @return list of relationship types for the entity type
     */
    public List<List<String>> getEntityRelationshipTypes(String entityTypeName)
    {
        List<List<String>> relTypeLists = this.entityRelationshipTypes.get(entityTypeName);
        return relTypeLists;
    }


    /**
     * Return the set of supported entity type names
     *
     * @return list of type names
     */
    public Set<String> getEntityTypeNames()
    {
        return this.entityRelationshipTypes.keySet();
    }



    /**
     * Set the pair of end types as the list for the named relationship type
     *
     * @param relationshipTypeName name of type
     * @param end1TypeName type for end 1
     * @param end2TypeName type for end 2
     */
    void addRelationshipEndTypes(String relationshipTypeName, String end1TypeName, String end2TypeName)
    {
        List<String> endTypeList= new ArrayList<>();
        endTypeList.add(end1TypeName);
        endTypeList.add(end2TypeName);
        this.relationshipEndTypes.put(relationshipTypeName, endTypeList);

    }


    /**
     * Return the list of end types for the named relationship type
     *
     * @param relationshipTypeName relationship type
     * @return list of end types
     */
    public List<String> getRelationshipEndTypes(String relationshipTypeName)
    {
        List<String> endTypeList = this.relationshipEndTypes.get(relationshipTypeName);
        return endTypeList;
    }


    /**
     * Return the set of supported relationship type names
     *
     * @return list of relationship types
     */
    public Set<String> getRelationshipTypeNames()
    {
        Set<String> keySet = this.relationshipEndTypes.keySet();
        return keySet;
    }


    /**
     * Remember the sets of instances for a given entity type. This is to support
     *
     * @param entityTypeName - name of entity type
     * @param set_0 - a list of entities
     * @param set_1 - a list of entities
     * @param set_2 - a list of entities
     */
    public void addEntityInstanceSets(String entityTypeName, List<EntityDetail> set_0, List<EntityDetail> set_1, List<EntityDetail> set_2)
    {
        List<List<EntityDetail>> setsList = new ArrayList<>();
        setsList.add(set_0);
        setsList.add(set_1);
        setsList.add(set_2);
        this.entityInstances.put(entityTypeName,setsList);
    }


    /**
     * Retrieve entity instances for the given type for the given instance set
     *
     * @param entityTypeName entity type name
     * @param setId which set
     * @return list of entities
     */
    public List<EntityDetail> getEntityInstanceSet(String entityTypeName, int setId)
    {
        if (this.entityInstances.get(entityTypeName) != null)
        {
            return this.entityInstances.get(entityTypeName).get(setId);
        }
        else
        {
            return null;
        }
    }


    /**
     * Clean up entity instances for the given type.
     * @param entityTypeName name of entity type
     */
    public void removeEntityInstanceSets(String entityTypeName)
    {
        this.entityInstances.remove(entityTypeName);
    }


    /**
     * Remember the sets of instances for a given relationship type. This is to support
     *
     * @param relationshipTypeName - type name for relationship
     * @param set_0 - a list of relationships
     * @param set_1 - a list of relationships
     * @param set_2 - a list of relationships
     */
    public void addRelationshipInstanceSets(String relationshipTypeName, List<Relationship> set_0, List<Relationship> set_1, List<Relationship> set_2)
    {
        List<List<Relationship>> setsList = new ArrayList<>();
        setsList.add(set_0);
        setsList.add(set_1);
        setsList.add(set_2);
        this.relationshipInstances.put(relationshipTypeName,setsList);
    }

    /**
     * Retrieve relationship instances for the given type for the given instance set
     *
     * @param relationshipTypeName relationship type name
     * @param setId which set?
     * @return list of relationships
     */
    public List<Relationship> getRelationshipInstanceSet(String relationshipTypeName, int setId)
    {
        if (this.relationshipInstances.get(relationshipTypeName) != null)
        {
            return this.relationshipInstances.get(relationshipTypeName).get(setId);
        }
        else
        {
            return null;
        }
    }


    /**
     * Clean up relationship instances for the given type.
     *
     * @param relationshipTypeName relationship type name
     */
    public void removeRelationshipInstanceSets(String relationshipTypeName)
    {
        this.relationshipInstances.remove(relationshipTypeName);
    }



    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "RepositoryConformanceWorkPad{" +
                "workbenchId='" + workbenchId + '\'' +
                ", workbenchName='" + workbenchName + '\'' +
                ", workbenchVersionNumber='" + workbenchVersionNumber + '\'' +
                ", workbenchDocURL='" + workbenchDocURL + '\'' +
                ", localServerUserId='" + localServerUserId + '\'' +
                ", localServerPassword='" + localServerPassword + '\'' +
                ", tutName='" + tutName + '\'' +
                ", maxSearchResults=" + maxSearchResults +
                ", tutType='" + tutType + '\'' +
                ", maxPageSize=" + maxPageSize +
                '}';
    }
}
