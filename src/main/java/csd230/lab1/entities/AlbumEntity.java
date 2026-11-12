package csd230.lab1.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALBUM")
public class AlbumEntity extends PublicationEntity {
    private String artist;
    private String genre;
    private String recordLabel;
    private Integer releaseYear;

    public AlbumEntity() {}

    public AlbumEntity(String title, Double price, int copies, String artist,
                       String genre, String recordLabel, Integer releaseYear) {
        super(title, price, copies);
        this.artist = artist;
        this.genre = genre;
        this.recordLabel = recordLabel;
        this.releaseYear = releaseYear;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRecordLabel() {
        return recordLabel;
    }

    public void setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public String toString() {
        return "Album{artist='" + artist + "', genre='" + genre
                + "', recordLabel='" + recordLabel + "', releaseYear=" + releaseYear
                + ", " + super.toString() + "}";
    }
}
