/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;


import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * The Connector is the interface for all connector instances.   Connectors are client-side interfaces to assets
 * such as data stores, data sets, APIs, analytical functions.  They handle the communication with the server that
 * hosts the assets, along with the communication with the metadata server to serve up metadata (properties) about
 * the assets.
 * </p>
 * <p>
 * Each connector implementation is paired with a connector provider.  The connector provider is the factory for
 * connector instances.
 * </p>
 * <p>
 * The Connector interface defines that a connector instance should be able to return a unique
 * identifier and a connection object.
 * </p>
 * <p>
 * Each specific implementation of a connector then extends the Connector interface to add the methods to work with the
 * particular type of asset it supports.  For example, a JDBC connector would add the standard JDBC SQL interface, the
 * OMRS Connectors add the metadata repository management APIs...
 * </p>
 * <p>
 * The initialize() method is called by the Connector Provider to set up the connector instance Identifier and the
 * Connection properties for the connector as part of its construction process.
 * </p>
 * <p>
 * The start() method is called by the code that creates the connector when it is ready for the connector to run.
 * This calling code is also responsible for calling disconnect().
 * </p>
 * <p>
 * Every connector is able to gather statistics during its operation - these are named properties, named counters and named timestamps.
 * The getConnectorStatistics() returns the combination of these values.
 * </p>
 * <p>
 * Both the connector and the connector provider have base classes (ConnectorBase and
 * ConnectorProviderBase respectively) that implement all the standard methods.  The connector developer extends
 * these classes to add the specific methods to manage the asset and configure the base classes.
 * </p>
 */
public abstract class Connector
{
    private final Map<String, Integer> counterMap   = new HashMap<>();
    private final Map<String, String>  propertyMap  = new HashMap<>();
    private final Map<String, Date>    timestampMap = new HashMap<>();

    /**
     * Default constructor
     */
    protected Connector()
    {
    }

    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connection   POJO for the configuration used to create the connector.
     */
    public abstract void initialize(String     connectorInstanceId,
                                    Connection connection);


    /**
     * Returns the unique connector instance id that assigned to the connector instance when it was created.
     * It is useful for error and audit messages.
     *
     * @return guid for the connector instance
     */
    public abstract String getConnectorInstanceId();


    /**
     * Returns the connection object that was used to create the connector instance.  Its contents are never refreshed
     * during the lifetime of a connector instance even if the connection information is updated or removed from
     * the originating metadata repository.
     *
     * @return connection properties object
     */
    public abstract Connection getConnection();


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public abstract void start() throws ConnectorCheckedException, UserNotAuthorizedException;


    /**
     * Retrieve all the statistics gathered by the connector.
     *
     * @return name-value pairs for the statistics
     */
    public Map<String, Object> getConnectorStatistics()
    {
        Map<String, Object>  statistics = new HashMap<>();

        if (! counterMap.isEmpty())
        {
            statistics.putAll(counterMap);
        }

        if (! propertyMap.isEmpty())
        {
            statistics.putAll(propertyMap);
        }

        if (! timestampMap.isEmpty())
        {
            statistics.putAll(timestampMap);
        }

        if (statistics.isEmpty())
        {
            return null;
        }
        else
        {
            return statistics;
        }
    }


    /**
     * Recalculate statistics for the supplied date.
     *
     * @param startDate start date for statistics calculations
     * @throws ConnectorCheckedException An issue occurred creating the statistics
     */
    public void refreshStatistics(Date startDate) throws ConnectorCheckedException
    {

    }


