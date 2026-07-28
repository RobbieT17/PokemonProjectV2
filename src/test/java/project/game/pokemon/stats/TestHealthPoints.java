package project.game.pokemon.stats;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestHealthPoints {
    
    HealthPoints healthPoints;

    @BeforeEach
    void setup() {
        this.healthPoints = new HealthPoints(100);
    }

    @Test
    void testConstruction() {
        assertThat(healthPoints.getMaxHealthPoints())
        .as("Current HP should be set to Max HP on class construction")
        .isEqualTo(healthPoints.getCurrentHealthPoints());

        assertThat(healthPoints.getMaxHealthPoints())
        .as("Wrong HP value was set")
        .isEqualTo(100);
    }

    @Test
    void testChangeNegative() {
        int expectedMaxHp = this.healthPoints.getMaxHealthPoints();

        this.healthPoints.change(-10);

        assertThat(this.healthPoints.getMaxHealthPoints())
        .as("Max HP should never change")
        .isEqualTo(expectedMaxHp);

        assertThat(this.healthPoints.getCurrentHealthPoints())
        .isEqualTo(90);

        this.healthPoints.change(-999);

        assertThat(this.healthPoints.getCurrentHealthPoints())
        .as("Current HP should never go below 0")
        .isEqualTo(0);
    }

    @Test
    void testChangePositive() {
        this.healthPoints.change(-99); // Set HP to 1
        this.healthPoints.change(25);

        assertThat(this.healthPoints.getCurrentHealthPoints()).isEqualTo(26);

        this.healthPoints.change(999);

        assertThat(this.healthPoints.getCurrentHealthPoints())
        .as("Current HP should never exceed max HP")
        .isEqualTo(this.healthPoints.getMaxHealthPoints());
    }

    @Test
    void testAtFullHP() {
        assertThat(this.healthPoints.atFullHP()).isTrue();

        this.healthPoints.change(-5);

        assertThat(this.healthPoints.atFullHP())
        .as("Should be false when health decreases")
        .isFalse();

        this.healthPoints.change(10);

        assertThat(this.healthPoints.atFullHP())
        .as("Should be true when health is restored back to full")
        .isTrue();
    }

    @Test
    @DisplayName(".depleted() method shoud return true iff current health is at 0")
    void testDepleted() {
        assertThat(this.healthPoints.depleted()).isFalse();

        this.healthPoints.change(-999);

        assertThat(this.healthPoints.depleted()).isTrue();
    }

}
