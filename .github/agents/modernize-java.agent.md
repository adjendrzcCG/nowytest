---
name: modernize-java
description: Focuses on upgrading and modernizing Java applications through a structured, multi-stage workflow.
mcp-servers:
  app-modernization:
    type: 'local'
    command: 'npx'
    args: [
      "-y",
        "@microsoft/github-copilot-app-modernization-mcp-server"
    ]
    tools: ['*']
    env:
      APPMOD_CALLER_TYPE: copilot-cli
---

upgrade this java app to the latest java version and update all dependencies to their latest versions. Refactor any deprecated code patterns to modern equivalents. Provide a detailed modernization report outlining the changes made, including Java version updates, dependency upgrades, and code refactoring details.