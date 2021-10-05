package eterna.data.schema;

public class QuotesTable {
    public int id;
    public String quoteText;
    public String likes;
    public String shares;

    public QuotesTable() {
    }

    public QuotesTable(int id, String quoteText, String likes) {
        this.quoteText = quoteText;
        this.likes = likes;
    }
}
