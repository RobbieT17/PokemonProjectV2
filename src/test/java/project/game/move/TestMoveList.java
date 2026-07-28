package project.game.move;

import java.lang.reflect.Array;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import project.game.battle.BattleData;
import project.game.battle.Weather;
import project.game.battle.Weather.WeatherEffect;
import project.game.event.EventManager;
import project.game.exceptions.MoveInterruptedException;
import project.game.move.Move.MoveCategory;
import project.game.move.Move.MoveStatus;
import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokemon;
import project.game.pokemon.PokemonConditions;
import project.game.pokemon.effects.StatusCondition;
import project.game.pokemon.effects.StatusCondition.StatusConditionID;
import project.game.pokemon.stats.StatPoint;
import project.game.pokemon.stats.StatPoint.StatID;
import project.game.processors.MoveProcessor;

@ExtendWith(MockitoExtension.class)
public class TestMoveList {

    private <T> T[] getMockObjectArray(Class<T> c, int length) {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(c, length);

        for (int i = 0; i < arr.length; i++) {
            arr[i] = mock(c);
        }
        return arr;
    }

    private void verifyCloudSync(WeatherEffect w, int statId) {

        Weather mockWeather = mock(Weather.class);

        when(this.eventManager.data.battleData.getCurrenWeather()).thenReturn(mockWeather);
        when(mockWeather.getWeatherEffect()).thenReturn(w);
        
        MoveList.cloudSync(this.eventManager);

        // Verify the target is the user
        assertThat(this.eventManager.data.effectTarget).isEqualTo(this.mockPokemonAtk);

        int[] stats = new int[7];
        stats[statId] = 2;

        verify(this.eventManager.moveActions, description("Wrong behavior for weather id " + w.name())
        ).changeStats(stats);
    }

    private void verifyFacade(PokemonConditions conditions, StatusConditionID id) {
        when(conditions.hasKey(any())).thenReturn(false); // All args passed reset to false
        when(conditions.hasKey(id)).thenReturn(true);
       
        MoveList.facade(this.eventManager);

        verify(this.mockMoveUsed, times(TestMoveList.callCount++).description("User has condition " + id.name() + " but power was not doubled"))
        .doublePower(); 
    }

    private void verifyGrassKnot(double weight, int expectedPower) {
        when(this.mockPokemonDef.getWeight()).thenReturn(weight);
        MoveList.grassKnot(this.eventManager);

        verify(this.mockMoveUsed, description("Pokemon weight: " + weight + " Expected power: " + expectedPower))
        .setPower(expectedPower);
    }
    
    @Mock
    BattleData mockBattleData;

    @Mock
    Pokemon mockPokemonAtk;

    @Mock
    Pokemon mockPokemonDef;

    @Mock
    Move mockMoveUsed;

    @Mock
    MoveActions mockMoveActions;

    EventManager eventManager;

    static int callCount;

    @BeforeEach
    void setUp() {
        this.eventManager = new EventManager(
            this.mockBattleData, 
            this.mockPokemonAtk, 
            this.mockPokemonDef, 
            this.mockMoveUsed,
            this.mockMoveActions);
    }

    @Test
    void testAvalanche() {
        PokemonConditions conditions = mock(PokemonConditions.class);
        when(this.mockPokemonAtk.getConditions()).thenReturn(conditions);

        // Test Case 1: User took damage, deals double damage
        when(conditions.tookDamage()).thenReturn(true);
        MoveList.avalanche(this.eventManager);
        
        verify(this.mockMoveUsed, description("User took damage but move power was not doubled")).doublePower();

        // Test Case 2: User did not take damage
        when(conditions.tookDamage()).thenReturn(false);
        MoveList.avalanche(this.eventManager);

        verify(this.mockMoveUsed, times(1).description("User did not take damage but power was doubled"))
        .doublePower();

        verify(this.eventManager.moveActions, times(2).description("User did not attack target"))
        .attackTarget();

    }

    @Test
    void testCloudSync() {
        // Test Case 1: Sunny weather sharply raises Attack
        verifyCloudSync(WeatherEffect.Sunny, 0);

        // Test Case 2: Clear weather sharply raises Defense
        verifyCloudSync(WeatherEffect.Sandstorm, 1);

        // Test Case 3: Clear weather sharply raises Sp. Attack
        verifyCloudSync(WeatherEffect.Rain, 2);

        // Test Case 4: Clear weather sharply raises Sp. Defense
        verifyCloudSync(WeatherEffect.Hail, 3);

        // Test Case 5: Clear weather sharply raises Speed
        verifyCloudSync(WeatherEffect.Clear, 4);

    }
    
