package org.odpi.openmetadata.adapters.connectors.database.gaian;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class GaianOCFConnectorTest {

    private  String userId;
    private String query;
    private GaianOCFConnector gaianOCFConnector;

    @BeforeMethod
    public void setUp() throws Exception {
        userId="gaiandb";
        query="select * from gaiandb.LT0";
        gaianOCFConnector=new GaianOCFConnector(
                "jdbc:derby://",
                "localhost",
                "6414",
                "gaiandb",
                "gaiandb",
                "passw0rd",
                true);
    }

    @Test
    void setDBUrl() {
        String url="jdbc:derby://localhost:6414/gaiandb;create=true;user=gaiandb;" +
                "password=passw0rd;proxy-user=gaiandb;proxy-pwd=passw0rd";
        try {
            gaianOCFConnector.setDBUrl(userId);
        } catch (ConnectionCheckedException e) {
            e.printStackTrace();
        }
        assertEquals(url,gaianOCFConnector.getUrl());
    }
}