package io.dataline.server.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dataline.config.SourceConnectionSpecification;
import io.dataline.config.persistence.ConfigPersistence;
import io.dataline.config.persistence.JsonSchemaValidation;
import io.dataline.config.persistence.PersistenceConfigType;
import java.util.UUID;

public class IntegrationSchemaValidation {
  private final ConfigPersistence configPersistence;

  private final ObjectMapper objectMapper;
  private final JsonSchemaValidation jsonSchemaValidation;

  public IntegrationSchemaValidation(ConfigPersistence configPersistence) {
    this.configPersistence = configPersistence;

    this.objectMapper = new ObjectMapper();
    jsonSchemaValidation = JsonSchemaValidation.getInstance();
  }

  public void validateSourceConnectionConfiguration(
      UUID sourceConnectionSpecificationId, Object configuration) {
    final SourceConnectionSpecification sourceConnectionSpecification =
        configPersistence.getConfig(
            PersistenceConfigType.SOURCE_CONNECTION_SPECIFICATION,
            sourceConnectionSpecificationId.toString(),
            SourceConnectionSpecification.class);

    final JsonNode schemaJson =
        objectMapper.valueToTree(sourceConnectionSpecification.getSpecification());
    final JsonNode configJson = objectMapper.valueToTree(configuration);

    jsonSchemaValidation.validateThrow(schemaJson, configJson);
  }
}
