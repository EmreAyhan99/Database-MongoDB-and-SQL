package game.model;

public enum Genre
{
    Drama(0),Horor(1),Fantasy(2),Nogenre(3),Romance(4);

    private Genre(final int genre) {
        this.genre = genre;
    }
    private int genre;



}



