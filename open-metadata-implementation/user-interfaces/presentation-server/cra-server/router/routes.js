/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
const express = require('express');
const router = express.Router();
const path = require('path');
const axios = require('axios');
const https = require('https');

const getAxiosInstance = require('../functions/getAxiosInstance');
const validateURL = require('../validations/validateURL');
const validateAdminURL = require('../validations/validateAdminURL');


/**
 * Middleware to handle post requests that start with /login i.e. the login request. The tenant segment has been removed by previous middleware. 
 * The login is performed using passport' local authentication (http://www.passportjs.org/docs/authenticate/). 
 * TODO support other authentication style e.g oauth and ldap both of which passport supports.
 */
router.post("/login", function (req, res, next) {
  console.log("/login");
  // get passport instance from app
  const passport = (req.app.get('passport'));
  passport.authenticate("local", function (err, user, next) {
    if (err) {
      return next(err);
    }
    if (!user) {
      return res.json({ status: "failed", error: "Invalid credentials" });
    }

    // req / res held in closure
    req.logIn(user, function (err) {
      if (err) {
        return next(err);
      }

      delete user.password; // <- super secure :)

      return res.json({ status: "success", user });
    });
  })(req, res, next);
});

/**
 * logout - destroy the session
 */
router.get("/logout", function (req, res) {
  console.log("/logout");
  req.session.destroy(function (err) {
    // https://stackoverflow.com/questions/13758207/why-is-passportjs-in-node-not-removing-session-on-logout
    //  explicity clear the cookie.
    res.clearCookie("connect.sid");
    console.log("re direct to /loggedOut");
    res.redirect("/" + req.query.serverName + "/login");
  });
});

const staticJoinedPath = path.join(__dirname, "../../dist");
router.use(express.static(staticJoinedPath, { index: false }));
const joinedPath = path.join(__dirname, "../../dist", "index.html");
/**
 * Process login url,
 */
router.get("/login", (req, res) => {
  console.log("/login called " + joinedPath);
  res.sendFile(joinedPath);
});

router.post("/servers/*", (req, res) => {
  const incomingUrl = req.url;
  console.log("/servers/* post called " + incomingUrl);
  const body = req.body;
  console.log("Got body:", body);
  const servers = (req.app.get('servers'));
  if (validateURL(incomingUrl, servers)) {
    const instance = getAxiosInstance(incomingUrl);
    instance
      .post("", body)
      .then(function (response) {
        console.log("response.data");
        console.log(response.data);
        const resBody = response.data;
        res.setHeader("Content-Type", "application/json");
        res.json(resBody);
      })
      .catch(function (error) {
        console.log(error);
        res.status(400).send(error);
      })
      .then(function () {
        // always executed
      });
  } else {
    res.status(400).send("Error, invalid supplied URL: " + incomingUrl);
  }
});

/**
 * Middleware to proxy put requests that start with /servers.
 * The outbound call is made with https. 
 */
router.put("/servers/*", (req, res) => {
  const incomingUrl = req.url;
  console.log("/servers/* put called " + incomingUrl);
  const body = req.body;
  console.log("Got body:", body);
  const servers = (req.app.get('servers'));
  if (validateURL(incomingUrl, servers)) {
    const instance = getAxiosInstance(incomingUrl);
    instance
      .put("", body)
      .then(function (response) {
        console.log("response.data");
        console.log(response.data);
        const resBody = response.data;
        res.setHeader("Content-Type", "application/json");
        res.json(resBody);
      })
      .catch(function (error) {
        console.log(error);
        res.status(400).send(error);
      })
      .then(function () {
        // always executed
      });
  } else {
    res.status(400).send("Error, invalid supplied URL: " + incomingUrl);
  }
});

/**
 * Middleware to proxy delete requests that start with /servers.
 * The outbound call is made with https. 
 */
router.delete("/servers/*", (req, res) => {
  const incomingUrl = req.url;
  console.log("/servers/* delete called " + incomingUrl);
  const servers = (req.app.get('servers'));
  if (validateURL(incomingUrl, servers)) {
    const instance = getAxiosInstance(incomingUrl);
    instance
      .delete()
      .then(function (response) {
        console.log("response.data");
        console.log(response.data);
        const resBody = response.data;
        res.setHeader("Content-Type", "application/json");
        res.json(resBody);
      })
      .catch(function (error) {
        console.log(error);
        res.status(400).send(error);
      })
      .then(function () {
        // always executed
      });
  } else {
    res.status(400).send("Error, invalid supplied URL: " + incomingUrl);
  }
});

/**
 * Middleware to proxy get requests that start with /servers.
 * The outbound call is made with https. 
 */
router.get("/servers/*", (req, res) => {
  const url = req.url;
  console.log("/servers/* get called " + url);
  const servers = (req.app.get('servers'));
  if (validateURL(url, servers)) {
    const instance = getAxiosInstance(url);
    instance
      .get()
      .then(function (response) {
        // console.log("response");
        // console.log(response);
        // console.log("response.data");
        // console.log(response.data);
        const resBody = response.data;
        res.setHeader("Content-Type", "application/json");
        res.json(resBody);
      })
      .catch(function (error) {
        console.log(error);
        res.status(400).send(error);
      })
      .then(function () {
        // always executed
      });
  } else {
    res.status(400).send("Error, invalid supplied URL: " + url);
  }
});

