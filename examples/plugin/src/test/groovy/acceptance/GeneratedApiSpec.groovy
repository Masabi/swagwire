package acceptance

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.swagger.client.ApiClient
import io.swagger.client.api.PetApi
import io.swagger.client.api.StoreApi
import io.swagger.client.api.SwagWiredPetApi
import io.swagger.client.api.SwagWiredStoreApi
import io.swagger.client.model.Order
import io.swagger.client.model.Pet
import io.swagger.client.model.SwagWiredOrder
import io.swagger.client.model.SwagWiredPet
import org.junit.Rule
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZoneOffset

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

class GeneratedApiSpec extends Specification {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort().dynamicHttpsPort());

    private PetApi petApi
    private SwagWiredPetApi remotePetApi = new SwagWiredPetApi()
    private StoreApi storeApi
    private SwagWiredStoreApi remoteStoreApi = new SwagWiredStoreApi()

    def setup() {
        String basePath = "http://localhost:${wireMockRule.port()}"
        println basePath
        petApi = new PetApi(new ApiClient().setBasePath(basePath))
        storeApi = new StoreApi(new ApiClient().setBasePath(basePath))
    }

    def "can GET a single object"() {
        given:
            SwagWiredPet pet1 = new SwagWiredPet().id(1L)
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


    def "can POST a new object with minimal information"() {
        given:
            SwagWiredPet newPet = new SwagWiredPet()
                .id(634L)
                .status(SwagWiredPet.StatusEnum.AVAILABLE)

            remotePetApi.addPet(newPet).succeeds()

        expect:
            Pet newActualPet = new Pet()
                .id(634L)
                .status(Pet.StatusEnum.AVAILABLE)
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

    def "serializes/deserializes dates correctly"() {
        given:
            SwagWiredOrder newOrder = new SwagWiredOrder()
                .shipDate(OffsetDateTime.of(2018, 1, 6, 16, 28, 3, 7, ZoneOffset.ofHours(1)))

            SwagWiredOrder returnOrder = new SwagWiredOrder()
                .shipDate(OffsetDateTime.of(2018, 1, 6, 16, 28, 3, 7, ZoneOffset.ofHours(1)))
            remoteStoreApi.placeOrder(newOrder).respondsWith(returnOrder)

        expect:
            Order newActualOrder = new Order()
                .shipDate(OffsetDateTime.of(2018, 1, 6, 16, 28, 3, 7, ZoneOffset.ofHours(1)))

            def order = storeApi.placeOrder(newActualOrder)
            order.shipDate.toEpochSecond() == newActualOrder.shipDate.toEpochSecond()
    }
}
