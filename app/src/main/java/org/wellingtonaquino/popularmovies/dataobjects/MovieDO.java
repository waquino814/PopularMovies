package org.wellingtonaquino.popularmovies.dataobjects;

/**
 * Created by wellingtonaquino on 7/3/16.
 */
public class MovieDO {
    private long id;
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String voteAverage;
    private String voteCount;



    public MovieDO() {
    }

    public long getId() {
        return id;
    }

    public MovieDO setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MovieDO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public MovieDO setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public MovieDO setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public MovieDO setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public MovieDO setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public MovieDO setVoteCount(String voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    @Override
    public String toString() {
        return "MovieDO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage='" + voteAverage + '\'' +
                ", voteCount='" + voteCount + '\'' +
                '}';
    }
}
