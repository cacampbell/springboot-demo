package app.service;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.stereotype.Service;

import app.domain.Category;

@Service
@JsonComponent
public class CategorySerializer extends JsonSerializer<Category> {
    @Override
    public void serialize(Category value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Category[] children = new Category[value.getChildren().size()];
        value.getChildren().toArray(children);
        int[] ids = new int[children.length];
        
        for (int i = 0; i < children.length; i++) {
            ids[i] = (int) (long) children[i].getId();
        }

        Arrays.sort(ids);
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeArrayFieldStart("children");
        gen.writeArray(ids, 0, ids.length);
        gen.writeEndObject();
    }
}