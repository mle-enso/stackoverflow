package de.mle.stackoverflow;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class StackOverflowController {
	private static final String JSON_FILE = StackOverflowController.class.getResource("/jsonFile.json").getFile();

	@Autowired
	private Environment env;

	@GetMapping("/stack/activeProfiles")
	public List<String> getActiveProfiles() {
		return Arrays.asList(env.getActiveProfiles());
	}

	@GetMapping("/stack/jsonFile")
	public String getJsonFile() throws IOException {
		return FileUtils.readFileToString(new File(JSON_FILE), "UTF-8");
	}

	@GetMapping("/stack/link")
	public ResponseEntity<String> link(@RequestParam(required = false) String param) throws IOException {
		return ResponseEntity.ok(param);
	}

	@GetMapping("/stack/deserialize")
	public ResponseEntity<String> getProject() {
		String content = "{\"projectId\":9,\"workspaceId\":74,\"projectName\":\"Test Project 1dea0d3e-4bba-4cc2-ace3-77dede4990d5\",\"phases\":[],\"estimateType\":{\"name\":\"WEEK\",\"displayName\":\"Weeks\",\"id\":2}}";
		return ResponseEntity.ok(content);
	}

    @PostMapping(path = "/stack/contract", consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity postMessage(@RequestBody List<MessageContract> contract) {
        return ResponseEntity.ok(contract);
    }

    @GetMapping("/stack/fluxWithManualSubscribe")
    private void fluxWithManualSubscribe() {
        Flux
                .fromStream(Stream.generate(() -> new Random().nextInt()))
                .delayElements(Duration.ofMillis(500))
                .doOnNext(System.out::println)
                .subscribe();
    }

    @GetMapping(path = "/stack/fluxWithSpring", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<Integer> fluxWithSpring() {
        return Flux
				.fromStream(Stream.generate(() -> new Random().nextInt()))
                .delayElements(Duration.ofMillis(500))
                .doOnNext(System.out::println);
    }
}
