Sauce Demo Tests:
This project contains automated tests for the Sauce Demo application using Selenium, TestNG, and Cucumber. 
The tests are designed to run in a CI/CD pipeline using GitLab CI.

Project Structure:

SAUCEDEMO-TESTS
├── .settings
├── .vscode
│   ├── launch.json
│   ├── settings.json
├── bin
├── src
│   ├── main
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           ├── config
│       │           │   └── AppConfig.java
│       │           ├── pages
│       │           │   ├── LoginPage.java
│       │           │   ├── ProductsPage.java
│       │           ├── CartOperationsStepDefinitions.java
│       │           ├── RunCucumberTest.java
│       │           └── SharedSteps.java
│       ├── resources
│           ├── features
│           │   └── cart_operations.feature
│           ├── config.properties
│           ├── credentials.properties
│           └── user_data.properties
├── target
├── .classpath
├── .gitlab-ci.yml
├── .project
├── pom.xml
├── readme.txt
└── testng.xml

Prerequisites:
Java 17
Maven 3.6+

Setup
Clone the repository:

Install dependencies and build the project:
-mvn clean install

To display available updates for project dependencies, run:
-mvn versions:display-dependency-updates

Running Tests:
To execute all tests, use:
mvn test

To run tests with the debug profile, use:
mvn test -Pdebug
and then from Run and Debug (cntr+shift+D) start "Attacked to Maven cucumber Test" (For Vscode)

To run tests with specific tags, use:
-mvn test -D cucumber.filter.tags="@smoketest"

CI/CD Pipeline
This project is configured to run in a GitLab CI/CD pipeline. The .gitlab-ci.yml file defines the pipeline stages and jobs.

.gitlab-ci.yml Configuration
yaml
Copy code
stages:
  - build
  - test
  - e2e

variables:
  MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository"

cache:
  paths:
    - .m2/repository

before_script:
  - echo "Setting up the environment"
  - apt-get update -qq && apt-get install -qqy openjdk-17-jdk maven
  - export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
  - export PATH=$JAVA_HOME/bin:$PATH
  - mvn --version

build:
  stage: build
  script:
    - echo "Building the project"
    - mvn clean compile

test:
  stage: test
  parallel:
    matrix:
      - TEST_GROUP: "unit"
      - TEST_GROUP: "integration"
  script:
    - echo "Running $TEST_GROUP tests"
    - if [ "$TEST_GROUP" = "unit" ]; then mvn test -Dtest=*UnitTest; fi
    - if [ "$TEST_GROUP" = "integration" ]; then mvn test -Dtest=*IntegrationTest; fi

e2e:
  stage: e2e
  parallel:
    matrix:
      - BROWSER: "chrome"
      - BROWSER: "firefox"
      - BROWSER: "safari"
  script:
    - echo "Running end-to-end tests on $BROWSER"
    - mvn test -D cucumber.filter.tags="@e2e" -Dbrowser=$BROWSER
  artifacts:
    paths:
      - cucumber-reports.html
