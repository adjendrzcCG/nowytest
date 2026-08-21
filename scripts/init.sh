#!/bin/bash

# GenTest Workspace Initialization Script
# Sets up the GenTest workspace

set -e

echo "🚀 GenTest Workspace Initialization"
echo "======================================"
echo ""

# Color codes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

URL="https://gensuitestorage.blob.core.windows.net/github-agents-container/gentest/gentest_repository.zip?sp=r&st=2026-08-03T12:48:46Z&se=2027-08-03T21:03:46Z&spr=https&sv=2026-02-06&sr=b&sig=F%2FxnzW%2BboDGHcSQsTR8FhQ76YRaV5xoTxwlEjJ1BwJQ%3D"
DEST_DIR="gentest"
ZIP_PATH="$DEST_DIR/gentest_repository.zip"

# Check if running in Codespace
if [ "$GITHUB_CODESPACES" = "true" ]; then
  echo -e "${BLUE}✓ Running in GitHub Codespace${NC}"
else
  echo -e "${YELLOW}⚠ Not running in GitHub Codespace (may be local dev)${NC}"
fi

# 1. Validate configuration
echo -e "${BLUE}Checking configuration...${NC}"
if [ ! -f "config/modernization-config.json" ]; then
  echo -e "${YELLOW}⚠ config/modernization-config.json not found - creating template${NC}"
  mkdir -p config
fi

# 2. Make scripts executable
echo -e "${BLUE}Setting up permissions...${NC}"
chmod +x scripts/*.sh
echo -e "${GREEN}✓ Scripts are executable${NC}"

# 3. Download and extract GenTest source repository
echo -e "${BLUE}Downloading GenTest source repository...${NC}"
mkdir -p "$DEST_DIR"

if [ -z "$URL" ]; then
  echo -e "${YELLOW}⚠ No source repository URL configured, skipping download${NC}"
else
  if command -v curl >/dev/null 2>&1; then
    curl -fL "$URL" -o "$ZIP_PATH"
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$ZIP_PATH" "$URL"
  else
    echo "Error: neither curl nor wget is installed." >&2
    exit 1
  fi

  if ! command -v unzip >/dev/null 2>&1; then
    echo "Error: unzip is not installed." >&2
    exit 1
  fi

  unzip -o "$ZIP_PATH" -d "$DEST_DIR"
  echo -e "${GREEN}✓ Repository downloaded and extracted to $DEST_DIR${NC}"
fi

# 4. Install Node.js dependencies if package.json exists
if [ -f "package.json" ]; then
  echo -e "${BLUE}Installing Node dependencies...${NC}"
  npm install
  echo -e "${GREEN}✓ Node dependencies installed${NC}"
fi

# 5. Install Python dependencies if requirements.txt exists
if [ -f "requirements.txt" ]; then
  echo -e "${BLUE}Installing Python dependencies...${NC}"
  pip install -r requirements.txt
  echo -e "${GREEN}✓ Python dependencies installed${NC}"
fi

# 6. Set up Git hooks
echo -e "${BLUE}Setting up Git hooks...${NC}"
if [ -d ".git/hooks" ]; then
  # Create a pre-commit hook placeholder
  cat > .git/hooks/pre-commit << 'EOF'
#!/bin/bash
# Pre-commit hook for GenTest workspace
# Add your checks here
exit 0
EOF
  chmod +x .git/hooks/pre-commit
  echo -e "${GREEN}✓ Git hooks configured${NC}"
fi

# 7. Create necessary directories
echo -e "${BLUE}Setting up workspace structure...${NC}"
mkdir -p docs logs build

# 8. Initialize environment
if [ -f ".env.template" ]; then
  if [ ! -f ".env" ]; then
    cp .env.template .env
    echo -e "${YELLOW}⚠ Created .env from template - please configure${NC}"
  fi
fi

# 9. Display workspace info
echo ""
echo -e "${GREEN}✓ Workspace initialized successfully!${NC}"
echo ""
echo "Next steps:"
echo "  1. Update config/modernization-config.json with your project details"
echo "  2. Review README.md for documentation"
echo "  3. Run: bash scripts/setup.sh (optional, for development setup)"
echo ""
