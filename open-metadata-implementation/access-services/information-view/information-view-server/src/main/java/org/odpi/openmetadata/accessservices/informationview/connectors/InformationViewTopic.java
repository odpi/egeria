/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.connectors;


import org.odpi.openmetadata.accessservices.informationview.events.ColumnContextEvent;
import org.odpi.openmetadata.accessservices.informationview.listeners.InformationViewListener;

public interface InformationViewTopic {

    void registerListener(InformationViewListener newListener);

    void sendEvent(ColumnContextEvent event) throws Exception;
}
