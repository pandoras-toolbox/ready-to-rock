import org.gradle.api.tasks.testing.logging.TestExceptionFormat

val log4jVersion = "2.13.3"
val allureVersion = "2.13.5"

plugins {
	java
	eclipse
	idea
	id("io.qameta.allure") version "2.8.1"
	// We need this plugin because checking for new dependency versions is not built into Gradle unlike Maven:
	id("com.github.ben-manes.versions") version "0.29.0"
	maven
}

group="org.rtr"
version="0.1.0"

description = "Ready to Rock"

repositories {
	jcenter()
	mavenCentral()
}

dependencies {
	// Common dependencies:
	// In order to log messages from dependencies through log4j2:
	implementation("org.apache.logging.log4j:log4j-api:${log4jVersion}")
	implementation("org.apache.logging.log4j:log4j-core:${log4jVersion}")
	implementation("org.apache.logging.log4j:log4j-slf4j-impl:${log4jVersion}")
	implementation("org.apache.logging.log4j:log4j-jul:${log4jVersion}")
	implementation("org.apache.logging.log4j:log4j-jcl:${log4jVersion}")

	implementation("commons-io:commons-io:2.7")

	// Dependencies for Apache Commons Configuration:
	implementation("org.apache.commons:commons-configuration2:2.7")
	implementation("commons-beanutils:commons-beanutils:1.9.4")

	implementation("io.qameta.allure:allure-java-commons:${allureVersion}")
	testImplementation("io.qameta.allure:allure-junit5:${allureVersion}")
	implementation(platform("org.junit:junit-bom:5.6.2"))
	implementation("org.junit.jupiter:junit-jupiter")
	implementation("org.assertj:assertj-core:3.16.1")
	// In order to add the assertions to the Allure report automatically:
	testImplementation("io.qameta.allure:allure-assertj:${allureVersion}")
	testImplementation("org.apache.commons:commons-lang3:3.11")

	// REST-Assured dependencies:
	testImplementation("io.rest-assured:rest-assured:4.3.1")
	// In order to make possible adding the request/response to the Allure report:
	testImplementation("io.qameta.allure:allure-rest-assured:${allureVersion}")

	// Selenium dependencies:
	implementation("org.seleniumhq.selenium:selenium-java:4.0.0-alpha-6")
	// Manages the binaries for the WebDriver automatically:
	implementation("io.github.bonigarcia:webdrivermanager:4.2.0")
	// Utility for screenshots:
	implementation("com.assertthat:selenium-shutterbug:1.1")
}

java {
	// For a newer Java version you also need to use newer versions of Allure and its dependencies.
	sourceCompatibility = JavaVersion.VERSION_14
	targetCompatibility = JavaVersion.VERSION_14
}

tasks.withType<Test>().configureEach {
	// Otherwise the JDK logging adapter won't work, see usage section: https://logging.apache.org/log4j/2.x/log4j-jul/index.html
	systemProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager")
	useJUnitPlatform {
		// Gradle does not support passing the tags from the command line unlike the plugin Surefire from Maven.
		if (project.hasProperty("includeTags")) {
			includeTags(project.property("includeTags").toString())
		}
		if (project.hasProperty("excludeTags")) {
			excludeTags(project.property("excludeTags").toString())
		}
	}
	testLogging {
		showStandardStreams = true
		exceptionFormat = TestExceptionFormat.FULL
		events("started", "passed", "skipped", "failed")
	}
}

// See: https://github.com/allure-framework/allure-gradle
allure {
	// Needed because it is mandatory and there is no default for it:
	version = allureVersion
	// Needs to be set to true, otherwise no steps would be displayed in the Allure report:
	autoconfigure = true
	// Needed because otherwise it would take a too old version:
	aspectjVersion = "1.9.6"
	// Needed because the default download link at bintray is broken:
	downloadLink = "https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/" +
			"${allureVersion}/allure-commandline-${allureVersion}.zip"
	useJUnit5 {
		version = allureVersion
	}
}
