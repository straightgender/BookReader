


// Try and double-click on the image to open it's true size image


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class User {
    public static void main(String[] args) throws IOException {
        String storyPath = "Books/Snow White";
        int wordsInOnePage = 300;
        String bookBackgroundPath = "book.jpg";
        String[] imagesPaths = {
                "images/Snow White/1st.jpg",
                "images/Snow White/2nd.jpg",
                "images/Snow White/3rd.jpg",
                "images/Snow White/4th.jpg",
                "images/Snow White/5th.png",
                "images/Snow White/6th.jpeg",
                "images/Snow White/7th.jpg"
        };
        new BookReader("Snow White and the Seven Dwarfs",
                        new String(Files.readAllBytes(Paths.get(storyPath))),
                        wordsInOnePage, bookBackgroundPath, imagesPaths);
    }

}
