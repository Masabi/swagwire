package acceptance

import acceptance.tobegenerated.RemotelyMockedPetApi
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.swagger.client.ApiClient
import io.swagger.client.api.PetApi
import io.swagger.client.model.Pet
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
            // The response should be an interface to keep the DSL clean
            remotePetApi.(1L).respondsWith(pet1)
            remotePetApi.getPetById(2L).respondsWith(pet2)

        expect:
            petApi.getPetById(1L).id == 1L
            petApi.getPetById(2L).id == 2L

    }
}
