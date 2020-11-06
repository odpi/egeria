/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
var records = [
  { id: 1, username: 'faith', password: 'admin', displayName: 'Faith Broker', emails: [ { value: 'faith@coco.com' } ], organizationName: 'myOrg' }
, { id: 2, username: 'faithbroker', password: 'admin', displayName: 'Faith Broker', emails: [ { value: 'faith@coco.com' } ], organizationName: 'myOrg' }
, { id: 3, username: 'callie', password: 'admin', displayName: 'Callie Quartile', emails: [ { value: 'callie@coco.com' } ], organizationName: 'myOrg' }
, { id: 4, username: 'calliequartile', password: 'admin', displayName: 'Callie Quartile', emails: [ { value: 'callie@coco.com' } ], organizationName: 'myOrg' }
, { id: 5, username: 'gary', password: 'admin', displayName: 'Faith Broker', emails: [ { value: 'gary@coco.com' } ], organizationName: 'myOrg' }
, { id: 6, username: 'garygeeke', password: 'admin', displayName: 'Gary Geeke', emails: [ { value: 'gary@coco.com' } ], organizationName: 'myOrg' }
, { id: 7, username: 'erin', password: 'admin', displayName: 'Erin Overview', emails: [ { value: 'erin@coco.com' } ], organizationName: 'myOrg' }
, { id: 8, username: 'erinoverview', password: 'admin', displayName: 'Erin Overview', emails: [ { value: 'erin@coco.com' } ], organizationName: 'myOrg' }
, { id: 9, username: 'zach', password: 'admin', displayName: 'Zach', emails: [ { value: 'zach@coco.com' } ], organizationName: 'myOrg' }
, { id: 10, username: 'steves', password: 'admin', displayName: 'Steves', emails: [ { value: 'steves@coco.com' } ], organizationName: 'myOrg' }
, { id: 11, username: 'terri', password: 'admin', displayName: 'Terri', emails: [ { value: 'terri@coco.com' } ], organizationName: 'myOrg' }
, { id: 12, username: 'tanyatidie', password: 'admin', displayName: 'Tanya Tidie', emails: [ { value: 'tanyatidie@coco.com' } ], organizationName: 'myOrg' }
, { id: 13, username: 'pollytasker', password: 'admin', displayName: 'Polly Tasker', emails: [ { value: 'pollytasker@coco.com' } ], organizationName: 'myOrg' }
, { id: 14, username: 'tessatube', password: 'admin', displayName: 'Tessa Tube', emails: [ { value: 'tessatube@coco.com' } ], organizationName: 'myOrg' }
, { id: 15, username: 'ivorpadlock', password: 'admin', displayName: 'Ivor Padlock', emails: [ { value: 'ivorpadlock@coco.com' } ], organizationName: 'myOrg' }
, { id: 16, username: 'bobnitter', password: 'admin', displayName: 'Bob Nitter', emails: [ { value: 'bobnitter@coco.com' } ], organizationName: 'myOrg' }
, { id: 17, username: 'sallycounter', password: 'admin', displayName: 'Sally Counter', emails: [ { value: 'sallycounter@coco.com' } ], organizationName: 'myOrg' }
, { id: 18, username: 'lemmiestage', password: 'admin', displayName: 'Lemmie Stage', emails: [ { value: 'lemmiestage@coco.com' } ], organizationName: 'myOrg' }
, { id: 19, username: 'harryhopeful', password: 'admin', displayName: 'Harry Hopeful', emails: [ { value: 'harryhopeful@coco.com' } ], organizationName: 'myOrg' }
, { id: 20, username: 'grantable', password: 'admin', displayName: 'Grant Able', emails: [ { value: 'grantable@coco.com' } ], organizationName: 'myOrg' }
, { id: 21, username: 'robbierecords', password: 'admin', displayName: 'Robbie Records', emails: [ { value: 'robbierecords@coco.com' } ], organizationName: 'myOrg' }
, { id: 22, username: 'reggiemint', password: 'admin', displayName: 'Reggie Mint', emails: [ { value: 'reggiemint@coco.com' } ], organizationName: 'myOrg' }
, { id: 23, username: 'peterprofile', password: 'admin', displayName: 'Peter Profile', emails: [ { value: 'peterprofile@coco.com' } ], organizationName: 'myOrg' }
, { id: 24, username: 'nancynoah', password: 'admin', displayName: 'Nancy Noah', emails: [ { value: 'nancynoah@coco.com' } ], organizationName: 'myOrg' }
, { id: 25, username: 'sidneyseeker', password: 'admin', displayName: 'Sidney Seeker', emails: [ { value: 'sidneyseeker@coco.com' } ], organizationName: 'myOrg' }
, { id: 26, username: 'tomtally', password: 'admin', displayName: 'Tom Tally', emails: [ { value: 'tomtally@coco.com' } ], organizationName: 'myOrg' }
, { id: 27, username: 'juliestitched', password: 'admin', displayName: 'Julie Stitched', emails: [ { value: 'juliestitched@coco.com' } ], organizationName: 'myOrg' }
, { id: 28, username: 'designa', password: 'admin', displayName: 'Designa', emails: [ { value: 'designa@coco.com' } ], organizationName: 'myOrg' }
, { id: 29, username: 'angelacummings', password: 'admin', displayName: 'Angela Cummings', emails: [ { value: 'angelacummings@coco.com' } ], organizationName: 'myOrg' }
, { id: 30, username: 'jukeskeeper', password: 'admin', displayName: 'Jukes Keeper', emails: [ { value: 'jukeskeeper@coco.com' } ], organizationName: 'myOrg' }
, { id: 31, username: 'stewfaster', password: 'admin', displayName: 'Stew Faster', emails: [ { value: 'stewfaster@coco.com' } ], organizationName: 'myOrg' }
, { id: 32, username: 'archiver01', password: 'admin', displayName: 'Archiver 01', emails: [ { value: 'archiver01@coco.com' } ], organizationName: 'myOrg' }
, { id: 33, username: 'dletl', password: 'admin', displayName: 'Dletl', emails: [ { value: 'dletl@coco.com' } ], organizationName: 'myOrg' }
, { id: 34, username: 'cocomds1npa', password: 'admin', displayName: 'Coco MDS1 NPA', emails: [ { value: 'cocomds1npa@coco.com' } ], organizationName: 'myOrg' }
, { id: 35, username: 'cocomds2npa', password: 'admin', displayName: 'Coco MDS2 NPA', emails: [ { value: 'cocomds2npa@coco.com' } ], organizationName: 'myOrg' }
, { id: 36, username: 'cocomds3npa', password: 'admin', displayName: 'Coco MDS3 NPA', emails: [ { value: 'cocomds3npa@coco.com' } ], organizationName: 'myOrg' }
, { id: 37, username: 'cocomds4npa', password: 'admin', displayName: 'Coco MDS4 NPA', emails: [ { value: 'cocomds4npa@coco.com' } ], organizationName: 'myOrg' }
, { id: 38, username: 'cocomds5npa', password: 'admin', displayName: 'Coco MDS5 NPA', emails: [ { value: 'cocomds5npa@coco.com' } ], organizationName: 'myOrg' }
, { id: 39, username: 'cocomds6npa', password: 'admin', displayName: 'Coco MDS6 NPA', emails: [ { value: 'cocomds6npa@coco.com' } ], organizationName: 'myOrg' }
, { id: 40, username: 'cocomdsxnpa', password: 'admin', displayName: 'Coco MDSX NPA', emails: [ { value: 'cocomdsxnpa@coco.com' } ], organizationName: 'myOrg' }


];

exports.findById = function(id, cb) {
process.nextTick(function() {
console.log("findByUserId");
var idx = id - 1;
if (records[idx]) {
  cb(null, Object.assign({}, records[idx]));
} else {
  cb(new Error('User ' + id + ' does not exist'));
}
});
}

exports.findByUsername = function(username, cb) {
process.nextTick(function() {
console.log("findByUsername");
for (var i = 0, len = records.length; i < len; i++) {
  var record = records[i];
  if (record.username === username) {
    return cb(null, Object.assign({}, record));
  }
}
return cb(null, null);
});
}
