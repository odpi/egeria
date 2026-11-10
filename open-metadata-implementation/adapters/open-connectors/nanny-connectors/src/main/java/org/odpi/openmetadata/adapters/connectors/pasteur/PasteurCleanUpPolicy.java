/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.pasteur;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * PasteurCleanUpPolicy decides which finished actions are old enough to be cleaned up.  It is built from the
 * daysToCleanUp and completionStatuses configuration properties and holds no connection to the open metadata
 * ecosystem, so the rules can be tested on their own.
 * <p>
 * An action is eligible for clean-up when all of these are true:
 * <ul>
 *     <li>its type, or the nearest of its supertypes, has an entry in daysToCleanUp;</li>
 *     <li>its activity status is one of the completion statuses;</li>
 *     <li>at least that many days have passed since it completed.  The completion time is the action's
 *     completionTime property, or the time it was last updated (or created) if that is not set.</li>
 * </ul>
 */
public class PasteurCleanUpPolicy
{
    private static final long millisecondsPerDay = 24L * 60L * 60L * 1000L;

    /**
     * The delete method used unless the deleteMethod configuration property names another.  An action with lineage
     * relationships is archived, so lineage queries can still find it, and the others are soft-deleted.
     */
    public static final DeleteMethod DEFAULT_DELETE_METHOD = DeleteMethod.LOOK_FOR_LINEAGE;

    /**
     * Type name to the minimum days after completion.  Sorted so the types are cleaned up, and reported, in the same
     * order each time.
     */
    private final Map<String, Long> daysToCleanUp = new TreeMap<>();

    /**
     * Symbolic names of the activity statuses that show an action has finished.
     */
    private final Set<String> completionStatuses = new HashSet<>();


    /**
     * Constructor.
     *
     * @param daysToCleanUp type name to the minimum days after completion - entries with a null key or value,
     *                      or a negative value, are skipped
     * @param completionStatuses symbolic names of the activity statuses that show an action has finished
     */
    public PasteurCleanUpPolicy(Map<String, Long>  daysToCleanUp,
                                Collection<String> completionStatuses)
    {
        if (daysToCleanUp != null)
        {
            for (String typeName : daysToCleanUp.keySet())
            {
                Long days = daysToCleanUp.get(typeName);

                if ((typeName != null) && (days != null) && (days >= 0))
                {
                    this.daysToCleanUp.put(typeName, days);
                }
            }
        }

        if (completionStatuses != null)
        {
            for (String completionStatus : completionStatuses)
            {
                if (completionStatus != null)
                {
                    this.completionStatuses.add(completionStatus);
                }
            }
        }
    }


    /**
     * Return the configured days to clean up.
     *
     * @return sorted map of type name to days
     */
    public Map<String, Long> getDaysToCleanUp()
    {
        return new TreeMap<>(daysToCleanUp);
    }


    /**
     * Return the type names that have an entry in daysToCleanUp, in the order they are cleaned up.
     *
     * @return list of type names
     */
    public List<String> getCleanUpTypeNames()
    {
        return new ArrayList<>(daysToCleanUp.keySet());
    }


    /**
     * Return the completion statuses.
     *
     * @return set of symbolic names
     */
    public Set<String> getCompletionStatuses()
    {
        return new HashSet<>(completionStatuses);
    }


    /**
     * Return whether there is anything to clean up.
     *
     * @return false if there are no days to clean up or no completion statuses
     */
    public boolean isActive()
    {
        return (! daysToCleanUp.isEmpty()) && (! completionStatuses.isEmpty());
    }


