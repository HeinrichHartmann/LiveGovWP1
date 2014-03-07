package eu.liveandgov.wp1.serialization.impl;

import eu.liveandgov.wp1.data.impl.Proximity;
import eu.liveandgov.wp1.serialization.SerializationCommons;
import eu.liveandgov.wp1.serialization.Wrapper;
import eu.liveandgov.wp1.util.LocalBuilder;

import java.util.Locale;
import java.util.Scanner;

/**
 * Created by Lukas Härtel on 08.02.14.
 */
public class ProximitySerialization extends AbstractSerialization<Proximity> {
    public static final ProximitySerialization PROXIMITY_SERIALIZATION = new ProximitySerialization();

    private ProximitySerialization() {
    }

    @Override
    protected void serializeRest(StringBuilder stringBuilder, Proximity proximity) {
        SerializationCommons.appendString(stringBuilder, proximity.key);
        stringBuilder.append(SerializationCommons.SLASH);
        stringBuilder.append(proximity.in);
        stringBuilder.append(SerializationCommons.SLASH);
        SerializationCommons.appendString(stringBuilder, proximity.of);

    }

    @Override
    protected Proximity deSerializeRest(String type, long timestamp, String device, Scanner scanner) {
        scanner.useDelimiter(SerializationCommons.SLASH_SEMICOLON_SEPARATED);

        final String key = SerializationCommons.nextString(scanner);
        final Boolean in = scanner.nextBoolean();
        final String of = SerializationCommons.nextString(scanner);

        return new Proximity(timestamp, device, key, in, of);
    }
}
