package com.waypointer.osmloader;

import com.waypointer.osmloader.mapping.MappingSet;

public class TestFullMapping {

    public static MappingSet createFullMapping() {
        MappingSet mapping = new MappingSet();

        addWaterMapping(mapping);
        addRestMapping(mapping);

        mapping.build();
        return mapping;
    }

    private static void addRestMapping(MappingSet mapping) {
        mapping.addRule().withTag("amenity", "shelter").mapTo("CAT_REST_REST_PLACE").useNameTag("amenity");

        mapping.addRule().withTag("leisure", "beach_resort").mapTo("CAT_REST_REST_PLACE").useName("Beach");
        mapping.addRule().withTag("leisure", "picnic_table").mapTo("CAT_REST_REST_PLACE").useNameTag("leisure");

        mapping.addRule().withTag("tourism", "alpine_hut").mapTo("CAT_REST_TOURIST_HAVEN").useNameTag("tourism");

        mapping.addRule().withTag("tourism", "camp_site").exceptTag("tents", "yes").mapTo("CAT_REST_CAMPING").useNameTag("tourism");
        mapping.addRule().withTag("tourism", "camp_site").withTag("tents", "yes").mapTo("CAT_REST_TENT_PLACE").useName("Tent place");
    }

    private static void addWaterMapping(MappingSet mapping) {
        mapping.addRule().withTag("natural", "spring").exceptTag("man_made", "water_well").mapTo("CAT_WATER_SPRING").useNameTag("natural");

        mapping.addRule().withTag("drinking_water", "yes").exceptTag("natural", "spring").mapTo("CAT_WATER_WELL").useName("Drinking water");
        mapping.addRule().withTag("amenity", "drinking_water").exceptTag("natural", "spring").mapTo("CAT_WATER_WELL").useNameTag("amenity");
        mapping.addRule().withTag("amenity", "water_point").exceptTag("natural", "spring").mapTo("CAT_WATER_WELL").useNameTag("amenity");
    }

}
