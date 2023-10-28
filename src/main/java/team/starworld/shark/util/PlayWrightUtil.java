package team.starworld.shark.util;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.ScreenshotType;
import j2html.tags.DomContent;
import j2html.tags.specialized.HtmlTag;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jsoup.Jsoup;

public class PlayWrightUtil {

    public static final String DISCORD_IMAGE_NAME = "image.png";

    public static byte[] makeScreenShot (HtmlTag content) {
        return makeScreenShot(content.render());
    }

    public static byte[] makeScreenShot (String html) {
        var doc = Jsoup.parse(html);
        var playwright = Playwright.create();
        Browser browser = playwright.webkit().launch();
        Page page = browser.newPage();
        page.setViewportSize(Integer.parseInt(doc.attr("width").isEmpty() ? "100" : doc.attr("width")), Integer.parseInt(doc.attr("height").isEmpty() ? "100" : doc.attr("height")));
        page.navigate("about:blank");
        page.setContent(html);
        var screenshot = page.screenshot(new Page.ScreenshotOptions().setType(ScreenshotType.PNG));
        playwright.close();
        return screenshot;
    }

    public static byte[] makeScreenShot (DomContent tag)  {
        return makeScreenShot(tag.render());
    }

    public static FileUpload getDiscordImage (byte[] image, String name) {
        return FileUpload.fromData(image, name);
    }

    public static FileUpload getDiscordImage (byte[] image) {
        return FileUpload.fromData(image, DISCORD_IMAGE_NAME);
    }

}
