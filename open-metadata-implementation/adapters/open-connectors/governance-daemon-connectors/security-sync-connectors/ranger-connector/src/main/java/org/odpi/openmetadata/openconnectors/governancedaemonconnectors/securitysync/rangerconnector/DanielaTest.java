/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector;

import com.google.gson.Gson;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEvent;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEventType;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.Context;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassification;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanielaTest {

    public static void main(String[] args) {
        GovernanceEngineEvent event = new GovernanceEngineEvent();
        event.setEventType(GovernanceEngineEventType.NEW_CLASSIFIED_ASSET);

        GovernedAsset asset = new GovernedAsset();
        asset.setGuid("Daniela-test");

        List<Context> contextList = new ArrayList<>();
        Context context = new Context();
        context.setColumn("DEPT_Column");
        context.setTable("DEPT_Table");
        contextList.add(context);

        Context context2 = new Context();
        context2.setColumn("Column_1");
        context2.setTable("Table_2");
        contextList.add(context2);

        asset.setContexts(contextList);

        List<GovernanceClassification> classifications = new ArrayList<>();
        GovernanceClassification classification = new GovernanceClassification();
        classification.setGuid("1");
        classification.setName("Confidentiality");
        classification.setTagId("1");
        Map<String, String> att = new HashMap<>(1);
        att.put("level", "Internal");
        classification.setAttributes(att);

        classifications.add(classification);
        asset.setAssignedGovernanceClassifications(classifications);
        event.setGovernedAsset(asset);

        Gson gson = new Gson();
        System.out.println(gson.toJson(event));
    }
}
