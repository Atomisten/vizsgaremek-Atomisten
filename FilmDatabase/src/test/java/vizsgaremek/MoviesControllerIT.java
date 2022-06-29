package vizsgaremek;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vizsgaremek.controller.MoviesController;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

public class MoviesControllerIT {

    @Autowired
    MoviesController moviesController;

    @Test
    void testHello() {
        System.out.println("lol");
    }
}
