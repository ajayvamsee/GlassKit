# Publishing GlassKit

This document describes how to build, test, and publish the **GlassKit** library to Maven Central, JitPack, or a local Maven repository (`mavenLocal`).

---

## 1. Publishing to Maven Central

GlassKit is configured with the [Vanniktech Gradle Maven Publish Plugin](https://github.com/vanniktech/gradle-maven-publish-plugin).

### Prerequisites

1. **Maven Central Account**: Control the `io.github.ajayvamsee` namespace on Sonatype / Central Portal.
2. **GPG Signing Key**: A GPG private key formatted as an armored ASCII string (`-----BEGIN PGP PRIVATE KEY BLOCK-----`).

### Configuration

Add your credentials and GPG key in your global `~/.gradle/gradle.properties` or set them as environment variables. **Do not commit these credentials to version control.**

```properties
mavenCentralUsername=your_maven_central_username
mavenCentralPassword=your_maven_central_password
signingInMemoryKey=-----BEGIN PGP PRIVATE KEY BLOCK-----\n...
signingInMemoryKeyPassword=your_gpg_key_passphrase
```

### Publishing Command

Run the Gradle publishing task:

```shell
./gradlew :glasskit:publishToMavenCentral
```

---

## 2. Publishing via JitPack

JitPack builds the library directly from GitHub releases or commit tags.

### Prerequisites

A `jitpack.yml` file is located at the root of the repository configuring OpenJDK 21.

### Steps

1. Commit and tag your release version:
   ```shell
   git add .
   git commit -m "Release 1.0.0"
   git tag 1.0.0
   git push origin main --tags
   ```
2. Navigate to [jitpack.io](https://jitpack.io) and enter `ajayvamsee/GlassKit`.
3. Click **Get it** to trigger the automated build.

---

## 3. Publishing to Maven Local (`mavenLocal`)

For local development and testing across projects on your local machine:

```shell
./gradlew :glasskit:publishToMavenLocal
```

This installs the artifact under `~/.m2/repository/io/github/ajayvamsee/glasskit/1.0.0/`.

---

## 4. Generating Documentation (Dokka)

To generate Javadoc HTML documentation locally:

```shell
./gradlew :glasskit:dokkaGeneratePublicationJavadoc
```
