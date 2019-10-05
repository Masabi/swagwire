package acceptance.dsl

import com.github.tomakehurst.wiremock.WireMockServer
import com.masabi.swagwire.core.SwagwiredServiceConfiguration
import io.swagger.client.ApiClient
import io.swagger.client.api.StoreApi
import io.swagger.client.api.SwagWiredStoreApi
import org.junit.Before

trait StoreApiDsl {
    StoreApi storeApi
    SwagWiredStoreApi remoteStoreApi

    abstract String wireMockUrl()
    abstract WireMockServer wireMockServer()

    @Before
    void setupStoreApi() {
        storeApi = new StoreApi(new ApiClient().setBasePath(wireMockUrl()))
        SwagwiredServiceConfiguration config = SwagwiredServiceConfiguration.builder()
            .wireMock(wireMockServer())
            .build()

        remoteStoreApi = new SwagWiredStoreApi(config)
    }
}