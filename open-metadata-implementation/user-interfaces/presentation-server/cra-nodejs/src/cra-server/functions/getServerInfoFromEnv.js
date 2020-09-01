/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

const getServerInfoFromEnv = () => {

  console.log(" getServerInfoFromEnv() 1");
  let modifiableServers = {};
  // capitals as Windows can be case sensitive.
  const env_prefix = "EGERIA_PRESENTATIONSERVER_SERVER_";
  console.log(process.env);

  const env = process.env;
  
  for (const envVariable in env) {
    try {
      if (envVariable.startsWith(env_prefix)) {
        // Found an environment variable with out prefix
        console.log(" getServerInfoFromEnv() 2");
        if (envVariable.length == env_prefix.length - 1) {
          console.log(
            "there is no server name specified in the environment Variable envVariable.length=" +
              envVariable.length +
              ",env_prefix.length - 1=" +
              env_prefix.length -
              1
          );
        } else {
          const serverName = envVariable.substr(env_prefix.length);
          console.log("Found server name " + serverName);
          const serverDetailsStr = env[envVariable];
          const serverDetails = JSON.parse(serverDetailsStr);
          if (
            serverDetails.remoteURL != undefined &&
            serverDetails.remoteServerName != undefined
          ) {
            modifiableServers[serverName] = serverDetails;
          } else {
            console.log(
              "Found server environment variable for server " +
                serverName +
                ", but the value was not as expected :" +
                serverDetailsStr
            );
          }
        }
      }
    } catch (error) {
      console.log(error);
      console.log(
        "Error occured processing environment variables. Ignore and carry on looking for more valid server content."
      );
    }
  }
  return modifiableServers;

}

module.exports = getServerInfoFromEnv;