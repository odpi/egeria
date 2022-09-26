package org.odpi.openmetadata.metadatasecurity.samples;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.http.HttpHeadersThreadLocal;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CocoPharmaServerSecurityConnectorTokenBased extends OpenMetadataServerSecurityConnector {
    /*
     * These variables represent the different groups of user.  Typically these would be
     * implemented as a look up to a user directory such as LDAP rather than in memory lists.
     * The lists are used here to make the demo easier to set up.
     */
    private List<String> allUsers = new ArrayList<>();
    private Map<String, String> usersJWT = new HashMap<>();

    public CocoPharmaServerSecurityConnectorTokenBased() {
        final String garyGeekeUserId = "garygeeke";

        final String garyGeekeJWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJnYXJ5Z2Vla2UiLCJhbGxvd2VkVG9Jc3N1ZVJlcXVlc3RzIjoieWVzIn0.rh1z32o92sSrgTErD6COZ8kM9KoB_w-UOB2mcuL6D9k";

        allUsers.add(garyGeekeUserId);

        usersJWT.put(garyGeekeUserId, garyGeekeJWT);
    }


    @Override
    public void validateUserForServer(String userId) throws UserNotAuthorizedException {
        if (allUsers.contains(userId)) {
            Map<String, String> headersMap = HttpHeadersThreadLocal.getHeadersThreadLocal().get();
            if (headersMap != null &&
                headersMap.get("authorisation").equals(usersJWT.get(userId))) {
                return;
            }
        }
        super.validateUserForServer(userId);
    }
}
