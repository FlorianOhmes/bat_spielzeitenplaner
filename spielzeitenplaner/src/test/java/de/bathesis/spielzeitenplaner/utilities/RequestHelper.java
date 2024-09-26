package de.bathesis.spielzeitenplaner.utilities;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


public class RequestHelper {

    public static ResultActions performGet(MockMvc mvc, String path) throws Exception {
        return mvc.perform(get(path));
    }

    public static Document performGetAndParseWithJSoup(MockMvc mvc, String path) throws Exception {
        String html = performGet(mvc, path).andReturn().getResponse().getContentAsString();
        return Jsoup.parse(html);
    }

    public static String extractTextFrom(Document page, String cssQuery) {
        return page.select(cssQuery).text();
    }

    public static String extractTextFrom(Elements htmlSnippet, String cssQuery) {
        return htmlSnippet.select(cssQuery).text();
    }

    public static Elements extractFrom(Document page, String cssQuery) {
        return page.select(cssQuery);
    }

    public static Elements extractFrom(Elements htmlSnippet, String cssQuery) {
        return htmlSnippet.select(cssQuery);
    }

}