    /**
     * Reset (or initialize) the value of a counter.  This method reserves the counterName as a name for counters.  This means the same name
     * can not be used for a property or a timestamp.
     *
     * @param counterName name of the counter
     * @param counterValue initial value of the counter
     * @param methodName calling method
     *
     * @throws InvalidParameterException the counter name is already in use as a timestamp or property
     */
    public void initializeStatisticCounter(String   counterName,
                                           int      counterValue,
                                           String   methodName) throws InvalidParameterException
    {
        final String actionDescription = "initialize a counter statistic";
        final String parameterName = "counterName";

        if (propertyMap.get(counterName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(counterName,
                                                                                                        methodName,
                                                                                                        actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(counterName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(counterName,
                                                                                                         methodName,
                                                                                                         actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            counterMap.put(counterName, counterValue);
        }
    }


    /**
     * Increment the named counter.  If the counter name is new, it is assumed to be set to zero, resulting in a value of 1.
     *
     * @param counterName name of counter
     * @param methodName calling method
     * @throws InvalidParameterException the counter name is already in use as a timestamp or property
     */
    public void incrementStatisticCounter(String   counterName,
                                          String   methodName) throws InvalidParameterException
    {
        final String actionDescription = "increment a counter statistic";
        final String parameterName = "counterName";

        if (propertyMap.get(counterName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(counterName,
                                                                                                        methodName,
                                                                                                        actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(counterName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(counterName,
                                                                                                         methodName,
                                                                                                         actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            int counterValue = 1;

            if (counterMap.get(counterName) != null)
            {
                counterValue = counterMap.get(counterName) + 1;
            }

            counterMap.put(counterName, counterValue);
        }
    }


    /**
     * Retrieve the value for a specific counter statistic.  If the counter is not known, it is defined in the statistics list with a zero value.
     *
     * @param counterName name of the counter
     * @param methodName calling method
     *
     * @return counter value or 0 if the counter is not known
     * @throws InvalidParameterException the counter name is actually in use as a timestamp or property
     */
    public int getStatisticCounter(String counterName,
                                   String methodName) throws InvalidParameterException
    {
        final String actionDescription = "increment a counter statistic";
        final String parameterName = "counterName";

        if (propertyMap.get(counterName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(counterName,
                                                                                                        methodName,
                                                                                                        actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(counterName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(counterName,
                                                                                                         methodName,
                                                                                                         actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            counterMap.putIfAbsent(counterName, 0);

            return counterMap.get(counterName);
        }
    }


    /**
     * Set up the requested property.  An exception is thrown if the property name clashes with a counter or timestamp statistic.
     *
     * @param propertyName name of the property
     * @param propertyValue new value
     * @param methodName calling method name
     * @throws InvalidParameterException the property name is already in use as either a counter or timestamp statistic
     */
    public void setStatisticProperty(String propertyName,
                                     String propertyValue,
                                     String methodName) throws InvalidParameterException
    {
        final String actionDescription = "set a property statistic";
        final String parameterName = "propertyName";

        if (counterMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(propertyName,
                                                                                                       methodName,
                                                                                                       actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(propertyName,
                                                                                                         methodName,
                                                                                                         actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            propertyMap.put(propertyName, propertyValue);
        }
    }


    /**
     * Remove a property statistic.  An exception is thrown if the property name is actually the name of either a counter or timestamp statistic.
     *
     * @param propertyName name of property
     * @param methodName calling method
     * @throws InvalidParameterException the property name is actually a counter or timestamp statistic
     */
    public void clearStatisticProperty(String propertyName,
                                       String methodName) throws InvalidParameterException
    {
        final String actionDescription = "remove a property statistic";
        final String parameterName = "propertyName";

        if (counterMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(propertyName,
                                                                                                       methodName,
                                                                                                       actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(propertyName,
                                                                                                         methodName,
                                                                                                         actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            propertyMap.remove(propertyName);
        }
    }


    /**
     * Return the value associated with the supplied property name (or null if the property name is not known.
     *
     * @param propertyName requested property name
     * @param methodName calling method
     * @return property value
     * @throws InvalidParameterException the property name is actually a counter or timestamp statistic
     */
    public String getStatisticProperty(String propertyName,
                                       String methodName) throws InvalidParameterException
    {
        final String actionDescription = "retrieve a property statistic";
        final String parameterName = "propertyName";

        if (counterMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(propertyName,
                                                                                                       methodName,
                                                                                                       actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(propertyName,
                                                                                                         methodName,
                                                                                                         actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            return propertyMap.get(propertyName);
        }
    }


    /**
     * Set up the requested timestamp.  An exception is thrown if the timestamp name clashes with a counter or property statistic.
     *
     * @param timestampName name of the timestamp
     * @param timestampValue new value
     * @param methodName calling method name
     * @throws InvalidParameterException the timestamp name is already in use as either a counter or property statistic
     */
    public void setStatisticTimestamp(String timestampName,
                                      Date   timestampValue,
                                      String methodName) throws InvalidParameterException
    {
        final String actionDescription = "set a timestamp statistic";
        final String parameterName = "timestampName";

        if (counterMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(timestampName,
                                                                                                       methodName,
                                                                                                       actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (propertyMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(timestampName,
                                                                                                        methodName,
                                                                                                        actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            timestampMap.put(timestampName, timestampValue);
        }
    }


    /**
     * Remove a timestamp statistic.  An exception is thrown if the timestamp name is actually the name of either a counter or property statistic.
     *
     * @param timestampName name of timestamp
     * @param methodName calling method
     * @throws InvalidParameterException the timestamp name is actually a counter or property statistic
     */
    public void clearStatisticTimestamp(String timestampName,
                                        String methodName) throws InvalidParameterException
    {
        final String actionDescription = "remove a timestamp statistic";
        final String parameterName = "timestampName";

        if (counterMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(timestampName,
                                                                                                       methodName,
                                                                                                       actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (propertyMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(timestampName,
                                                                                                        methodName,
                                                                                                        actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            timestampMap.remove(timestampName);
        }
    }


    /**
     * Return the value associated with the supplied timestamp name (or null if the timestamp name is not known).
     *
     * @param timestampName requested timestamp name
     * @param methodName calling method
     * @return timestamp value
     * @throws InvalidParameterException the timestamp name is actually a counter or property statistic
     */
    public Date getStatisticTimestamp(String timestampName,
                                      String methodName) throws InvalidParameterException
    {
        final String actionDescription = "retrieve a timestamp statistic";
        final String parameterName = "timestampName";

        if (counterMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(timestampName,
                                                                                                       methodName,
                                                                                                       actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (propertyMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(OCFErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(timestampName,
                                                                                                        methodName,
                                                                                                        actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            return timestampMap.get(timestampName);
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public abstract void disconnect() throws ConnectorCheckedException;
}