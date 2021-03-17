---
name: Release
about: Template for Tracking a new release
title: Release [x.y]
labels: release
assignees: planetf1

---

Create release 

- [ ] slack post
- [ ] agree in team call 
- [ ] Create branch 
- [ ] Reassign any issues not being worked on to the next release
- [ ] Update master version
- [ ] Update ref to presentation server image in chart(s)
- [ ] Ensure any remaining fixes are merged 
- [ ] Update branch version 
- [ ] Remove release notes from branch 
- [ ] update release notes & rename 
- [ ] Verify notebooks (see #4842)
- [ ] Check polymer UI 
- [ ] Check node (ps) UI (see #4843)
- [ ] Run CTS 
- [ ] Run FVTs 
- [ ] Check samples 
- [ ] Check swagger doc 
- [ ] Verify docker image/charts/compose 
- [ ] create release candidate (manual github action)
- [ ] Verify RC packaging on MC
- [ ] Fix up maven artifacts as needed
- [ ] Create final github releases (launches pipeline)
- [ ] Verify artifacts all on maven central 
- [ ] Release on maven central
- [ ] ensure any fixes ported to master