    /**
     * Return the minimum days after completion for an action's type.  The entry for the type itself is used if
     * there is one, otherwise the entry for the nearest supertype.
     *
     * @param typeName name of the action's type
     * @param superTypeNames names of its supertypes, nearest first - may be null
     * @return days, or null if neither the type nor any of its supertypes has an entry
     */
    public Long getDaysToCleanUp(String       typeName,
                                 List<String> superTypeNames)
    {
        if (typeName != null)
        {
            Long days = daysToCleanUp.get(typeName);

            if (days != null)
            {
                return days;
            }
        }

        if (superTypeNames != null)
        {
            for (String superTypeName : superTypeNames)
            {
                if (superTypeName != null)
                {
                    Long days = daysToCleanUp.get(superTypeName);

                    if (days != null)
                    {
                        return days;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Return whether the activity status shows the action has finished.
     *
     * @param activityStatus symbolic name of the action's activity status - may be null
     * @return boolean
     */
    public boolean isCompletionStatus(String activityStatus)
    {
        return (activityStatus != null) && (completionStatuses.contains(activityStatus));
    }


    /**
     * Return when the action completed.  This is its completionTime property if it is set.  Otherwise, it is the
     * time the action was last updated, since setting the final status is normally the last update.  An action
     * that has never been updated uses its creation time.
     *
     * @param completionTime the action's completionTime property - may be null
     * @param updateTime when the action was last updated - may be null
     * @param createTime when the action was created - may be null
     * @return date, or null if none of them is known
     */
    public static Date getCompletionDate(Date completionTime,
                                         Date updateTime,
                                         Date createTime)
    {
        if (completionTime != null)
        {
            return completionTime;
        }

        if (updateTime != null)
        {
            return updateTime;
        }

        return createTime;
    }


    /**
     * Return whether enough time has passed since an action completed.
     *
     * @param completionDate when the action completed - null means it is not known, so the action is kept
     * @param days minimum days after completion
     * @param now the time to measure from
     * @return boolean
     */
    public static boolean isOldEnough(Date completionDate,
                                      long days,
                                      Date now)
    {
        if ((completionDate == null) || (now == null))
        {
            return false;
        }

        return (now.getTime() - completionDate.getTime()) >= (days * millisecondsPerDay);
    }


    /**
     * Return whether an action is eligible for clean-up.
     *
     * @param typeName name of the action's type
     * @param superTypeNames names of its supertypes, nearest first - may be null
     * @param activityStatus symbolic name of the action's activity status - may be null
     * @param completionTime the action's completionTime property - may be null
     * @param updateTime when the action was last updated - may be null
     * @param createTime when the action was created - may be null
     * @param now the time to measure from
     * @return boolean
     */
    public boolean isEligible(String       typeName,
                              List<String> superTypeNames,
                              String       activityStatus,
                              Date         completionTime,
                              Date         updateTime,
                              Date         createTime,
                              Date         now)
    {
        Long days = this.getDaysToCleanUp(typeName, superTypeNames);

        if (days == null)
        {
            return false;
        }

        if (! this.isCompletionStatus(activityStatus))
        {
            return false;
        }

        return isOldEnough(getCompletionDate(completionTime, updateTime, createTime), days, now);
    }


    /**
     * Turn the value of the daysToCleanUp configuration property into a map of type name to days.  The values
     * arrive as numbers (Integer or Long, depending on their size, when the connection is read from JSON) or
     * as strings.  The entries that can not be turned into a number of days that is zero or more are skipped and
     * described in invalidEntries.
     *
     * @param configurationValue value of the configuration property - may be null
     * @param invalidEntries list to add descriptions of the skipped entries to
     * @return map of type name to days - empty if the value is null or not a map
     */
    public static Map<String, Long> parseDaysToCleanUp(Object       configurationValue,
                                                       List<String> invalidEntries)
    {
        Map<String, Long> daysToCleanUp = new TreeMap<>();

        if (configurationValue instanceof Map<?, ?> configuredMap)
        {
            for (Object key : configuredMap.keySet())
            {
                Object value = configuredMap.get(key);
                Long   days  = null;

                if (value instanceof Number number)
                {
                    days = number.longValue();
                }
                else if (value instanceof String stringValue)
                {
                    try
                    {
                        days = Long.parseLong(stringValue.trim());
                    }
                    catch (NumberFormatException notANumber)
                    {
                        days = null;
                    }
                }

                if ((key instanceof String typeName) && (! typeName.isBlank()) && (days != null) && (days >= 0))
                {
                    daysToCleanUp.put(typeName.trim(), days);
                }
                else
                {
                    invalidEntries.add(key + "=" + value);
                }
            }
        }
        else if (configurationValue != null)
        {
            invalidEntries.add(configurationValue.toString());
        }

        return daysToCleanUp;
    }


    /**
     * Check the configured completion statuses against the ActivityStatus enum.  The comparison ignores case and
     * surrounding spaces.  The statuses that are not recognized are skipped and added to invalidStatuses.
     *
     * @param configuredStatuses value of the completionStatuses configuration property - null means use the default
     * @param defaultStatuses statuses to use if none are configured
     * @param invalidStatuses list to add the unrecognized statuses to
     * @return list of the symbolic names of the recognized statuses
     */
    public static List<String> parseCompletionStatuses(List<String> configuredStatuses,
                                                       List<String> defaultStatuses,
                                                       List<String> invalidStatuses)
    {
        List<String> requestedStatuses = configuredStatuses;

        if ((requestedStatuses == null) || (requestedStatuses.isEmpty()))
        {
            requestedStatuses = defaultStatuses;
        }

        List<String> completionStatuses = new ArrayList<>();

        if (requestedStatuses != null)
        {
            for (String requestedStatus : requestedStatuses)
            {
                ActivityStatus activityStatus = null;

                if (requestedStatus != null)
                {
                    String symbolicName = requestedStatus.trim();

                    for (ActivityStatus candidate : ActivityStatus.values())
                    {
                        if (candidate.name().equalsIgnoreCase(symbolicName))
                        {
                            activityStatus = candidate;
                            break;
                        }
                    }
                }

                if (activityStatus == null)
                {
                    invalidStatuses.add(requestedStatus);
                }
                else if (! completionStatuses.contains(activityStatus.name()))
                {
                    completionStatuses.add(activityStatus.name());
                }
            }
        }

        return completionStatuses;
    }


    /**
     * Return the delete method named in the deleteMethod configuration property.
     *
     * @param configuredDeleteMethod symbolic name of a DeleteMethod value (case is ignored), or null
     * @return the named delete method, the default if none is named, or null if the name is not a delete method
     */
    public static DeleteMethod parseDeleteMethod(String configuredDeleteMethod)
    {
        if ((configuredDeleteMethod == null) || (configuredDeleteMethod.isBlank()))
        {
            return DEFAULT_DELETE_METHOD;
        }

        for (DeleteMethod deleteMethod : DeleteMethod.values())
        {
            if (deleteMethod.name().equalsIgnoreCase(configuredDeleteMethod.trim()))
            {
                return deleteMethod;
            }
        }

        return null;
    }
}
