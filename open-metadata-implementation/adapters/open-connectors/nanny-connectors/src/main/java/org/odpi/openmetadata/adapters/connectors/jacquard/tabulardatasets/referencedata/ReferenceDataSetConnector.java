/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata;

import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.DynamicOpenMetadataDataSetConnectorBase;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextClientBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ValidValueDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;

import java.util.List;


/**
 * ReferenceDataSetConnector enables interaction with a valid value set as if it is a tabular data set.
 */
public class ReferenceDataSetConnector extends DynamicOpenMetadataDataSetConnectorBase
{
    private static final String myConnectorName = ReferenceDataSetConnector.class.getName();

    private String startingElementGUID     = null;

    /**
     * Default constructor
     */
    public ReferenceDataSetConnector()
    {
        super(myConnectorName);
    }


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId unique id for the connector instance   useful for messages etc
     * @param connectionDetails   POJO for the configuration used to create the connector
     */
    public void initialize(String     connectorInstanceId,
                           Connection connectionDetails) throws ConnectorCheckedException
    {
        super.initialize(connectorInstanceId, connectionDetails);

        final String methodName = "initialize";

        startingElementGUID = super.getStringConfigurationProperty(TabularDataSetConfigurationProperty.STARTING_ELEMENT_GUID.getName(), connectionBean.getConfigurationProperties());
        if (startingElementGUID == null)
        {
            super.throwMissingConfigurationProperty(connectorName,
                                                    this.getClass().getName(),
                                                    TabularDataSetConfigurationProperty.STARTING_ELEMENT_GUID.getName(),
                                                    methodName);
        }

        super.productDefinition = new ReferenceDataSetProvider().getProductDefinition(startingElementGUID, identifierPropertyValue, canonicalName, description);
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    public void refreshCache() throws ConnectorCheckedException
    {
        final String methodName = "refreshCache";

        /*
         * Retrieve the valid values for the reference data set and store them in the records map.
         */
        try
        {
            ClassificationExplorerClient classificationExplorerClient = connectorContext.getClassificationExplorerClient(OpenMetadataType.REFERENCE_DATA_VALUE.typeName);
            /*
             * The set is read with its members and nothing else; each member is then read the same way so that
             * nested sets are followed.
             */
            OpenMetadataRootElement validValueSet = classificationExplorerClient.getRootElementByGUID(startingElementGUID,
                                                                                                     this.getWithMembersOptions(classificationExplorerClient));

            if (validValueSet != null)
            {
                processNestedValues(validValueSet, refreshExistingRecords());
            }
        }
        catch (Exception error)
        {
            super.logRecord(methodName, TabularDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       error.getMessage()));

            throw new ConnectorCheckedException(TabularDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Process any nested records.
     *
     * @param element retrieved element
     * @param knownGUIDs list of records already processed
     */
    @Override
    protected void processNestedValues(OpenMetadataRootElement element,
                                       List<String>            knownGUIDs)
    {
        final String methodName = "processNestedValues";

        if ((element != null) && (element.getValidValueMembers() != null))
        {
            ValidValueDefinitionClient validValueDefinitionClient = connectorContext.getValidValueDefinitionClient(OpenMetadataType.REFERENCE_DATA_VALUE.typeName);

            for (RelatedMetadataElementSummary member : element.getValidValueMembers())
            {
                if (member != null)
                {
                    try
                    {
                        /*
                         * The member may itself be a set, so its members are read too.
                         */
                        OpenMetadataRootElement rootElement = validValueDefinitionClient.getValidValueDefinitionByGUID(member.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                       this.getWithMembersOptions(validValueDefinitionClient));

                        if (rootElement != null)
                        {
                            /*
                             * The record is added if it is not already in the record map.
                             */
                            if (!knownGUIDs.contains(rootElement.getElementHeader().getGUID()))
                            {
                                records.put((long) records.size(), rootElement);
                                knownGUIDs.add(rootElement.getElementHeader().getGUID());
                            }

                            /*
                             * Process the reference data set members.
                             */
                            processNestedValues(rootElement, knownGUIDs);
                        }
                    }
                    catch (Exception error)
                    {
                        logRecord(methodName, TabularDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()));
                    }
                }
            }
        }
    }


    /**
     * Options for reading a valid value with its direct members and nothing else.
     *
     * @param client the client the options are for
     * @return get options
     */
    private GetOptions getWithMembersOptions(ConnectorContextClientBase client)
    {
        GetOptions getOptions = client.getGetOptions();

        getOptions.setGraphQueryDepth(1);
        getOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName));

        return getOptions;
    }
}
