cd ../../ && \
./gradlew compileJava publishToMavenLocal && \
cd - && \
../../gradlew --stop && ../../gradlew --stacktrace
