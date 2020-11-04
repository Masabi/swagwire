package acceptance.dsl

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.Slf4jNotifier
import com.github.tomakehurst.wiremock.junit.WireMockRule

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

trait WireMockPerTest {
    @Lazy WireMockRule wireMockRule = {
        wireMockRule = new WireMockRule(wireMockConfig().notifier(new Slf4jNotifier(true)).dynamicPort().dynamicHttpsPort())
        wireMockRule.start()
        WireMock.configureFor(wireMockRule.port())
        return wireMockRule
    }()

    WireMockServer wireMockServer() {
        return wireMockRule
    }
    
    String wireMockUrl() {
        return "http://localhost:${wireMockRule.port()}"
    }
}
