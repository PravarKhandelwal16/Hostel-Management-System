import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a notice to be displayed on the notice board.
 */
public class Notice implements Serializable {
    private String title;
    private String content;
    private LocalDate datePosted;
    private String author; // e.g., "Admin", "Warden"

    // Constructor
    public Notice(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.datePosted = LocalDate.now();
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDatePosted() {
        return datePosted;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Notice on " + datePosted + " by " + author + "\nTitle: " + title + "\n" + content;
    }
}
