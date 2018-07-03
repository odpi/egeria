/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.listeners;


import org.apache.log4j.Logger;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.v1.OMRSEventV1;


public class GovernanceEngineOMRSTopicListener implements OMRSTopicListener {


    private static final org.apache.log4j.Logger log      = Logger.getLogger(GovernanceEngineOMRSTopicListener.class);

    private OMRSInstanceEventProcessor instanceEventProcessor;
    private OMRSAuditLog auditLog;

    public GovernanceEngineOMRSTopicListener(Connection assetConsumerOutTopic,
                                             OMRSRepositoryHelper repositoryHelper,
                                             OMRSRepositoryValidator repositoryValidator,
                                             String                  componentName) {

        this.instanceEventProcessor = instanceEventProcessor;
        this.auditLog = auditLog;
    }


    /**
     * @param event - inbound event
     */
    public void processEvent(OMRSEventV1 event) {
        String actionDescription = "processEvent";
        log.debug(">>" + actionDescription + " : Processing " + event);

        /*
         * The event should not be null but worth checking.
         */
        if (event != null) {
            /*
             * Determine the category of event to process.
             */
            switch (event.getEventCategory()) {
//TODO More event processing needed
                case INSTANCE:
                    this.processInstanceEvent(new OMRSInstanceEvent(event));
                    break;
                case REGISTRY:
                case TYPEDEF:
                default:
                    break;

            }
        }else {
                /*
                 * A bad event was passed - probably should not happen so log audit record.
                 */
                OMRSAuditCode auditCode = OMRSAuditCode.NULL_OMRS_EVENT_RECEIVED;

                auditLog.logRecord(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());


                log.debug("Null OMRS Event received ");
            }

        log.debug("<<" + actionDescription);

    }


    /**
     * @param instanceEvent - the event coming from enterprise topic
     */
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent) {
        String actionDescription = "processInstanceEvent";


        log.debug(">> " + actionDescription + " : Processing instance event : " + instanceEvent);

        if (instanceEvent == null) {
            log.debug("Null instance event - ignoring event");
        } else {
            OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
            OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

            if ((instanceEventType != null) && (instanceEventOriginator != null)) {
                switch (instanceEventType) {

                    //TODO Event decoding needs work
                    case NEW_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processNewRelationshipEvent("EnterpriseOMRSTopic",
                                instanceEventOriginator.getMetadataCollectionId(),
                                instanceEventOriginator.getServerName(),
                                instanceEventOriginator.getServerType(),
                                instanceEventOriginator.getOrganizationName(),
                                instanceEvent.getRelationship());
                        break;

                    default:

                }
            } else {
                log.debug("Ignored instance event - null type");
            }
        }
        log.debug("<< " + actionDescription );


    }

}
