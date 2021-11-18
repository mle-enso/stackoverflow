# Stack Overflow showcases
For illustrating the solution of Stack Overflow tasks.

[![GPLv3](https://img.shields.io/badge/licence-GPLv3-brightgreen.svg)](http://www.gnu.org/licenses/gpl-3.0.html)

[![Java CI](https://github.com/mle-enso/stackoverflow/actions/workflows/maven.yml/badge.svg)](https://github.com/mle-enso/stackoverflow/actions/workflows/maven.yml)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8d59d011cddc40afb612f3008b5d4d56)](https://app.codacy.com/gh/mle-enso/stackoverflow?utm_source=github.com&utm_medium=referral&utm_content=mle-enso/stackoverflow&utm_campaign=Badge_Grade_Settings)

## Start

Either build the application own your own and run it…

* `mvn clean package`
* `java -jar target/stackoverflow-0.0.1-SNAPSHOT.jar`

…or build a Docker image and start it…

* `mvn clean spring-boot:build-image`
* `mvn io.fabric8:docker-maven-plugin:start`
* `docker run --network=host -p 8080:8080 docker.io/library/stackoverflow:0.0.1-SNAPSHOT`
