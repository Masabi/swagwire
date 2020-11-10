package swagwire

import com.masabi.swagwire.core.UrlEncoderBasedPathBuilder
import spock.lang.Specification

class ReplacingPathBuilderSpec extends Specification {
    String base = ""
    String path = ""
    Map<String, String> pathParams = [:]

    def "concats the base and path"() {
        given:
            baseOf("some_base")
            pathOf("some_path")

        expect:
            builtPath() == "some_base/some_path"
    }

    def "ignores base if not present"() {
        given:
            noBase()

        expect:
            builtPath() == path
    }

    def "ignores path if not present"() {
        given:
            noPath()

        expect:
            builtPath() == base
    }

    def "replaces placeholder with value in path params"() {
        given:
            baseOf("root/{id}")
            pathParamsOf([id: "some_id"])

        expect:
            builtPath() == "root/some_id"
    }

    def "replaces placeholder appearing multiple times with value in path params"() {
        given:
            baseOf("root/{id}")
            pathOf("path/{id}")
            pathParamsOf([id: "some_id"])

        expect:
            builtPath() == "root/some_id/path/some_id"
    }

    def "does not replace placeholder if not provided in param list"() {
        given:
            baseOf("root/{id}")

        expect:
            builtPath() == "root/{id}"
    }

    def "encodes path params according to application/x-www-form-urlencoded format"() {
        given:
            pathOf("/name/{name}")
            pathParamsOf([name: "John O'Grady"])

        expect:
            builtPath() == "/name/John%20O%27Grady"
    }

    void baseOf(String base) {
        this.base = base
    }

    void pathOf(String path) {
        this.path = path
    }

    String builtPath() {
        return new UrlEncoderBasedPathBuilder().buildPath(base, path, pathParams)
    }

    void noBase() {
        this.base = ""
    }

    void noPath() {
        this.path = ""
    }

    void pathParamsOf(Map<String, String> pathParams) {
        this.pathParams = pathParams
    }
}