// Handle admin services
router.get("/open-metadata/admin-services/*", (req, res) => {
  const incomingPath = req.path;
  console.log("/open-metadata/admin-services/* get called " + incomingPath);
  if (!(validateAdminURL(incomingPath))) {
    res.status(400).send("Error, invalid supplied URL: " + incomingPath);
    return;
  }
  const servers = (req.app.get('servers'));
  const urlRoot = servers[req.query.tenantId].remoteURL;
  const apiReq = {
    method: 'get',
    url: urlRoot + incomingPath,
    httpsAgent: new https.Agent({
      // ca: - at some stage add the certificate authority
      cert: router.get('cert'),
      key: router.get('key'),
      rejectUnauthorized: false,
    }),
    headers: {
      "Access-Control-Allow-Origin": "*",
    }
  }
  axios(apiReq)
    .then(function (response) {
      // console.log({response});
      const resBody = response.data;
      // console.log({resBody});
      if (resBody.relatedHTTPCode == 200) {
        res.json(resBody);
      } else {
        throw new Error(resBody.exceptionErrorMessage);
      }
    })
    .catch(function (error) {
      console.error({error});
      res.status(400).send(error);
    })
});

router.post("/open-metadata/admin-services/*", (req, res) => {
  const incomingUrl = req.url;
  console.log("/open-metadata/admin-services/* post called " + incomingUrl);
  if (!(validateAdminURL(incomingUrl))) {
    res.status(400).send("Error, invalid supplied URL: " + incomingUrl);
    return;
  }
  const {
    config,
    tenantId,
  } = req.body;
  const servers = (req.app.get('servers'));
  const urlRoot = servers[tenantId].remoteURL;
  const apiReq = {
    method: 'post',
    url: urlRoot + incomingUrl,
    headers: { 
      'Content-Type': 'application/json',
      "Access-Control-Allow-Origin": "*",
    },
    httpsAgent: new https.Agent({
      // ca: - at some stage add the certificate authority
      cert: router.get('cert'),
      key: router.get('key'),
      rejectUnauthorized: false,
    }),
  };
  if (config) apiReq.data = config;
  axios(apiReq)
    .then(function (response) {
      const resBody = response.data;
      if (resBody.relatedHTTPCode == 400) {
        // Config parameter error
        throw new Error(resBody.exceptionErrorMessage);
      }
      res.setHeader("Content-Type", "application/json");
      res.json(resBody);
    })
    .catch(function (error) {
      console.log(error);
      res.status(400).send(error);
    });
});

router.delete("/open-metadata/admin-services/*", (req, res) => {
  const incomingUrl = req.url;
  console.log("/open-metadata/admin-services/* delete called " + incomingUrl);
  if (!(validateAdminURL(incomingUrl))) {
    res.status(400).send("Error, invalid supplied URL: " + incomingUrl);
    return;
  }
  const {
    config,
    tenantId,
  } = req.body;
  const servers = (req.app.get('servers'));
  const urlRoot = servers[tenantId].remoteURL;
  const apiReq = {
    method: 'delete',
    url: urlRoot + incomingUrl,
    headers: { 
      'Content-Type': 'application/json',
    },
    httpsAgent: new https.Agent({
      // ca: - at some stage add the certificate authority
      cert: router.get('cert'),
      key: router.get('key'),
      rejectUnauthorized: false,
    }),
  };
  if (config) apiReq.data = config;
  axios(apiReq)
    .then(function (response) {
      const resBody = response.data;
      if (resBody.relatedHTTPCode == 400) {
        // Config parameter error
        throw new Error(resBody.exceptionErrorMessage);
      }
      res.setHeader("Content-Type", "application/json");
      res.json(resBody);
    })
    .catch(function (error) {
      console.log(error);
      res.status(400).send(error);
    });
});

// Handle platform services
router.get("/open-metadata/platform-services/*", (req, res) => {
  const incomingPath = req.path;
  console.log("/open-metadata/platform-services/* get called " + incomingPath);
  // TODO: Add validator for platform url
  // if (!(validatePlatformURL(incomingPath))) {
  //   res.status(400).send("Error, invalid supplied URL: " + incomingPath);
  //   return;
  // }
  const servers = (req.app.get('servers'));
  const urlRoot = servers[req.query.tenantId].remoteURL;
  const apiReq = {
    method: 'get',
    url: urlRoot + incomingPath,
    httpsAgent: new https.Agent({
      // ca: - at some stage add the certificate authority
      cert: router.get('cert'),
      key: router.get('key'),
      rejectUnauthorized: false,
    }),
  }
  axios(apiReq)
    .then(function (response) {
      const resBody = response.data;
      if (resBody.relatedHTTPCode == 200) {
        res.json(resBody);
      } else {
        throw new Error(resBody.exceptionErrorMessage)
      }
    })
    .catch(function (error) {
      console.error({error});
      res.status(400).send(error);
    })
});

module.exports = router;