package com.dlvb.graphqlmoviecatalogue.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProtobufMessageSerializer extends JsonSerializer<Message> {

    @Override
    public void serialize(Message message, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String jsonString = JsonFormat.printer().print(message);
        gen.writeRawValue(jsonString);
    }

}
