FROM openjdk:8

WORKDIR /usrapp/bin

ENV PORT 4569

COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency

CMD ["java","-cp","./classes:./dependency/*","co.edu.escuelaing.log_service.LogService"]