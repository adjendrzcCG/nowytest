# GenTest Workspace Template

Welcome to your GenTest workspace! This template provides a structured environment for application testing and quality assurance using GenTest.

## Overview

This workspace is preconfigured with:
- **Development Container** - Consistent development environment
- **Modernization Configuration** - Core settings for your testing project
- **Setup Scripts** - Initialization and automation tools
- **Documentation** - Guidelines and best practices

## Getting Started

### 1. Initialize Your Workspace
```bash
bash scripts/init.sh
```

This script will:
- Install dependencies
- Configure Git hooks
- Set up local environment variables
- Download the GenTest source repository into the `gentest` folder

### 2. Review Configuration
Edit `config/modernization-config.json` with your project details:
```json
{
  "projectName": "Your Application Name",
  "targetPlatform": "Azure",
  "estimatedComplexity": "medium",
  "stakeholders": ["team@company.com"]
}
```

### 3. Access GenSuite Platform
- Navigate to: https://genrevive-cockpit.azurewebsites.net
- Link this codespace repository in your portfolio
- Begin the testing workflow

## Project Structure

```
.
├── config/                  # Configuration files
│   └── modernization-config.json
├── scripts/                 # Utility scripts
│   ├── init.sh            # Initialize workspace
│   └── setup.sh           # Development setup
├── .devcontainer/         # Container configuration
│   └── devcontainer.json
├── gentest/                # Downloaded GenTest source repository
└── README.md              # This file
```

## Next Steps

1. **Configure Your Project**: Update `config/modernization-config.json`
2. **Initialize**: Run `bash scripts/init.sh`
3. **Explore**: Review the GenTest source in `gentest/`
4. **Execute**: Follow the generated testing plan

## Support

For questions or issues:
- Review testing best practices in `/docs/`
- Contact your modernization team

## Troubleshooting

### Setup fails
- Ensure Docker/Podman is running for devcontainer
- Run scripts with `bash` to avoid execute-bit issues after template upload: `bash scripts/init.sh`, `bash scripts/setup.sh`
- Optional: restore execute permissions if you prefer `./scripts/...`: `chmod +x scripts/*.sh`

### Configuration errors
- Validate JSON in `config/modernization-config.json`
- Ensure all required fields are populated

### Still need help?
Contact: modernization-support@genrevive.io

---

**Generated**: $(date)  
**Template Version**: 1.0  
**GenSuite Platform**: GenTest
