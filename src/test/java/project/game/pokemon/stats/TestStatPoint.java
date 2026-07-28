package project.game.pokemon.stats;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import project.game.pokemon.stats.StatPoint.StatID;

public class TestStatPoint {
    
    StatPoint statAtk;
    StatPoint statAcc;

    @BeforeEach
    void setUp() {
        this.statAtk = new StatPoint(StatID.Attack, 100);
        this.statAcc = new StatPoint(StatID.Accuracy, 100);
    }

    @Test
    void testConstructor() {
        assertThat(this.statAtk.getStatID()).isEqualTo(StatID.Attack);
        assertThat(this.statAtk.getBase()).isEqualTo(100);
        assertThat(this.statAtk.getStage()).isEqualTo(0);
        assertThat(this.statAtk.getMod()).isEqualTo(1.0);
    }

    @Test
    void testSetStage() {
        assertThatExceptionOfType(IllegalArgumentException.class)
        .as("Should raise exception if arg is outside range of [-6, 6]")
        .isThrownBy(() -> this.statAtk.setStage(7));

        assertThatExceptionOfType(IllegalArgumentException.class)
        .as("Should raise exception if arg is outside range of [-6, 6]")
        .isThrownBy(() -> this.statAtk.setStage(-7)); 

        for (int i = -6; i <= 6; i++) {
            int val = i;
            assertDoesNotThrow(() -> this.statAtk.setStage(val));
            assertThat(this.statAtk.getStage()).isEqualTo(val);
        }

    }

    @Test
    void testChangeStage() {
        assertThatExceptionOfType(IllegalArgumentException.class)
        .as("Should raise exception if arg is 0")
        .isThrownBy(() -> this.statAtk.changeStage(0));

        assertThat(this.statAtk.changeStage(3))
        .as("Should change stage by 3: 0 -> 3")
        .isTrue();

        assertThat(this.statAtk.changeStage(-2))
        .as("Should change stage by -2: 3 -> 1")
        .isTrue();

        this.statAtk.setStage(6);
        assertThat(this.statAtk.changeStage(1))
        .as("Positive change at max stage expected to fail")
        .isFalse();

        this.statAtk.setStage(-6);
        assertThat(this.statAtk.changeStage(-2))
        .as("Negative change at min stage expected to fail")
        .isFalse();
    }

    @Test
    void testCalcPower() {
        // Test Stage change
        int[] expectedAtkPower = new int[] {25, 28, 33, 40, 50, 66, 100, 150, 200, 250, 300, 350, 400};
        int[] expectedAccPower = new int[] {33, 37, 42, 50, 60, 75, 100, 133, 166, 200, 233, 266, 300};

        // Verifies all correct stage values [-6, 6]
        for (int i = -6, j = 0; i <= 6; i++, j++) {
            this.statAtk.setStage(i);
            this.statAcc.setStage(i);

            assertThat(this.statAtk.calcPower())
            .as("Wrong attack power for stage " + i)
            .isEqualTo(expectedAtkPower[j]);

            assertThat(this.statAcc.calcPower())
            .as("Wrong accuracy power for stage " + i)
            .isEqualTo(expectedAccPower[j]);
        }

        // Test Mod
        this.statAtk.setStage(0);
        this.statAtk.setMod(50);
        this.statAcc.setStage(0);
        this.statAcc.setMod(200);

        assertThat(this.statAtk.calcPower())
        .as("Wrong power with 50% mod")
        .isEqualTo(50);

        assertThat(this.statAcc.calcPower())
        .as("Wrong power with 200% mod")
        .isEqualTo(200);
    }
}
