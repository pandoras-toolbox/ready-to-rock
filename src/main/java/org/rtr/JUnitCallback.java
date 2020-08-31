package org.rtr;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class JUnitCallback implements BeforeAllCallback, AfterEachCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        AllureUtil.createEnvironmentFile();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        boolean guiTest = context.getTags().contains(TestGroup.GUI_TESTS);
        if (context.getExecutionException().isPresent() && guiTest) {
            AllureUtil.takeScreenshot("Post Mortem Screenshot", false);
        }
        if (guiTest) {
            DriverProvider.INSTANCE.getWebDriver().quit();
        }
    }

}
