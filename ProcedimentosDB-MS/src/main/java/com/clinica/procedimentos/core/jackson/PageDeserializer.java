package com.clinica.procedimentos.core.jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.clinica.procedimentos.domain.dto.ProcedureDTOOutput;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@JsonComponent
public class PageDeserializer<T> extends StdDeserializer<Page<?>> {

	public PageDeserializer() {
		super(Page.class);
	}

	@Override
	public Page<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		ObjectMapper mapper = (ObjectMapper) p.getCodec();
		JsonNode node = mapper.readTree(p);

		List<?> content = new ArrayList<>();
		
		if (node.has("content")) {
			content = mapper.convertValue(node.get("content"), new TypeReference<List<T>>() {
			});
		}

		long totalElements = node.get("totalElements").asLong();
		int size = node.get("size").asInt();
		node.get("totalPage").asInt();
		node.get("number").asInt();

		List<?> typedContent = new ArrayList<>();
		for (JsonNode element : node.get("content")) {
			typedContent.add(mapper.treeToValue(element, ctxt.getTypeFactory().constructType(ProcedureDTOOutput.class)));
		}

		return new PageImpl<>(typedContent, Pageable.ofSize(size), totalElements);
	}

}
