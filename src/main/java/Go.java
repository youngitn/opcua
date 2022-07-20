import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hurence.opc.ConnectionState;
import com.hurence.opc.OpcSession;
import com.hurence.opc.OpcTagInfo;
import com.hurence.opc.da.OpcDaConnectionProfile;
import com.hurence.opc.da.OpcDaOperations;

import com.hurence.opc.ua.OpcUaConnectionProfile;
import com.hurence.opc.ua.OpcUaOperations;
import com.hurence.opc.ua.OpcUaSessionProfile;
import com.hurence.opc.ua.OpcUaTemplate;

import io.reactivex.subscribers.TestSubscriber;

public class Go {
	private OpcDaOperations opcDaOperations;
	private OpcDaConnectionProfile connectionProfile;
	private final static Logger logger = LoggerFactory.getLogger(Go.class);

	private OpcUaConnectionProfile createProsysConnectionProfile() {
		return (new OpcUaConnectionProfile()
				.withConnectionUri(URI.create("opc.tcp://046374WIN10NB.topkey.com.tw:53530/OPCUA/SimulationServer"))
				.withClientIdUri("hurence:opc-simple:client:test").withClientName("Simple OPC test client")
				.withSocketTimeout(Duration.ofSeconds(5)));
	}

	private OpcUaConnectionProfile createConnectionProfile() {
		return (new OpcUaConnectionProfile()
				.withConnectionUri(URI.create("opc.tcp://localhost:53530/OPCUA/SimulationServer"))
				.withClientIdUri("UUU").withClientName("AAA").withSocketTimeout(Duration.ofSeconds(5)));
	}

	public static void main(String[] args) throws Exception {
		OpcUaConnectionProfile createConnectionProfile = new OpcUaConnectionProfile()
				.withConnectionUri(URI.create("opc.tcp://localhost:53530/OPCUA/SimulationServer"))
				.withClientIdUri("UUU").withClientName("AAA").withSocketTimeout(Duration.ofSeconds(5));

		try (OpcUaTemplate opcUaTemplate = new OpcUaTemplate()) {
			Collection<OpcTagInfo> ret = opcUaTemplate.connect(createConnectionProfile).toFlowable()
					.flatMap(client -> client.browseTags()).toList().blockingGet();

			// logger.info("{}", ret);
			// Optional<OpcTagInfo> sint = ret.stream().filter(t ->
			// "SinT".equals(t.getName())).findFirst();
			/* Optional<OpcTagInfo> sint = */
			// ret.stream().toList();
			ret.stream().filter(t -> "Counter".equals(t.getName())).map(OpcTagInfo::getProperties)
					.flatMap(Collection::stream).forEach(System.out::println);
			;
//			ret.stream().filter(t -> "Counter".equals(t.getName())).map(t -> "" + t.getId() + " " + t.getProperties())
//					.forEach(System.out::println);
//			Assert.assertTrue(sint.isPresent());
//			Assert.assertFalse(ret.isEmpty());

		}
	}
}
