/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

const axios = require('axios');
const getServerInfoFromEnv = require('./getServerInfoFromEnv');

const getAxiosInstance = (url) => {
  const urlArray = url.split("/");

  const suppliedServerName = urlArray[2];
  const remainingURL = urlArray.slice(3).join("/");
  const servers = getServerInfoFromEnv();
  const urlRoot = servers[suppliedServerName].remoteURL;
  const remoteServerName = servers[suppliedServerName].remoteServerName;
  const downStreamURL =
    urlRoot +
    "/servers/" +
    remoteServerName +
    "/open-metadata/view-services/" +
    remainingURL;
  console.log("downstream url " + downStreamURL);
  const instance = axios.create({
    baseURL: downStreamURL,
    httpsAgent: new https.Agent({
      // ca: - at some stage add the certificate authority
      cert: cert,
      key: key,
      rejectUnauthorized: false,
    }),
  });
  return instance;
};

module.exports = getAxiosInstance;