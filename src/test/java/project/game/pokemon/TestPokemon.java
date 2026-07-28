package project.game.pokemon;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import project.game.pokemon.stats.HealthPoints;
import project.game.pokemon.stats.StatPoint;
import project.game.pokemon.stats.StatPoint.StatID;

@ExtendWith(MockitoExtension.class)
class TestPokemon {
    

    @Mock
    private PokemonType mockType;    

    @Mock
    private HealthPoints mockHp;

    private StatPoint[] mockStats;
    private Pokemon pokemon;

    @BeforeEach
    void setUp() {
        this.mockStats = new StatPoint[StatID.values().length];
        
        for (int i = 0; i < this.mockStats.length; i++) {
            this.mockStats[i] = mock(StatPoint.class);
        }

        this.pokemon = new Pokemon(
        50, 
        "Test Pokemon", 
        this.mockType, 
        0, 
        200, 
        this.mockHp, 
        this.mockStats);
    }

    @Test
    @DisplayName("Pokemon takes normal damage")
    void takeDamageReduceHPByValue() {
        when(this.mockHp.depleted()).thenReturn(false);
        // when(this.pokemon.getConditions().getEndure().isActive()).thenReturn(false);
        this.pokemon.takeDamage(50);

        verify(this.mockHp).change(-50);

        assertThat(this.pokemon.getDamageReceived())
        .as("Pokemon should've recieved 50 point of damage")
        .isEqualTo(50);
    }


   

}
