package com.curtis.tracking.trackingwebsever;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.curtis.tracking.trackingwebserver.TrackingWebserverApplication;
import com.curtis.tracking.trackingwebserver.filters.PingHandlerFilter;
import com.curtis.tracking.trackingwebserver.handlers.TrackingHandler;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TrackingWebserverApplication.class)
public class TrackingWebserverApplicationTests {
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
    @Test
    public void imgRouteShouldReturnOKAndPNG() throws IOException {
    	TrackingHandler trackingHandler = new TrackingHandler("transparent-pixel.gif");
    	TrackingWebserverApplication application = new TrackingWebserverApplication(trackingHandler, null);

		Resource resource = new ClassPathResource("transparent-pixel.gif");
        InputStream inputStream = resource.getInputStream();
		byte[] gif = IOUtils.toByteArray(inputStream);
    	
    	final WebTestClient client = WebTestClient
            .bindToRouterFunction(application.getImgRoute())
            .build();

        client.get()
            .uri("/img")
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.IMAGE_PNG)
            .expectBody(byte[].class)
            .isEqualTo(gif);
    }
	
    @Test
    public void pingRouteShouldReturnOKWhenFileIsPresent() throws IOException {
    	PingHandlerFilter pingHandlerFilter = new PingHandlerFilter(folder.newFile("ok").getAbsolutePath());
    	TrackingWebserverApplication application = new TrackingWebserverApplication(null, pingHandlerFilter);
    	
    	final WebTestClient client = WebTestClient
            .bindToRouterFunction(application.getPingRoute())
            .build();

        client.get()
            .uri("/ping")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .isEqualTo("OK");
    }
    
    @Test
    public void pingRouteShouldReturn503WhenFileIsNotPresent() throws IOException {    	
    	PingHandlerFilter pingHandlerFilter = new PingHandlerFilter(folder.getRoot().getAbsolutePath() + "/not_ok");
    	TrackingWebserverApplication application = new TrackingWebserverApplication(null, pingHandlerFilter);
    	
    	final WebTestClient client = WebTestClient
            .bindToRouterFunction(application.getPingRoute())
            .build();

        client.get()
            .uri("/ping")
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
