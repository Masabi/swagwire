![Build Status](https://codebuild.eu-west-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiSkp3amVvYkpweHB0TktiaGY0bURuRTVCbTgrZEJzY1V2Q1lOYXEwZjFPV2JUWkVkNDF0RGRYSU5VZkFkdVJNbnZ0MGxVK3Qzd0lGNjdCQmJ5YXdTR0R3PSIsIml2UGFyYW1ldGVyU3BlYyI6InpvZWR5RDJGdlF1VTJraVIiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master)

Latest version 1.0.7-openapi3

# Swagwire


## Introduction

Swagwire is a project that combines [OpenApi 3](https://swagger.io/) and [Wiremock](http://wiremock.org/) to ease the burden when writing tests against APIs.

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

Swagwire helps to overcome this by generating code stubs for the Wiremock bindings, in a similar way to how you might use it for production generation.  After running the generator, you can change the above test to the following:

```
    @Rule
    WireMockRule wireMockRule = new WireMockRule(0)

    def "can fetch a pet"() {
        given:
            SwagWiredServiceConfiguration configuration = SwagWiredServiceConfiguration.builder()
                .wireMock(wireMockRule)
                .build()
            SwagWiredPetApi api = new SwagWiredPetApi(configuration)
            SwagWiredPet pet = new SwagWiredPet()
                .name("Rover")
                .photoUrls(["http://url1", "http://url2"])
            api.getPetById(1234L).respondsWith(pet)

        expect:
            fetchingPet("1234").name == "Rover"
    }
```



Note that here our code is focused around domain objects instead of HTTP requests.  Behind the scenes this has done exactly the same as the previous test, but with the safety that everything is done according to the openapi spec.

As of version `0.0.27`, you can provide a `WireMockServer` instance to the configuration which allows for better test isolation when running as part of a complex build.  This can typically just be the `WireMockRule` you're using.

## Getting Started
Swagwire can be used the same way as any openapi code generation library.  When setting up your codegen, ensure `swagwire-codegen` is on your classpath and specify the language as `swagwire`.  A Gradle example is present in the `examples` directory.
In addition, the `swagwire-core` jar is required to be on your test compile classpath.  The provides the necessary libs and dependencies required for operation.

Swagwire will generate a Service API class for your service and corresponding data objects.  For example, for the Pet store example you end up with the following files:
```
api/
   PetApi.java
   StoreApi.java
   UserApi.java
model/
   Category.java
   Order.java
   Pet.java
   Tag.java
   User.java
```
(plus supporting files)

All operations are created in the service API class, which can can create like this:

```
        String context = "/pet"
        SwagwiredServiceConfiguration config = SwagwiredServiceConfiguration.builder()
            .basePath(context)
            .wireMock(wireMockServer)
            .build()
        petApi = new PetApi(new ApiClient().setBasePath("${wireMockUrl()}/pet"))
```

This creates a Swagwire API for the `Pet` set of operations mounted at the `/pet` context.

### Specify Requests
Requests are setup by calling the appropriate api method on your api class.  These map directly to the operations specified in your openapi spec.  All parameters will be automatically mapped to their correct location - path params, query params etc.  When a body is provided this can either be a generated domain object or a `ContentPattern<String>`.  The latter is provided for flexibility, for example only matching part of the body.

If you need to manipulate the request in a way Swagwire doesn't support directly, the `RemoteOperation` allows direct access to the current `MappingBuilder`.

#### Encoding Params
By default, Swagwire will not URL encode parameters that are part of the path.  This can be enabled through configuration with a default encoder being provided that uses the JDK build in `URLEncoder`.

Setting up using the default `URLEncoder`:
```
SwagWiredServiceConfiguration configuration = SwagWiredServiceConfiguration.builder()
    .pathEncoder(new InterpolatingPathEncoder(UrlEncoderBasedUrlValueEncoder.INSTANCE))
    .build()
```

If this doesn't satisfy your needs you can provide your own `PathEncoder`.  If you just want to provide a different mechanism for encoding the invidivual parts, you can use the `InterpolatingPathEncoder` and just provide your own implementation of `UrlValueEncoder`.

### Response Configuration
When setting up an expectation you have to inform Swagwire how you want it to be handled, e.g.

```
    api.getPetById(1234L).respondsWith(pet)
```

If the API doesn't have a response object, you can inform Swagwire to return the correct code with no body:

```
    api.deletePet(1234L, "apiKey").succeeds()
```

All response operations are controlled via the `RemoteOperation<T>` object, where `T` is the type of response object, or `Void` where none is expected.  From here you can setup standard responses, or if you want more control you can provide a `ResponseDefinitionBuilder` instance.  For example, to simulate network problems:

```
    api.getPetById(1234L).respondsWith(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER))
```

Note that if you fail to setup any expectations, your test will fail with Wiremock complaining that no stubs have been setup.

#### Multiple Responses

As of version `0.0.26` it is possible to setup multiple responses for a single request.  This can be done by providing multiple reponses at once, as used in the examples:
```
    def "can respond multiple times"() {
        given:
            askingForPet().respondsWith(
                Data.of(rover),
                Response.ofStatus(404)
            )

        expect:
            fetchingPet().name == rover.name

        when:
            fetchingPet()

        then:
            thrown(ApiException)
    }
```

Interally this uses Wiremock scenarios, but hides the tediousness of setting them up from you.

### Verification

If you want to use this to verify API calls happen after the fact, use the relevant operations on the `RemoteOperation` to validate what was called, like this:
```
    expect:
        askingForPet.wasCalled()
```

To setup calls in a lenient fashion, you can use wildcards for all input parameters, like this:
```
    given:
        remotePetApi.addPet(SwagwiredPet.ANY).succeeds()

    when:
        businessLogicIsExecuted()

    then:
        remotePetApi.addPet(SwagwiredPet.ANY).wasCalled()
```

For strings, the literal `".*"` can be used.

## Downloading
Swagwire is available from Maven Central.

*Example Gradle*
```
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        compile("com.masabi.swagwire:swagwire-codegen:1.0.6-openapi3")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testCompile("com.masabi.swagwire:swagwire-core:1.0.6-openapi3")
}
```

## FAQs
*Do I have to be using Swagger/openapi codegen in production to use this?*

Not at all.  The test bindings are completely isolated from your production code

*Can I control JSON serialization/deserialization?*

Yes.  Your API constructor is accepts a configuration object which can include your own `Gson` instance in.

*Do I have to use Spock/Groovy/Kotlin?*

No, although you should because it's awesome.  The bindings generated are 100% pure Java.  The `RemoteOperation` class is written in Kotlin but it is 100% Java compatible.

*How is date/time handled?*

By default, the Java 8 date library type is used along with `GsonJava8TypeAdapterFactory()`.  If you wish to change this, provide the necessary config in your codegen build and provide a relevantly configured `Gson` instance
`