    @Test
    void testDataCorruption() {
        StatPoint[] mockStats = getMockObjectArray(StatPoint.class, StatID.values().length);

        when(this.mockPokemonDef.getStats()).thenReturn(mockStats);

        // Target stages: (Atk: +2, Def: -1, Speed: +2, Accuracy: -1)
        when(mockStats[0].getStage()).thenReturn(2);
        when(mockStats[1].getStage()).thenReturn(-1);
        when(mockStats[4].getStage()).thenReturn(2);
        when(mockStats[5].getStage()).thenReturn(-1);

        MoveList.dataCorruption(this.eventManager);

        verify(this.eventManager.moveActions).rollForAccuracy();
        assertThat(this.eventManager.data.effectTarget).isEqualTo(this.mockPokemonDef);
        
        for (StatPoint stat : mockStats) {
            int s = stat.getStage();
            verify(stat).setStage(-s);
        }
    }

    @Test
    void testDebugger() {

    }

    @Test
    void testEarthquake() {
        PokemonConditions conditions = mock(PokemonConditions.class);
        when(this.mockPokemonDef.getConditions()).thenReturn(conditions);

        // Test Case 1: Move power double when target is underground
        when(conditions.hasKey(StatusCondition.StatusConditionID.Dig_State)).thenReturn(true);
        MoveList.earthquake(this.eventManager);

        verify(this.mockMoveUsed, description("Move power should double when target is underground"))
        .doublePower();

         // Test Case 2: Normal move behavior
        when(conditions.hasKey(StatusCondition.StatusConditionID.Dig_State)).thenReturn(false);
        MoveList.earthquake(this.eventManager);

        verify(this.mockMoveUsed, times(1).description("Target was not underground but power was doubled"))
        .doublePower();

        verify(this.eventManager.moveActions, times(2).description("User did not attack target"))
        .attackTarget();

    }

    @Test
    void testErrorCorrection() {
        // Test Case 1: Last move is null 
        assertThatExceptionOfType(MoveInterruptedException.class)
        .as("Should raise an exception if there is no previous move")
        .isThrownBy(() -> MoveList.errorCorrection(this.eventManager));

        // Test Case 2: Last move was successful
        Move mockMove = mock(Move.class);
        when(this.mockPokemonAtk.getLastMove()).thenReturn(mockMove);
        when(mockMove.isStatus(MoveStatus.Success)).thenReturn(true);

        assertThatExceptionOfType(MoveInterruptedException.class)
        .as("Should raise an exception if the previous move was successful")
        .isThrownBy(() -> MoveList.errorCorrection(this.eventManager));

        // Test Case 3: Error correct was the last move used
        when(mockMove.getMoveID()).thenReturn(946);
        when(mockMove.isStatus(MoveStatus.Success)).thenReturn(false);

        assertThatExceptionOfType(MoveInterruptedException.class)
        .as("Should raise an exception if the previous move was Error Correction")
        .isThrownBy(() -> MoveList.errorCorrection(this.eventManager));

        // Test Case 4: Error Correct is successful
        try (
            MockedConstruction<EventManager> mockedEventConstruct = mockConstruction(EventManager.class);
            MockedConstruction<MoveProcessor> mockedProcessorConstruct = mockConstruction(MoveProcessor.class)
        ) {
            when(this.mockPokemonAtk.getLastMove()).thenReturn(mockMove);
            when(mockMove.isStatus(MoveStatus.Success)).thenReturn(false);
            when(mockMove.getMoveID()).thenReturn(0);

            // Test Case 4A: Previous move was a status move and get perfect accuracy
            when(mockMove.isCategory(MoveCategory.Status)).thenReturn(true);
            MoveList.errorCorrection(this.eventManager);

            MoveProcessor mockedProcessor = mockedProcessorConstruct.constructed().get(0);

            verify(mockMove, description("Should set to perfect accuracy for status move")).perfectAccuracy();

            assertThat(mockedEventConstruct.constructed())
            .as("A new event manager should have been constructed")
            .hasSize(1);

            assertThat(this.eventManager.getProcessor())
            .as("Wrong processor assigned to event manager")
            .isEqualTo(mockedProcessor);

            // Test Case 4B: Previous move was non-status move and has 150% base power
            when(mockMove.isCategory(MoveCategory.Status)).thenReturn(false);
            MoveList.errorCorrection(this.eventManager);

            mockedProcessor = mockedProcessorConstruct.constructed().get(1);

            verify(mockMove, description("Should increase power by 50% for non-status moves")).changePowerByPercent(150);
            verify(this.mockPokemonAtk, times(2)).setMoveSelected(mockMove);

            assertThat(mockedEventConstruct.constructed())
            .as("A new event manager should have been constructed")
            .hasSize(2);

            assertThat(this.eventManager.getProcessor())
            .as("Wrong processor assigned to event manager")
            .isEqualTo(mockedProcessor);
        }

        verify(this.mockPokemonAtk, times(2).description("Pokemon move was not set")).setMoveSelected(mockMove);
        verify(mockMove, times(1).description("Perfect accuracy set when it shouldn't have")).perfectAccuracy();
        verify(mockMove, times(1).description("Power increased when it shouldn't have")).changePowerByPercent(150);

    }

