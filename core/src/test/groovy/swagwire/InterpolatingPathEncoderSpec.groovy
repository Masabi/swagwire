package swagwire

import com.masabi.swagwire.core.InterpolatingPathEncoder
import com.masabi.swagwire.core.NoEncodingEncoder
import com.masabi.swagwire.core.UrlValueEncoder
import spock.lang.Specification

class InterpolatingPathEncoderSpec extends Specification {
    String path = ""
    Map<String, String> pathParams = [:]
    UrlValueEncoder encoder = NoEncodingEncoder.INSTANCE

    def "replaces placeholder with value in path params"() {
        given:
            pathOf("root/{id}")
            pathParamsOf([id: "some_id"])

        expect:
            builtPath() == "root/some_id"
    }

    def "replaces placeholder appearing multiple times with value in path params"() {
        given:
            pathOf("root/{id}/path/{id}")
            pathParamsOf([id: "some_id"])

        expect:
            builtPath() == "root/some_id/path/some_id"
    }

    def "does not replace placeholder if not provided in param list"() {
        given:
            pathOf("root/{id}")

        expect:
            builtPath() == "root/{id}"
    }

    void pathOf(String path) {
        this.path = path
    }

    String builtPath() {
        return new InterpolatingPathEncoder(encoder).buildPath(path, pathParams)
    }

    void pathParamsOf(Map<String, String> pathParams) {
        this.pathParams = pathParams
    }
}
