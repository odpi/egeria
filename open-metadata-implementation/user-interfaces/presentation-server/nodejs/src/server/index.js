/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
const https = require("https");
const fs = require("fs");
const express = require("express");
var session = require("express-session");
var bodyParser = require("body-parser");
const passport = require("passport");
const axios = require("axios");
const LocalStrategy = require("passport-local").Strategy;
const db = require("./db");
const path = require("path");
// Create the Express app
const app = express();

const PORT = process.env.PORT || 8091;

// ssl self signed certificate and key
const cert = fs.readFileSync(__dirname + "/../../ssl/keys/server.cert");
const key = fs.readFileSync(__dirname + "/../../ssl/keys/server.key");
const options = {
  key: key,
  cert: cert,
};

/*
 * This middleware method takes off the first segment which is the serverName an puts it into a query parameter
 * I did consider using Regex match and replace along these lines 'const matchCriteria = /^\/([A-Za-z_][A-Za-z_0-9]+)\//;'
 * but decided not to in case the characters I ws tolerating whas not enough. Note the split slice join is not not very performant
 * due to the creation of array elements.
 *
 * For urls that start with servers - these are rest calls that need to be passed through to the back end.
 * URLs before and after
 *   /   => /
 *   /servers/aaa => /servers/aaa
 *   /servers/aaa/bbb => /servers/aaa/bbb
 *   /coco1/ => /?servername=coco1
 *   /coco1/abc => /abc?servername=coco1
 *   /coco1/abc/de => /abc/de?servername=coco1
 *   /display.ico => /display.ico
 *
 */
app.use(function (req, res, next) {
  console.log("before " + req.url);
  const segmentArray = req.url.split("/");
  const segmentNumber = segmentArray.length;

  if (segmentNumber > 1) {
    const segment1 = segmentArray.slice(1, 2).join("/");
    console.log("segment1 " + segment1);

    if (segment1 != "servers") {
      // in a production scenario we are looking at login, favicon.ico and bundle.js for for now look for those in the last segment
      // TODO once we have development webpack, maybe the client should send a /js/ or a /static/ segment after the servername so we know to keep the subsequent segments.

      const lastSegment = segmentArray.slice(-1);
      console.log("Last segment is " + lastSegment);
      if (
        lastSegment == "bundle.js" ||
        lastSegment == "favicon.ico" ||
        lastSegment == "login"
      ) {
        req.url = "/" + lastSegment;
      } else {
        // remove the server name and pass through
        req.url = "/" + segmentArray.slice(2).join("/");
      }
      req.query.serverName = segment1;
    }
  }
  console.log("after " + req.url);
  next();
});

// app.use(express.static("dist"));
// Initialize Passport and restore authentication state, if any, from the
// session.

// app.use(express.static("public"));
app.use(session({ secret: "cats" }));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(passport.initialize());
app.use(passport.session());

passport.use(
  new LocalStrategy(function (username, password, cb) {
    console.log("username: " + username);
    console.log("password: " + password);
    db.users.findByUsername(username, function (err, user) {
      if (err) {
        return cb(err);
      }
      if (!user) {
        return cb(null, false);
      }
      if (user.password != password) {
        return cb(null, false);
      }
      return cb(null, user);
    });
  })
);
// Configure Passport authenticated session persistence.
//
// In order to restore authentication state across HTTP requests, Passport needs
// to serialize users into and deserialize users out of the session.  The
// typical implementation of this is as simple as supplying the user ID when
// serializing, and querying the user record by ID from the database when
// deserializing.
passport.serializeUser(function (user, cb) {
  console.log("serializeUser called with user " + user);
  cb(null, user.id);
});

passport.deserializeUser(function (id, cb) {
  console.log("deserializeUser called with id " + id);
  db.users.findById(id, function (err, user) {
    console.log("passport.deserializeUser user is " + user + ",err is" + err);
    if (err) {
      return cb(err);
    }
    cb(null, user);
  });
});

app.post("/login", function (req, res, next) {
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

function loggedIn(req, res, next) {
  if (req.user) {
    next();
  } else {
    res.redirect("/" + req.query.serverName + "/login");
  }
}

app.get("/logout", function (req, res) {
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
app.use(express.static(staticJoinedPath, { index: false }));
const joinedPath = path.join(__dirname, "../../dist", "index.html");

app.get("/login", (req, res) => {
  console.log("/login called " + joinedPath);
  res.sendFile(joinedPath);
});
app.use(bodyParser.json());

const validateURL = (url) => {
  const urlArray = url.split("/");
  let isValid = true;
  // we are expecting /servers/<serverName>/<view-service-name>/users/<userId>/<optional additional endpoint information>
  
  if (url.length < 5) {
    console.log("Supplied url not long enough " + url);
    isValid = false;
  } else {
    if (urlArray[2].length == 0) {
      console.log("No supplied serverName ");
      isValid = false;
    } else if (urlArray[4] !="users") {
      console.log("Users expected in url " + url);
      isValid = false;
    } else if (urlArray[5].length == 0) {
      console.log("No user supplied");
      isValid = false;
    }
  }
  return isValid;
}

const getAxiosInstance = (url) => {
  const urlArray = url.split("/");

  const suppliedServerName = urlArray[2];
  const remainingURL = urlArray.slice(3).join("/");

  // TODO get from configuration
  const urlRoot = "https://localhost:9443";
  // TODO get from configuration
  const viewServerName = "cocoMDSV1";

  const downStreamURL =
    urlRoot +
    "/servers/" +
    viewServerName +
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

app.post("/servers/*", (req, res) => {
  const incomingUrl = req.url;
  console.log("/servers/* post called " + incomingUrl);
  const body = req.body;
  console.log("Got body:", body);
  if (validateURL(incomingUrl)) {
  const instance = getAxiosInstance(incomingUrl);
  instance.post("", body)
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


app.get("/servers/*", (req, res) => {
  const url = req.url;
  console.log("/servers/* get called " + url);
  if (validateURL(url)) {
  const instance = getAxiosInstance(url);
  instance.get()
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
    res.status(400).send("Error, invalid supplied URL: " + url);
  }
});

app.use("*", loggedIn, (req, res) => {
  res.sendFile(joinedPath);
});

// create the https server
https.createServer(options, app).listen(PORT, () => {
  console.log(`App listening to ${PORT}....`);
  console.log("Press Ctrl+C to quit.");
});
