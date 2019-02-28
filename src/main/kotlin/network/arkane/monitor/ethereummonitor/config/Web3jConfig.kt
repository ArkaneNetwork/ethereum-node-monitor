package network.arkane.monitor.ethereummonitor.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

@Configuration
class Web3jConfig {

    @Bean(name = ["ethereumWeb3j"])
    @Primary
    fun ethereumWeb3j(@Value("\${network.arkane.ethereum.endpoint.url}") ethereumEndpoint: String): Web3j {
        return Web3j.build(HttpService(ethereumEndpoint))
    }
}