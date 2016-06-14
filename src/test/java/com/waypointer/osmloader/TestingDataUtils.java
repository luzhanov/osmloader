package com.waypointer.osmloader;

import com.waypointer.osmloader.mapping.MappingResult;
import com.waypointer.osmloader.mapping.MappingRule;
import com.waypointer.osmloader.mapping.MappingSet;
import com.waypointer.osmloader.utils.Coordinate;
import org.openstreetmap.osmosis.core.domain.v0_6.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Igor Luzhanov, 14/01/2015
 */
public class TestingDataUtils {

    public static final String CAT_WATER_SPRING = "cat.water.spring";
    public static final String CAT_WATER_RESERVOIR = "cat.water.res";
    public static final String NODE_ABANDONED = "node.abandoned";
    public static final String CAT_FINANCECOMM_BANK = "financecom.bank";

    public static MappingSet createSpringMappingSet() {
        MappingSet mapping = new MappingSet();
        mapping.addRule().withTag("natural", "spring").mapTo(CAT_WATER_SPRING).useNameTag("natural");
        mapping.build();
        return mapping;
    }

    public static MappingSet createSpringPondsMappingSet() {
        MappingSet mapping = new MappingSet();
        mapping.addRule().withTag("natural", "spring").mapTo(CAT_WATER_SPRING).useNameTag("natural");
        mapping.addRule().withTag("landuse", "reservoir").mapTo(CAT_WATER_RESERVOIR).useName("Water reservoir");
        mapping.build();
        return mapping;
    }

    public static MappingResult createNodeMappingResult() {
        MappingRule rule = MappingRule.createRule().withTag("natural", "spring").mapTo(CAT_WATER_SPRING).useNameTag("natural");

        Entity node = createNodeWithTags("natural", "spring");
        return new MappingResult(node, rule);
    }

    public static MappingResult createNodeMappingResult(String... tags) {
        return createNodeMappingResult(MappingRule.createRule(), tags);
    }

    public static MappingResult createNodeMappingResult(MappingRule rule, String... tags) {
        Entity node = createNodeWithTags(tags);
        return new MappingResult(node, rule);
    }

    public static MappingResult createNodeMappingResult(Collection<MappingRule> rules, String... tags) {
        Entity node = createNodeWithTags(tags);
        return new MappingResult(node, rules);
    }

    public static MappingResult createWayMappingResult() {
        MappingRule rule = MappingRule.createRule().withTag("natural", "spring").mapTo(CAT_WATER_SPRING).useNameTag("natural");
        CommonEntityData entityData = new CommonEntityData(1, 0, new Date(), null, 0);
        entityData.getTags().add(new Tag("natural", "spring"));

        Way node = new Way(entityData);
        node.getWayNodes().add(new WayNode(1));
        node.getWayNodes().add(new WayNode(2));
        node.getWayNodes().add(new WayNode(3));
        node.getWayNodes().add(new WayNode(4));

        return new MappingResult(node, rule);
    }

    public static void createCoordinatesMapForWay(MappingResult mappingResult) {
        if (!(mappingResult.getOrigin() instanceof MappingResult.WayOrigin)) {
            throw new IllegalArgumentException("Instance should be only Way");
        }
        MappingResult.WayOrigin way = (MappingResult.WayOrigin) mappingResult.getOrigin();

        int order = 0;
        int size = way.getWayNodes().size();
        for (WayNode wayNode : way.getWayNodes()) {
            way.getWayCoordinates().add(pointOnCircle(1, order * (360 / size), new Coordinate(3, 3)));
            order++;
        }
    }

    public static Coordinate pointOnCircle(float radius, float angleInDegrees, Coordinate origin) {
        // Convert from degrees to radians via multiplication by PI/180
        double xCoord = (radius * Math.cos(angleInDegrees * Math.PI / 180D)) + origin.getLatitude();
        double yCoord = (radius * Math.sin(angleInDegrees * Math.PI / 180D)) + origin.getLongitude();

        return new Coordinate(xCoord, yCoord);
    }

    public static Entity createNodeWithTag(String name, String value) {
        return createNodeWithTags(name, value);
    }

    public static Entity createNodeWithTags(String... tags) {
        CommonEntityData entityData = new CommonEntityData(1, 0, new Date(), null, 0);
        entityData.getTags().addAll(createTagCollection(tags));
        return new Node(entityData, 1.0, 2.0);
    }

    public static Collection<Tag> createTagCollection(String... tags) {
        if (tags.length % 2 != 0) {
            throw new IllegalArgumentException("You should set a key-value pairs for tags");
        }

        Collection<Tag> resultList = new ArrayList<>();
        for (int i = 0; i < tags.length; i = i + 2) {
            resultList.add(new Tag(tags[i], tags[i + 1]));
        }

        return resultList;
    }
}
