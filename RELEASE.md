# Release Process

This document describes how to create a release of the Emoji Encoder application.

## Automated Release Workflow

The repository includes a GitHub Actions workflow (`.github/workflows/release.yml`) that automatically builds and creates releases for both the web and Android applications.

## How to Create a Release

### Method 1: Using Git Tags (Recommended)

1. Create and push a version tag:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. The workflow will automatically:
   - Build the Next.js web application as static files
   - Build the Android APK
   - Create a GitHub release with both artifacts

### Method 2: Manual Trigger

1. Go to the repository's Actions tab on GitHub
2. Select "Build and Release" workflow
3. Click "Run workflow"
4. Enter the desired version (e.g., `v1.0.0`)
5. Click "Run workflow"

## Release Artifacts

Each release will include:

- **emoji-encoder-web.zip** - Static web application files
  - Can be deployed to any web server
  - Contains HTML, CSS, and JavaScript files
  - No server-side runtime required

- **app-release-unsigned.apk** - Android application
  - Minimum Android version: 7.0 (API 24)
  - Target Android version: 14 (API 34)
  - **Note**: Unsigned APK - users will need to enable "Install from unknown sources"

## For Production Releases

### Web Application
The web application is exported as static files and can be deployed to:
- GitHub Pages
- Netlify
- Vercel
- Any static hosting service
- Traditional web servers (Apache, Nginx, etc.)

### Android Application
For production Android releases, you should:
1. Sign the APK with your own keystore
2. Optimize with ProGuard/R8 (already configured)
3. Consider publishing to Google Play Store

## Version Numbering

Follow semantic versioning:
- `v1.0.0` - Major.Minor.Patch
- `v1.0.0-beta.1` - Pre-release versions

## Troubleshooting

If the workflow fails:
1. Check the Actions tab for detailed logs
2. Ensure all tests pass locally with `npm test`
3. Verify the build works locally with `npm run build`
4. For Android issues, check the Gradle build logs

## Local Testing

Before creating a release, test the builds locally:

```bash
# Test web build
npm install
npm run build
# Check the 'out' directory

# Test Android build (requires Android SDK)
cd android-app
./gradlew assembleRelease
# Check android-app/app/build/outputs/apk/release/
```
