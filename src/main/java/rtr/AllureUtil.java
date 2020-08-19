package rtr;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class AllureUtil {

    private AllureUtil() {
        // utility class
    }

    public static void step(String name, Runnable runnable) {
        String uuid = UUID.randomUUID().toString();
        StepResult result = new StepResult().setName(name);
        Allure.getLifecycle().startStep(uuid, result);
        try {
            runnable.run();
            Allure.getLifecycle().updateStep(uuid, step -> step.setStatus(Status.PASSED));
        } catch (Exception e) {
            Allure.getLifecycle().updateStep(uuid, step -> step
                    .setStatus(ResultsUtils.getStatus(e).orElse(Status.BROKEN))
                    .setStatusDetails(ResultsUtils.getStatusDetails(e).orElse(null)));
            throw e;
        } finally {
            Allure.getLifecycle().stopStep(uuid);
        }
    }

    // There is no other way currently, see: https://github.com/allure-framework/allure2/issues/907
    public static void createEnvironmentFile() throws IOException {
        String properties = String.format("System=Production%sBrowser=%s", System.lineSeparator(), Configuration.INSTANCE.getBrowser());
        File file = new File("build/allure-results/environment.properties");
        if (!file.exists()) {
            FileUtils.writeStringToFile(file, properties, UTF_8);
        }
    }

    public static void takeScreenshot(String name) {
        takeScreenshot(name, true);
    }

    public static void takeScreenshot(String name, boolean step) {
        Runnable runnable = () -> {
            try {
                byte[] bytes = Shutterbug.shootPage(DriverProvider.INSTANCE.getWebDriver()).getBytes();
                Allure.getLifecycle().addAttachment(name, "image/png", null, bytes);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        };
        if (step) {
            step("takeScreenshot", runnable);
        } else {
            runnable.run();
        }
    }

}
