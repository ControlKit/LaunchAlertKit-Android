# JitPack Setup Guide

This guide will help you publish LaunchAlertKit to JitPack for easy distribution.

## Prerequisites

1. GitHub repository (already set up)
2. JitPack account (free)
3. Proper Git tags for versioning

## Step 1: Prepare Your Repository

### 1.1 Update Version Information

Make sure your `launchalertkit/build.gradle.kts` has the correct version:

```kotlin
version = "0.0.2"  // Update this for each release
```

### 1.2 Update Library Information

Ensure your library module has proper configuration:

```kotlin
android {
    namespace = "com.sepanta.controlkit.launchalertkit"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        buildConfigField("int", "LIB_VERSION_CODE", "2")
        buildConfigField("String", "LIB_VERSION_NAME", "\"${project.version}\"")
    }
}
```

### 1.3 Update JitPack Configuration

Your `jitpack.yml` should be:

```yaml
jdk:
  - openjdk17
```

## Step 2: Create Git Tags

### 2.1 Create and Push Tags

```bash
# Create a tag for the current version
git tag -a v0.0.2 -m "Release version 0.0.2"

# Push the tag to GitHub
git push origin v0.0.2
```

### 2.2 Verify Tags

Check that your tags are on GitHub:
- Go to your repository on GitHub
- Click on "Releases" or "Tags"
- Verify your tag is there

## Step 3: Connect to JitPack

### 3.1 Add Repository to JitPack

1. Go to [JitPack.io](https://jitpack.io)
2. Sign in with your GitHub account
3. Click "Add Repository"
4. Enter your repository URL: `ControlKit/LaunchAlertKit-Android`
5. Click "Look up"

### 3.2 Build Your Library

1. JitPack will automatically detect your tags
2. Click on your version (e.g., "0.0.2")
3. Click "Get it" to trigger the build
4. Wait for the build to complete (usually 2-5 minutes)

## Step 4: Test the Integration

### 4.1 Update Your Example App

In your `app/build.gradle.kts`, comment out the local dependency and add the JitPack dependency:

```kotlin
dependencies {
    // implementation(project(":launchalertkit"))  // Comment this out
    implementation("com.github.ControlKit:LaunchAlertKit-Android:0.0.2")  // Add this
}
```

### 4.2 Test the Build

```bash
./gradlew clean build
```

## Step 5: Update Documentation

### 5.1 Update README.md

Make sure your README.md has the correct JitPack badge and installation instructions:

```markdown
[![JitPack](https://img.shields.io/jitpack/v/github/ControlKit/LaunchAlertKit-Android.svg)](https://jitpack.io/#ControlKit/LaunchAlertKit-Android)

dependencies {
    implementation 'com.github.ControlKit:LaunchAlertKit-Android:0.0.2'
}
```

### 5.2 Update Version Information

Update all version references in your documentation.

## Step 6: Create GitHub Release

### 6.1 Create Release on GitHub

1. Go to your repository on GitHub
2. Click "Releases" → "Create a new release"
3. Choose your tag (e.g., "v0.0.2")
4. Add release title: "LaunchAlertKit v0.0.2"
5. Add release notes from your CHANGELOG.md
6. Click "Publish release"

## Troubleshooting

### Common Issues

#### Build Fails on JitPack

**Problem**: JitPack build fails with errors

**Solutions**:
1. Check your `jitpack.yml` configuration
2. Ensure all dependencies are available
3. Check build logs on JitPack for specific errors
4. Make sure your `build.gradle.kts` files are correct

#### Version Not Found

**Problem**: Users can't find your version on JitPack

**Solutions**:
1. Ensure the tag exists on GitHub
2. Wait a few minutes for JitPack to sync
3. Check that the tag name matches the version in build.gradle.kts

#### Dependency Resolution Issues

**Problem**: Users get dependency resolution errors

**Solutions**:
1. Ensure all your dependencies are publicly available
2. Check that your library doesn't depend on local modules
3. Verify that all required repositories are accessible

### Build Logs

To check build logs:
1. Go to your repository on JitPack
2. Click on the version
3. Click "Log" to see the build output

## Best Practices

### Version Management

1. **Use Semantic Versioning**: `MAJOR.MINOR.PATCH`
2. **Update Version Consistently**: Update version in all relevant files
3. **Create Tags for Each Release**: Don't skip version tags
4. **Test Before Release**: Always test locally before creating tags

### Documentation

1. **Keep README Updated**: Update installation instructions
2. **Maintain Changelog**: Document all changes
3. **Provide Examples**: Include usage examples
4. **Update API Docs**: Keep API documentation current

### Quality Assurance

1. **Run Tests**: Ensure all tests pass
2. **Check Linting**: Fix all linting issues
3. **Test Integration**: Verify the library works in a real project
4. **Performance Check**: Ensure the library doesn't impact app performance

## Release Checklist

Before each release:

- [ ] Update version in `build.gradle.kts`
- [ ] Update `CHANGELOG.md`
- [ ] Update documentation
- [ ] Run all tests
- [ ] Check linting
- [ ] Test local build
- [ ] Create and push Git tag
- [ ] Create GitHub release
- [ ] Verify JitPack build
- [ ] Test integration in example app
- [ ] Update README.md with new version

## Support

If you encounter issues:

1. Check JitPack build logs
2. Review GitHub issues
3. Check JitPack documentation
4. Contact JitPack support if needed

## Next Steps

After successful JitPack setup:

1. Share your library with the community
2. Monitor usage and feedback
3. Plan future releases
4. Consider adding to Android Arsenal or other directories
5. Build a community around your library

---

**Congratulations!** Your LaunchAlertKit library is now available on JitPack for the Android community to use! 🎉
