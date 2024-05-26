package com.warehousemanagement.pojo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrantedAuthorityDeserializer extends JsonDeserializer<List<GrantedAuthority>> {

	@Override
	public List<GrantedAuthority> deserialize(final JsonParser jsonParser, final DeserializationContext context)
			throws IOException {
		final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
		final List<GrantedAuthority> authorities = new ArrayList<>();
		if (jsonNode.isArray()) {
			for (final JsonNode node : jsonNode) {
				authorities.add(new SimpleGrantedAuthority(node.get("authority").asText()));
			}
		}
		return authorities;
	}
	
}
