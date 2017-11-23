package acceptance

import acceptance.tobegenerated.RemotelyMockedPetApi
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.swagger.client.ApiClient
import io.swagger.client.api.PetApi
//import io.swagger.client.api.RemotelyMockedPetApi
import io.swagger.client.model.Pet
import io.swagger.client.model.RemotelyMockedCategory
import io.swagger.client.model.RemotelyMockedPet
import org.junit.Rule
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

    // TODO this needs to verify the body - It's possible we need a whole set of tests for this
    def "can POST a new object"() {
        given:
            RemotelyMockedPet newPet = new RemotelyMockedPet()
                .id(634L)
                .status(RemotelyMockedPet.StatusEnum.AVAILABLE)

            // for more fine grained control it might be worth allowing the returns
            // RemoteOperation object to be poked in various ways? Allowing pass throughs
            // straight to WireMock would avoid lots of extra code

            remotePetApi.addPet(newPet).succeeds()

        expect:
            Pet newActualPet = new Pet()
                .id(635L)
                .status(Pet.StatusEnum.AVAILABLE)
            petApi.addPet(newActualPet)
    }
}
