package game.model;

import org.bson.types.ObjectId;

public class Author
{
    private ObjectId authorID;
    private String name;


    /**
     *
     * @param authorID
     * @param name
     */
    public Author(ObjectId authorID, String name) {
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
    public ObjectId getAuthorID() {
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
                "name: " + name + '\''+
                ", authorID: " + authorID
                ;
    }
}
