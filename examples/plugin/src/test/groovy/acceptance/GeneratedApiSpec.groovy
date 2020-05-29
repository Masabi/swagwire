package acceptance

import acceptance.dsl.PetApiDsl
import acceptance.dsl.StoreApiDsl
import acceptance.dsl.WireMockPerTest
import com.github.tomakehurst.wiremock.client.WireMock
import io.swagger.client.model.Order
import io.swagger.client.model.Pet
import io.swagger.client.model.SwagWiredOrder
import io.swagger.client.model.SwagWiredPet
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZoneOffset

class GeneratedApiSpec extends Specification implements WireMockPerTest, PetApiDsl, StoreApiDsl {
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
                .name("fido")
                .photoUrls(["example.com"])

            remotePetApi.addPet(newPet).succeeds()

        expect:
            Pet newActualPet = new Pet()
                .name("fido")
                .photoUrls(["example.com"])
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
