# SonarQube - What is it?

SonarQube is an automated code analysis tool that allows you to detect possible flaws in your code that might not be always obvious for you. It supports around 26 languages, including Java.

SonarQube checks your project based upon provided rule sets and its test coverages. These are separated in these tiers:
* Major
* Bugs
* Code Smells

Their highly customizable rules allows you to set your own quality standards throughout your development project.

## Getting started
You can either download SonarQube and Sonar Scanner and run it locally. If you prefer a cleaner environment which you can get rid of easily, you can also use Docker to deploy the software.

If you feel restricted by the available feature set of SonarQube's Community Edition, you might think about upgrading to Developers Edition or use [SonarCloud](https://sonarcloud.io) instead.

During the presentation, you can use a demo account created by me too:
* [SonarQube link (https://sonarqube.gahr.dev)](https://sonarqube.gahr.dev)
* Username: `demo`
* Password: `tinf18b4`

## Deployment
### PREREQUISITES (LINUX)
You might run into errors if you haven't set some `sysctl` properties appropriately due to ElasticSearch's nature. These are commands providided by SonarQube's official Docker docs.
```bash
$ sysctl -w vm.max_map_count=262144
$ sysctl -w fs.file-max=65536
$ ulimit -n 65536
$ ulimit -u 4096 
```
However, these values are applied only until restart. Afterwards you need to set them again. You can avoid this by modifying these values in `/etc/systcl.d`
### Local
1. [Download](https://www.sonarqube.org/downloads/) SonarQube.
2. Extract it.
3. Run `StartSonar.bat` (Windows) or `sonar.sh console` (Linux/Mac).

### Docker
If you want to run it locally, simple use:
```bash
# Create a volume
$ docker volume create sonarqube

# Use 'docker run' and mount created volume
$ docker run -d  \
            --name sonarqube  \
            -p 9000:9000  \
            -v sonarqube:/opt/sonarqube  \
            sonarqube
```

In case you want to deploy SonarQube on your dedicated server, feel free to use my provided `docker-compose.yml`. Simply do:
```bash
$ curl -LO https://raw.githubusercontent.com/humenius/how-to-sonarqube/master/docker-compose.yml
$ docker-compose up -d
```

To look up logs, use `docker-compose logs [-f]`.

In this `docker-compose.yml`, we're using [Bitnami]()'s images as official images haven't worked on the author's machine properly. However, feel free to try out the [official images](https://hub.docker.com/_/sonarqube/).


## Usage (Maven)
After setting up SonarQube, if you want to scan your Java project (which uses Maven), you need to add the SonarQube Maven Plugin. Add this to your `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.sonarsource.scanner.maven</groupId>
            <artifactId>sonar-maven-plugin</artifactId>
            <version>3.6.0.1398</version>
        </plugin>
    </plugins>
</build>
```

Afterwards, run the following Maven goal (it requires you to have an access token from SonarQube!):
```bash
$ mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=the-generated-token
```

Alternatively, you can set the parameters as properties in your `.m2/settings.xml`.

### Usage (Gradle)
`build.gradle`:
```gradle
plugins {
  // ...
  id 'org.sonarqube' version '2.8'
  // ...
}
```

`gradle.properties`:
```properties
systemProp.sonar.host.url=<YOUR_SONAR_URL>
systemProp.sonar.login=<YOUR_SONAR_TOKEN>

# SonarQube may complain about different bytecodes than expected. You can fix this by adding this property:
# systemProp.sonar.java.libraries=<FOLDER_WITH_LIBRARIES>
```

In Bash (or IDE):
```bash
$ gradle sonarqube

# If you don't have Gradle installed, use this command if Gradle wrapper exists:
$ ./gradlew sonarqube
```

### Sonar Source
#### Local
1. [Download](https://github.com/SonarSource/sonar-scanner-cli/releases) Sonar Scanner CLI.
2. Extract it.
3. Check if the app works by using `sonar-scanner.bat -h` (Windows) or `sonar-scanner -h` (Linux).
4. Create a `sonar-project.properties` in your project directory. (e. g. for Java 8 w/ Gradle):
```properties
sonar.projectName=<YOUR_PROJECT_NAME>
sonar.projectKey=<YOUR_PROJECT_KEY>
sonar.projectVersion=<YOUR_PROJECT_VERSION>

sonar.sources=src
sonar.java.source=1.8
sonar.java.target=1.8
sonar.java.binaries=build/classes
```
5. Start Sonar Scanner from your project directory.

#### Docker
This creates a Docker container, runs it with console attached and stops the container without deleting it. Make sure you are in your project directory and `sonar-project.properties` is included.
java
In Bash:
```bash
$ docker run -e SONAR_HOST_URL=<YOUR_SONAR_URL> \
             -e SONAR_TOKEN=<YOUR_SONAR_TOKEN> \
             --name sonar-scanner \
             --user="$(id -u):$(id -g)" \
             -it \
             -v "$PWD:/usr/src" \
             sonarsource/sonar-scanner-cli
```

If you want to use the same container again, simply use `docker start sonar-scanner`.

## Integrating into CI
If you want to use them in your CI infrastructure, you can simply add the necessary commands to your configuration. Make sure your files are in the same repository too. For TeamCity, there is an official plugin that provides a Sonar Scanner. If you use GitLab CI or anything else that can use Docker images (including TeamCity!), there is an [Sonar Scanner CLI image](https://hub.docker.com/r/sonarsource/sonar-scanner-cli). However, be sure to fill out most of the required fields when setting up your Sonar Scanner config on the web frontend/for your project.

If the run was successful, you'll find your project on the web frontend. Its name is based of either:
- Maven GAV / Gradle: `groupId:artifactId`
- e. g. Gradle: `groupName:currentFolderName`
- Specified `sonar.projectKey`