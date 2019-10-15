package com.redhat.developers.demo;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;
import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.kafka.KafkaMessage;

/**
 * StreamGreeter
 */
@ApplicationScoped
public class StreamGreeter {

    Logger logger = Logger.getLogger(StreamGreeter.class);

    private static final String JSON_MSG_FORMAT =
            "{ \"targetLangCode\":\"%s\",\"greeting\" : \"%s\"}";

    @ConfigProperty(name = "google.api.translate.targetLangCodes")
    List<String> targetLangCodes;

    @ConfigProperty(name = "stream.greeter.greeting")
    String greeting;

    @Outgoing("translated-greetings")
    public Flowable<KafkaMessage<String, String>> greetings() {
        return Flowable.interval(30, TimeUnit.SECONDS).map(tick -> {
            String targetLangCode = targetLangCodes.get(0);
            Collections.rotate(targetLangCodes, 1);
            String msgKey = String.valueOf("msg-" + System.currentTimeMillis());
            KafkaMessage<String, String> msg = KafkaMessage.of(msgKey, buildJsonString(targetLangCode,greeting));
            logger.debug("Sending message:["+msg.getPayload()+"]");
            return msg;
        });
    }

    private String buildJsonString(String targetLangCode, String message) {
        return String.format(JSON_MSG_FORMAT, targetLangCode, message);
    }

}
