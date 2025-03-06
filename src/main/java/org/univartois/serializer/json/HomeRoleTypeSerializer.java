package org.univartois.serializer.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.univartois.enums.HomeRoleType;

import java.io.IOException;

public class HomeRoleTypeSerializer extends StdSerializer<HomeRoleType> {

    public HomeRoleTypeSerializer() {
        super(HomeRoleTypeSerializer.class, true);
    }

    @Override
    public void serialize(HomeRoleType homeRoleType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString(homeRoleType.name());
        jsonGenerator.writeFieldName("value");
        jsonGenerator.writeString(homeRoleType.getValue());
        jsonGenerator.writeEndObject();
    }
}
