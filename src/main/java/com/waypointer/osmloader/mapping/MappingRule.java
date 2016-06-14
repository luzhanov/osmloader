package com.waypointer.osmloader.mapping;

import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.waypointer.osmloader.utils.ConverterUtils.buildMapKey;
import static com.waypointer.osmloader.utils.ConverterUtils.buildMapKeyNullValue;
import static java.util.Arrays.asList;

/**
 * @author Igor Luzhanov, 09/01/2015
 */
public class MappingRule {

//    @InjectLogger
//    private Logger logger;

    private Set<String> mappingRules = new HashSet<>();
    private Set<String> exceptRules = new HashSet<>();
    private Set<String> categories = new HashSet<>();

    private Set<String> namingRules = new HashSet<>();
    private Set<String> descriptionRules = new HashSet<>();
    private String exactName;

    private MappingRule() {
    }

    public boolean checkForTags(Collection<Tag> tagsList) {
        boolean hasExceptRules = false;
        int fitRulesCnt = 0;

        for (Tag tag : tagsList) {
            for (String mappingRule : mappingRules) {
                if (buildMapKey(tag).equalsIgnoreCase(mappingRule) || buildMapKeyNullValue(tag).equalsIgnoreCase(mappingRule)) {
                    fitRulesCnt++;
                }
            }
            for (String exceptRule : exceptRules) {
                if (buildMapKey(tag).equalsIgnoreCase(exceptRule) || buildMapKeyNullValue(tag).equalsIgnoreCase(exceptRule)) {
                    hasExceptRules = true;
                }
            }
        }

        boolean fitAllRules = !mappingRules.isEmpty() && fitRulesCnt == mappingRules.size();

        boolean ruleApplied = fitAllRules && !hasExceptRules;
//        if (ruleApplied) {
//            logger.trace("Categories {} applied to tags [{}]", categories, ConverterUtils.tagsToString(tagsList));
//        }

        return ruleApplied;
    }

    public static MappingRule createRule() {
        return new MappingRule();
    }

    public MappingRule withTag(String tagKey) {
        mappingRules.add(tagKey + ":null");
        return this;
    }

    /**
     * Add positive rule - if all of 'with' tags are exists then the rule should trigger
     *
     * @param tagKey
     * @param tagValue
     * @return
     */
    public MappingRule withTag(String tagKey, String tagValue) {
        mappingRules.add(tagKey + ":" + tagValue);
        return this;
    }

    public MappingRule exceptTag(String tagKey) {
        exceptRules.add(tagKey + ":null");
        return this;
    }

    public MappingRule exceptTag(String tagKey, String tagValue) {
        exceptRules.add(tagKey + ":" + tagValue);
        return this;
    }

    public MappingRule useName(String name) {
        this.exactName = name;
        return this;
    }

    public MappingRule useNameTag(String tagKey) {
        namingRules.add(tagKey);
        return this;
    }

    public MappingRule useDescrTag(String tagKey) {
        descriptionRules.add(tagKey);
        return this;
    }

    public MappingRule mapTo(String... categoryCode) {
        categories.addAll(asList(categoryCode));
        return this;
    }

    public boolean isValid() {
        return !mappingRules.isEmpty() && !categories.isEmpty();
    }

    public Set<String> getCategories() {
        return categories;
    }

    public Set<String> getNamingRules() {
        return namingRules;
    }

    public Set<String> getDescriptionRules() {
        return descriptionRules;
    }

    public String getExactName() {
        return exactName;
    }

    @Override
    public String toString() {
        return "MappingRule{" +
                "categories=" + categories +
                ", mappingRules=" + mappingRules +
                '}';
    }
}
