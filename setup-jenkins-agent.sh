#!/bin/bash

# Jenkins Agent Setup Script
# Installs Java 17 and Maven 3.9.6 if not already present

set -e  # Exit on any error

echo "🚀 Starting Jenkins agent setup..."

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to install Java 17
install_java() {
    echo "📦 Installing Java 17..."
    
    if command_exists java; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
        if [ "$JAVA_VERSION" -ge 17 ]; then
            echo "✅ Java $JAVA_VERSION already installed"
            return 0
        fi
    fi
    
    # Update package list
    sudo apt-get update
    
    # Install OpenJDK 17
    sudo apt-get install -y openjdk-17-jdk
    
    # Set JAVA_HOME
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
    echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> ~/.bashrc
    
    echo "✅ Java 17 installed successfully"
}

# Function to install Maven
install_maven() {
    echo "📦 Installing Maven 3.9.6..."
    
    if command_exists mvn; then
        MAVEN_VERSION=$(mvn -version | head -n 1 | cut -d' ' -f3)
        echo "✅ Maven $MAVEN_VERSION already installed"
        return 0
    fi
    
    # Create Maven directory
    MAVEN_HOME=/opt/maven
    sudo mkdir -p $MAVEN_HOME
    
    # Download Maven
    cd /tmp
    wget https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
    
    # Extract Maven
    sudo tar -xzf apache-maven-3.9.6-bin.tar.gz -C $MAVEN_HOME --strip-components=1
    
    # Set Maven environment variables
    echo "export MAVEN_HOME=$MAVEN_HOME" >> ~/.bashrc
    echo "export PATH=\$PATH:\$MAVEN_HOME/bin" >> ~/.bashrc
    
    # Make Maven available in current session
    export MAVEN_HOME=$MAVEN_HOME
    export PATH=$PATH:$MAVEN_HOME/bin
    
    # Clean up
    rm -f apache-maven-3.9.6-bin.tar.gz
    
    echo "✅ Maven 3.9.6 installed successfully"
}

# Function to install Playwright browsers
install_playwright() {
    echo "🎭 Installing Playwright browsers..."
    
    # This will be run after Maven is available
    if command_exists mvn; then
        mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium firefox webkit" || {
            echo "⚠️ Playwright browser installation failed, but continuing..."
        }
        echo "✅ Playwright browsers installed"
    else
        echo "⚠️ Maven not available for Playwright installation"
    fi
}

# Main installation process
main() {
    echo "🔍 Checking system requirements..."
    
    # Update package list
    sudo apt-get update
    
    # Install basic dependencies
    sudo apt-get install -y wget curl unzip
    
    # Install Java 17
    install_java
    
    # Install Maven
    install_maven
    
    # Install Playwright browsers (if Maven is available)
    install_playwright
    
    # Verify installations
    echo "🔍 Verifying installations..."
    
    if command_exists java; then
        echo "✅ Java: $(java -version 2>&1 | head -n 1)"
    else
        echo "❌ Java installation failed"
        exit 1
    fi
    
    if command_exists mvn; then
        echo "✅ Maven: $(mvn -version | head -n 1)"
    else
        echo "❌ Maven installation failed"
        exit 1
    fi
    
    echo "🎉 Jenkins agent setup completed successfully!"
    echo "📋 Installed components:"
    echo "  - Java 17"
    echo "  - Maven 3.9.6"
    echo "  - Playwright browsers (Chromium, Firefox, WebKit)"
}

# Run main function
main "$@"
