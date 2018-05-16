![Build Status](https://codebuild.eu-west-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiSkp3amVvYkpweHB0TktiaGY0bURuRTVCbTgrZEJzY1V2Q1lOYXEwZjFPV2JUWkVkNDF0RGRYSU5VZkFkdVJNbnZ0MGxVK3Qzd0lGNjdCQmJ5YXdTR0R3PSIsIml2UGFyYW1ldGVyU3BlYyI6InpvZWR5RDJGdlF1VTJraVIiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master)

# Swagwire


## Introduction

Swagwire is a project that combines [Swagger](https://swagger.io/) and [Wiremock](http://wiremock.org/) to ease the burden when writing tests against APIs.

Wiremock is that de facto tool for writing test code that goes over the wire, but writing tests is not only cumbersome, it requires knowledge of how to interact with the API you're working with.  Not only does this open you up to error, but offers no guarantees that a passing test means your integration is correct.  

If we take the canonical petstore example, a traditional Wiremock based test might look something like this:

```
class PetstoreSpec extends Specification {
    @Rule
    WireMockRule wireMockRule = new WireMockRule(0)

    def setup() {
        WireMock.configureFor(wireMockRule.port())
    }

    def "can fetch a pet"() {
        given:
            stubFor(get(urlMatching("/pet/1234"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody("""{
                      "name": "Rover",
                      "photoUrls": [ "http://url1", "http://url2" ]      
                    }""")
            ))

        expect:
            fetchingPet("1234").name == "Rover"
    }
```

They key things here are the knowledge of the URL structure and the format of the pet body.  It's all too easy to make a mistake here and replicate it in production code, ending up with green tests that verify you made the same mistake twice!

Swagwire helps to overcome this by generated code stubs for the Wiremock bindings, in a similar way to how you might use it for production generation.  After running the generator, you can change the above test to the following:

```
    @Rule
    WireMockRule wireMockRule = new WireMockRule(0)

    def setup() {
        WireMock.configureFor(wireMockRule.port())
    }

    def "can fetch a pet"() {
        given:
            SwagWiredPetApi api = new SwagWiredPetApi()
            SwagWiredPet pet = new SwagWiredPet()
                .name("Rover")
                .photoUrls(["http://url1", "http://url2"])
            api.getPetById(1234L).respondsWith(pet)

        expect:
            fetchingPet("1234").name == "Rover"
    }
```

Note that here our code is focused around domain objects instead of HTTP requests.  Behind the scenes this has done exactly the same as the previous test, but with the safety that everything is done according to the swagger spec.

## Getting Started
Swagwire can be used the same way as any Swagger code generation library.  When setting up your codegen, ensure `swagwire-codegen` is on your classpath and specify the language as `swagwire`.  A Gradle example is present in the `examples` directory.
In addition, the `swagwire-core` jar is required to be on your test compile classpath.  The provides the necessary libs and dependencies required for operation. 

### Specify Requests
Requests are setup by calling the appropriate api method on your api class.  These map directly to the operations specified in your swagger spec.  All parameters will be automatically mapped to their correct location - path params, query params etc.  When a body is provided this can either be a generated domain object or a `ContentPattern<String>`.  The latter is provided for flexibility, for example only matching part of the body.

### Response Configuration
When setting up an expectation you have to inform Swagwire how you want it to be handled, e.g.

```
    api.getPetById(1234L).respondsWith(pet)
```

If the API doesn't have a response object, you can inform Swagger to return the correct code with no body:

```
    api.deletePet(1234L, "apiKey").succeeds()
```

All response operations are controlled via the `RemoteOperation<T>` object, where `T` is the type of response object, or `Void` where none is expected.  From here you can setup standard responses, or if you want more control you can provide a `ResponseDefinitionBuilder` instance.  For example, to simulate network problems:

```
    api.getPetById(1234L).respondsWith(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER))
```

Note that if you fail to setup any expectations, your test will fail with Wiremock complaining that no stubs have been setup.


## Downloading
Swagwire is available from Maven Central.

*Example Gradle*
```
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        compile("com.masabi.swagwire:swagwire-codegen:0.0.24")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testCompile("com.masabi.swagwire:swagwire-core:0.0.24")
}
```

## FAQs
*Do I have to be using Swagger codegen in production to use this?*

Not at all.  The test bindings are completely isolated from your production code

*Can I control JSON serialization/deserialization?*

Yes.  Your API constructor is overloaded to accept a `Gson` instance which will be used instead of the default one.

*Do I have to use Spock/Groovy?*

No, although you should because it's awesome.  The bindings generated are 100% pure Java.

*How is date/time handled?*

By default, the Java 8 date library type is used along with `GsonJava8TypeAdapterFactory()`.  If you wish to change this, provide the necessary config in your codegen build and provide a relevantly configured `Gson` instance
`
