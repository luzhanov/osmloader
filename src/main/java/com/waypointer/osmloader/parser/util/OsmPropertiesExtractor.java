package com.waypointer.osmloader.parser.util;

import com.waypointer.osmloader.mapping.MappingResult;
import com.waypointer.osmloader.utils.ConverterUtils;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.waypointer.osmloader.utils.ConverterUtils.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author Igor Luzhanov, 14/01/2015
 */
public class OsmPropertiesExtractor {

    private static Logger logger = LoggerFactory.getLogger(OsmPropertiesExtractor.class);

    public static String extractDescriptionFromMapResult(MappingResult mappingResult) {
        Collection<Tag> tags = mappingResult.getTags();

        StringBuilder sb = new StringBuilder();

        appendToStringBuilder(getTagValue("description", tags), sb);

        if (sb.length() == 0) {
            appendToStringBuilder(getTagValuePrefixed("description", tags), sb);
        }

        String comment = getTagValue("comment", tags);
        appendToStringBuilder(comment, sb);

        //todo: add this to tests
        //get int name
        String intName = getTagValue("int_name", tags);
        if (isBlank(intName)) {
            intName = getTagValue("name:en", tags);
        }
        appendToStringBuilder(intName, sb);

        for (String descRule : mappingResult.getDescriptionRules()) {
            String tmpName = getTagValue(descRule, tags);
            if (!isBlank(tmpName)) {
                appendToStringBuilder(tmpName, sb);
            }
        }

        return sb.toString();
    }

    public static String extractNameFromMapResult(MappingResult mappingResult) {
        Collection<Tag> tags = mappingResult.getTags();

        String name = null;

        if (isBlank(name)) {
            name = getTagValue("int_name", tags);
        }

        if (isBlank(name)) {
            name = getTagValue("name", tags);
        }

        //try to get english name
        if (isBlank(name)) {
            name = getTagValue("name:en", tags);
        }

        //try to get any other language name
        if (isBlank(name)) {
            name = getTagValuePrefixed("name", tags);
        }

        //try to use description
        if (isBlank(name)) {
            name = getTagValue("description", tags);
        }

        if (isBlank(name)) {
            name = getTagValuePrefixed("description", tags);
        }

        if (isBlank(name)) {
            name = getTagValuePrefixed("note", tags);
        }

        if (isBlank(name)) {
            name = getTagValuePrefixed("wikipedia", tags);
        }

//        if (!isEmptyString(name) && tags.size() > 2) {
//            logger.debug("Use name [{}] for point with tags [{}]", name, ConverterUtils.tagsToString(tags));
//        }

        //use naming rule with tags
        if (isBlank(name)) {
            StringBuilder sb = new StringBuilder();
            for (String namingRule : mappingResult.getNamingRules()) {
                String tmpName = getTagValue(namingRule, tags);
                if (!isBlank(tmpName)) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(tmpName);
                }
            }
            name = sb.toString();
        }

        //use naming rule with direct name
        if (isBlank(name)) {
            name = mappingResult.getExactName();
        }

        if (isBlank(name) && tags.size() > 1) {
            logger.debug("No name extracted [{}] for point with tags [{}]", name, ConverterUtils.tagsToString(tags));
        }

        return name;
    }
}
