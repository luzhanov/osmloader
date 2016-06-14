package com.waypointer.osmloader.mapping;

import com.waypointer.osmloader.utils.Coordinate;
import org.openstreetmap.osmosis.core.domain.v0_6.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.waypointer.osmloader.utils.ConverterUtils.appendToStringBuilder;
import static java.util.Collections.singletonList;

/**
 * @author Igor Luzhanov, 14/01/2015
 */
public class MappingResult {

    private static Logger logger = LoggerFactory.getLogger(MappingResult.class);

    private long objectId;

    private Collection<Tag> tags;

    private Origin origin;

    private final Collection<MappingRule> mappingRules = new ArrayList<>();

    public MappingResult(Entity entity, MappingRule mappingRule) {
        this(entity, mappingRule != null ? singletonList(mappingRule) : null);
    }

    public MappingResult(Entity entity, Collection<MappingRule> mappingRules) {
        this.objectId = entity.getId();
        this.tags = entity.getTags();
        if (entity instanceof Node) {
            this.origin = new NodeOrigin((Node) entity);
        } else if (entity instanceof Way) {
            this.origin = new WayOrigin((Way) entity);
        } else {
            throw new IllegalArgumentException("Unacceptable type: " + entity);
        }

        if (mappingRules != null) {
            this.mappingRules.addAll(mappingRules);
        }
    }

    public long getObjectId() {
        return objectId;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public Origin getOrigin() {
        return origin;
    }

    public Set<String> getCategoriesCodeStrings() {
        Set<String> stringCodes = new TreeSet<>();

        for (MappingRule mappingRule : mappingRules) {
            stringCodes.addAll(mappingRule.getCategories());
        }

        //todo: remove this from common part of library
        //limit categories to 3 - to avoid problems during big uploads
        if (stringCodes.size() >= 3) {
            logger.warn("Point with more than 3 categories []", mappingRules);
            List<String> list = new ArrayList<>(stringCodes);
            stringCodes = new TreeSet<>(list.subList(0, 3));
        }

        return stringCodes;
    }

    public Set<String> getDescriptionRules() {
        TreeSet<String> result = new TreeSet<>();

        for (MappingRule mappingRule : mappingRules) {
            result.addAll(mappingRule.getDescriptionRules());
        }

        return result;
    }

    public Set<String> getNamingRules() {
        TreeSet<String> result = new TreeSet<>();

        for (MappingRule mappingRule : mappingRules) {
            result.addAll(mappingRule.getNamingRules());
        }

        return result;
    }

    public String getExactName() {
        StringBuilder sb = new StringBuilder();

        for (MappingRule mappingRule : mappingRules) {
            appendToStringBuilder(mappingRule.getExactName(), sb);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "MappingResult{" +
                "objectId=" + objectId +
                ", tags=" + tags +
                ", origin=" + origin +
                ", mappingRules=" + mappingRules +
                '}';
    }

    public static abstract class Origin {

    }

    public static class WayOrigin extends Origin {
        private final List<WayNode> wayNodes;
        private final List<Coordinate> wayCoordinates = new LinkedList<>();

        public WayOrigin(Way way) {
            wayNodes = way.getWayNodes();
        }

        public List<WayNode> getWayNodes() {
            return wayNodes;
        }

        public List<Coordinate> getWayCoordinates() {
            return wayCoordinates;
        }
    }

    public static class NodeOrigin extends Origin {
        private final double latitude;
        private final double longitude;

        public NodeOrigin(Node node) {
            this.latitude = node.getLatitude();
            this.longitude = node.getLongitude();
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MappingResult)) return false;

        MappingResult that = (MappingResult) obj;

        if (objectId != that.objectId) return false;
        if (mappingRules != null ? !mappingRules.equals(that.mappingRules) : that.mappingRules != null) return false;
        if (origin != null ? !origin.equals(that.origin) : that.origin != null) return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (objectId ^ (objectId >>> 32));
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (origin != null ? origin.hashCode() : 0);
        result = 31 * result + (mappingRules != null ? mappingRules.hashCode() : 0);
        return result;
    }
}
