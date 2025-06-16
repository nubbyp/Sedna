import java.io.IOException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SubDomainCrawler {

    /**
     * Recursively crawls the given URL and collects subdomains.
     * @param inputUrl The base URL to check against.
     * @param url The current URL being crawled.
     * @param visited A set of visited URLs to avoid cycles.
     */
    public static void crawl(String inputUrl, String url, Set<String> visited, Set<String> externalLinks) {
        if (!visited.contains(url)) {
            visited.add(url);

            Document doc = fetchDocumentWithFallback(url);
            if (doc == null)
                return;

            System.out.println("Crawled: " + url);

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String href = link.absUrl("href");
                if (href.contains(inputUrl)) {
                    String cleanedHref = href.split("#")[0];
                    if (href.contains("mailto")) {
                        System.out.println("Ignoring mailto link: " + href);
                        continue; // Ignore mailto link
                    } else if (href.endsWith(".pdf") || href.endsWith(".doc") || href.endsWith(".docx")) {
                        System.out.println("Ignoring document link: " + href);
                        continue; // Ignore document links
                    }
                    crawl(inputUrl, cleanedHref, visited, externalLinks);
                } else {
                    if (!externalLinks.contains(href)) {
                        externalLinks.add(href);
                        System.out.println("Ignoring external link: " + href);
                    }
                }
            }

        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a URL or enter exit to leave the program: ");
            String inputUrl = scanner.nextLine();
            if (inputUrl.equals("exit")) {
                System.out.println("Exiting program...");
                break;
            }

            Set<String> subdomains = new HashSet<>();
            Set<String> externalLinks = new HashSet<>();

            // Strip off www. if it exists
            inputUrl = (inputUrl.startsWith("www.")) ? inputUrl.substring(4) : inputUrl;

            Document doc = fetchDocumentWithFallback(inputUrl);

            if (doc == null) {
                System.out.println("Please check the URL and try again.");
                continue;
            }

            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String href = link.absUrl("href");

                if (href.contains(inputUrl)) {
                    String cleanedHref = href.split("#")[0];
                    if(cleanedHref.equals(inputUrl)) {
                        continue; // Skip the main URL itself
                    }
                    crawl(inputUrl, cleanedHref, subdomains, externalLinks);
                } else {
                    System.out.println("Ignoring external link: " + href);
                }
            }

            // Convert to TreeSet to sort alphabetically
            Set<String> sortedSubDomains = new TreeSet<>(subdomains);
            System.out.println("Discovered subdomains for " + inputUrl + ": ");
            for (String sub : sortedSubDomains) {
                System.out.println(sub);
            }

            System.out.println("Number of subdomains found: " + sortedSubDomains.size());
        }
    }

    /**
     * Fetches a Document from the given URL, trying both HTTP and HTTPS schemes.
     * User may leave off HTTP or HTTPS.
     * @param inputUrl The URL to fetch.
     * @return The Document if successful, null otherwise.
     */
    public static Document fetchDocumentWithFallback(String inputUrl) {

        String[] schemes = {"https://", "http://"};
        for (String scheme : schemes) {
            String fullUrl = inputUrl.matches("^(?i)https?://.*") ? inputUrl : scheme + inputUrl;
            try {
                //System.out.println("Trying: " + fullUrl);
                return Jsoup.connect(fullUrl).get();
            } catch (IOException e) {
                // Continue to next scheme
            }
        }
        return null; // All attempts failed
    }

}
