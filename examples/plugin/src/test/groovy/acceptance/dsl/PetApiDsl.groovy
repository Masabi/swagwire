package acceptance.dsl

import com.github.tomakehurst.wiremock.WireMockServer
import com.masabi.swagwire.core.SwagwiredServiceConfiguration
import io.swagger.client.ApiClient
import io.swagger.client.ServerConfiguration
import io.swagger.client.api.PetApi
import io.swagger.client.api.SwagWiredPetApi
import org.junit.Before

trait PetApiDsl {
    PetApi petApi
    SwagWiredPetApi remotePetApi

    abstract String wireMockUrl()
    abstract WireMockServer wireMockServer()

    @Before
    void setupPetApi() {
        String context = "/pet"
        SwagwiredServiceConfiguration config = SwagwiredServiceConfiguration.builder()
            .basePath(context)
            .wireMock(wireMockServer())
            .build()
        petApi = new PetApi(new ApiClient().setServers([new ServerConfiguration("${wireMockUrl()}/pet", "local wiremock instance", [:])]))
        remotePetApi = new SwagWiredPetApi(config)
    }
}
