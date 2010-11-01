#! /bin/bash
mvn sonar:sonar -e \
  -Dsonar.jdbc.url=jdbc:postgresql://$1/sonar \
  -Dsonar.jdbc.driver=org.postgresql.Driver \
  -Dsonar.jdbc.username=sonar \
  -Dsonar.jdbc.password=sonar \
  -Dsonar.host.url=http://$1:8080/sonar