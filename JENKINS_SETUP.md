# Jenkins CI/CD Pipeline Setup

This document provides instructions for setting up the Jenkins CI/CD pipeline for the Playwright Java E-commerce Framework.

## Prerequisites

- Jenkins server with Pipeline plugin installed
- **Java 17 installed on Jenkins agents** (manually installed)
- **Maven 3.8.7+ installed on Jenkins agents** (manually installed)
- Git plugin for Jenkins
- Credentials plugin for Jenkins
- Maven Integration plugin (optional but recommended)

## Manual Dependencies Installation

### Install Java 17 and Maven on Jenkins Agent

**If using Docker container:**
```bash
# Exec into the Jenkins container as root
docker exec -it <container_name> bash

# Install Java 17
apt-get update
apt-get install -y openjdk-17-jdk

# Install Maven
apt-get install -y maven

# Verify installations
java -version
mvn --version

# Exit container
exit
```

**If using Jenkins agent directly:**
```bash
# For Ubuntu/Debian
sudo apt update
sudo apt install -y openjdk-17-jdk maven

# For CentOS/RHEL
sudo yum install -y java-17-openjdk-devel maven

# Verify installations
java -version
mvn --version
```

## Pipeline Configuration

### 1. Create Multibranch Pipeline

1. **New Item** → **Multibranch Pipeline**
2. **Name**: `playwright-java-ecom-framework`
3. **Branch Sources**: Add Git repository
   - **Repository URL**: Your Git repository URL
   - **Credentials**: Add your Git credentials if needed

### 2. Configure Credentials

Add the following credentials in Jenkins:

#### API Credentials
- **ID**: `api-consumer-key`
- **Type**: Secret text
- **Value**: Your WooCommerce API Consumer Key

- **ID**: `api-consumer-secret`  
- **Type**: Secret text
- **Value**: Your WooCommerce API Consumer Secret

### 3. Pipeline Features

The Jenkinsfile includes:

#### ✅ **Parallel Execution**
- Build & Quality Gates run in parallel
- Frontend and Backend tests run in parallel
- Multi-browser testing (Chrome, Firefox, WebKit)

