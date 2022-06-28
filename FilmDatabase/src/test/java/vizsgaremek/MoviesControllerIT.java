package vizsgaremek;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vizsgaremek.controller.MoviesController;
import vizsgaremek.service.MoviesService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class MoviesControllerIT {

    @Autowired
    MoviesController moviesController;
//    @Mock
//    MoviesService moviesService;


//    @Autowired
//    MoviesController moviesController;

    @Test
    void testHello(){
        System.out.println("lol");
    }
}
