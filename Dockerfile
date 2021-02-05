FROM alpine as build

ARG MAVEN_VERSION=3.6.3
ARG USER_HOME_DIR="/root"
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries


# Install Java.
RUN apk --update --no-cache add openjdk8 curl

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
 && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
 && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
 && rm -f /tmp/apache-maven.tar.gz \
 && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

# Define working directory.

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/default-jvm/

RUN apk update
RUN apk add git


WORKDIR /data
RUN git clone https://github.com/oitejjho/al-micro-service.git
WORKDIR /data/al-micro-service
RUN mvn install

RUN mkdir /app
RUN cd /app
COPY /target/al-micro-service-1.0.0.jar /app/al-micro-service-1.0.0.jar
WORKDIR /app
CMD ["java","-jar", "al-micro-service-1.0.0.jar"]