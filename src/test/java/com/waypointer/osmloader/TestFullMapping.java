package com.waypointer.osmloader;

import com.waypointer.osmloader.mapping.MappingSet;

public class TestFullMapping {

    public static MappingSet createFullMapping() {
        MappingSet mapping = new MappingSet();

        addWaterMapping(mapping);
        addFoodMapping(mapping);
        addReliefMapping(mapping);
        addTransportMapping(mapping);
        addEmergencyMapping(mapping);
        addHealthMapping(mapping);
        addFinanceCommunicationMapping(mapping);
        addBuildingMapping(mapping);
        addCultureMapping(mapping);
        addNaturalMapping(mapping);
        addAbandonedMapping(mapping);
        addPathMapping(mapping);
        addRestMapping(mapping);

        mapping.build();
        return mapping;
    }

    private static void addRestMapping(MappingSet mapping) {
        mapping.addRule().withTag("amenity", "toilets").exceptTag("abandoned", "yes").mapTo("CAT_REST_TOILET").useName("Toilets");
        mapping.addRule().withTag("amenity", "shelter").mapTo("CAT_REST_REST_PLACE").useNameTag("amenity");
        //todo: check all shelters - some of them are not present on maps 'Planinarski dom'

        mapping.addRule().withTag("leisure", "beach_resort").mapTo("CAT_REST_REST_PLACE").useName("Beach");
        mapping.addRule().withTag("leisure", "picnic_table").mapTo("CAT_REST_REST_PLACE").useNameTag("leisure");

        mapping.addRule().withTag("tourism", "alpine_hut").mapTo("CAT_REST_TOURIST_HAVEN").useNameTag("tourism");

        mapping.addRule().withTag("tourism", "camp_site").exceptTag("tents", "yes").mapTo("CAT_REST_CAMPING").useNameTag("tourism");
        mapping.addRule().withTag("tourism", "camp_site").withTag("tents", "yes").mapTo("CAT_REST_TENT_PLACE").useName("Tent place");
        //for tent place use detailed processing here - http://wiki.openstreetmap.org/wiki/Tag:tourism%3Dcamp_site

        mapping.addRule().withTag("tourism", "chalet").mapTo("CAT_REST_HOSTEL").useNameTag("tourism");
        mapping.addRule().withTag("tourism", "guest_house").exceptTag("abandoned", "yes").mapTo("CAT_REST_HOSTEL").useNameTag("tourism");
        mapping.addRule().withTag("tourism", "hostel").exceptTag("abandoned", "yes").mapTo("CAT_REST_HOSTEL")
                .useNameTag("tourism").useNameTag("operator");
        mapping.addRule().withTag("tourism", "hotel").exceptTag("abandoned", "yes").mapTo("CAT_REST_HOTEL")
                .useNameTag("tourism").useNameTag("operator");
        mapping.addRule().withTag("tourism", "motel").exceptTag("abandoned", "yes").mapTo("CAT_REST_HOTEL")
                .useNameTag("tourism").useNameTag("operator");
        mapping.addRule().withTag("tourism", "picnic_site").mapTo("CAT_REST_REST_PLACE").useNameTag("tourism");
        mapping.addRule().withTag("tourism", "wilderness_hut").mapTo("CAT_REST_TOURIST_HAVEN").useName("Hut");
    }

    private static void addPathMapping(MappingSet mapping) {
        //    mapping.addRule().withTag("bridge", null).mapTo(CategoryCode.CAT_PATH_BRIDGE);
        //    mapping.addRule().withTag("tunnel", null).mapTo(CategoryCode.CAT_PATH_TUNNEL);
        mapping.addRule().withTag("tourism", "information").exceptTag("information", "route_marker")
                .exceptTag("information", "office").mapTo("CAT_PATH_SIGNPOST_INFO").useName("Information").useDescrTag("ele");
        mapping.addRule().withTag("tourism", "information").withTag("information", "office").mapTo("CAT_PATH_SIGNPOST_INFO")
                .useName("Information office");
    }

