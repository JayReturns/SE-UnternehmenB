plugins {
	java
	id("org.springframework.boot") version "3.0.6"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.dhbw.unternehmenb"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.create("buildFrontend"){
	doFirst {
		val dir: File = projectDir.parentFile.parentFile
		exec{
			workingDir = File("${dir.path}/Frontend")

			executable = "ng.cmd"
			args = listOf("build")
		}
		val frontendSource = File("${dir.path}/Frontend/dist/ssp")
		val backendResources = File("${projectDir.path}/src/main/resources/public")
		copy {
			from(frontendSource)
			into(backendResources)
		}
	}
}
task("installAngular"){
	doLast{
		val dir: File = projectDir.parentFile.parentFile
		exec{
			workingDir = File("${dir.path}/Frontend")
			executable = "npm.cmd"
			args = listOf("install", "-g", "@angular/cli")
		}
		exec {
			workingDir = File("${dir.path}/Frontend")
			executable = "npm.cmd"
			args = listOf("ci")
		}
	}
}

tasks.classes{
	dependsOn("buildFrontend")
}
