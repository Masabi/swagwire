package acceptance

import acceptance.tobegenerated.RemotelyMockedPetApi
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.swagger.client.ApiClient
import io.swagger.client.api.PetApi
import io.swagger.client.model.Pet

import io.swagger.client.api.RemotelyMockedPetApi
import io.swagger.client.model.RemotelyMockedPet
import io.swagger.client.model.RemotelyMockedTag
import org.junit.Rule
import spock.lang.PendingFeature
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

class GeneratedApiSpec extends Specification {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort().dynamicHttpsPort());

    private PetApi petApi
    private RemotelyMockedPetApi remotePetApi = new RemotelyMockedPetApi()

    def setup() {
        String basePath = "http://localhost:${wireMockRule.port()}"
        petApi = new PetApi(new ApiClient().setBasePath(basePath))
    }

    def "can GET a single object"() {
        given:
            RemotelyMockedPet pet1 = new RemotelyMockedPet().id(1L)
            remotePetApi.getPetById(1L).respondsWith(pet1)

        expect:
            petApi.getPetById(1L).id == 1L
    }

    def "can DELETE a single object"() {
        given:
            remotePetApi.deletePet(1L, "api-key").succeeds()
        
        expect:
            petApi.deletePet(1L, "api-key")
    }


    public static void main(String[] args) {
        WireMock.stubFor(
            WireMock.request("POST", WireMock.urlMatching("/pet/1"))
                .willReturn(WireMock.aResponse().withBody('{"one":1}').withStatus(200)))
    }

    // TODO this needs to verify the body - It's possible we need a whole set of tests for this
    def "can POST a new object with minimal information"() {
        given:
            // the problem here is that the enum is being registered as uppercase whereas it should
            // be the case the library/spec expects it in - meaning we can't use raw Gson to do this
            RemotelyMockedPet newPet = new RemotelyMockedPet()
                .id(634L)
//                .status(RemotelyMockedPet.StatusEnum.AVAILABLE)

            remotePetApi.addPet(newPet).succeeds()

        expect:
            Pet newActualPet = new Pet()
                .id(634L)
//                .status(Pet.StatusEnum.AVAILABLE)
            petApi.addPet(newActualPet)
    }

    def "can POST a new object against a WireMock matcher"() {
        given:
            remotePetApi.addPet(WireMock.matchingJsonPath('$[?(@.id == 634)]')).succeeds()

        expect:
            Pet newActualPet = new Pet()
                .id(634L)
            petApi.addPet(newActualPet)
    }


    def "maintains enum casing when registering body matcher"() {
        given:
            // the problem here is that the enum is being registered as uppercase whereas it should
            // be the case the library/spec expects it in - meaning we can't use raw Gson to do this
            RemotelyMockedPet newPet = new RemotelyMockedPet()
                .id(635L)
                .status(RemotelyMockedPet.StatusEnum.AVAILABLE)

            remotePetApi.addPet(newPet).succeeds()

        expect:
            Pet newActualPet = new Pet()
                    .id(635L)
                .status(Pet.StatusEnum.AVAILABLE)
            petApi.addPet(newActualPet)
    }

    // date/time specified formats are well defined - they should be ISO - just stick to this
    // and use Java 8 bindings as that's what we're targetting
    // https://swagger.io/specification/#dataTypes
    def "what does okhttp do?"() {
    }

    // for more fine grained control it might be worth allowing the returns
    // RemoteOperation object to be poked in various ways? Allowing pass throughs
    // straight to WireMock would avoid lots of extra code
    @PendingFeature
    def "can send a partial object"() {
        // something like petApi.addPet(Partial.of(blahblah)) or something like that - perhaps a matcher or straight
        // into wiremock?
    }

}
