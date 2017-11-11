package acceptance

import PetsTestApi
import org.junit.Before
import spock.lang.Specification

class GeneratedApiSpec extends Specification {
    def "can make a simple GET call"() {
        given:
            String endpoint = "http://localhost:8000"
//            PetsTestApi api = new PetsTestApi(endpoint)
            TestService service = new TestService(endpoint: endpoint)

        expect:
            1 == 1

    }
}
