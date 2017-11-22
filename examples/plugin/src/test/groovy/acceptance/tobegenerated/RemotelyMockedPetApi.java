package acceptance.tobegenerated;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.swagger.client.StringUtil;
import io.swagger.client.model.RemotelyMockedPet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RemotelyMockedPetApi {
    public RemoteOperation<RemotelyMockedPet> getPetById(Long petId) {
        String localVarPath = "/pet/{petId}"
                .replaceAll("\\{" + "petId" + "\\}", escapeString(petId.toString()));

        final String[] localVarAccepts = {
                "application/json", "application/xml"
        };

        // TODO this only works for Json right now - to do something else we need
        // to handle the serialization further down

        // These are inverted from the normal generation meaning as when programming
        // wiremock we're doing the inverse
        final String contentType = selectHeaderAccept(localVarAccepts);

        MappingBuilder mappingBuilder = WireMock.get(WireMock.urlEqualTo(localVarPath));
        return new WireMockedRemoteOperation<>(mappingBuilder, contentType);
    }

    /**
     * TODO should be shared somewhere and can be static
     */
    public static String escapeString(String str) {
        try {
            return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    public static String selectHeaderAccept(String[] accepts) {
        if (accepts.length == 0) {
            return null;
        }
        for (String accept : accepts) {
            if (isJsonMime(accept)) {
                return accept;
            }
        }
        return StringUtil.join(accepts, ",");
    }

    public static boolean isJsonMime(String mime) {
        String jsonMime = "(?i)^(application/json|[^;/ \t]+/[^;/ \t]+[+]json)[ \t]*(;.*)?$";
        return mime != null && (mime.matches(jsonMime) || mime.equalsIgnoreCase("application/json-patch+json"));
    }

    public RemoteOperation<Void> deletePet(Long petId, String apiKey) {
        String localVarPath = "/pet/{petId}"
                .replaceAll("\\{" + "petId" + "\\}", escapeString(petId.toString()));

//        if (apiKey != null)
//            localVarHeaderParams.put("api_key", apiClient.parameterToString(apiKey));

        final String[] localVarAccepts = {
                "application/json", "application/xml"
        };
        final String contentType = selectHeaderAccept(localVarAccepts);

        MappingBuilder mappingBuilder = WireMock.request("DELETE", WireMock.urlEqualTo(localVarPath));
        mappingBuilder.withHeader("api_key", WireMock.equalTo(apiKey));
        return new WireMockedRemoteOperation<>(mappingBuilder, contentType);
    }
}
