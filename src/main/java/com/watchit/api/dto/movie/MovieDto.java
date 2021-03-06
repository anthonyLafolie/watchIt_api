package com.watchit.api.dto.movie;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDto {
    
    String title;
    String posterPath;
    int idMovie;

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || !this.getClass().equals(obj.getClass()))
            return false;
            MovieDto other = (MovieDto) obj;
        if (title.equals(other.getTitle()) && posterPath.equals(other.getPosterPath()) && idMovie == other.idMovie)
            return true;
        return false;
    }
}
