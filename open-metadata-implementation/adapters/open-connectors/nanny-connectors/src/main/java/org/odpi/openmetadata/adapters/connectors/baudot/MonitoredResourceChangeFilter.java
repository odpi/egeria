/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot;

import org.odpi.openmetadata.frameworks.openmetadata.enums.OpenMetadataClassificationPurpose;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Decides whether a change event for a monitored resource is a change to the resource itself, and so worth
 * telling its notification types' subscribers about, or only a record of activity on it.
 * <br><br>
 * Observers keep records of what happens to an element in classifications on it: Pepys counts how often it is
 * read (UsageActivity), the open lineage cataloguer counts the runs that read it, and Lovelace records the results
 * of each analysis.  None of these changes the element.  Treating them as changes made a subscription feed itself:
 * the delivery read the product's data set, the read was recorded on the data set, and the record was taken as
 * new data, so the subscription was delivered again as soon as its minimum notification interval allowed - about
 * six times an hour for each ongoing-update subscription, whether or not the data had changed.
 * <br><br>
 * The rule is:
 * <ul>
 *     <li>A change to the element's own properties, or to one of its anchored elements, is a change.</li>
 *     <li>A change to a classification whose purpose is ANALYTICAL is not: those classifications are the
 *         observers' records of activity on the element.</li>
 *     <li>A change to the DataScope classification is a change only if it moves the data collection start or
 *         end time - the times that say which data the resource holds.  Its additional properties hold read
 *         counts and analysis results.</li>
 *     <li>A change to any other classification is a change.</li>
 * </ul>
 */
public class MonitoredResourceChangeFilter
{
    private static final Map<String, OpenMetadataClassificationPurpose> classificationPurposes = new HashMap<>();

    static
    {
        for (OpenMetadataType openMetadataType : OpenMetadataType.values())
        {
            if (openMetadataType.classificationPurpose != null)
            {
                classificationPurposes.put(openMetadataType.typeName, openMetadataType.classificationPurpose);
            }
        }
    }

    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Return whether the event describes a change to the monitored resource rather than a record of activity on it.
     *
     * @param event change event for the monitored resource (or one of its anchored elements)
     * @return true if the subscribers should be told
     */
    public boolean isChangeToResource(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "isChangeToResource";

        if ((event == null) || (event.getClassificationName() == null))
        {
            return true;
        }

        String classificationName = event.getClassificationName();

        if (OpenMetadataClassificationPurpose.ANALYTICAL == classificationPurposes.get(classificationName))
        {
            return false;
        }

        if (OpenMetadataType.DATA_SCOPE_CLASSIFICATION.typeName.equals(classificationName))
        {
            return (! sameDate(OpenMetadataProperty.DATA_COLLECTION_START_TIME.name, event.getClassificationProperties(), event.getPreviousClassificationProperties(), methodName)) ||
                   (! sameDate(OpenMetadataProperty.DATA_COLLECTION_END_TIME.name, event.getClassificationProperties(), event.getPreviousClassificationProperties(), methodName));
        }

        return true;
    }


    /**
     * Compare a date property in the new and previous properties of a classification.
     *
     * @param propertyName name of the property
     * @param properties new properties (null if the classification has been removed)
     * @param previousProperties previous properties (null if the classification is new)
     * @param methodName calling method
     * @return true if the values are the same
     */
    private boolean sameDate(String            propertyName,
                             ElementProperties properties,
                             ElementProperties previousProperties,
                             String            methodName)
    {
        Date value         = propertyHelper.getDateProperty(BaudotSubscriptionManagementConnector.class.getName(), propertyName, properties, methodName);
        Date previousValue = propertyHelper.getDateProperty(BaudotSubscriptionManagementConnector.class.getName(), propertyName, previousProperties, methodName);

        return Objects.equals(value, previousValue);
    }
}