    private static void addAbandonedMapping(MappingSet mapping) {
        mapping.addRule().withTag("historic", "ruins").exceptTag("abandoned", "yes").mapTo("CAT_ABANDONED_RUINS").useNameTag("historic");

        mapping.addRule().withTag("abandoned", "yes").withTag("place").useDescrTag("place").useName("Abandoned locality")
                .mapTo("CAT_ABANDONED_LOCALITY");

        mapping.addRule().withTag("abandoned", "yes")
                .exceptTag("ruins", "yes").exceptTag("place").exceptTag("power", "tower").exceptTag("historic", "ruins")
                .exceptTag("railway", "level_crossing")
                .exceptTag("highway")
                .useNameTag("man_made").useNameTag("military").useNameTag("landuse").useNameTag("amenity")
                .useNameTag("service").useName("Abandoned object")
                .useDescrTag("man_made").useDescrTag("military").useDescrTag("landuse").useDescrTag("amenity").useDescrTag("service")
                .mapTo("CAT_ABANDONED_OTHER");

        mapping.addRule().withTag("abandoned", "yes").withTag("building").exceptTag("ruins", "yes")
                .useName("Abandoned building").mapTo("CAT_ABANDONED_OTHER");

        mapping.addRule().withTag("ruins", "yes").exceptTag("highway").mapTo("CAT_ABANDONED_RUINS").useNameTag("man_made")
                .useNameTag("military").useNameTag("landuse");

        mapping.addRule().withTag("ruins", "yes").withTag("building", "collapsed").mapTo("CAT_ABANDONED_RUINS").useName("Collapsed building");
        mapping.addRule().withTag("ruins", "yes").withTag("building").exceptTag("abandoned", "yes").useName("Building ruins")
                .mapTo("CAT_ABANDONED_RUINS");
    }

    private static void addCultureMapping(MappingSet mapping) {
        mapping.addRule().withTag("historic", "archaeological_site").mapTo("CAT_CULTURE_ARCHEOLOGY").useName("Archaeological site");
        mapping.addRule().withTag("historic", "boundary_stone").mapTo("CAT_CULTURE_ARCHEOLOGY").useNameTag("historic");
        mapping.addRule().withTag("historic", "rune_stone").mapTo("CAT_CULTURE_ARCHEOLOGY").useNameTag("historic");

        mapping.addRule().withTag("historic", "castle").mapTo("CAT_CULTURE_CASTLE").useNameTag("historic");
        mapping.addRule().withTag("historic", "fort").mapTo("CAT_CULTURE_CASTLE").useNameTag("historic");
        mapping.addRule().withTag("historic", "manor").mapTo("CAT_CULTURE_CASTLE").useNameTag("historic");

        mapping.addRule().withTag("historic", "memorial").mapTo("CAT_CULTURE_MONUMENT").useNameTag("historic");
        mapping.addRule().withTag("historic", "monument").mapTo("CAT_CULTURE_MONUMENT").useNameTag("historic");

        mapping.addRule().withTag("historic", "tomb").mapTo("CAT_CULTURE_CEMETERY").useNameTag("historic");
        mapping.addRule().withTag("amenity", "grave_yard").mapTo("CAT_CULTURE_CEMETERY").useName("Graveyard");

        mapping.addRule().withTag("amenity", "place_of_worship").mapTo("CAT_CULTURE_RELIGION").useNameTag("religion")
                .useName("Religion place");
        mapping.addRule().withTag("building", "chapel").mapTo("CAT_CULTURE_RELIGION").useNameTag("building");

        mapping.addRule().withTag("man_made", "cross").mapTo("CAT_CULTURE_RELIGION").useNameTag("man_made");

        mapping.addRule().withTag("tourism", "artwork").exceptTag("historic", "memorial").exceptTag("historic", "monument")
                .mapTo("CAT_CULTURE_LANDSCAPE_ART").useNameTag("artwork_type").useName("Artwork");

        mapping.addRule().withTag("amenity", "cinema").mapTo("CAT_CULTURE_CINEMA").useNameTag("amenity");
        mapping.addRule().withTag("amenity", "theatre").exceptTag("abandoned", "yes").mapTo("CAT_CULTURE_THEATER").useNameTag("amenity");

        mapping.addRule().withTag("tourism", "gallery").mapTo("CAT_CULTURE_MUSEUM").useNameTag("tourism");
        mapping.addRule().withTag("tourism", "museum").mapTo("CAT_CULTURE_MUSEUM").useNameTag("tourism");

        mapping.addRule().withTag("tourism", "zoo").mapTo("CAT_CULTURE_ZOO").useNameTag("tourism");
        mapping.addRule().withTag("tourism", "aquarium").mapTo("CAT_CULTURE_ZOO").useNameTag("tourism");

        mapping.addRule().withTag("tourism", "theme_park").mapTo("CAT_CULTURE_OTHER").useNameTag("tourism");
    }

    private static void addNaturalMapping(MappingSet mapping) {
        mapping.addRule().withTag("leisure", "garden").mapTo("CAT_NATURAL_PARK").useNameTag("leisure");
        mapping.addRule().withTag("leisure", "park").mapTo("CAT_NATURAL_PARK").useNameTag("leisure");
        mapping.addRule().withTag("leisure", "nature_reserve").mapTo("CAT_NATURAL_RESERVE").useNameTag("leisure");
        mapping.addRule().withTag("tourism", "viewpoint").mapTo("CAT_NATURAL_VIEWPOINT").useName("Viewpoint");
    }

