FROM docker.nexus.yamoney.ru/openjdk:8-jre-alpine
COPY ./build/libs/reviewer.jar /root/reviewer.jar
WORKDIR /root
CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=4", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=4", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "reviewer.jar"]