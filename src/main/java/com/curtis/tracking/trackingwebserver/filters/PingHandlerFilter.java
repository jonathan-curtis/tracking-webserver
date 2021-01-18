package com.curtis.tracking.trackingwebserver.filters;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class PingHandlerFilter {

	private final Path pingPath;

	public PingHandlerFilter(@Value("${pingFileLocation:/tmp/ok}") String pingFileLocation) {
		this.pingPath = Paths.get(pingFileLocation);
	}

	public Mono<ServerResponse> filterPingRequest(ServerRequest serverRequest,
			HandlerFunction<ServerResponse> handlerFunction) {
		if (Files.notExists(pingPath)) {
			return ServerResponse.status(SERVICE_UNAVAILABLE).build();
		}
		return handlerFunction.handle(serverRequest);
	}

}
