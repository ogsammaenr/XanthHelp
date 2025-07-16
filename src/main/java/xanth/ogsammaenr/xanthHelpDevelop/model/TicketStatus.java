package xanth.ogsammaenr.xanthHelpDevelop.model;

public enum TicketStatus {
    OPEN("açık"),
    IN_PROGRESS("İşlemde"),
    RESOLVED("Çözüldü"),
    CANCELED("İptal Edildi");


    private final String displayName;

    TicketStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
