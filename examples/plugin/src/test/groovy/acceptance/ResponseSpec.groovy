package acceptance

import acceptance.dsl.PetApiDsl
import acceptance.dsl.PetsDsl
import acceptance.dsl.WireMockPerTest
import com.masabi.swagwire.core.Data
import com.masabi.swagwire.core.Fault
import com.masabi.swagwire.core.RemoteOperation
import com.masabi.swagwire.core.Response
import io.swagger.client.ApiException
import io.swagger.client.model.SwagWiredPet
import spock.lang.Specification

import javax.ws.rs.ProcessingException

import static com.github.tomakehurst.wiremock.http.Fault.MALFORMED_RESPONSE_CHUNK

class ResponseSpec extends Specification implements WireMockPerTest, PetApiDsl, PetsDsl {
    def "can respond with object"() {
        given:
            askingForPet().respondsWith(Data.of(rover))

        expect:
            fetchingPet().name == rover.name
    }

    def "can respond with server error"() {
        given:
            askingForPet().respondsWith(Response.ofStatus(503))

        when:
            fetchingPet()

        then:
            thrown(ApiException)
    }

    def "can respond with wiremock fault"() {
        given:
            askingForPet().respondsWith(Fault.of(MALFORMED_RESPONSE_CHUNK))

        when:
            fetchingPet()

        then:
            thrown(ProcessingException)
    }

    def "can respond multiple times"() {
        given:
            askingForPet().respondsWith(
                Data.of(rover),
                Response.ofStatus(404)
            )

        expect:
            fetchingPet().name == rover.name

        when:
            fetchingPet()

        then:
            thrown(ApiException)
    }


    def fetchingPet() {
        return petApi.getPetById(rover.id)
    }

    protected RemoteOperation<SwagWiredPet> askingForPet() {
        remotePetApi.getPetById(rover.id)
    }
}