    private static void addBuildingMapping(MappingSet mapping) {
        mapping.addRule().withTag("waterway", "dock").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("waterway");
        mapping.addRule().withTag("waterway", "boatyard").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("waterway");

        mapping.addRule().withTag("amenity", "hunting_stand").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("amenity");

        mapping.addRule().withTag("leisure", "wildlife_hide").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("leisure");
        mapping.addRule().withTag("leisure", "bird_hide").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("leisure");

        mapping.addRule().withTag("historic", "building").exceptTag("amenity").mapTo("CAT_BUILDING_CONSTRUCTION")
                .useName("Historic building");
        mapping.addRule().withTag("historic", "city_gate").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("historic");

        mapping.addRule().withTag("man_made", "beacon").exceptTag("abandoned", "yes").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("man_made");
        mapping.addRule().withTag("man_made", "campanile").exceptTag("abandoned", "yes").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("man_made");
        mapping.addRule().withTag("man_made", "communications_tower").exceptTag("abandoned", "yes")
                .mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("man_made");
        mapping.addRule().withTag("man_made", "lighthouse").exceptTag("abandoned", "yes").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("man_made");
        mapping.addRule().withTag("man_made", "watermill").exceptTag("abandoned", "yes").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("man_made");
        mapping.addRule().withTag("man_made", "water_tower").exceptTag("abandoned", "yes").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("man_made");
        mapping.addRule().withTag("man_made", "windmill").exceptTag("abandoned", "yes").mapTo("CAT_BUILDING_CONSTRUCTION").useNameTag("man_made");
    }

    private static void addFinanceCommunicationMapping(MappingSet mapping) {
        //    mapping.addRule().withTag("amenity", "atm").mapTo("CAT_FINANCECOMM_BANK").useNameTag("amenity").useNameTag("operator");
        mapping.addRule().withTag("amenity", "bank").mapTo("CAT_FINANCECOMM_BANK").useNameTag("amenity").useNameTag("operator");
    }

    private static void addHealthMapping(MappingSet mapping) {
        mapping.addRule().withTag("amenity", "clinic").mapTo("CAT_HEALTH_HOSPITAL").useNameTag("amenity");
        mapping.addRule().withTag("amenity", "hospital").mapTo("CAT_HEALTH_HOSPITAL").useNameTag("amenity");

        mapping.addRule().withTag("amenity", "dentist").mapTo("CAT_HEALTH_DENTIST").useNameTag("amenity");

        mapping.addRule().withTag("amenity", "pharmacy").mapTo("CAT_HEALTH_PHARMACY").useNameTag("amenity");
        mapping.addRule().withTag("shop", "chemist").mapTo("CAT_HEALTH_PHARMACY").useNameTag("shop");

        mapping.addRule().withTag("leisure", "stadium").mapTo("CAT_HEALTH_SPORT").useNameTag("leisure");
        mapping.addRule().withTag("leisure", "swimming_pool").mapTo("CAT_HEALTH_SPORT").useNameTag("leisure");
        mapping.addRule().withTag("sport", "climbing").mapTo("CAT_HEALTH_SPORT").useDescrTag("sport").useDescrTag("height").useName("Climbing sport");

        mapping.addRule().withTag("shop", "beauty").mapTo("CAT_HEALTH_BEAUTY_SALON").useName("Beauty shop/salon");
        mapping.addRule().withTag("shop", "hairdresser").mapTo("CAT_HEALTH_BEAUTY_SALON").useNameTag("shop");
    }

    private static void addEmergencyMapping(MappingSet mapping) {
        mapping.addRule().withTag("amenity", "police").mapTo("CAT_EMERGENCY_POLICE").useNameTag("amenity");

        mapping.addRule().withTag("amenity", "rescue_station").mapTo("CAT_EMERGENCY_RESCUE").useNameTag("amenity");
        mapping.addRule().withTag("emergency", "ambulance_station").mapTo("CAT_EMERGENCY_RESCUE").useNameTag("emergency");

        mapping.addRule().withTag("amenity", "embassy").mapTo("CAT_EMERGENCY_EMBASSY");
    }

