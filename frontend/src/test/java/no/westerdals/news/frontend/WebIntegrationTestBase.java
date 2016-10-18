package no.westerdals.news.frontend;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class WebIntegrationTestBase {
    static WebDriver driver;

    public static boolean isUnix() {
        return !System.getProperty("os.name").startsWith("Windows");
    }

    public static WebDriver findWebDriver() {
        Path path = findExecutable("geckodriver");
        if (path != null) {
            System.setProperty("webdriver.gecko.driver", path.toString());
            return new FirefoxDriver();
        }
        path = findExecutable("chromedriver");
        if (path != null) {
            System.setProperty("webdriver.chrome.driver", path.toString());
            return new ChromeDriver();
        }
        return null;
    }

    public static Path findExecutable(String executableName) {
        Path path;
        if (isUnix()) {
            path = Paths.get("/usr/bin", executableName);
            if (Files.exists(path)) {
                return path;
            }
            path = Paths.get("/bin", executableName);
            if (Files.exists(path)) {
                return path;
            }
            path = Paths.get(System.getProperty("user.home"), executableName);
            if (Files.exists(path)) {
                return path;
            }
        } else {
            path = Paths.get("C:/selenium", executableName + ".exe");
            if (Files.exists(path)) {
                return path;
            }
            path = Paths.get(System.getProperty("user.home"), executableName + ".exe");
            if (path != null) {
                return path;
            }
        }
        return null;
    }

    @BeforeClass
    public static void init() throws Exception {
        driver = findWebDriver();
        for (int i = 0; i < 30 && !isJBossRunning(); i++) {
            Thread.sleep(1000);
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.close();
    }

    private static ResteasyClient getClient() {
        // Setting digest credentials
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "admin");
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpclient, true);

        // Creating HTTP client
        return new ResteasyClientBuilder().httpEngine(engine).build();
    }

    public static boolean isJBossRunning() {

        try {
            WebTarget target = getClient().target("http://localhost:9990/management").queryParam("operation", "attribute").queryParam("name", "server-state");
            Response response = target.request(MediaType.APPLICATION_JSON).get();
            return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("running");
        } catch (Exception e){
            return false;
        }
    }
}
