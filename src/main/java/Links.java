import java.util.List;

public class Links {

    private String linkName;
    public List<Links> childLinksName;

    public Links() {
    }

    public static int getLevel(String linkName) {
        String[] levels = linkName.replace("https://", "").strip().split("/");
        return levels.length;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public List<Links> getChildLinksName() {
        return childLinksName;
    }

    public void setChildLinksName(List<Links> childLinksName) {
        this.childLinksName = childLinksName;
    }
}