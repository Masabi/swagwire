cd ../../ && \
./gradlew compileJava publishToMavenLocal && \
cd - && \
../../gradlew --stop && ../../gradlew  clean --stacktrace generateSwaggerCode
