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
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation("com.google.firebase:firebase-admin:9.1.1")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	compileOnly("org.projectlombok:lombok:1.18.26")
	annotationProcessor("org.projectlombok:lombok:1.18.26")

}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.create("buildFrontend"){
	doFirst {
		val dir: File = projectDir.parentFile.parentFile
		exec{
			workingDir = File("${dir.path}/Frontend")

            executable = if (org.gradle.internal.os.OperatingSystem.current().isWindows) "npx.cmd" else "npx"

			args = listOf("-p", "@angular/cli", "ng", "build")
		}
		val frontendSource = File("${dir.path}/Frontend/dist/ssp")
		val backendResources = File("${projectDir.path}/src/main/resources/public/angular")
		copy {
			from(frontendSource)
			into(backendResources)
		}
	}
}
task("installAngular"){
	doLast{
		val dir: File = projectDir.parentFile.parentFile
		exec {
			workingDir = File("${dir.path}/Frontend")
            executable = if (org.gradle.internal.os.OperatingSystem.current().isWindows) "npm.cmd" else "npm"
			args = listOf("ci")
		}
	}
}

tasks.create("cleanFrontend"){
	doLast{
		delete(files(projectDir.path + "/src/main/resources/public/angular"))
	}
}

tasks.classes{
	dependsOn("buildFrontend")
}

tasks.clean{
	dependsOn("cleanFrontend")
}
