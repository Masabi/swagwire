cd ../../ && \
./gradlew compileJava publishLocalTestPublicationToMavenLocal && \
cd - && \
./gradlew --stop && ./gradlew --stacktrace
