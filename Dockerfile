FROM bosenet/tool:mvn AS MAVEN_TOOL_CHAIN

COPY infra/config/settings.xml /root/.m2/settings.xml
WORKDIR /tmp
COPY pom.xml /tmp/

COPY src /tmp/src/
RUN mvn clean verify

FROM bosenet/tool:mvn
WORKDIR /app
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
