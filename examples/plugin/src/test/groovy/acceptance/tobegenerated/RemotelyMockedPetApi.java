package acceptance.tobegenerated;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import io.swagger.client.model.RemotelyMockedPet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RemotelyMockedPetApi {
    public RemoteOperation<RemotelyMockedPet> getPetById(Long petId) {
        String localVarPath = "/pet/{petId}"
                .replaceAll("\\{" + "petId" + "\\}", escapeString(petId.toString()));

        MappingBuilder mappingBuilder = WireMock.get(WireMock.urlEqualTo(localVarPath));
        return new WireMockedRemoteOperation<>(mappingBuilder);
    }

    /**
     * TODO should be shared somewhere and can be static
     */
    public String escapeString(String str) {
        try {
            return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

}
