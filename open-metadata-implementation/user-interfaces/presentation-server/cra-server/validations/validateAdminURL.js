/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

const validateAdminURL = (url) => {
  const urlArray = url.split("/");
  let isValid = true;
  if (url.length < 7) {
    console.log("Supplied url not long enough " + url);
    console.debug(urlArray.length);
    isValid = false;
  } else if (urlArray[3] != "users") {
    console.log("Users expected in url " + url);
    console.debug(urlArray[3]);
    isValid = false;
  } else if (urlArray[4].length == 0) {
    console.log("No user supplied");
    console.debug(urlArray[4]);
    isValid = false;
  } else if (urlArray[5] != "servers") {
    console.log("Servers expected in url");
    console.debug(urlArray[5]);
    isValid = false;
  } else if (urlArray[6].length == 0) {
    console.log("No server supplied");
    console.debug(urlArray[6]);
    isValid = false;
  }
  return isValid;
};

module.exports = validateAdminURL;