package com.waypointer.osmloader.mapping;

import org.junit.Test;
import org.openstreetmap.osmosis.core.domain.v0_6.CommonEntityData;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.waypointer.osmloader.TestingDataUtils.*;
import static org.assertj.core.api.Assertions.assertThat;


public class MappingSetTest {

    @Test(expected = IllegalStateException.class)
    public void shouldFailIfWasNotBuilt() throws Exception {
        MappingSet mapping = new MappingSet();
        mapping.addRule().withTag("key1", "value1").mapTo(NODE_ABANDONED);

        mapping.getMappingRulesForEntity(new Node(new CommonEntityData(0L, 0, new Date(), null, 0L), 0.0, 0.0));
    }

    @Test
    public void shouldDetectExactTag() throws Exception {
        MappingSet mapping = new MappingSet();
        mapping.addRule().withTag("key1", "value1").mapTo(NODE_ABANDONED);
        mapping.build();

        MappingRule mappingRule = mapping.getMappingRulesForEntity(createNodeWithTag("key1", "value1")).get(0);
        Collection<String> resultCode = mappingRule.getCategories();
        assertThat(resultCode).containsExactly(NODE_ABANDONED);
    }

    @Test
    public void shouldDetectGeneralTag() throws Exception {
        MappingSet mapping = new MappingSet();
        mapping.addRule().withTag("key1", null).mapTo(NODE_ABANDONED);
        mapping.build();

        MappingRule mappingRule = mapping.getMappingRulesForEntity(createNodeWithTag("key1", "value1")).get(0);
        Collection<String> resultCode = mappingRule.getCategories();
        assertThat(resultCode).containsExactly(NODE_ABANDONED);
    }

    @Test
    public void shouldDetectGeneralAndExactTag() throws Exception {
        MappingSet mapping = new MappingSet();
        mapping.addRule().withTag("key1", null).mapTo(NODE_ABANDONED);
        mapping.addRule().withTag("key1", "value2").mapTo(CAT_FINANCECOMM_BANK);
        mapping.build();

        List<MappingRule> ruleList1 = mapping.getMappingRulesForEntity(createNodeWithTag("key1", "value1"));
        assertThat(ruleList1).hasSize(1);
        MappingRule mappingRule = ruleList1.get(0);
        Collection<String> resultCode = mappingRule.getCategories();
        assertThat(resultCode).containsExactly(NODE_ABANDONED);

        List<MappingRule> ruleList = mapping.getMappingRulesForEntity(createNodeWithTag("key1", "value2"));
        assertThat(ruleList).hasSize(2);
    }

    @Test
    public void shouldDetectFewRules() throws Exception {
        MappingSet mapping = new MappingSet();
        mapping.addRule().withTag("key1", null).mapTo(NODE_ABANDONED);
        mapping.addRule().withTag("key2", "val2").mapTo(CAT_WATER_SPRING);
        mapping.addRule().withTag("key2", "val3").mapTo(CAT_WATER_SPRING);
        mapping.build();

        Entity nodeWithTag = createNodeWithTags("key1", "val1", "key2", "val2");
        Collection<MappingRule> mappedRules = mapping.getMappingRulesForEntity(nodeWithTag);
        assertThat(mappedRules).hasSize(2);
    }

}