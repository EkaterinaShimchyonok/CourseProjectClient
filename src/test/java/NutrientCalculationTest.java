import static org.junit.jupiter.api.Assertions.*;

import org.example.courseproject.Controllers.MainController;
import org.example.courseproject.Controllers.ProfileController;
import org.example.courseproject.POJO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NutrientCalculationTest {

    private ProfileController profile;

    @BeforeEach
    public void setUp() {
        MainController main = new MainController();
        main.setUser(new User());
        profile = new ProfileController();
        profile.setUser(main);
    }

    @Test
    public void testCalculateDailyIntake() {
        User user = profile.main.user;
        user.getInfo().setHeight(170);
        user.getInfo().setWeight(70);
        user.getInfo().setAge(30);
        user.getInfo().setMale(true);
        user.getInfo().setActivityLevel(2);

        profile.calculateDailyIntake();
        Nutrients nutrients = user.getInfo().getNorm();

        assertNotNull(nutrients);
        assertNotNull(nutrients.getMacroNutrients());
        assertNotNull(nutrients.getVitamins());
        assertNotNull(nutrients.getMinerals());
    }

    @Test
    public void testCalcMacroNutr() {
        MacroNutrients macroNutrients = profile.calcMacroNutr(70, 170, 30, true, 2);

        assertEquals(2507, macroNutrients.getCalories(), 10); // примерное значение
        assertEquals(84, macroNutrients.getProteins(), 5);
        assertEquals(71, macroNutrients.getFats(), 5);
        assertEquals(385, macroNutrients.getCarbs(), 10);
    }

    @Test
    public void testCalcMicroNutr() {
        Nutrients nutrients = profile.calcMicroNutr(30, true, 2);
        Vitamins vitamins = nutrients.getVitamins();
        Minerals minerals = nutrients.getMinerals();

        assertEquals(900, vitamins.getA());
        assertEquals(15, vitamins.getD());
        assertEquals(15, vitamins.getE());
        assertEquals(120, vitamins.getK());
        assertEquals(100, vitamins.getC());
        assertEquals(2.4, vitamins.getB12(), 0.1);

        assertEquals(1000, minerals.getCa());
        assertEquals(8, minerals.getFe());
        assertEquals(450, minerals.getMg());
        assertEquals(11, minerals.getZn());
        assertEquals(0.9, minerals.getCu(), 0.1);
        assertEquals(55, minerals.getSe());
    }
}
