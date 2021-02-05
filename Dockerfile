FROM java:8-jdk-alpine
RUN mkdir /app
RUN cd /app
COPY ./target/al-micro-service-1.0.0.jar /app/al-micro-service-1.0.0.jar
WORKDIR /app
CMD ["java","-jar", "al-micro-service-1.0.0.jar"]