import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class SiteMapMaker extends RecursiveTask<Set<String>> {

    public static String url = "https://skillbox.ru/";

    public static String siteMap = "C:\\Users\\Vitaliy\\IDEA\\java_basics14\\Multithreading\\ForkJoinPool\\data\\siteMap.txt";
    public static Set<String> linkSet = new HashSet<>();
    public static List<Links> objects = new ArrayList<>();
    public static List<String> finish = new LinkedList<>();
    private String childLink;

    public SiteMapMaker(String childLink) {
        this.childLink = childLink;
    }

    @Override
    protected Set<String> compute() throws RuntimeException {
        Document document;
        List<SiteMapMaker> taskList = new ArrayList<>();
        try {
            document = Jsoup.connect(childLink).get();
            Thread.sleep(100);
            Elements links = document.select("a");
            for (Element link : links) {
                childLink = link.attr("abs:href");
                if (linkSet.contains(childLink)) {
                    continue;
                }
                if (isLink(childLink) & !linkSet.contains(childLink)) {
                    linkSet.add(childLink);
                    SiteMapMaker task = new SiteMapMaker(childLink);
                    task.fork();
                    taskList.add(task);
                }
            }
            for (SiteMapMaker task : taskList) {
                linkSet.addAll(task.join());
            }
        } catch (IOException | InterruptedException | ConcurrentModificationException e) {
            e.printStackTrace();
        }
        return linkSet;
    }

    private boolean isLink(String childLink) {
        return childLink.startsWith(url) & childLink.endsWith("/") & !childLink.endsWith("#");
    }

    public static void print(List<String> finish, String siteMap) throws Exception {
        FileWriter writer = new FileWriter(siteMap);
        String tab = "\t";
        for (String str : finish) {
            writer.write(tab.repeat(Links.getLevel(str) - 1) + str + System.lineSeparator());
        }
        writer.close();
    }

    public static void mapWrite(List<Links> objects, String url) {
        for (Links object : objects) {
            if (Links.getLevel(object.getLinkName()) - Links.getLevel(url) == 1
                    && object.getLinkName().startsWith(url)
                    && (object.getChildLinksName() == null || object.getChildLinksName().size() == 0)) {
                finish.add(object.getLinkName());
            } else if (Links.getLevel(object.getLinkName()) - Links.getLevel(url) == 1
                    && object.getLinkName().startsWith(url)
                    && object.getChildLinksName().size() > 0) {
                finish.add(object.getLinkName());
                mapWrite(objects, object.getLinkName());
            }
        }
    }

    public static void linksListCreator(Set<String> linkSet) {
        try {
            for (String link : linkSet) {
                Links object = new Links();
                object.setLinkName(link);
                List<Links> childLinksName = new ArrayList<>();
                for (String childLink : linkSet) {
                    Links childObject = new Links();
                    childObject.setLinkName(childLink);
                    if ((Links.getLevel(childObject.getLinkName()) - Links.getLevel(object.getLinkName()) == 1)
                            && childObject.getLinkName().startsWith(object.getLinkName())) {
                        childLinksName.add(childObject);
                    }
                }
                object.setChildLinksName(childLinksName);
                objects.add(object);
            }
            finish.add(url);
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }
}