FROM maven:3.8.2-jdk-11
VOLUME /tmp
WORKDIR /app
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run
