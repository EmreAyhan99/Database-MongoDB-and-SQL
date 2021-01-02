package game.model;

public enum Genre
{
    DRAMA(0),HOROR(1),FANTASY(2),NOGENRE(3);

    private Genre(final int genre) {
        this.genre = genre;
    }
    private int genre;



}



