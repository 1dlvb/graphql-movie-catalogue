package com.dlvb.graphqlmoviecatalogue.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom serializer for Google Protocol Buffers (protobuf) {@link Message} objects.
 * @author Matushkin Anton
 */
@Component
public class ProtobufMessageSerializer extends JsonSerializer<Message> {

    /**
     * Serializes a protobuf {@link Message} into its JSON representation.
     *
     * @param message The protobuf message to serialize
     * @param gen The {@link JsonGenerator} used to write JSON output
     * @param serializers The {@link SerializerProvider} (not used here)
     * @throws IOException If an I/O error occurs during serialization
     */
    @Override
    public void serialize(Message message, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String jsonString = JsonFormat.printer().print(message);
        gen.writeRawValue(jsonString);
    }

}
