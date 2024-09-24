package de.bathesis.spielzeitenplaner.utilities;

import java.util.List;
import java.util.ArrayList;


public class ExpectedElements {

    private static final String NAVBRAND_TEXT = "SpielzeitenPlaner";
    private static final String FOOTER_TEXT = "© 2024 SpielzeitenPlaner. Alle Rechte vorbehalten.";
    private static final List<String> FEATURES = new ArrayList<>(List.of(
        "Recap", 
        "Spielzeiten planen", 
        "Team verwalten", 
        "Einstellungen" 
    ));
    private static final String SPIELZEITEN_TITLE = "Spielzeiten planen";


    public static String navbrandText() {
        return NAVBRAND_TEXT;
    }
    public static String footerText() {
        return FOOTER_TEXT;
    }
    public static List<String> features() {
        return FEATURES;
    }
    public static String spielzeitenTitle() {
        return SPIELZEITEN_TITLE;
    }

}
