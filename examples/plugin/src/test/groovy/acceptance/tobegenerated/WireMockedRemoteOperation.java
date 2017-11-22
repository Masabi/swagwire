package acceptance.tobegenerated;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.google.gson.Gson;

public class WireMockedRemoteOperation<TYPE> implements RemoteOperation<TYPE> {
    private StubMapping stubMapping;
    private MappingBuilder mappingBuilder;

    public WireMockedRemoteOperation(MappingBuilder mappingBuilder) {
        this.mappingBuilder = mappingBuilder;
    }

    @Override
    public void respondsWith(TYPE object) {
        WireMock.stubFor(mappingBuilder.willReturn(
            WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(new Gson().toJson(object))
        ));
    }
}
