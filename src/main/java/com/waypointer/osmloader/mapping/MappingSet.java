package com.waypointer.osmloader.mapping;

import org.openstreetmap.osmosis.core.domain.v0_6.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing mapping rules
 *
 * @author Igor Luzhanov, 08/01/2015
 */
public class MappingSet {

    private final List<MappingRule> mappingList = new ArrayList<>();

    private boolean wasBuilt = false;

    public MappingRule addRule() {
        MappingRule rule = MappingRule.createRule();
        mappingList.add(rule);
        return rule;
    }

    public List<MappingRule> getMappingRulesForEntity(Entity entity) {
        checkSetState();
        ArrayList<MappingRule> resultRules = new ArrayList<>();

        for (MappingRule mappingRule : mappingList) {
            if (mappingRule.checkForTags(entity.getTags())) {
                resultRules.add(mappingRule);
            }
        }
        return resultRules;
    }

    private void checkSetState() {
        if (!wasBuilt) {
            throw new IllegalStateException("Mapping set was not built");
        }
    }

    public void build() {
        for (MappingRule mappingRule : mappingList) {
            if (!mappingRule.isValid()) {
                throw new IllegalStateException("Invalid mapping rule=" + mappingRule);
            }

        }
        wasBuilt = true;
    }
}
