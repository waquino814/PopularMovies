package org.wellingtonaquino.popularmovies.dataobjects;

/**
 * Created by wellingtonaquino on 7/3/16.
 */
public class MovieDO {
    private long id;
    private String title;
    private String posterPath;
    private String overview;

    public MovieDO() {
    }

    public MovieDO(long id, String title, String posterPath, String overview) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
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

    @Override
    public String toString() {
        return "MovieDO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", overview='" + overview + '\'' +
                '}';
    }
}
