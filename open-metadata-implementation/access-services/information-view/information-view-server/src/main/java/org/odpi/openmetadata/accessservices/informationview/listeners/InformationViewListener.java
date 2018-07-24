/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.listeners;


import org.odpi.openmetadata.accessservices.informationview.events.InformationViewEvent;

public interface InformationViewListener {

    void processEvent(InformationViewEvent event);

}
