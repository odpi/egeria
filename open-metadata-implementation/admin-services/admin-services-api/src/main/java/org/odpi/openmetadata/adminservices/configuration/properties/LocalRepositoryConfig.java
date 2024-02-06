/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * LocalRepositoryConfig provides the properties to control the behavior of the metadata repository associated with
 * this server.
 * <ul>
 *     <li>
 *         metadataCollectionId - unique id of local repository's metadata collection.  If this value is set to
 *         null, the server will generate a unique Id.
 *     </li>
 *     <li>
 *         metadataCollectionName - display name of local repository's metadata collection.  If this value is set to
 *         null, the server will use the local server name.
 *     </li>
 *     <li>
 *         localRepositoryLocalConnection - the connection properties used to create a locally optimized connector
 *         to the local repository for use by this local server's components. If this value is null then the
 *         localRepositoryRemoteConnection is used.
 *     </li>
 *     <li>
 *         localRepositoryRemoteConnection - the connection properties used to create a connector
 *         to the local repository for use by remote servers.
 *     </li>
 *     <li>
 *         eventsToSaveRule - enumeration describing which open metadata repository events should be saved to
 *         the local repository.
 *     </li>
 *     <li>
 *         selectedTypesToSave - list of TypeDefs in supported of the eventsToSave.SELECTED_TYPES option.
 *     </li>
 *     <li>
 *         eventsToSendRule - enumeration describing which open metadata repository events should be sent from
 *         the local repository.
 *     </li>
 *     <li>
 *         selectedTypesToSend - list of TypeDefs in supported of the eventsToSend.SELECTED_TYPES option.
 *     </li>
 *     <li>
 *         eventMapperConnection - the connection properties for the event mapper for the local repository.
 *         The event mapper is an optional component used when the local repository has proprietary external
 *         APIs that can change metadata in the repository without going through the OMRS interfaces.
 *         It maps the proprietary events from the local repository to the OMRS Events.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LocalRepositoryConfig extends AdminServicesConfigHeader
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String                   metadataCollectionId            = null;
    private String                   metadataCollectionName          = null;
    private LocalRepositoryMode      localRepositoryMode             = null;
    private Connection               localRepositoryLocalConnection  = null;
    private Connection               localRepositoryRemoteConnection = null;
    private OpenMetadataExchangeRule eventsToSaveRule                = null;
    private List<TypeDefSummary>     selectedTypesToSave             = null;
    private OpenMetadataExchangeRule eventsToSendRule                = null;
    private List<TypeDefSummary>     selectedTypesToSend             = null;
    private Connection               eventMapperConnection           = null;


    /**
     * Default constructor used for JSON to Java processes - does not do anything useful because all
     * local variables are initialized to null in their declaration.
     */
    public LocalRepositoryConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LocalRepositoryConfig(LocalRepositoryConfig  template)
    {
        super(template);

        if (template != null)
        {
            this.metadataCollectionId = template.getMetadataCollectionId();
            this.metadataCollectionName = template.getMetadataCollectionName();
            this.localRepositoryMode = template.getLocalRepositoryMode();
            this.localRepositoryLocalConnection = template.getLocalRepositoryLocalConnection();
            this.localRepositoryRemoteConnection = template.getLocalRepositoryRemoteConnection();
            this.eventsToSaveRule = template.getEventsToSaveRule();
            this.selectedTypesToSave = template.getSelectedTypesToSave();
            this.eventsToSendRule = template.getEventsToSendRule();
            this.selectedTypesToSend = template.getSelectedTypesToSend();
            this.eventMapperConnection = template.getEventMapperConnection();
        }
    }


    /**
     * Return the unique id of local repository's metadata collection.  If this value is set to
     * null, the server will generate a unique Id.
     *
     * @return String unique Id
     */
    public String getMetadataCollectionId()
    {
        return metadataCollectionId;
    }


    /**
     * Set up the unique id of local repository's metadata collection.  If this value is set to
     * null, the server will generate a unique Id.
     *
     * @param metadataCollectionId String unique Id
     */
    public void setMetadataCollectionId(String metadataCollectionId)
    {
        this.metadataCollectionId = metadataCollectionId;
    }


    /**
     * Return the display name of the metadata collection. (The local server name is used if this is null).
     *
     * @return string name
     */
    public String getMetadataCollectionName()
    {
        return metadataCollectionName;
    }


    /**
     * Set up the display name of the metadata collection. (The local server name is used if this is null).
     *
     * @param metadataCollectionName string name
     */
    public void setMetadataCollectionName(String metadataCollectionName)
    {
        this.metadataCollectionName = metadataCollectionName;
    }


    /**
     * Return the mode that the local repository is operating in.  The enum implementation has the
     * description of each mode.
     *
     * @return LocalRepositoryMode enum
     */
    public LocalRepositoryMode getLocalRepositoryMode()
    {
        return localRepositoryMode;
    }


    /**
     * Set up the mode that the local repository is operating in.  The enum implementation has the
     * description of each mode.
     *
     * @param localRepositoryMode LocalRepositoryMode enum
     */
    public void setLocalRepositoryMode(LocalRepositoryMode localRepositoryMode)
    {
        this.localRepositoryMode = localRepositoryMode;
    }


    /**
     * Return the connection properties used to create a locally optimized connector to the local repository for
     * use by this local server's components.  If this value is null then the localRepositoryRemoteConnection is used.
     *
     * @return Connection properties object
     */
    public Connection getLocalRepositoryLocalConnection()
    {
        return localRepositoryLocalConnection;
    }


    /**
     * Set up the connection properties used to create a locally optimized connector to the local repository for
     * use by this local server's components.  If this value is null then the localRepositoryRemoteConnection is used.
     *
     * @param localRepositoryLocalConnection Connection properties object
     */
    public void setLocalRepositoryLocalConnection(Connection localRepositoryLocalConnection)
    {
        this.localRepositoryLocalConnection = localRepositoryLocalConnection;
    }


    /**
     * Return the connection properties used to create a connector to the local repository for use by remote servers.
     *
     * @return Connection properties object
     */
    public Connection getLocalRepositoryRemoteConnection()
    {
        return localRepositoryRemoteConnection;
    }


    /**
     * Set up the connection properties used to create a connector to the local repository for use by remote servers.
     *
     * @param localRepositoryRemoteConnection Connection properties object
     */
    public void setLocalRepositoryRemoteConnection(Connection localRepositoryRemoteConnection)
    {
        this.localRepositoryRemoteConnection = localRepositoryRemoteConnection;
    }


    /**
     * Return the enumeration describing which open metadata repository events should be saved to
     * the local repository.
     *
     * @return OpenMetadataExchangeRule enum
     */
    public OpenMetadataExchangeRule getEventsToSaveRule()
    {
        return eventsToSaveRule;
    }


    /**
     * Set up the enumeration describing which open metadata repository events should be saved to
     * the local repository.
     *
     * @param eventsToSaveRule OpenMetadataExchangeRule enum
     */
    public void setEventsToSaveRule(OpenMetadataExchangeRule eventsToSaveRule)
    {
        this.eventsToSaveRule = eventsToSaveRule;
    }


    /**
     * Return the list of TypeDefs in supported of the eventsToSave.SELECTED_TYPES option.
     *
     * @return list of types
     */
    public List<TypeDefSummary> getSelectedTypesToSave()
    {
        if (selectedTypesToSave == null)
        {
            return null;
        }
        else if (selectedTypesToSave.isEmpty())
        {
            return null;
        }
        else
        {
            List<TypeDefSummary> resultList = new ArrayList<>();

            for (TypeDefSummary  typeDefSummary : selectedTypesToSave)
            {
                if (typeDefSummary != null)
                {
                    resultList.add(new TypeDefSummary(typeDefSummary));
                }
            }

            return resultList;
        }
    }


    /**
     * Set up the list of TypeDefs in supported of the eventsToSave.SELECTED_TYPES option.
     *
     * @param selectedTypesToSave list of types
     */
    public void setSelectedTypesToSave(List<TypeDefSummary> selectedTypesToSave)
    {
        this.selectedTypesToSave = selectedTypesToSave;
    }


    /**
     * Return the enumeration describing which open metadata repository events should be sent from
     * the local repository.
     *
     * @return OpenMetadataExchangeRule enum
     */
    public OpenMetadataExchangeRule getEventsToSendRule()
    {
        return eventsToSendRule;
    }


    /**
     * Set up the enumeration describing which open metadata repository events should be sent from
     * the local repository.
     *
     * @param eventsToSendRule OpenMetadataExchangeRule enum
     */
    public void setEventsToSendRule(OpenMetadataExchangeRule eventsToSendRule)
    {
        this.eventsToSendRule = eventsToSendRule;
    }


    /**
     * Return the list of TypeDefs in supported of the eventsToSend.SELECTED_TYPES option.
     *
     * @return list of types
     */
    public List<TypeDefSummary> getSelectedTypesToSend()
    {
        if (selectedTypesToSend == null)
        {
            return null;
        }
        else if (selectedTypesToSend.isEmpty())
        {
            return null;
        }
        else
        {
            List<TypeDefSummary> resultList = new ArrayList<>();

            for (TypeDefSummary  typeDefSummary : selectedTypesToSend)
            {
                if (typeDefSummary != null)
                {
                    resultList.add(new TypeDefSummary(typeDefSummary));
                }
            }

            return resultList;
        }
    }


    /**
     * Set up the list of TypeDefs in supported of the eventsToSend.SELECTED_TYPES option.
     *
     * @param selectedTypesToSend list of types
     */
    public void setSelectedTypesToSend(List<TypeDefSummary> selectedTypesToSend)
    {
        this.selectedTypesToSend = selectedTypesToSend;
    }


    /**
     * Return the connection properties for the event mapper for the local repository.  The event mapper is an
     * optional component used when the local repository has proprietary external APIs that can change metadata
     * in the repository without going through the OMRS interfaces.  It maps the proprietary events from
     * the local repository to the OMRS Events.
     *
     * @return Connection properties object
     */
    public Connection getEventMapperConnection()
    {
        return eventMapperConnection;
    }


    /**
     * Set up the connection properties for the event mapper for the local repository.  The event mapper is an
     * optional component used when the local repository has proprietary external APIs that can change metadata
     * in the repository without going through the OMRS interfaces.  It maps the proprietary events from
     * the local repository to the OMRS Events.
     *
     * @param eventMapperConnection Connection properties object
     */
    public void setEventMapperConnection(Connection eventMapperConnection)
    {
        this.eventMapperConnection = eventMapperConnection;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "LocalRepositoryConfig{" +
                "metadataCollectionId='" + metadataCollectionId + '\'' +
                ", metadataCollectionName='" + metadataCollectionName + '\'' +
                ", localRepositoryMode=" + localRepositoryMode +
                ", localRepositoryLocalConnection=" + localRepositoryLocalConnection +
                ", localRepositoryRemoteConnection=" + localRepositoryRemoteConnection +
                ", eventsToSaveRule=" + eventsToSaveRule +
                ", selectedTypesToSave=" + selectedTypesToSave +
                ", eventsToSendRule=" + eventsToSendRule +
                ", selectedTypesToSend=" + selectedTypesToSend +
                ", eventMapperConnection=" + eventMapperConnection +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        LocalRepositoryConfig that = (LocalRepositoryConfig) objectToCompare;
        return Objects.equals(metadataCollectionId, that.metadataCollectionId) &&
                Objects.equals(metadataCollectionName, that.metadataCollectionName) &&
                localRepositoryMode == that.localRepositoryMode &&
                Objects.equals(localRepositoryLocalConnection, that.localRepositoryLocalConnection) &&
                Objects.equals(localRepositoryRemoteConnection, that.localRepositoryRemoteConnection) &&
                eventsToSaveRule == that.eventsToSaveRule &&
                Objects.equals(selectedTypesToSave, that.selectedTypesToSave) &&
                eventsToSendRule == that.eventsToSendRule &&
                Objects.equals(selectedTypesToSend, that.selectedTypesToSend) &&
                Objects.equals(eventMapperConnection, that.eventMapperConnection);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getMetadataCollectionId(), getMetadataCollectionName(), getLocalRepositoryMode(),
                            getLocalRepositoryLocalConnection(), getLocalRepositoryRemoteConnection(),
                            getEventsToSaveRule(), getSelectedTypesToSave(),
                            getEventsToSendRule(), getSelectedTypesToSend(), getEventMapperConnection());
    }
}
