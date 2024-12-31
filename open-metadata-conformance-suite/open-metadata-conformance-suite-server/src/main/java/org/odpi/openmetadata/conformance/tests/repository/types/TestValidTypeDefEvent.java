/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.types;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEventType;

import java.util.Date;


/**
 * TestValidTypeDefEvent validates the format of an incoming TypeDefEvent
 */
public class TestValidTypeDefEvent extends RepositoryConformanceTestCase
{
    private static final  String rootTestCaseId = "repository-type-definition-event";
    private static final  String testCaseName   = "Validate incoming type definition event test case";


    private static final  String assertion1    = rootTestCaseId + "-01";
    private static final  String assertionMsg1 = "Event is not null.";
    private static final  String assertion2    = rootTestCaseId + "-02";
    private static final  String assertionMsg2 = "Event timestamp is present.";
    private static final  String assertion3    = rootTestCaseId + "-03";
    private static final  String assertionMsg3 = "Event type is valid.";
    private static final  String assertion4    = rootTestCaseId + "-04";
    private static final  String assertionMsg4 = "Event originator is set.";
    private static final  String assertion5    = rootTestCaseId + "-05";
    private static final  String assertionMsg5 = "Metadata collection id is set.";
    private static final  String assertion6    = rootTestCaseId + "-06";
    private static final  String assertionMsg6 = "Metadata collection id matches technology under test.";
    private static final  String assertion7    = rootTestCaseId + "-07";
    private static final  String assertionMsg7 = "Server name is set.";
    private static final  String assertion8    = rootTestCaseId + "-08";
    private static final  String assertionMsg8 = "Server name matches technology under test.";
    private static final  String assertion9    = rootTestCaseId + "-09";
    private static final  String assertionMsg9 = "TypeDef supplied for TypeDef event.";
    private static final  String assertion10    = rootTestCaseId + "-10";
    private static final  String assertionMsg10 = "AttributeTypeDef null for TypeDef event.";
    private static final  String assertion11    = rootTestCaseId + "-11";
    private static final  String assertionMsg11 = "AttributeTypeDef supplied for AttributeTypeDef event.";
    private static final  String assertion12    = rootTestCaseId + "-12";
    private static final  String assertionMsg12 = "TypeDef null for AttributeTypeDef event.";
    private static final  String assertion13    = rootTestCaseId + "-13";
    private static final  String assertionMsg13 = "TypeDefPatch supplied for update event.";

    private static final String eventTypePropertyName             = "Event type";
    private static final String eventTimestampPropertyName        = "Event time stamp";
    private static final String metadataCollectionIdPropertyName  = "metadata collection id";
    private static final String serverNamePropertyName            = "server name";
    private static final String serverTypePropertyName            = "server type";
    private static final String orgNamePropertyName               = "organization name";
    private static final String typeDefGUIDPropertyName           = "TypeDef unique identifier (GUID)";
    private static final String typeDefNamePropertyName           = "TypeDef unique name";
    private static final String attributeTypeDefGUIDPropertyName  = "AttributeTypeDef unique identifier (GUID)";
    private static final String attributeTypeDefNamePropertyName  = "AttributeTypeDef unique name";

    private static final String successMessage  = "Type definition event successfully processed";

    private String            testCaseId;
    private OMRSTypeDefEvent  event;
    private AttributeTypeDef  attributeTypeDef = null;
    private TypeDef           typeDef = null;
    private int               eventIdentifier;

