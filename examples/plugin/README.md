# Swagwire

## Gradle Example Project

This is an example project showing how to make use of Swagwire using Gradle.

In order the view the project run the following:

```
$ ../../gradlew generateSwaggerCode idea
```

This will generate code for both the production and swagwire test code into the `build/` directory.  This will also generate an IDEA project you can open directly to view the code.

Tests can be found under `src/test/groovy`.  The `GeneratedApiSpec` gives an overview of how to do basic API interactions.  The `PetStoreSpec` is a more detailed look at interacting with a single API.