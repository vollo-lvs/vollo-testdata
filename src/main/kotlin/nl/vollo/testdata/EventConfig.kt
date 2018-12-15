package nl.vollo.testdata

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.connection.SingleConnectionFactory
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import javax.jms.Connection
import javax.jms.ConnectionFactory

@Configuration
@EnableJms
class EventConfig {

    @Bean
    fun jmsListenerContainerFactory(connectionFactory: ConnectionFactory,
                  configurer: DefaultJmsListenerContainerFactoryConfigurer
    ): JmsListenerContainerFactory<*> {
        return DefaultJmsListenerContainerFactory().apply {
            configurer.configure(this, connectionFactory)
            this.setPubSubDomain(true)
            this.setSubscriptionDurable(true)
            this.setClientId("vollo-testdata")
            this.setConcurrency("3-10")
        }
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter {
        return MappingJackson2MessageConverter().apply {
            this.setTargetType(MessageType.TEXT)
            this.setTypeIdPropertyName("_type")
        }
    }
}