/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.eventmanagement;

import org.odpi.openmetadata.adminservices.configuration.properties.OpenMetadataExchangeRule;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;


import java.util.ArrayList;
import java.util.List;

/**
 * OMRSRepositoryEventExchangeRule determines if particular types of events should be exchanged on the OMRS Topic.
 */
public class OMRSRepositoryEventExchangeRule
{
    private OpenMetadataExchangeRule           exchangeRule;
    private List<String>                       selectedTypesToProcess = new ArrayList<>();


    /**
     * Constructor provides all of the objects used in the event exchange decision.
     *
     * @param exchangeRule enum detailing the types of events to process.
     * @param selectedTypesToProcess supplementary list to support selective processing of events.
     */
    public OMRSRepositoryEventExchangeRule(OpenMetadataExchangeRule     exchangeRule,
                                           List<TypeDefSummary>         selectedTypesToProcess)
    {
        final String  methodName = "OMRSRepositoryEventExchangeRule constructor";

        /*
         * Validate the supplied parameters.
         */
        if (exchangeRule == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_EXCHANGE_RULE.getMessageDefinition(methodName),
                                              this.getClass().getName(),
                                              methodName);
        }

        this.exchangeRule = exchangeRule;

        /*
         * The selected types are loaded into a hash map for easy retrieval.
         */
        if (selectedTypesToProcess != null)
        {
            for (TypeDefSummary  typeDefSummary : selectedTypesToProcess)
            {
                if (typeDefSummary != null)
                {
                    String   typeDefSummaryGUID = typeDefSummary.getGUID();

                    if (typeDefSummaryGUID != null)
                    {
                        this.selectedTypesToProcess.add(typeDefSummaryGUID);
                    }
                }
            }
        }
    }


    /**
     * Determine if TypeDef events should be processed.  TypeDef events are processed unless the rule is set
     * to registration events only.
     *
     * @return boolean flag indicating if the event should be processed.
     */
    public boolean processTypeDefEvents()
    {
        return exchangeRule != OpenMetadataExchangeRule.REGISTRATION_ONLY;
    }


    /**
     * Determine from the type of the instance if an instance event should be processed.
     *
     * @param typeDefGUID unique identifier of the type
     * @param typeDefName unique name of the type
     * @return boolean flag
     */
    public boolean processInstanceEvent(String   typeDefGUID, String   typeDefName)
    {
        if ((exchangeRule == OpenMetadataExchangeRule.REGISTRATION_ONLY) ||
            (exchangeRule == OpenMetadataExchangeRule.JUST_TYPEDEFS))
        {
            /*
             * The rule says not to process instances
             */
            return false;
        }
        else if ((typeDefGUID == null) || (typeDefName == null))
        {
            /*
             * The instance is invalid this will be logged elsewhere.
             */
            return false;
        }
        else
        {
            if (exchangeRule == OpenMetadataExchangeRule.ALL)
            {
                /*
                 * All active types should be processed
                 */
                return true;
            }
            else if (exchangeRule == OpenMetadataExchangeRule.DESELECTED_TYPES)
            {
                /*
                 * All active types should be processed
                 */
                return (! selectedTypesToProcess.contains(typeDefGUID));
            }
            else
            {
                /*
                 * The exchange rule is either SELECTED_TYPES or LEARNED_TYPES. For either, the instance
                 * is processed if its type is in the selectedTypesToProcess list.
                 */
                return selectedTypesToProcess.contains(typeDefGUID);
            }

        }
    }


    /**
     * Determine from the type of the instance if an instance event should be processed.
     *
     * @param typeDefSummary details of the type
     * @return boolean flag
     */
    public boolean processInstanceEvent(TypeDefSummary   typeDefSummary)
    {
        if (typeDefSummary == null)
        {
            return false;
        }
        else
        {
            return this.processInstanceEvent(typeDefSummary.getGUID(), typeDefSummary.getName());
        }
    }


    /**
     * Determine from the type of the instance if an instance event should be processed.
     *
     * @param instance details of the instance to test
     * @return boolean flag
     */
    public boolean processInstanceEvent(InstanceHeader instance)
    {
        if (instance == null)
        {
            return false;
        }
        else
        {
            InstanceType type = instance.getType();

            if (type == null)
            {
                return false;
            }
            else
            {
                return this.processInstanceEvent(type.getTypeDefGUID(), type.getTypeDefName());
            }
        }
    }


    /**
     * If the rule is in learning mode, determine if the type of the instance should be added to the list
     * of types being processed.  For this to happen, the instance header must include a valid type, the type
     * must be an active type for this server and not already included in the list.
     *
     * Any errors discovered in the types, of this rule's set up result in a false result.  No diagnostics are
     * created because this method is called very frequently and the errors will be trapped and logged elsewhere.
     *
     * @param instance details of the instance to test
     * @return boolean flag true if the instance should be saved as a learned instance.
     */
    public boolean learnInstanceEvent(InstanceHeader instance)
    {

        if (exchangeRule == OpenMetadataExchangeRule.LEARNED_TYPES)
        {
            InstanceType type = instance.getType();

            if (type != null)
            {
                String  typeDefGUID = type.getTypeDefGUID();

                if (typeDefGUID != null)
                {
                    /*
                     * All active types should be learned and added to the rule so save copies are updated by
                     * incoming events.
                     */
                    if (! selectedTypesToProcess.contains(type.getTypeDefGUID()))
                    {
                        selectedTypesToProcess.add(typeDefGUID);
                    }

                    /*
                     * The instance should be saved if it is not already known.
                     */
                    return true;
                }
            }
        }

        return false; /* rule is not set to LEARNED_TYPES (or a problem with the instance) */
    }
}
