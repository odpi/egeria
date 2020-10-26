/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.context;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * IntegrationContext is the base class for the integration context provided to the integration connector to provide access to open metadata
 * services.  Each integration service specializes this class to provide the method appropriate for the particular type of technology it
 * is supporting.
 *
 * This base class supports the common methods available to all types of integration connectors.
 */
public abstract class IntegrationContext
{
    private Map<String, Integer> counterMap  = new HashMap<>();
    private Map<String, String>  propertyMap = new HashMap<>();
    private Map<String, Date>    timestampMap = new HashMap<>();


    /**
     * Default constructor
     */
    protected IntegrationContext()
    {
    }


    /**
     * Retrieve all of the statistics gathered by the connector.
     *
     * @return name-value pairs for the statistics
     */
    public Map<String, Object>  getStatistics()
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
     * Reset (or initialize) the value of a counter.  This method reserves the counterName as a name for counters.  This means the same name
     * can not be used for a property or a timestamp.
     *
     * @param counterName name of the counter
     * @param counterValue initial value of the counter
     * @param methodName calling method
     *
     * @throws InvalidParameterException the counter name is already in use as a timestamp or property
     */
    public void initializeCounter(String   counterName,
                                  int      counterValue,
                                  String   methodName) throws InvalidParameterException
    {
        final String actionDescription = "initialize a counter statistic";
        final String parameterName = "counterName";

        if (propertyMap.get(counterName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(counterName,
                                                                                                                          methodName,
                                                                                                                          actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(counterName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(counterName,
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
    public void incrementCounter(String   counterName,
                                 String   methodName) throws InvalidParameterException
    {
        final String actionDescription = "increment a counter statistic";
        final String parameterName = "counterName";

        if (propertyMap.get(counterName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(counterName,
                                                                                                                              methodName,
                                                                                                                              actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(counterName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(counterName,
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
                counterValue = counterMap.get(counterName);
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
    public int getCounter(String counterName,
                          String methodName) throws InvalidParameterException
    {
        final String actionDescription = "increment a counter statistic";
        final String parameterName = "counterName";

        if (propertyMap.get(counterName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(counterName,
                                                                                                                              methodName,
                                                                                                                              actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(counterName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(counterName,
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
    public void setProperty(String propertyName,
                            String propertyValue,
                            String methodName) throws InvalidParameterException
    {
        final String actionDescription = "set a property statistic";
        final String parameterName = "propertyName";

        if (counterMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(propertyName,
                                                                                                                             methodName,
                                                                                                                             actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(propertyName,
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
    public void clearProperty(String propertyName,
                              String methodName) throws InvalidParameterException
    {
        final String actionDescription = "remove a property statistic";
        final String parameterName = "propertyName";

        if (counterMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(propertyName,
                                                                                                                             methodName,
                                                                                                                             actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(propertyName,
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
    public String getProperty(String propertyName,
                              String methodName) throws InvalidParameterException
    {
        final String actionDescription = "retrieve a property statistic";
        final String parameterName = "propertyName";

        if (counterMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(propertyName,
                                                                                                                             methodName,
                                                                                                                             actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (timestampMap.get(propertyName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_TIMESTAMP_NAME.getMessageDefinition(propertyName,
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
    public void setTimestamp(String timestampName,
                            Date    timestampValue,
                            String  methodName) throws InvalidParameterException
    {
        final String actionDescription = "set a timestamp statistic";
        final String parameterName = "timestampName";

        if (counterMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(timestampName,
                                                                                                                             methodName,
                                                                                                                             actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (propertyMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(timestampName,
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
    public void clearTimestamp(String timestampName,
                               String methodName) throws InvalidParameterException
    {
        final String actionDescription = "remove a timestamp statistic";
        final String parameterName = "timestampName";

        if (counterMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(timestampName,
                                                                                                                             methodName,
                                                                                                                             actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (propertyMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(timestampName,
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
    public Date getTimestamp(String timestampName,
                             String methodName) throws InvalidParameterException
    {
        final String actionDescription = "retrieve a timestamp statistic";
        final String parameterName = "timestampName";

        if (counterMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_COUNTER_NAME.getMessageDefinition(timestampName,
                                                                                                                             methodName,
                                                                                                                             actionDescription),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else if (propertyMap.get(timestampName) != null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.ALREADY_PROPERTY_NAME.getMessageDefinition(timestampName,
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
}
