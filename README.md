# Stack Overflow showcases
For illustrating the solution of Stack Overflow tasks.

[![GPLv3](https://img.shields.io/badge/licence-GPLv3-brightgreen.svg)](http://www.gnu.org/licenses/gpl-3.0.html)

[![Java CI](https://github.com/mle-enso/stackoverflow/actions/workflows/maven.yml/badge.svg)](https://github.com/mle-enso/stackoverflow/actions/workflows/maven.yml)

## Start

Either build the application own your own and run it…

* `mvn clean package`
* `java -jar target/stackoverflow-0.0.1-SNAPSHOT.jar`

…or build a Docker image and start it…

* `mvn spring-boot:build-image`
* `docker run -p 8080:8080 docker.io/library/stackoverflow:0.0.1-SNAPSHOT`

