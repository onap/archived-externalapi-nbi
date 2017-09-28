Convert .doc/.docx to RST
=========================
- Install Python - https://www.python.org/downloads/
- Install Pandoc - https://pandoc.org/installing.html
- Run Pandoc Conversion
    - pandoc -s -t rst --toc {SOURCE_FILE} -o {TARGET_FILE}
- Handle Common Anomolies
    - Replace all \*** with \**
    - Ensure all tables are formatted per the RST Table Rules (ie. | and + are all aligned)
- Test Locally
    - tox -edocs            (open docs\_build\html\index.html)
        - Fix Warnings and Errors from the Tox Output
    - tox -edocs-linkcheck  (checks that all included links are valid



Commit Changes to Gerrit
========================
- Install git       - https://git-scm.com/book/id/v2/Getting-Started-Installing-Git
- Install gitreview - https://docs.openstack.org/infra/git-review/installation.html
- Clone Repository  - https://gerrit.onap.org/r/#/admin/projects/externalapi/nbi
    - SSH
        - git clone ssh://<LinuxFoundationUsername>@gerrit.onap.org:29418/externalapi/nbi
    - HTTP
        - git clone http://<LinuxFoundationUsername>@gerrit.onap.org/r/a/externalapi/nbi
- Set Remote
    - SSH
        - git remote add gerrit ssh://<LinuxFoundationUsername>@gerrit.onap.org:29418/externalapi/nbi
    - HTTP
        - git remote add gerrit http://<LinuxFoundationUsername>@gerrit.onap.org/r/a/externalapi/nbi
- Install Git Commit Message Hook
    - SSH
       - scp -p -P 29418 <LinuxFoundationUsername>@gerrit.onap.org:hooks/commit-msg .git/hooks
    - HTTP
        - curl -Lo .git/hooks  <LinuxFoundationUsername>@gerrit.onap.org:hooks/commit-msg
- Add Changes
    - git add -A
- Check Staged Changes
    - git status
- Commit Changes
    - git-review -s
    - git commit -s
    - Write Commit Message Per the ONAP Instructions - https://wiki.onap.org/display/DW/Commit+Messages
- Submit Patch to Gerrit
    - git review
