package de.bathesis.spielzeitenplaner.templates.fragments;

import de.bathesis.spielzeitenplaner.utilities.ExpectedElements;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.File;
import java.io.IOException;


class FragmentsTest {

    Document fragmentsTemplate;

    @BeforeEach
    void getFragmentsTemplate() throws IOException {
        File fragmentsFile = new File("src/main/resources/templates/fragments/basics.html");
        fragmentsTemplate = Jsoup.parse(fragmentsFile, "UTF-8");
    }


    @Test
    @DisplayName("Die Navigationsleiste ist korrekt strukturiert.")
    void test_01() {
        String navbarBrandText = RequestHelper.extractTextFrom(fragmentsTemplate, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractTextFrom(fragmentsTemplate, "nav.navbar ul.navbar-nav li.nav-item");

        assertThat(navbarBrandText).isEqualTo(ExpectedElements.navbrandText());
        assertThat(navigationItemsTerms).contains(ExpectedElements.features());
    }

    @Test
    @DisplayName("Der Footer ist korrekt strukturiert.")
    void test_02() {
        String footerText = RequestHelper.extractTextFrom(fragmentsTemplate, "footer p");
        assertThat(footerText).isEqualTo(ExpectedElements.footerText());
    }

}
