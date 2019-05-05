package acceptance

import acceptance.dsl.PetApiDsl
import acceptance.dsl.PetsDsl
import acceptance.dsl.WireMockPerTest
import com.masabi.swagwire.core.RemoteOperation
import io.swagger.client.model.SwagWiredPet
import spock.lang.Specification

class ResponseSpec extends Specification implements WireMockPerTest, PetApiDsl, PetsDsl {
    def "can respond with object"() {
        given:
            askingForPet().respondsWith(rover)

        expect:
            fetchingPet().name == rover.name
    }
    
    def fetchingPet() {
        return petApi.getPetById(rover.id)
    }

    protected RemoteOperation<SwagWiredPet> askingForPet() {
        remotePetApi.getPetById(rover.id)
    }
}
