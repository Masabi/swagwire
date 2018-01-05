package acceptance.tobegenerated;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.client.EnumTypeAdapterFactory;
import io.swagger.client.StringUtil;
import io.swagger.client.model.RemotelyMockedPet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static io.swagger.client.ApiUtils.selectHeaderAccept;

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

    public RemoteOperation<Void> addPet(RemotelyMockedPet body) {
        Object localVarPostBody = body;

        // create path and map variables


        String localVarPath = "/pet";

        final String[] localVarAccepts = {
                "application/json", "application/xml"
        };
/*
        final String localVarAccept = selectHeaderAccept(localVarAccepts);

        // query params
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();




            final String[] localVarContentTypes = {
        "application/json", "application/xml"
            };
            final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

            String[] localVarAuthNames = new String[] { "petstore_auth" };

            apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
        */

        // TODO this only works for Json right now - to do something else we need
        // to handle the serialization further down

        // These are inverted from the normal generation meaning as when programming
        // wiremock we're doing the inverse
        final String contentType = selectHeaderAccept(localVarAccepts);

        /**
         * This is probably going to be different depending on the operation being done -
         * maybe use the HTTP verb to figure it out
         */

        // TODO Gson is putting the expected request enum as uppercase - the actual code is sending lowercase
        // regardless of which is correct - can we handle it in a generic enough way?

        // When instantiating the api class here we could have the ability to provide functionality to
        // do the actual json serialization
        // above means we actual want a builder with a default strategy.
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create();
        MappingBuilder mappingBuilder = WireMock
            .request("POST", WireMock.urlEqualTo(localVarPath))
            .withRequestBody(WireMock.equalToJson(gson.toJson(body), false, false));

        return new WireMockedRemoteOperation<>(mappingBuilder, contentType);
    }

}
