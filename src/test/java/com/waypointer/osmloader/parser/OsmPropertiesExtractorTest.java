package com.waypointer.osmloader.parser;

import com.waypointer.osmloader.TestingDataUtils;
import com.waypointer.osmloader.mapping.MappingResult;
import com.waypointer.osmloader.mapping.MappingRule;
import com.waypointer.osmloader.parser.util.OsmPropertiesExtractor;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class OsmPropertiesExtractorTest {

    @Test
    public void shouldExtractNameTagDirectly() throws Exception {
        MappingResult result = TestingDataUtils.createNodeMappingResult("name", "Aaa");

        String name = OsmPropertiesExtractor.extractNameFromMapResult(result);
        assertThat(name).isEqualTo("Aaa");
    }

    @Test
    public void shouldExtractNameIntName() throws Exception {
        MappingResult result = TestingDataUtils.createNodeMappingResult("int_name", "Aaa");

        String name = OsmPropertiesExtractor.extractNameFromMapResult(result);
        assertThat(name).isEqualTo("Aaa");
    }

    @Test
    public void shouldExtractNameEn() throws Exception {
        MappingResult result = TestingDataUtils.createNodeMappingResult("name:en", "Aaa");

        String name = OsmPropertiesExtractor.extractNameFromMapResult(result);
        assertThat(name).isEqualTo("Aaa");
    }

    @Test
    public void shouldExtractNameLang() throws Exception {
        MappingResult result = TestingDataUtils.createNodeMappingResult("name:fr", "Aaa");

        String name = OsmPropertiesExtractor.extractNameFromMapResult(result);
        assertThat(name).isEqualTo("Aaa");
    }


    @Test
    public void shouldExtractNameDescription() throws Exception {
        MappingResult result = TestingDataUtils.createNodeMappingResult("description", "Aaa");

        String name = OsmPropertiesExtractor.extractNameFromMapResult(result);
        assertThat(name).isEqualTo("Aaa");
    }

    @Test
    public void shouldExtractNameDescriptionLng() throws Exception {
        MappingResult result = TestingDataUtils.createNodeMappingResult("description:fr", "Aaa");

        String name = OsmPropertiesExtractor.extractNameFromMapResult(result);
        assertThat(name).isEqualTo("Aaa");
    }

    @Test
    public void shouldExtractNameUsingRuleTag() throws Exception {
        MappingRule rule = MappingRule.createRule().useNameTag("tagx");
        MappingResult result = TestingDataUtils.createNodeMappingResult(rule, "tagx", "Aaa");

        String name = OsmPropertiesExtractor.extractNameFromMapResult(result);
        assertThat(name).isEqualTo("Aaa");
    }

    @Test
    public void shouldExtractNameUsingRuleNaming() throws Exception {
        MappingRule rule = MappingRule.createRule().useName("Zzz");
        MappingResult result = TestingDataUtils.createNodeMappingResult(rule, "tagx", "Aaa");

        String name = OsmPropertiesExtractor.extractNameFromMapResult(result);
        assertThat(name).isEqualTo("Zzz");
    }

    @Test
    public void shouldJoinNamesUsingRuleNaming() throws Exception {
        MappingRule rule1 = MappingRule.createRule().useNameTag("tag1");
        MappingRule rule2 = MappingRule.createRule().useNameTag("tag2");
        MappingResult result = TestingDataUtils.createNodeMappingResult(Arrays.asList(rule1, rule2), "tag1", "Aaa", "tag2", "Bbb");

        String name = OsmPropertiesExtractor.extractNameFromMapResult(result);
        assertThat(name).isEqualTo("Aaa, Bbb");
    }

    @Test
    public void shouldExtractDescriptionDirectly() throws Exception {
        MappingResult result = TestingDataUtils.createNodeMappingResult("description", "Desc");

        String name = OsmPropertiesExtractor.extractDescriptionFromMapResult(result);
        assertThat(name).isEqualTo("Desc");
    }

    @Test
    public void shouldExtractDescriptionLang() throws Exception {
        MappingResult result = TestingDataUtils.createNodeMappingResult("description:fr", "Desc");

        String name = OsmPropertiesExtractor.extractDescriptionFromMapResult(result);
        assertThat(name).isEqualTo("Desc");
    }

    @Test
    public void shouldExtractDescriptionComment() throws Exception {
        MappingResult result = TestingDataUtils.createNodeMappingResult("comment", "Desc");

        String name = OsmPropertiesExtractor.extractDescriptionFromMapResult(result);
        assertThat(name).isEqualTo("Desc");
    }

    @Test
    public void shouldExtractDescriptionFromDescAndComment() throws Exception {
        MappingResult result = TestingDataUtils.createNodeMappingResult("description", "Desc", "comment", "Comm");

        String name = OsmPropertiesExtractor.extractDescriptionFromMapResult(result);
        assertThat(name).isEqualTo("Desc | Comm");
    }

    @Test
    public void shouldExtractDescriptionFromDescRule() throws Exception {
        MappingRule rule = MappingRule.createRule().useDescrTag("tagY");
        MappingResult result = TestingDataUtils.createNodeMappingResult(rule, "tagY", "Bbb");

        String name = OsmPropertiesExtractor.extractDescriptionFromMapResult(result);
        assertThat(name).isEqualTo("Bbb");
    }

    @Test
    public void shouldExtractDescriptionFromDescRuleAndDescTag() throws Exception {
        MappingRule rule = MappingRule.createRule().useDescrTag("tagY");
        MappingResult result = TestingDataUtils.createNodeMappingResult(rule, "description", "Desc", "tagY", "Bbb");

        String name = OsmPropertiesExtractor.extractDescriptionFromMapResult(result);
        assertThat(name).isEqualTo("Desc | Bbb");
    }

    @Test
    public void shouldJoinDescriptionUsingRuleNaming() throws Exception {
        MappingRule rule1 = MappingRule.createRule().useDescrTag("tag1");
        MappingRule rule2 = MappingRule.createRule().useDescrTag("tag2");
        MappingResult result = TestingDataUtils.createNodeMappingResult(Arrays.asList(rule1, rule2), "tag1", "Aaa", "tag2", "Bbb");

        String name = OsmPropertiesExtractor.extractDescriptionFromMapResult(result);
        assertThat(name).isEqualTo("Aaa | Bbb");
    }

    @Test
    public void shouldJoinExactNames() throws Exception {
        MappingRule rule1 = MappingRule.createRule().useName("tag1");
        MappingRule rule2 = MappingRule.createRule().useName("tag2");
        MappingResult result = TestingDataUtils.createNodeMappingResult(Arrays.asList(rule1, rule2), "tagX", "Aaa", "tagY", "Bbb");

        String name = OsmPropertiesExtractor.extractNameFromMapResult(result);
        assertThat(name).isEqualTo("tag1 | tag2");
    }

}