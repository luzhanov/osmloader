package com.waypointer.osmloader.utils;

import com.waypointer.osmloader.TestingDataUtils;
import org.junit.Test;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


public class ConverterUtilsTest {

    @Test
    public void testAppendToStringBuilder() throws Exception {
        StringBuilder sb = new StringBuilder();
        ConverterUtils.appendToStringBuilder("Aaa", sb);
        assertThat(sb.toString()).isEqualTo("Aaa");

        ConverterUtils.appendToStringBuilder("Bbb", sb);
        assertThat(sb.toString()).isEqualTo("Aaa | Bbb");
    }

    @Test
    public void shouldConvertTagsToStorageString() throws Exception {
        Collection<Tag> multipleTags = TestingDataUtils.createTagCollection("tagA", "valA", "tagB", "valB");
        String multipleResult = ConverterUtils.tagsToExternalWaypointDataString(multipleTags);
        assertThat(multipleResult).isEqualTo("[tagA=valA][tagB=valB]");

        Collection<Tag> nullTags = TestingDataUtils.createTagCollection("tagC", null);
        String nullResult = ConverterUtils.tagsToExternalWaypointDataString(nullTags);
        assertThat(nullResult).isEqualTo("[tagC=null]");

        Collection<Tag> singleTags = TestingDataUtils.createTagCollection("tagC", "valC");
        String singleResult = ConverterUtils.tagsToExternalWaypointDataString(singleTags);
        assertThat(singleResult).isEqualTo("[tagC=valC]");
    }

    @Test
    public void shouldConvertStorageStringToTags() throws Exception {
        String storageString = "[tagA=valA][tagB=valB]";
        Collection<Tag> parsedTags = ConverterUtils.externalWaypointDataStringToTags(storageString);
        assertThat(parsedTags).isNotNull();
        assertThat(parsedTags).isNotEmpty();
        assertThat(parsedTags).hasSize(2);

        String secondConverted = ConverterUtils.tagsToExternalWaypointDataString(parsedTags);
        assertThat(storageString).isEqualTo(secondConverted);
    }

    @Test
    public void shouldConvertStorageStringToTagsSingle() throws Exception {
        String storageString = "[tagA=valA]";
        Collection<Tag> parsedTags = ConverterUtils.externalWaypointDataStringToTags(storageString);
        assertThat(parsedTags).isNotNull();
        assertThat(parsedTags).isNotEmpty();
        assertThat(parsedTags).hasSize(1);

        String secondConverted = ConverterUtils.tagsToExternalWaypointDataString(parsedTags);
        assertThat(storageString).isEqualTo(secondConverted);
    }


    @Test
    public void shouldConvertStorageStringToTagsNull() throws Exception {
        String storageString = "[tagA=valA][tagB=null]";
        Collection<Tag> parsedTags = ConverterUtils.externalWaypointDataStringToTags(storageString);
        assertThat(parsedTags).isNotNull();
        assertThat(parsedTags).isNotEmpty();
        assertThat(parsedTags).hasSize(2);

        String secondConverted = ConverterUtils.tagsToExternalWaypointDataString(parsedTags);
        assertThat(storageString).isEqualTo(secondConverted);
    }

}