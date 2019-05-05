package acceptance.dsl

import io.swagger.client.ApiClient
import io.swagger.client.api.StoreApi
import io.swagger.client.api.SwagWiredStoreApi
import org.junit.Before

trait StoreDsl {
    StoreApi storeApi
    SwagWiredStoreApi remoteStoreApi

    abstract String wireMockUrl()

    @Before
    void setupStoreApi() {
        storeApi = new StoreApi(new ApiClient().setBasePath(wireMockUrl()))
        remoteStoreApi = new SwagWiredStoreApi()
    }
}