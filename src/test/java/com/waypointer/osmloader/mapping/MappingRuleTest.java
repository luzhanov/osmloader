package com.waypointer.osmloader.mapping;

import org.junit.Test;

import static com.waypointer.osmloader.TestingDataUtils.CAT_WATER_SPRING;
import static com.waypointer.osmloader.TestingDataUtils.createTagCollection;
import static org.assertj.core.api.Assertions.assertThat;

public class MappingRuleTest {

    @Test
    public void shouldBeInvalidWithoutRulesAndCategories() {
        MappingRule mappingRule = MappingRule.createRule();
        assertThat(mappingRule.isValid()).isFalse();
    }

    @Test
    public void shouldBeInvalidWithoutRules() {
        MappingRule mappingRule = MappingRule.createRule();
        mappingRule.mapTo(CAT_WATER_SPRING);
        assertThat(mappingRule.isValid()).isFalse();
    }

    @Test
    public void shouldBeInvalidWithoutCategories() {
        MappingRule mappingRule = MappingRule.createRule();
        mappingRule.withTag("aaa");
        assertThat(mappingRule.isValid()).isFalse();
    }

    @Test
    public void shouldBeValidWithRulesAndCategories() {
        MappingRule mappingRule = MappingRule.createRule();
        mappingRule.mapTo(CAT_WATER_SPRING);
        mappingRule.withTag("aaa");
        assertThat(mappingRule.isValid()).isTrue();
    }

    @Test
    public void shouldMatchOnExactTag() {
        MappingRule mappingRule = MappingRule.createRule().withTag("tag", "value");

        boolean checkResult = mappingRule.checkForTags(createTagCollection("tag", "value", "name", "val"));
        assertThat(checkResult).isTrue();
    }

    @Test
    public void shouldMatchOnNullValueTag() {
        MappingRule mappingRule = MappingRule.createRule().withTag("tag");

        boolean checkResult = mappingRule.checkForTags(createTagCollection("tag", "value", "name", "val"));
        assertThat(checkResult).isTrue();
    }

    @Test
    public void shouldNotMatchWithExceptTag() {
        MappingRule mappingRule = MappingRule.createRule().withTag("tag", "value").exceptTag("name", "val");

        boolean checkResult = mappingRule.checkForTags(createTagCollection("tag", "value", "name", "val"));
        assertThat(checkResult).isFalse();
    }

    @Test
    public void shouldNotMatchWithExceptNullValueTag() {
        MappingRule mappingRule = MappingRule.createRule().withTag("tag", "value").exceptTag("name");

        boolean checkResult = mappingRule.checkForTags(createTagCollection("tag", "value", "name", "val"));
        assertThat(checkResult).isFalse();
    }

    @Test
    public void shouldMatchOnMultipleRule() {
        MappingRule mappingRule = MappingRule.createRule().withTag("tag", "value").withTag("tag2", "value2");

        boolean checkResult = mappingRule.checkForTags(createTagCollection("tag", "value", "name", "val", "tag2", "value2"));
        assertThat(checkResult).isTrue();
    }

    @Test
    public void shouldNotMatchOnMultipleRule() {
        MappingRule mappingRule = MappingRule.createRule().withTag("tag", "value").withTag("tag2", "value2");

        boolean checkResult = mappingRule.checkForTags(createTagCollection("tag", "value", "name", "val"));
        assertThat(checkResult).isFalse();
    }

}