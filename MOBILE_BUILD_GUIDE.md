# 📱 Mobile Build Guide

## 1. Automated GitOps (Recommended)
Run the automation script to push code and trigger build:
```bash
cd CogniCoreSentinel
./automate_gitops.sh [YOUR_REPO_URL]
```

## 2. Manual Upload
1. Upload this `CogniCoreSentinel` folder to a new private GitHub Repository.
2. Go to **Actions** tab > **CogniCore Autonomous Build** > **Run workflow**.

## 3. Download
Once the build completes (check for green checkmark), download the `CogniCore-Sentinel-APK` artifact.