    private static void addTransportMapping(MappingSet mapping) {
        mapping.addRule().withTag("aeroway", "aerodrome").mapTo("CAT_TRANSPORT_AERO").useNameTag("aeroway");

        mapping.addRule().withTag("amenity", "car_rental").mapTo("CAT_TRANSPORT_CAR_RENT").useNameTag("amenity");
        mapping.addRule().withTag("amenity", "fuel").mapTo("CAT_TRANSPORT_FUEL").useNameTag("amenity");  //todo: be aware of count
        //mapping.addRule().withTag("amenity", "parking").mapTo(CategoryCode.CAT_TRANSPORT_PARKING).useNameTag("amenity");  //todo: be aware of count
        mapping.addRule().withTag("highway", "speed_camera").mapTo("CAT_TRANSPORT_TRAFFIC_CAMERA").useNameTag("highway")
                .useDescrTag("maxspeed"); //todo think about them

        mapping.addRule().withTag("amenity", "ferry_terminal").mapTo("CAT_TRANSPORT_PORT").useNameTag("amenity");  //use 'cargo' for descr

        mapping.addRule().withTag("railway", "halt").mapTo("CAT_TRANSPORT_RAILWAY").useName("Railway halt");
        mapping.addRule().withTag("railway", "station").mapTo("CAT_TRANSPORT_RAILWAY").useName("Railway station");
        //   mapping.addRule().withTag("public_transport", "platform").mapTo("CAT_TRANSPORT_RAILWAY");    //see i.e [bus:yes|public_transport:platform]

        mapping.addRule().withTag("amenity", "bus_station").mapTo("CAT_TRANSPORT_STOP_OTHER").useName("Bus station");
        //  mapping.addRule().withTag("highway", "bus_stop").mapTo("CAT_TRANSPORT_STOP_OTHER").useName("Bus stop");
    }

    private static void addReliefMapping(MappingSet mapping) {
        mapping.addRule().withTag("natural", "peak").mapTo("CAT_RELIEF_TOP").useNameTag("ele").useDescrTag("ele").useName("Peak");
        mapping.addRule().withTag("natural", "volcano").mapTo("CAT_RELIEF_VOLCANO");

        mapping.addRule().withTag("natural", "saddle").mapTo("CAT_RELIEF_PASS").useName("Pass");
        mapping.addRule().withTag("mountain_pass", "yes").mapTo("CAT_RELIEF_PASS").useName("Pass");

        mapping.addRule().withTag("natural", "rock").mapTo("CAT_RELIEF_ROCK").useNameTag("natural");
        mapping.addRule().withTag("natural", "stone").mapTo("CAT_RELIEF_ROCK").useNameTag("natural");

        mapping.addRule().withTag("natural", "cave_entrance").mapTo("CAT_RELIEF_CAVE").useName("Cave");
        mapping.addRule().withTag("man_made", "adit").mapTo("CAT_RELIEF_CAVE").useNameTag("man_made");
    }

    private static void addWaterMapping(MappingSet mapping) {
        mapping.addRule().withTag("natural", "spring").exceptTag("man_made", "water_well").mapTo("CAT_WATER_SPRING").useNameTag("natural");

        mapping.addRule().withTag("drinking_water", "yes").exceptTag("natural", "spring").mapTo("CAT_WATER_WELL").useName("Drinking water");
        mapping.addRule().withTag("amenity", "drinking_water").exceptTag("natural", "spring").mapTo("CAT_WATER_WELL").useNameTag("amenity");
        mapping.addRule().withTag("amenity", "water_point").exceptTag("natural", "spring").mapTo("CAT_WATER_WELL").useNameTag("amenity");
        mapping.addRule().withTag("man_made", "water_well").exceptTag("drinking_water", "no").mapTo("CAT_WATER_WELL").useName("Well");
        mapping.addRule().withTag("amenity", "fountain").mapTo("CAT_WATER_WELL").useNameTag("amenity");

        mapping.addRule().withTag("landuse", "reservoir").mapTo("CAT_WATER_RESERVOIR").useName("Water reservoir");
    }

    private static void addFoodMapping(MappingSet mapping) {
        mapping.addRule().withTag("amenity", "bar").mapTo("CAT_FOOD_CAFE").useNameTag("amenity");
        mapping.addRule().withTag("amenity", "cafe").mapTo("CAT_FOOD_CAFE").useNameTag("amenity");
        mapping.addRule().withTag("amenity", "fast_food").mapTo("CAT_FOOD_CAFE").useNameTag("amenity");
        mapping.addRule().withTag("amenity", "pub").mapTo("CAT_FOOD_CAFE").useNameTag("amenity");
        mapping.addRule().withTag("amenity", "restaurant").mapTo("CAT_FOOD_CAFE").useNameTag("amenity").useDescrTag("cuisine");

        mapping.addRule().withTag("amenity", "marketplace").mapTo("CAT_FOOD_MARKET").useNameTag("amenity");

        mapping.addRule().withTag("shop", "general").mapTo("CAT_FOOD_SHOP").useName("Shop");
        mapping.addRule().withTag("shop", "convenience").mapTo("CAT_FOOD_SHOP").useName("Shop");
        mapping.addRule().withTag("shop", "greengrocer").mapTo("CAT_FOOD_SHOP").useNameTag("shop");
        mapping.addRule().withTag("shop", "mall").mapTo("CAT_FOOD_SHOP").useNameTag("shop");
        mapping.addRule().withTag("shop", "supermarket").mapTo("CAT_FOOD_SHOP").useNameTag("shop").useNameTag("operator");
    }    
    
}
