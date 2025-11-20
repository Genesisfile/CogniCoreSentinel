#!/bin/bash
# G3P Full GitOps Automation Instructions
# Target Environment: Termux / Mobile

# 1. Define Target
REPO_URL=${1:-"https://github.com/Genesisfile/CogniCore.git"}
BRANCH="main"
echo "[*] Target: $REPO_URL ($BRANCH)"

# 2. Ensure Files
if [ ! -f gradlew ]; then
    echo "[!] gradlew missing. Attempting bootstrap..."
    if command -v gradle &> /dev/null; then
        gradle wrapper
    else
        echo "[!] Gradle not found locally. CI/CD will auto-bootstrap wrapper."
    fi
fi

# 3. Permissions
chmod +x gradlew 2>/dev/null || true

# 4. Git Init & Remote
if [ ! -d ".git" ]; then
    git init
    git branch -m $BRANCH
    git remote add origin "$REPO_URL"
else
    git remote set-url origin "$REPO_URL"
fi

# 5. Stage
git add gradlew gradlew.bat gradle/wrapper/* build.gradle settings.gradle MOBILE_BUILD_GUIDE.md .github/workflows/android_build.yml app src ci simulations

# 6. Commit
git config --global user.email "bot@cognicore.ai"
git config --global user.name "CogniCore Sentinel"
git commit -m "Add Gradle wrapper, build configs, and project sources for mobile GitOps" || echo "No changes."

# 7. Pull/Rebase
git pull origin $BRANCH --rebase || echo "Remote pull failed (new repo?), continuing..."

# 8. Push
git push -u origin $BRANCH

# 9. Trigger
echo "[*] Triggering Cloud Build..."
echo "# Trigger build $(date)" >> TRIGGER_BUILD.md
git add TRIGGER_BUILD.md
git commit -m "Trigger workflow"
git push origin $BRANCH

echo "[SUCCESS] GitOps Cycle Complete."
