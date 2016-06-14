package com.waypointer.osmloader.utils;

import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Igor Luzhanov, 11.01.2015
 */
public class ConverterUtils {

    public static String tagsToExternalWaypointDataString(Collection<Tag> tags) {
        StringBuilder sb = new StringBuilder();

        for (Tag tag : tags) {
            sb.append("[");
            sb.append(tag.getKey());
            sb.append("=");
            sb.append(tag.getValue());
            sb.append("]");
        }

        return sb.toString();
    }

    public static Collection<Tag> externalWaypointDataStringToTags(String storageString) {
        ArrayList<Tag> resultList = new ArrayList<>();

        String[] parts = storageString.split("(?=\\[)|(?<=\\])");

        for (String part : parts) {
            if (part != null && !part.trim().isEmpty()) {
                resultList.add(storageStringToTag(part));
            }
        }

        return resultList;
    }

    private static Tag storageStringToTag(String storageString) {
        storageString = storageString.replace("[", "");
        storageString = storageString.replace("]", "");

        String[] keyParts = storageString.split("=", 2);
        String name = keyParts[0].trim();
        String value = null;
        if (keyParts.length > 1) {
            value = "null".equalsIgnoreCase(keyParts[1].trim()) ? null : keyParts[1].trim();
        }

        return new Tag(name, value);
    }

    public static String tagsToString(Collection<Tag> tags) {
        StringBuilder sb = new StringBuilder();

        for (Tag tag : tags) {
            if (sb.length() > 0) {
                sb.append("|");
            }

            sb.append(tag.getKey());
            sb.append(":");
            sb.append(tag.getValue());
        }

        return sb.toString();
    }

    public static void appendToStringBuilder(String newString, StringBuilder sb) {
        if (newString != null && !newString.trim().isEmpty()) {
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            sb.append(newString);
        }
    }

    public static boolean isTagContains(String key, String value, Collection<Tag> tags) {
        for (Tag tag : tags) {
            if (tag.getKey().equalsIgnoreCase(key)) {
                if (value != null) {
                    if (value.equalsIgnoreCase(tag.getValue())) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getTagValue(String tagName, Collection<Tag> tags) {
        for (Tag tag : tags) {
            if (tag.getKey().equalsIgnoreCase(tagName)) {
                return tag.getValue();
            }
        }
        return null;
    }

    public static String getTagValuePrefixed(String tagNamePrefix, Collection<Tag> tags) {
        for (Tag tag : tags) {
            if (tag.getKey().startsWith(tagNamePrefix)) {
                return tag.getValue();
            }
        }
        return null;
    }

    public static String buildMapKey(Tag checkedTag) {
        return checkedTag.getKey() + ":" + checkedTag.getValue();
    }

    public static String buildMapKeyNullValue(Tag checkedTag) {
        return checkedTag.getKey() + ":null";
    }
}
