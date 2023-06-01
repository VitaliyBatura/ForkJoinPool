import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) throws Exception {

        new ForkJoinPool().invoke(new SiteMapMaker(SiteMapMaker.url));

        SiteMapMaker.linksListCreator(SiteMapMaker.linkSet);
        SiteMapMaker.mapWrite(SiteMapMaker.objects, SiteMapMaker.url);
        SiteMapMaker.print(SiteMapMaker.finish, SiteMapMaker.siteMap);
    }
}