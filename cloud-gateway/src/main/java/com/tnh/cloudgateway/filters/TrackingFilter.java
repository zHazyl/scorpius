package com.tnh.cloudgateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//Global filters implements the GlobalFilter interface and must override the filter() method
@Order(1)
@Component
public class TrackingFilter implements GlobalFilter {
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    //Commonly used functions across your filters are encapsulated in the FilterUtils class
    @Autowired
    FilterUtils filterUtils;

    //Code that executes every time a request passes through the filter
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //Extracts the HTTP header from the request using the ServerWebExchange
        // object passed by parameters to the filter() method
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();

        if (isCorrelationIdPresent(requestHeaders)) {
            logger.debug("tmx-correlation-id found in tracking filter: {}", filterUtils.getCorrelationId(requestHeaders));
        } else {
            String correlationID = generateCorrelationId();
            exchange = filterUtils.setCorrelationId(exchange, correlationID);
            logger.debug("tmx-correlation-id generated in tracking filter: {}.", correlationID);
        }
        return chain.filter(exchange);

    }
    // A helper method that checks if there's a correlation ID in the request header
    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        if (filterUtils.getCorrelationId(requestHeaders) != null) {
            return true;
        }
        return false;
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

}
