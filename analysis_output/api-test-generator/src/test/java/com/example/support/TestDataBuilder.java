package com.example.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Fluent builder that creates JSON request bodies for the HelloWorld Greeting API.
 *
 * <p>Example usage:
 * <pre>
 *   String body = TestDataBuilder.greeting()
 *       .withName("Alice")
 *       .withTemplate("Hello, {name}!")
 *       .build();
 * </pre>
 */
public class TestDataBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Private constructor – use static factory methods
    private TestDataBuilder() {
    }

    // -------------------------------------------------------------------------
    // Factory methods
    // -------------------------------------------------------------------------

    /** Returns a new {@link GreetingBuilder}. */
    public static GreetingBuilder greeting() {
        return new GreetingBuilder();
    }

    /** Returns a new {@link UpdateBuilder}. */
    public static UpdateBuilder update() {
        return new UpdateBuilder();
    }

    /** Returns a new {@link BatchUpdateBuilder}. */
    public static BatchUpdateBuilder batchUpdate() {
        return new BatchUpdateBuilder();
    }

    // =========================================================================
    // Inner builders
    // =========================================================================

    /**
     * Builder for a create-greeting request body.
     */
    public static class GreetingBuilder {

        private String name;
        private String template;

        public GreetingBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public GreetingBuilder withTemplate(String template) {
            this.template = template;
            return this;
        }

        /**
         * Serializes the accumulated fields to a JSON string.
         *
         * @return JSON string
         * @throws RuntimeException if serialisation fails
         */
        public String build() {
            try {
                ObjectNode node = MAPPER.createObjectNode();
                if (name != null) {
                    node.put("name", name);
                }
                if (template != null) {
                    node.put("template", template);
                } else {
                    node.put("template", "Hello, {name}!");
                }
                return MAPPER.writeValueAsString(node);
            } catch (Exception e) {
                throw new RuntimeException("Failed to build greeting request body", e);
            }
        }
    }

    /**
     * Builder for an update-greeting request body.
     */
    public static class UpdateBuilder {

        private String name;
        private String template;

        public UpdateBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public UpdateBuilder withTemplate(String template) {
            this.template = template;
            return this;
        }

        public String build() {
            try {
                ObjectNode node = MAPPER.createObjectNode();
                if (name != null) {
                    node.put("name", name);
                }
                if (template != null) {
                    node.put("template", template);
                }
                return MAPPER.writeValueAsString(node);
            } catch (Exception e) {
                throw new RuntimeException("Failed to build update request body", e);
            }
        }
    }

    /**
     * Builder for a batch-update request body.
     */
    public static class BatchUpdateBuilder {

        private String[] ids;
        private String template;

        public BatchUpdateBuilder withIds(String... ids) {
            this.ids = ids;
            return this;
        }

        public BatchUpdateBuilder withTemplate(String template) {
            this.template = template;
            return this;
        }

        public String build() {
            try {
                ObjectNode node = MAPPER.createObjectNode();
                if (ids != null) {
                    com.fasterxml.jackson.databind.node.ArrayNode arr = MAPPER.createArrayNode();
                    for (String id : ids) {
                        arr.add(id);
                    }
                    node.set("ids", arr);
                }
                if (template != null) {
                    node.put("template", template);
                }
                return MAPPER.writeValueAsString(node);
            } catch (Exception e) {
                throw new RuntimeException("Failed to build batch-update request body", e);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Convenience factory methods for common test payloads
    // -------------------------------------------------------------------------

    /** Returns a valid create-greeting JSON body for the given name. */
    public static String createGreetingBody(String name) {
        return greeting().withName(name).build();
    }

    /** Returns a valid update JSON body for the given name and template. */
    public static String updateGreetingBody(String name, String template) {
        return update().withName(name).withTemplate(template).build();
    }

    /** Returns a minimal greeting JSON body using default template. */
    public static String defaultGreetingBody(String name) {
        return greeting()
                .withName(name)
                .withTemplate("Hello, {name}!")
                .build();
    }
}
