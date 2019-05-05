package acceptance

import acceptance.dsl.PetApiDsl
import acceptance.dsl.PetsDsl
import acceptance.dsl.WireMockPerTest
import com.masabi.swagwire.core.Fault
import com.masabi.swagwire.core.RemoteOperation
import com.masabi.swagwire.core.RemoteOperationResponse
import com.masabi.swagwire.core.Success
import io.swagger.client.model.SwagWiredPet
import spock.lang.Specification

import javax.ws.rs.ProcessingException

import static com.github.tomakehurst.wiremock.http.Fault.CONNECTION_RESET_BY_PEER
import static com.github.tomakehurst.wiremock.http.Fault.EMPTY_RESPONSE

class ResponseSpec extends Specification implements WireMockPerTest, PetApiDsl, PetsDsl {
    def "can respond with object"() {
        given:
            askingForPet().respondsWith(rover)

        expect:
            fetchingPet().name == rover.name
    }

    def "can respond with fault"() {
        given:
            askingForPet().respondsWith(
                new Success(503)
            )

        when:
            fetchingPet()

        then:
            thrown(ProcessingException)
    }
    
    def fetchingPet() {
        return petApi.getPetById(rover.id)
    }

    protected RemoteOperation<SwagWiredPet> askingForPet() {
        remotePetApi.getPetById(rover.id)
    }
}
