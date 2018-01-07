package acceptance

import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.AnythingPattern
import groovy.transform.Canonical
import io.swagger.client.ApiClient
import io.swagger.client.api.PetApi
import io.swagger.client.api.RemotelyMockedPetApi
import io.swagger.client.model.Category
import io.swagger.client.model.Pet
import io.swagger.client.model.RemotelyMockedCategory
import io.swagger.client.model.RemotelyMockedPet
import io.swagger.client.model.RemotelyMockedTag
import io.swagger.client.model.Tag
import org.junit.Rule
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig


class PetStoreSpec extends Specification {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort().dynamicHttpsPort());

    private PetApi actualPetApi
    private RemotelyMockedPetApi mockedPetApi

    def setup() {
        String basePath = "http://localhost:${wireMockRule.port()}"
        actualPetApi = new PetApi(new ApiClient().setBasePath(basePath))
        mockedPetApi = new RemotelyMockedPetApi()
    }

    /** pet requires auth to work - this needs to be setup correctly when doing expected requests! **/

    def "can add a pet successfully"() {
        given:
            mockedPetApi.addPet(TestPets.ROVER.remotePet).succeeds()

        when:
            actualPetApi.addPet(TestPets.ROVER.actualPet)

        then:
            notThrown(Exception)
    }

    def "can fail to add a pet when instructed"() {
        given:
            mockedPetApi.addPet(RemotelyMockedPet.ANY).succeeds()

        when:
            actualPetApi.addPet(TestPets.SNUFFLES.actualPet)

        then:
            notThrown(Exception)
    }

}

public class TestPets {
    public static final ROVER = new TestPet(
            actualPet: new Pet()
                .id(1234)
                .name("Rover")
                .status(Pet.StatusEnum.SOLD)
                .category(new Category().id(1).name("Dog"))
                .addPhotoUrlsItem("http://photos.url/path/to/rover/1")
                .addPhotoUrlsItem("http://photos.url/path/to/rover/2")
                .addTagsItem(new Tag().id(10).name("Fluffy"))
                .addTagsItem(new Tag().id(11).name("Smelly")),
            remotePet: new RemotelyMockedPet()
                .id(1234)
                .name("Rover")
                .status(RemotelyMockedPet.StatusEnum.SOLD)
                .category(new RemotelyMockedCategory().id(1).name("Dog"))
                .addPhotoUrlsItem("http://photos.url/path/to/rover/1")
                .addPhotoUrlsItem("http://photos.url/path/to/rover/2")
                .addTagsItem(new RemotelyMockedTag().id(10).name("Fluffy"))
                .addTagsItem(new RemotelyMockedTag().id(11).name("Smelly"))
        )

    public static final SNUFFLES = new TestPet(
            actualPet: new Pet()
                    .id(999)
                    .name("Snuffles")
                    .status(Pet.StatusEnum.AVAILABLE)
                    .category(new Category().id(2).name("Cat"))
                    .addPhotoUrlsItem("http://photos.url/path/to/snuffles/1")
                    .addPhotoUrlsItem("http://photos.url/path/to/snuffles/2")
                    .addTagsItem(new Tag().id(10).name("Fluffy"))
                    .addTagsItem(new Tag().id(11).name("Evil")),
            remotePet: new RemotelyMockedPet()
                    .id(999)
                    .name("Rover")
                    .status(RemotelyMockedPet.StatusEnum.AVAILABLE)
                    .category(new RemotelyMockedCategory().id(2).name("Cat"))
                    .addPhotoUrlsItem("http://photos.url/path/to/snuffles/1")
                    .addPhotoUrlsItem("http://photos.url/path/to/snuffles/2")
                    .addTagsItem(new RemotelyMockedTag().id(10).name("Fluffy"))
                    .addTagsItem(new RemotelyMockedTag().id(11).name("Evil"))
    )

}

@Canonical
public class TestPet {
    Pet actualPet
    RemotelyMockedPet remotePet
}