package acceptance

import acceptance.dsl.PetApiDsl
import acceptance.dsl.PetsDsl
import acceptance.dsl.WireMockPerTest
import com.masabi.swagwire.core.RemoteOperation
import com.masabi.swagwire.core.RemoteOperationResponse
import com.masabi.swagwire.core.Success
import groovy.transform.CompileStatic
import io.swagger.client.model.SwagWiredPet
import spock.lang.Specification

import javax.ws.rs.ProcessingException

@CompileStatic
class ResponseSpec extends Specification implements WireMockPerTest, PetApiDsl, PetsDsl {
    def "can respond with object"() {
        given:
            askingForPet().respondsWith(rover)

        expect:
            fetchingPet().name == rover.name
    }

    def "can respond with fault"() {
        given:
            RemoteOperationResponse success = new Success(503)
            askingForPet().respondsWith(success)

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
