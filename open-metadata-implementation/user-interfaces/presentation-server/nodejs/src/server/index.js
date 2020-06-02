/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
const https = require('https');
const fs = require('fs');
const express = require("express");
var session = require("express-session");
var bodyParser = require("body-parser");
const passport = require("passport");
const LocalStrategy = require("passport-local").Strategy;
const db = require("./db");
const path = require("path");
// Create the Express app
const app = express();

const PORT = process.env.PORT || 8091;

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
app.use(function(req, res, next) {
    console.log("before " + req.url);
    const segmentArray = req.url.split("/")
    const segmentNumber = segmentArray.length;

    if (segmentNumber > 1) {
      const segment1 = segmentArray.slice(1, 2).join("/");
      console.log("segment1 " + segment1);
  
      if (segment1 != "servers") {
        // in a production scenario we are looking at login, favicon.ico and bundle.js for for now look for those in the last segment
        // TODO once we have development webpack, maybe the client should send a /js/ or a /static/ segment after the servername so we know to keep the subsequent segments.

       const lastSegment = segmentArray.slice(-1);
       console.log("Last segment is " + lastSegment);
       if (lastSegment == "bundle.js" || lastSegment == "favicon.ico" || lastSegment == "login") {
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
  new LocalStrategy(function(username, password, cb) {
    console.log("username: " + username);
    console.log("password: " + password);
    db.users.findByUsername(username, function(err, user) {
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
passport.serializeUser(function(user, cb) {
  console.log("serializeUser called with user " + user);
  cb(null, user.id);
});

passport.deserializeUser(function(id, cb) {
    console.log("deserializeUser called with id " + id);
    db.users.findById(id, function(err, user) {
    console.log("passport.deserializeUser user is " +user + ",err is" + err);  
    if (err) {
      return cb(err);
    }
    cb(null, user);
  });
});

app.post("/login", function(req, res, next) {
  console.log("/login");
  passport.authenticate("local", function(err, user, next) {
    if (err) { return next(err); }
    if (!user) { return res.json({ status:"failed", "error": "Invalid credentials" }); }
  
  // req / res held in closure
    req.logIn(user, function(err) {
        if (err) { return next(err); }
  
        return res.json({ "status":"success"});
    })
  })(req, res, next);
});



// app.use(function(req, res, next) {
//   console.log("check whether logged in req.user is " + req.user );
//   if (!req.user) {
//     res.redirect("/" + req.query.serverName + "/loggedOut");
//   }
// });
function loggedIn(req, res, next) {
  if (req.user) {
      next();
  } else {
    res.redirect("/" + req.query.serverName + "/login");
  }
}

app.get('/logout', function (req, res){
  console.log("/logout");
  req.session.destroy(function (err) {
    // https://stackoverflow.com/questions/13758207/why-is-passportjs-in-node-not-removing-session-on-logout
    //  explicity clear the cookie. 
    res.clearCookie('connect.sid');
    console.log("re direct to /loggedOut");
    res.redirect("/" + req.query.serverName + "/login");
  });
});

const staticJoinedPath = path.join(__dirname, "../../dist");
app.use(express.static(staticJoinedPath, {index:false}));
// app.use("bundle.js", (req, res) => {
//   res.sendFile(staticJoinedPath);
// });

const joinedPath = path.join(__dirname, "../../dist", "index.html");
//app.use(express.static(joinedPath));


app.get("/login", (req, res) => {
  console.log("/login called " + joinedPath);
  res.sendFile(joinedPath);
});
app.use(bodyParser.json());

app.post("/servers/*", (req, res) => {
  console.log("/servers/* post called " + req.url);
  console.log('Got body:', req.body);
  const urlArray = req.url.split('/');
  console.log(" segment 2 - this is the supplied serverName:" + urlArray[2]);
  // TODO the server name is currently hard coded
  const viewServerName = 'viewserver';
  const downStreamURL = "/servers/" + viewServerName + "/open-metadata/view-services/" +urlArray.slice(3).join('/');
  console.log("downstream url " + downStreamURL);  
  const request = require('request');
  const body = req.body;
  console.log(" body ") + body;
   // TODO the downstream URL is currently hard coded
  request.post('http://localhost:8085' + downStreamURL, {
    json: req.body
  }, (error, res2, body2) => {
    if (error) {
      console.log("Error " + error);
      // TODO proper errors
      res.status(400).send(error);
      // res.status(400).send({errMsg:"The presentation server got an error attempting to connect to the view server " + viewServerName + ". Contact your administrator to ensure that server is correctly configured and is active." );
      return;
    }
    console.log(`statusCode: ${res2.statusCode}`);
    console.log("post response content:" + body2);
    res.setHeader('Content-Type', 'application/json');
    res.json(body2);
  });

  //res.sendFile(joinedPath);
});

app.get("/servers/*", (req, res) => {
  console.log("/servers/* get called " + req.url);
  const urlArray = req.url.split('/');
  console.log(" segment 2" + urlArray[2]);
  const viewServerName = 'viewserver';
  const downStreamURL = "/servers/" + viewServerName + "/open-metadata/view-services/" +urlArray.slice(3).join('/');
  console.log("downstream url " + downStreamURL);  

  const request = require('request');
  request.get('http://localhost:8085' + downStreamURL, {
    json: true
  }, (error, res3, body3) => {
    if (error) {
      console.log("Error " + error);
      // TODO proper errors
      res.status(400).send(error);
      // res.status(400).send({errMsg:"The presentation server got an error attempting to connect to the view server " + viewServerName + ". Contact your administrator to ensure that server is correctly configured and is active." );
      return;
    }
    console.log(`statusCode: ${res3.statusCode}`);
    console.log("get response content:" + body3);
    res.setHeader('Content-Type', 'application/json');
    res.json(body3);
  });

  //res.sendFile(joinedPath);
});

app.use("*", loggedIn, (req, res) => {
  
  res.sendFile(joinedPath);
});

// ssl self signed certificate and key
var cert = fs.readFileSync(__dirname + '/../../ssl/keys/server.cert');
var key = fs.readFileSync(__dirname + '/../../ssl/keys/server.key');
var options = {
  key: key,
  cert: cert
};

// create the https server 
https.createServer(options, app).listen(PORT, () =>
     {
          console.log(`App listening to ${PORT}....`);
          console.log("Press Ctrl+C to quit.");
     }
);
