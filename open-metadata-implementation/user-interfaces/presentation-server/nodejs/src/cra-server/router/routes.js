/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
const express = require('express');
const router = express.Router();
const path = require('path');
const axios = require('axios');

const getAxiosInstance = require('../functions/getAxiosInstance');
const validateURL = require('../validations/validateURL');
const validateAdminURL = require('../validations/validateAdminURL');


router.get('/', (req, res) => {
  res.send('Egeria server is live!');
})

/**
 * Middleware to handle post requests that start with /login i.e. the login request. The tenant segment has been removed by previous middleware. 
 * The login is performed using passport' local authentication (http://www.passportjs.org/docs/authenticate/). 
 * TODO support other authentication style e.g oauth and ldap both of which passport supports.
 */
router.post("/login", function (req, res, next) {
  console.log("/login");
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

      return res.json({ status: "success" });
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
  if (validateURL(incomingUrl)) {
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
  if (validateURL(incomingUrl)) {
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
  if (validateURL(incomingUrl)) {
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
  if (validateURL(url)) {
    const instance = getAxiosInstance(url);
    instance
      .get()
      .then(function (response) {
        console.log("response");
        console.log(response);
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
    res.status(400).send("Error, invalid supplied URL: " + url);
  }
});

// Handle admin services
router.get("/open-metadata/admin-services/*", (req, res) => {
  const incomingUrl = req.path;
  console.log("/open-metadata/admin-services/* get called " + incomingUrl);
  if (!(validateAdminURL(incomingUrl))) {
    res.status(400).send("Error, invalid supplied URL: " + incomingUrl);
    return;
  }
  const {
    platformURL
  } = req.query;
  const apiReq = {
    method: 'get',
    url: decodeURIComponent(platformURL) + incomingUrl,
    httpsAgent: new https.Agent({
      // ca: - at some stage add the certificate authority
      cert: router.get('cert'),
      key: router.get('key'),
      rejectUnauthorized: false,
    }),
  }
  axios(apiReq)
    .then(function (response) {
      console.log({response})
      const resBody = response.data;
      console.log({resBody});
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

router.post("/open-metadata/admin-services/*", (req, res) => {
  const incomingUrl = req.url;
  console.log("/open-metadata/admin-services/* post called " + incomingUrl);
  const {
    config,
    platformURL,
  } = req.body;
  if (validateAdminURL(incomingUrl)) {
    const apiReq = {
      method: 'post',
      url: platformURL + incomingUrl,
      headers: { 
        'Content-Type': 'application/json'
      },
      httpsAgent: new https.Agent({
        // ca: - at some stage add the certificate authority
        cert: router.get('cert'),
        key: router.get('key'),
        rejectUnauthorized: false,
      }),
      data: config,
    }
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
  } else {
    res.status(400).send("Error, invalid supplied URL: " + incomingUrl);
  }
});

module.exports = router;