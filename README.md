## Overview

This is a test automation example project. The purpose is to:

* show how selected open source tools can be chosen to create a useful test automation tool
* be an example for a programmatic approach to automation
* illustrate some newer technologies if they are better than previous ones
* demonstrate helpful techniques regarding test automation and the tooling for it
* serve as an initial template for a new test automation tool
* try out new libraries and approaches

Usually there are only specific examples on the internet, and you wonder if and how that fits together. 
Not here, at least I hope so.

This project should work well with Ubuntu 20.04 LTS and likely also with other operating systems.

## Features

* test report generation
* behavior-driven development by simply using annotations
* running only certain groups of tests
* parallel test execution
* capturing log output from dependencies
* configuration via property files
* data-driven testing with partly random input test data

## Supported ways of testing in this example

* Unit tests
* REST API tests
* Web browser application tests

## Out of scope (currently)

The example does not include integration into a CI/CD system, no Jenkins job and no Docker images exists for it.

Also, there is no integration with machine data tools like Kibana and Splunk. The tests and the applications under test 
log on the console and file only. Otherwise, we could have added in the test result report automatically a link to 
Kibana for example, which would point to the log events which occurred during the execution of a particular test.

## Architecture decisions

**Java**

I am most familiar with Java, so I have chosen it, and I think it is a great programming language. 

**Gradle**

I believe Gradle is better than Maven. Furthermore, Maven does not seem to be maintained very well anymore.

**JUnit 5**

This is a well-designed test framework which supports nearly everything you need, also for end-to-end tests.

**Allure**

Allure is nearly the perfect tool to create a useful test result report. To use BDD Java methods can be annotated with 
`@Story` and `@Step` for example. The integration with JUnit 5 is great. Compared to frameworks like Cucumber, with Allure 
the test automation is much more flexible, and the execution much more transparent.

**Apache Commons Configuration**

This dependency supports inheritance of property files, so that property values can be overridden. Also, it allows 
reusing already defined properties and thus not having to repeat them, like: `fullName=${lastName}, ${firstName}`

## Useful commands

**Run all tests:**

`./gradlew clean build`

**Display Allure report:**

`./gradlew allureReport allureServe`

**Run certain tests:**

`./gradlew clean build -PincludeTags='unitTests | apiTests'`

`./gradlew clean build -PexcludeTags='guiTests'`

**Check for dependency updates:**

`./gradlew dependencyUpdates`

**Update Gradle wrapper:**

`./gradlew wrapper --gradle-version 6.6`

**Terminate WebDriver zombie processes:**

`killall --regexp 'chromedriver|geckodriver'`

## Troubleshooting

### Selenium tests fail

Probably you need to configure the path to the binary of Chrome and Firefox. Create a private property file with your 
username as the file name with the suffix `.properties` in the folder `src/main/resources/`.

Then add the properties `selenium.chrome.binary` and `selenium.firefox.binary` with the path to the binaries as values.

### Cannot create Allure report

If showing the Allure report does not work, change allureVersion to 2.9.0 in [build.gradle.kts](build.gradle.kts).

The error would be something like: 
`Cannot find allure commanline in /home/zeljko/IdeaProjects/ready-to-rock/.allure/allure-2.13.5`

After the report once has been displayed successfully, change back to the previous value, which should work now.

## Links

* [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
* [Allure Test Report](http://allure.qatools.ru/)