    @Test
    void testFacade() {
        PokemonConditions conditions = mock(PokemonConditions.class);
        when(this.mockPokemonAtk.getConditions()).thenReturn(conditions);

        // Test Case 1: User does not bear any status effect
        MoveList.facade(this.eventManager);

        // Test Case 2: User bears one of the follow conditions (Burn, Paralysis, Poison)
        TestMoveList.callCount = 1;

        verifyFacade(conditions, StatusConditionID.Burn);
        verifyFacade(conditions, StatusConditionID.Paralysis);
        verifyFacade(conditions, StatusConditionID.Poison);
        verifyFacade(conditions, StatusConditionID.Bad_Poison);

        verify(this.eventManager.moveActions, times(5).description("Pokemon did not attack target"))
        .attackTarget();

    }

    @Test
    void testFactoryReset() {
        Pokemon[] mockPokemonInBattle = getMockObjectArray(Pokemon.class, 5);
        PokemonConditions[] mockConditions = getMockObjectArray(PokemonConditions.class, 5);

        when(this.mockBattleData.getAllPokemonInBattle()).thenReturn(mockPokemonInBattle);

        for (int i = 0; i < mockConditions.length; i++) {
            when(mockPokemonInBattle[i].getConditions()).thenReturn(mockConditions[i]);
        }

        MoveList.factoryReset(this.eventManager);
        
        // Checks for pokemon stats nullified
        for (Pokemon p : mockPokemonInBattle) {
            verify(p, description("Pokemon stats were not reset")).resetStatChanges();
            verify(p.getConditions(), description("Pokemon status effects were not cleared")).clearVolatileConditions();   
        }

        // Checks for weather effect clear
        verify(this.mockBattleData, description("Weather effect was not reset")).setCurrenWeather(WeatherEffect.Clear);
    
    }

    @Test
    void testFakeOut() {
        // Test Case 1: Move fails if not used on first turn
        when(this.mockPokemonAtk.isFirstRound()).thenReturn(false);
 
        assertThatExceptionOfType(MoveInterruptedException.class)
        .as("Should raise an exception if not the first round")
        .isThrownBy(() -> MoveList.fakeOut(this.eventManager));

        // Test Case 2: Move executes normally
        when(this.mockPokemonAtk.isFirstRound()).thenReturn(true);
        MoveList.fakeOut(this.eventManager);

        assertThat(this.eventManager.data.effectTarget).isEqualTo(this.mockPokemonDef);
        verify(this.eventManager.moveActions).attackTarget();
        verify(this.eventManager.moveActions).applyCondition(StatusCondition.StatusConditionID.Flinch);

    }

    @Test
    void testFileTransfer() {
        int length = StatID.values().length;
        StatPoint[] statsA = getMockObjectArray(StatPoint.class, length);
        StatPoint[] statsB = getMockObjectArray(StatPoint.class, length);

        when(this.mockPokemonAtk.getStats()).thenReturn(statsA);
        when(this.mockPokemonDef.getStats()).thenReturn(statsB);

        // User stages (Atk: +1, Def: +2, Speed: -3)
        when(statsA[0].getStage()).thenReturn(1);
        when(statsA[1].getStage()).thenReturn(2);
        when(statsA[4].getStage()).thenReturn(-3);
       
        // TEST CASE 1: User transfer stats to opponent Pokemon
        PokemonTrainer ownerA = mock(PokemonTrainer.class);
        PokemonTrainer ownerB = mock(PokemonTrainer.class);

        when(this.mockPokemonAtk.getOwner()).thenReturn(ownerA);
        when(this.mockPokemonDef.getOwner()).thenReturn(ownerB);

        MoveList.fileTransfer(this.eventManager);
        
        // Verifies move rolls for accuracy for enemy pokemon
        verify(this.eventManager.moveActions).rollForAccuracy();

        // Verifies the effects apply to the correct pokemon
        assertThat(this.eventManager.data.effectTarget).isEqualTo(this.mockPokemonDef);
        
        // Verifies user stats were set to 0 and targets stat changed
        for (int i = 0; i < statsA.length; i++) {
            String statName = StatID.values()[i].name();
            StatPoint a = statsA[i];
            StatPoint b = statsB[i];

            int change = a.getStage();

            verify(a, description("User's " + statName + " was not set to 0"))
            .setStage(0);

            verify(b, description("Target's " + statName + " did not change by " + change))
            .changeStage(change);
        }    
        
        // TEST CASE 2: User transfer stats to ally
        when(this.mockPokemonDef.getOwner()).thenReturn(ownerA);      
        
        MoveList.fileTransfer(this.eventManager);
        verify(
            this.eventManager.moveActions,
            times(1).description("Transfer to ally should ignore accuracy check"))
        .rollForAccuracy();

    }

    @Test
    void testGrassKnot() {
        verifyGrassKnot(10, 20);
        verifyGrassKnot(45, 40);
        verifyGrassKnot(68, 60);
        verifyGrassKnot(150, 80);
        verifyGrassKnot(300, 100);
        verifyGrassKnot(600, 120);

        verify(this.eventManager.moveActions, times(6).description("Pokemon did not attack target"))
        .attackTarget();
    }

}
