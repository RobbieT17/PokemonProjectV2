package project.game.pokemon.effects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import project.game.battle.BattleData;
import project.game.event.EventData;
import project.game.event.GameEvents;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.PokemonCannotActException;
import project.game.exceptions.PokemonFaintedException;
import project.game.move.Move;
import project.game.pokemon.Pokemon;
import project.game.pokemon.PokemonConditions;
import project.game.pokemon.effects.StatusCondition.StatusConditionID;
import project.game.utility.RandomValues;

@ExtendWith(MockitoExtension.class)
public class TestStatusConditionManager {

    @Mock
    Pokemon mockPokemonA;

    @Mock
    Pokemon mockPokemonB;

    @Mock
    Move mockMoveUsed;

    @Mock
    PokemonConditions mockPokemonConditions;

    @Mock
    StatusConditionID mockId;

    @Mock
    BattleData mockBattleData;

    StatusContext context;
    GameEvents pokemonEvents;
    EventData data;

    private void assertCondition(StatusCondition c, StatusConditionID id, Pokemon p, EventID... flags) {
        assertThat(c.getBearer()).isEqualTo(p);
        assertThat(c.getId()).isEqualTo(id);
        assertThat(c.getEffectName()).isEqualTo(id.name());
        assertThat(c.getFlags()).containsExactly(flags);
    }
    
    private void invokeEventUpdate(EventID id) {
        this.mockPokemonA.getEvents().updateEventImmediate(id, this.data);
    }

    @BeforeEach
    void setUp() {
        this.context = new StatusContext(this.mockId, this.mockPokemonA);
        this.pokemonEvents = new GameEvents();
        this.data = new EventData(this.mockBattleData, this.mockPokemonB, this.mockPokemonA, this.mockMoveUsed);

        when(this.mockPokemonA.getEvents()).thenReturn(this.pokemonEvents);
        when(this.mockPokemonA.getConditions()).thenReturn(this.mockPokemonConditions);
        // when(this.mockPokemonB.getEvents()).thenReturn(this.eventsB);
    }

    @Test
    void testBurnEffect() {
        // Verify burn is applied
        StatusCondition condition = StatusConditionManager.addBurn(this.context);
        assertCondition(condition, StatusConditionID.Burn, this.mockPokemonA, EventID.PRIMARY_STATUS_AFTER, EventID.ATK_DAMAGE_MULTIPLIER);

        // Verify burn damage at end of round
        invokeEventUpdate(EventID.PRIMARY_STATUS_AFTER);
        verify(this.mockPokemonA).takeDamagePercentMaxHP(100.0 / 16.0, " from the burn");

        // Verify pokemon takes burn damage and faints at low HP
        when(this.mockPokemonConditions.isFainted()).thenReturn(true);

        assertThatThrownBy(() -> invokeEventUpdate(EventID.PRIMARY_STATUS_AFTER))
        .isInstanceOf(PokemonFaintedException.class);

        // Verify physical power drops by 50% for physical moves only
        when(this.mockMoveUsed.isCategory(Move.MoveCategory.Physical)).thenReturn(true);
        this.data.otherDamageMods = 1.5;

        invokeEventUpdate(EventID.ATK_DAMAGE_MULTIPLIER);
        assertThat(this.data.otherDamageMods)
        .as("Move bonus multiplier of 0.5 for physical moves")
        .isEqualTo(0.75);

        // Verify physical power remains the same for special moves only
        when(this.mockMoveUsed.isCategory(Move.MoveCategory.Physical)).thenReturn(false);
        this.data.otherDamageMods = 1.0;

        invokeEventUpdate(EventID.ATK_DAMAGE_MULTIPLIER);
        assertThat(this.data.otherDamageMods)
        .as("Move bonus multiplier should not change for special moves")
        .isEqualTo(1.0);

    }

    @Test
    void testFreezeEffect() {
        StatusCondition condition = StatusConditionManager.addFreeze(this.context);
        assertCondition(condition, StatusConditionID.Freeze, this.mockPokemonA, EventID.PRIMARY_STATUS_BEFORE);

        try (MockedStatic<RandomValues> mockRandomValues = mockStatic(RandomValues.class)) {
            // Test Case 1: Pokemon is not frozen and thaws
            mockRandomValues.when(() -> RandomValues.chance(20)).thenReturn(true);
            invokeEventUpdate(EventID.PRIMARY_STATUS_BEFORE);

            verify(this.mockPokemonA.getConditions(), description("A pokemon should not be immobilized when it thaws"))
            .setImmobilized(false);

            verify(this.mockPokemonA.getConditions(), description("Effect should clear when pokemon thaws"))
            .clearPrimary();

            // Test Case 2: Pokemon is frozen
            mockRandomValues.when(() -> RandomValues.chance(20)).thenReturn(false);
            when(this.mockPokemonA.getMoveSelected()).thenReturn(this.mockMoveUsed);

            assertThatThrownBy(() ->  invokeEventUpdate(EventID.PRIMARY_STATUS_BEFORE))
            .as("Should raise exception if Pokemon is frozen")
            .isInstanceOf(PokemonCannotActException.class);

            try {
                invokeEventUpdate(EventID.PRIMARY_STATUS_BEFORE);
            } catch (Exception e) {
            }

            verify(this.mockPokemonA.getConditions(), 
            times(2).description("Pokemon should be immobilized when frozen"))
            .setImmobilized(true);

            // Test Case 3: Scorching Sands (move_id=815) removes Freeze effect
            mockRandomValues.when(() -> RandomValues.chance(20)).thenReturn(false);
            when(this.mockMoveUsed.getMoveID()).thenReturn(815);

            invokeEventUpdate(EventID.PRIMARY_STATUS_BEFORE);

            verify(this.mockPokemonA.getConditions(), times(2).description("Invalid behavior for move exception"))
            .setImmobilized(false);

            verify(this.mockPokemonA.getConditions(), times(2).description("Invalid behavior for move exception"))
            .clearPrimary();
            
        } 
        

    }
}
