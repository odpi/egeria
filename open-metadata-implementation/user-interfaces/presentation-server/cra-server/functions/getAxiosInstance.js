/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

const axios = require('axios');
const https = require("https");
const fs = require("fs");
const path = require("path")
const getServerInfoFromEnv = require('./getServerInfoFromEnv');

const cert = fs.readFileSync(path.join(__dirname, '../../') + "/../presentation-server/ssl/keys/server.cert");
const key = fs.readFileSync(path.join(__dirname, '../../') + "/../presentation-server/ssl/keys/server.key");

const getAxiosInstance = (url) => {

  try {

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

  } catch (err) {
    
    console.log(err);
    throw err;

  }

};

module.exports = getAxiosInstance;