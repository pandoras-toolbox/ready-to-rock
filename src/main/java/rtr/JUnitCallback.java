package rtr;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static rtr.AllureUtil.createEnvironmentFile;
import static rtr.AllureUtil.takeScreenshot;
import static rtr.TestGroup.GUI_TESTS;

public class JUnitCallback implements BeforeAllCallback, AfterEachCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        createEnvironmentFile();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        boolean guiTest = context.getTags().contains(GUI_TESTS);
        if (context.getExecutionException().isPresent() && guiTest) {
            takeScreenshot("Post Mortem Screenshot", false);
        }
        if (guiTest) {
            WebDriverProvider.INSTANCE.getWebDriver().quit();
        }
    }

}
