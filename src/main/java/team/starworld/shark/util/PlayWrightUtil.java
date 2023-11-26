package team.starworld.shark.util;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.ScreenshotType;
import j2html.TagCreator;
import j2html.tags.Renderable;
import j2html.tags.specialized.StyleTag;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.utils.FileUpload;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import team.starworld.shark.core.registries.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PlayWrightUtil {

    @Getter @Data
    public static class CDNConfig {

        private String urlStyleMDB = "https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css";
        private String urlStyleFontAwesome = "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css";

        public static final Map <String, String> cache = new HashMap <> ();

        @SneakyThrows
        public static String cacheStyle (String url) {
            if (cache.containsKey(url)) return cache.get(url);
            try {
                var content = Request.Get(url).execute().returnContent().asString();
                cache.put(url, content);
                return cacheStyle(url);
            } catch (Throwable throwable) {
                return "";
            }
        }

        public static StyleTag getStyle (String url) {
            return TagCreator.style(cacheStyle(url));
        }

    }

    public static final CDNConfig CDN_CONFIG = ConfigUtil.useConfig(ResourceLocation.of("cdn"), CDNConfig.class, CDNConfig::new);
    public static final String DISCORD_IMAGE_NAME = "image.png";


    public static byte[] makeScreenShot (String html) {
        var doc = Jsoup.parse(html);
        var playwright = Playwright.create();
        Browser browser = playwright.firefox().launch();
        Page page = browser.newPage();
        if (!doc.attr("min-width").isEmpty())
            page.setViewportSize(Integer.parseInt(doc.attr("min-width").isEmpty() ? "100" : doc.attr("min-width")), Integer.parseInt(doc.attr("min-height").isEmpty() ? "100" : doc.attr("min-height")));
        page.navigate("about:blank");
        page.setContent(html);
        var screenshot = page.screenshot(new Page.ScreenshotOptions().setType(ScreenshotType.PNG));
        playwright.close();
        return screenshot;
    }

    public static byte[] makeScreenShot (Renderable tag)  {
        return makeScreenShot(tag.render());
    }

    public static FileUpload getDiscordImage (byte[] image, String name) {
        return FileUpload.fromData(image, name);
    }

    public static FileUpload getDiscordImage (byte[] image) {
        return FileUpload.fromData(image, DISCORD_IMAGE_NAME);
    }

    public static FileUpload getDiscordImage (Renderable renderable) {
        return getDiscordImage(makeScreenShot(renderable.render()));
    }

}
