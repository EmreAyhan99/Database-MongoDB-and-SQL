package game.model;

public class Author
{
    private int authorID;
    private String name;


    /**
     *
     * @param authorID
     * @param name
     */
    public Author(int authorID, String name) {
        this.authorID = authorID;
        this.name = name;
    }

    public Author(String name) {
        this.name = name;
    }

    /**
     * getting author
     * @return
     */
    public int getAuthorID() {
        return authorID;
    }

    /**
     * getting name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * setting name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "" +
                "authorID=" + authorID +
                ", name='" + name + '\''
                ;
    }
}
