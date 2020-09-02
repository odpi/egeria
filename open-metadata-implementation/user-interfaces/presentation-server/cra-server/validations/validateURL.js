/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

const validateURL = (url, servers) => {
  const urlArray = url.split("/");
  let isValid = true;
  if (url.length < 5) {
    console.log("Supplied url not long enough " + url);
    isValid = false;
  } else if (urlArray[4] != "users") {
    console.log("Users expected in url " + url);
    isValid = false;
  } else if (urlArray[5].length == 0) {
    console.log("No user supplied");
    isValid = false;
  } else {
    const suppliedserverName = urlArray[2];
    if (suppliedserverName.length == 0) {
      console.log("No supplied serverName ");
      isValid = false;
    } else {
      // check against environment -which have been parsed into the servers variable
      const serverDetails = servers[suppliedserverName];
      if (serverDetails == null) {
        console.log("ServerName not configured");
        isValid = false;
      } else if (serverDetails.remoteURL == undefined) {
        console.log(
          "ServerName " +
            suppliedserverName +
            " found but there was no associated remoteURL"
        );
        isValid = false;
      } else if (serverDetails.remoteServerName == undefined) {
        console.log(
          "ServerName " +
            suppliedserverName +
            " found but there was no associated remoteServerName"
        );
        isValid = false;
      }
    }
  }
  return isValid;
};

module.exports = validateURL;