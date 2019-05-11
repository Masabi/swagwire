package acceptance.dsl

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

trait WireMockPerTest {
    @Lazy WireMockRule wireMockRule = {
        wireMockRule = new WireMockRule(wireMockConfig().dynamicPort().dynamicHttpsPort());
        wireMockRule.start()
        WireMock.configureFor(wireMockRule.port())
        return wireMockRule
    }()

    String wireMockUrl() {
        return "http://localhost:${wireMockRule.port()}"
    }
}
