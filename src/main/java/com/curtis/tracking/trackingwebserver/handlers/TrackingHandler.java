package com.curtis.tracking.trackingwebserver.handlers;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class TrackingHandler {

	private final byte[] pngPixel;

	public TrackingHandler(@Value("${pixelFilename:transparent-pixel.gif}") String pixelFilename) throws IOException {
		Resource resource = new ClassPathResource(pixelFilename);
        InputStream inputStream = resource.getInputStream();
		pngPixel = IOUtils.toByteArray(inputStream);
	}
	
	public Mono<ServerResponse> handleImg(ServerRequest serverRequest) {
		return ok()
		.contentType(MediaType.IMAGE_PNG)
		.body(fromValue(getPixel()));
	}
	
	private byte[] getPixel() {
		return pngPixel;
	}
}
