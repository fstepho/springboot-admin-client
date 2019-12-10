package fstepho.SpringBootClient;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
public class RestAPI {

	private static final String template = "Hello, %s!";

	private final MeterRegistry meterRegistry;
	private Map<String, Counter> countersUploads = new HashMap<String, Counter>();
	private Map<String, Counter> countersPages = new HashMap<String, Counter>();
	private Map<String, Counter> countersFormalites = new HashMap<String, Counter>();

	public RestAPI(MeterRegistry meterRegistry) {
		super();
		this.meterRegistry = meterRegistry;
	}

	private Counter initCounterUploads(String uuid) {
		if (countersUploads.get(uuid) == null) {
			countersUploads.put(uuid, Counter.builder("notaire.uploads").tag("Edude", uuid)
					.description("The number of uploads for client").register(meterRegistry));
		}
		return countersUploads.get(uuid);
	}

	private Counter initCounterPages(String uuid) {
		if (countersPages.get(uuid) == null) {
			countersPages.put(uuid, Counter.builder("notaire.pages").tag("Edude", uuid)
					.description("The number of pages for client").register(meterRegistry));
		}
		return countersPages.get(uuid);
	}

	private Counter initCounterFormalites(String uuid) {
		if (countersFormalites.get(uuid) == null) {
			countersFormalites.put(uuid, Counter.builder("notaire.formalites").tag("Edude", uuid)
					.description("The number of formalités for client").register(meterRegistry));
		}
		return countersFormalites.get(uuid);
	}

	@RequestMapping("/api1/{uuid}")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name,
			@PathVariable(name = "uuid") String uuid) {

		Counter counterUploads = initCounterUploads(uuid);
		counterUploads.increment(1); // increment the counters

		Counter countersPages = initCounterPages(uuid);
		countersPages.increment(4); // increment the counters

		Counter countersFormalites = initCounterFormalites(uuid);
		countersFormalites.increment(6); // increment the counters

		return new Greeting(countersPages.count(), String.format(template, name));
	}
}
