package com.curtis.tracking.trackingwebserver;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.curtis.tracking.trackingwebserver.filters.PingHandlerFilter;
import com.curtis.tracking.trackingwebserver.handlers.TrackingHandler;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class TrackingWebserverApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrackingWebserverApplication.class);

	private static final String IMG_ROUTE = "img";
	private static final String PING_ROUTE = "ping";

	private TrackingHandler trackingHandler;
	private PingHandlerFilter pingHandlerFilter;

	public TrackingWebserverApplication(final TrackingHandler trackingHandler,
			final PingHandlerFilter pingHandlerFilter) {
		this.trackingHandler = trackingHandler;
		this.pingHandlerFilter = pingHandlerFilter;
	}

	public static void main(String[] args) {
		SpringApplication.run(TrackingWebserverApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> getImgRoute() {
		return RouterFunctions.route().GET(IMG_ROUTE, trackingHandler::handleImg).after((req, resp) -> {
			LOGGER.info("{} {} {} {} {}", req.exchange().getLogPrefix(),
					req.remoteAddress().isPresent() ? req.remoteAddress().get().toString() : "null", req.method(),
					req.path(), resp.rawStatusCode());
			return resp;
		}).build();
	}

	@Bean
	public RouterFunction<ServerResponse> getPingRoute() {
		return RouterFunctions.route(GET(PING_ROUTE), request -> ok().body(Mono.just("OK"), String.class))
				.filter(pingHandlerFilter::filterPingRequest);
	}
}