    private String            metadataCollectionId = null;
    private String            serverName = null;



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param eventIdentifier event counter to keep test case ids unique
     * @param event incoming event that triggered the test case
     */
    public TestValidTypeDefEvent(RepositoryConformanceWorkPad workPad,
                                 int                          eventIdentifier,
                                 OMRSTypeDefEvent             event)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_NOTIFICATIONS.getProfileId(),
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_NOTIFICATIONS.getRequirementId());

        testCaseId = rootTestCaseId + "-" + eventIdentifier;
        super.updateTestId(rootTestCaseId, testCaseId, testCaseName);

        this.event = event;
        this.eventIdentifier = eventIdentifier;
    }


    /**
     * Return the originator's server name extracted from the event.
     *
     * @return string name
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Return the originator's metadata collection Id extracted from the event.
     *
     * @return string name
     */
    public String getMetadataCollectionId()
    {
        return metadataCollectionId;
    }


    /**
     * Return the attribute type definition extracted from the event (or null).
     *
     * @return AttributeTypeDef object
     */
    public AttributeTypeDef getExtractedAttributeTypeDef()
    {
        return attributeTypeDef;
    }


    /**
     * Return the type definition extracted from the event (or null).
     *
     * @return Typedef object
     */
    public TypeDef getExtractedTypeDef()
    {
        return typeDef;
    }


    /**
     * Dummy method needed to satisfy the superclass
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        assertCondition((event != null),
                        assertion1,
                        eventIdentifier + assertionMsg1,
                        defaultProfileId,
                        defaultRequirementId);

        Date   eventTimestamp = event.getEventTimestamp();

        verifyCondition((eventTimestamp != null),
                        assertion2,
                        eventIdentifier + assertionMsg2,
                        defaultProfileId,
                        defaultRequirementId);

        if (eventTimestamp != null)
        {
            addDiscoveredProperty(eventTimestampPropertyName,
                                  eventTimestamp,
                                  RepositoryConformanceProfileRequirement.COHORT_REGISTRATION.getProfileId(),
                                  RepositoryConformanceProfileRequirement.COHORT_REGISTRATION.getRequirementId());
        }

        OMRSTypeDefEventType  eventType = event.getTypeDefEventType();

        verifyCondition(((eventType != null) && (eventType != OMRSTypeDefEventType.UNKNOWN_TYPEDEF_EVENT)),
                        assertion3,
                        eventIdentifier + assertionMsg3,
                        defaultProfileId,
                        defaultRequirementId);


        OMRSEventOriginator  originator = event.getEventOriginator();

        verifyCondition((originator != null),
                        assertion4,
                        eventIdentifier + assertionMsg4,
                        defaultProfileId,
                        defaultRequirementId);

        if (originator != null)
        {
            metadataCollectionId = originator.getMetadataCollectionId();

            verifyCondition((metadataCollectionId != null),
                            assertion5,
                            assertionMsg5,
                            defaultProfileId,
                            defaultRequirementId);

            if ((metadataCollectionId != null) && (metadataCollectionId.equals(repositoryConformanceWorkPad.getTutMetadataCollectionId())))
            {
                /*
                 * Only check type events from the TUT.
                 */
                addDiscoveredProperty(metadataCollectionIdPropertyName,
                                      originator.getMetadataCollectionId(),
                                      defaultProfileId,
                                      defaultRequirementId);
                serverName = originator.getServerName();
                verifyCondition((serverName != null),
                                assertion7,
                                assertionMsg7,
                                defaultProfileId,
                                defaultRequirementId);
                verifyCondition(((serverName != null) && (serverName.equals(repositoryConformanceWorkPad.getTutServerName()))),
                                assertion8,
                                assertionMsg8,
                                defaultProfileId,
                                defaultRequirementId);
                addDiscoveredProperty(serverNamePropertyName,
                                      serverName,
                                      defaultProfileId,
                                      defaultRequirementId);

                if (originator.getServerType() != null)
                {
                    addDiscoveredProperty(serverTypePropertyName,
                                          originator.getServerType(),
                                          defaultProfileId,
                                          defaultRequirementId);
                }

                if (originator.getOrganizationName() != null)
                {
                    addDiscoveredProperty(orgNamePropertyName,
                                          originator.getOrganizationName(),
                                          defaultProfileId,
                                          defaultRequirementId);
                }

                if (eventType != null)
                {
                    addDiscoveredProperty(eventTypePropertyName,
                                          eventType.getName(),
                                          defaultProfileId,
                                          defaultRequirementId);


                    typeDef          = event.getTypeDef();
                    attributeTypeDef = event.getAttributeTypeDef();

                    switch (eventType)
                    {
                        case NEW_TYPEDEF_EVENT:
                        case DELETED_TYPEDEF_EVENT:
                        case RE_IDENTIFIED_TYPEDEF_EVENT:
                            verifyCondition((typeDef != null),
                                            assertion9,
                                            assertionMsg9,
                                            defaultProfileId,
                                            defaultRequirementId);
                            verifyCondition((attributeTypeDef == null),
                                            assertion10,
                                            assertionMsg10,
                                            defaultProfileId,
                                            defaultRequirementId);

                            if (typeDef != null)
                            {
                                addDiscoveredProperty(typeDefGUIDPropertyName,
                                                      typeDef.getGUID(),
                                                      defaultProfileId,
                                                      defaultRequirementId);
                                addDiscoveredProperty(typeDefNamePropertyName,
                                                      typeDef.getName(),
                                                      defaultProfileId,
                                                      defaultRequirementId);
                            }
                            break;

                        case UPDATED_TYPEDEF_EVENT:
                            /*
                             * If this is a type update event then it does not have the (new) TypeDef available,
                             * so verify that the event contains the patch.
                             */
                            TypeDefPatch typeDefPatch = event.getTypeDefPatch();
                            verifyCondition((typeDefPatch != null),
                                            assertion13,
                                            assertionMsg13,
                                            defaultProfileId,
                                            defaultRequirementId);
                            verifyCondition((typeDef == null),
                                            assertion9,
                                            assertionMsg9,
                                            defaultProfileId,
                                            defaultRequirementId);
                            verifyCondition((attributeTypeDef == null),
                                            assertion10,
                                            assertionMsg10,
                                            defaultProfileId,
                                            defaultRequirementId);
                            break;

                        case NEW_ATTRIBUTE_TYPEDEF_EVENT:
                        case DELETED_ATTRIBUTE_TYPEDEF_EVENT:
                        case RE_IDENTIFIED_ATTRIBUTE_TYPEDEF_EVENT:
                            verifyCondition((attributeTypeDef != null),
                                            assertion11,
                                            assertionMsg11,
                                            defaultProfileId,
                                            defaultRequirementId);
                            verifyCondition((typeDef == null),
                                            assertion12,
                                            assertionMsg12,
                                            defaultProfileId,
                                            defaultRequirementId);

                            if (attributeTypeDef != null)
                            {
                                addDiscoveredProperty(attributeTypeDefGUIDPropertyName,
                                                      attributeTypeDef.getGUID(),
                                                      defaultProfileId,
                                                      defaultRequirementId);
                                addDiscoveredProperty(attributeTypeDefNamePropertyName,
                                                      attributeTypeDef.getName(),
                                                      defaultProfileId,
                                                      defaultRequirementId);
                            }
                            break;

                        case TYPEDEF_ERROR_EVENT:

                            break;
                    }
                }
            }
        }

        super.setSuccessMessage(successMessage);
    }
}
