package io.swipepay.clientdesk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swipepay.cryptoservicelib.client.CryptoServiceClient;
import io.swipepay.cryptoservicelib.client.CryptoServiceClientProperties;

@Configuration
public class ClientDeskConfig {
	
	@Bean
	@ConfigurationProperties("crypto-service-client")
	public CryptoServiceClientProperties cryptoServiceClientProperties() {
		return new CryptoServiceClientProperties();
	}
	
	@Bean
	public CryptoServiceClient cryptoServiceClient(CryptoServiceClientProperties cryptoServiceClientProperties) {
		return new CryptoServiceClient(cryptoServiceClientProperties);
	}
}