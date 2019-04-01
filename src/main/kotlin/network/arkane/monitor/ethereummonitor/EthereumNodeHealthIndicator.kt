package network.arkane.monitor.ethereummonitor

import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status
import org.springframework.stereotype.Component
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthBlock
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class EthereumNodeHealthIndicator(private val web3j: Web3j) : AbstractReactiveHealthIndicator() {

    private val latestBlock: Optional<EthBlock>
        get() {
            try {
                return Optional.ofNullable(web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).flowable().blockingFirst())
            } catch (ex: Exception) {
                ex.printStackTrace()
                return Optional.empty()
            }

        }

    override fun doHealthCheck(builder: Health.Builder): Mono<Health> {
        try {
            val block = latestBlock
            return block
                    .map {
                        val date = Date(block.get().block.timestamp.toLong() * 1000)
                        val blockTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
                        if (blockTime.plus(10, ChronoUnit.MINUTES).isBefore(LocalDateTime.now(ZoneId.systemDefault()))) {
                            Mono.just(builder.status(Status.DOWN)
                                    .withDetail("ethereumnode", "last block is older than 30 minutes.(" + blockTime.toString() + ", block: " + block.get().block.number + ")")
                                    .build())
                        } else {
                            Mono.just(builder.up().withDetail("ethereumnode", "latest block is " + block.get().block.number).build())
                        }
                    }.orElse(
                            Mono.just(
                                    builder.down()
                                            .withDetail("ethereumnode.down", "Unable to fetch status for ethereum node").build())
                    )
        } catch (ex: Exception) {
            return Mono.just(
                    builder.down()
                            .withDetail("ethereumnode.exception", ex.message).build())
        }
    }
}