#### ✅ **Environment-Specific Deployment**
- **Dev**: All branches (develop, main, feature/*)
- **Staging**: Main branch only
- **Production**: Main branch only

#### ✅ **Artifact Collection**
- Test reports (Cucumber HTML, TestNG XML)
- Screenshots on test failures
- Security scan reports
- Build artifacts

#### ✅ **Smart Branching Logic**
- Uses `when` conditions to control stage execution
- Matches GitHub Actions behavior exactly

## Pipeline Stages

### Stage 1: Build & Quality Gates
- **Build**: Framework compilation (simulated)
- **Security Scan**: OWASP dependency check (simulated)
- **Lint Main App**: Main application linting (simulated)
- **Lint Automation Code**: Real Maven validation, compilation, and SpotBugs
- **Framework Validation**: Framework structure validation (simulated)

### Stage 2: Deploy to Dev
- **Deploy to Dev**: Simulated deployment
- **Health Check Dev**: Real health check of demo site

### Stage 3: UAT on Dev
- **Frontend Smoke Tests**: Real Playwright tests with Maven execution
- **Backend Smoke Tests**: Real API tests with Maven execution

### Stage 4: Deploy to Staging
- **Deploy to Staging**: Simulated deployment (main branch only)
- **Health Check Staging**: Real health check

### Stage 5: UAT on Staging
- **Frontend Regression Tests**: Multi-browser testing (Chrome, Firefox, WebKit) with Maven
- **Backend Regression Tests**: API regression tests with Maven

### Stage 6: Deploy to Prod
- **Deploy to Prod**: Simulated deployment (main branch only)

### Stage 7: Post-Deployment Checks
- **Post-Deployment Checks**: Health checks and critical smoke tests

## Environment Variables

The pipeline uses these environment variables:

```groovy
environment {
    JAVA_VERSION = '17'
    MAVEN_VERSION = '3.9.6'
    API_CONSUMER_KEY = credentials('api-consumer-key')
    API_CONSUMER_SECRET = credentials('api-consumer-secret')
}
```

## Artifact Collection

The pipeline automatically collects:

- **Test Reports**: `target/cucumber-reports/`, `target/surefire-reports/`
- **Screenshots**: `target/screenshots/failed/` (on test failures)
- **Security Reports**: `target/dependency-check-report.*`

## Branch Strategy

| Branch | Dev Deploy | Staging Deploy | Prod Deploy |
|--------|------------|----------------|-------------|
| `main` | ✅ | ✅ | ✅ |
| `develop` | ✅ | ❌ | ❌ |
| `feature/*` | ✅ | ❌ | ❌ |
| Other | ❌ | ❌ | ❌ |

## Webhook Configuration

To trigger builds automatically on Git push:

1. **Repository Settings** → **Webhooks**
2. **Add Webhook**:
   - **Payload URL**: `https://your-jenkins-server/github-webhook/`
   - **Content Type**: `application/json`
   - **Events**: Push, Pull Request

## Monitoring & Notifications

The pipeline includes:

- **Build Status**: Success/Failure/Unstable indicators
- **Timestamps**: All log entries include timestamps
- **Color Output**: ANSI color support for better readability
- **Artifact Archiving**: All reports and screenshots preserved
- **Build History**: Keeps last 10 builds

## Troubleshooting

### Common Issues

1. **Java Version Mismatch**
   - Ensure Jenkins agents have Java 17 manually installed
   - Check `JAVA_HOME` environment variable
   - Run `java -version` to verify installation

2. **Maven Not Found**
   - Ensure Maven is manually installed on Jenkins agent
   - Run `mvn --version` to verify installation
   - Check Maven is in PATH environment variable

3. **Playwright Browsers**
   - Browsers are installed automatically during test execution via Maven
   - Ensure sufficient disk space for browser binaries
   - Check Maven can download and install browsers

4. **API Credentials**
   - Verify credentials are properly configured in Jenkins
   - Check credential IDs match the Jenkinsfile (`API_CONSUMER_KEY`, `API_CONSUMER_SECRET`)

5. **Docker Container Issues**
   - If using Docker, ensure Java and Maven are installed in the container
   - Container changes may be lost on restart - consider using a custom image

### Logs Location

- **Pipeline Logs**: Jenkins → Build → Console Output
- **Test Reports**: Build → Artifacts → target/cucumber-reports/
- **Screenshots**: Build → Artifacts → target/screenshots/

## Advanced Configuration

### Custom Build Parameters

To add custom build parameters, modify the Jenkinsfile:

```groovy
parameters {
    choice(
        name: 'ENVIRONMENT',
        choices: ['dev', 'staging', 'prod'],
        description: 'Target environment'
    )
    booleanParam(
        name: 'SKIP_TESTS',
        defaultValue: false,
        description: 'Skip test execution'
    )
}
```

### Slack Integration

Add Slack notifications in the `post` section:

```groovy
post {
    success {
        slackSend channel: '#devops',
                  color: 'good',
                  message: "✅ Pipeline ${env.BUILD_NUMBER} succeeded!"
    }
    failure {
        slackSend channel: '#devops',
                  color: 'danger',
                  message: "❌ Pipeline ${env.BUILD_NUMBER} failed!"
    }
}
```

## Security Considerations

- **Credentials**: Store sensitive data in Jenkins Credentials Store
- **Agent Security**: Ensure Jenkins agents are properly secured
- **Network Access**: Configure firewall rules for required ports
- **Artifact Retention**: Set appropriate retention policies for build artifacts

---

**Note**: This pipeline is designed to mirror the GitHub Actions workflow exactly, providing the same functionality and behavior in Jenkins.
