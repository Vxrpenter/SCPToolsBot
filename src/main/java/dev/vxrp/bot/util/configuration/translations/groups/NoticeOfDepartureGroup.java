package dev.vxrp.bot.util.configuration.translations.groups;

public class NoticeOfDepartureGroup {
    private String first_title;
    private String first_body;

    public NoticeOfDepartureGroup(String first_title, String first_body) {
        this.first_title = first_title;
        this.first_body = first_body;
    }
    public String getFirst_title() {
        return first_title;
    }
    public String getFirst_body() {
        return first_body;
    }

    public void setFirst_title(String first_title) {
        this.first_title = first_title;
    }

    public void setFirst_body(String first_body) {
        this.first_body = first_body;
    }
}
