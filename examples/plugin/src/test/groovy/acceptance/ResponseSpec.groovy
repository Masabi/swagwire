package acceptance

import acceptance.dsl.PetDsl
import acceptance.dsl.WireMockPerTest
import com.masabi.swagwire.core.RemoteOperation
import io.swagger.client.model.SwagWiredPet
import spock.lang.Specification

class ResponseSpec extends Specification implements WireMockPerTest, PetDsl {
    // Some kind of Pet Dsl
    SwagWiredPet pet = new SwagWiredPet()
        .name("Rover")
        .status(SwagWiredPet.StatusEnum.AVAILABLE)

    def "can respond with object"() {
        given:
            askingForPet().respondsWith(pet)

        expect:
            fetchingPet().name == pet.name
    }

    def fetchingPet() {
        return petApi.getPetById(1234)
    }

    protected RemoteOperation<SwagWiredPet> askingForPet() {
        remotePetApi.getPetById(1234)
    }
}
