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

    private String basePath
    private PetApi petApi

    def setup() {
        ApiClient client = new ApiClient()
        basePath = "http://localhost:${wireMockRule.port()}"
        client.setBasePath(basePath)
        petApi = new PetApi(client)
    }

    def "can make a simple GET call"() {
        given:
            String endpoint = basePath
//            PetsTestApi api = new PetsTestApi(endpoint)
            RemotelyMockedPetApi remotePetApi = new RemotelyMockedPetApi()
            RemotelyMockedPet remotePet = new RemotelyMockedPet()
            remotePet.id(1234L)

            // The response should be an interface to keep the DSL clean
            remotePetApi.getPetById(1234L).respondsWith(remotePet)
        
        when:
            Pet actualPet = petApi.getPetById(1234)

        then:
            actualPet.id == 1234L

    }
}
