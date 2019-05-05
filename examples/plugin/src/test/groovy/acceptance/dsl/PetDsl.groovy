package acceptance.dsl


import io.swagger.client.ApiClient
import io.swagger.client.api.PetApi
import io.swagger.client.api.SwagWiredPetApi
import org.junit.Before

trait PetDsl {
    PetApi petApi
    SwagWiredPetApi remotePetApi

    abstract String wireMockUrl()

    @Before
    void setupPetApi() {
        petApi = new PetApi(new ApiClient().setBasePath(wireMockUrl()))
        remotePetApi = new SwagWiredPetApi()
    }
}
