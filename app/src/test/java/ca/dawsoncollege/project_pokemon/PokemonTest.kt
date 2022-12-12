package ca.dawsoncollege.project_pokemon


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.Assert.*
import kotlin.math.pow

class PokemonTest {
    // test pokemon constructor
    @Test
    fun testPokemonConstructor() {
        val pokemon = Pokemon(10)
        assertEquals(10, pokemon.level)
    }

    // test getBattle stats method
    @Test
    fun testGetBattleStats() {
        val pokemon = Pokemon(10)
        pokemon.getBattleStats()

        // check that pokemon stats are being set
        assertTrue (pokemon.battleStat.maxHP > 0)
        assertTrue(pokemon.battleStat.attack > 0)
        assertTrue(pokemon.battleStat.defense > 0)
        assertTrue(pokemon.battleStat.speed > 0)
        assertTrue(pokemon.battleStat.specialAttack > 0)
        assertTrue(pokemon.battleStat.specialDefence > 0)
    }

    // test addExp method
    @Test
    fun testAddExp() {
        val pokemon = Pokemon(10)
        // test that exp is set according  to level
        assertTrue(pokemon.experience == 10.0.pow(3).toInt())
        // add enough xp for pokemon to reach level 11
        pokemon.addExp(400)
        assertEquals(11, pokemon.level)

    }

    // test proposeMove method
    @Test
    fun testProposeMove() {
        val pokemon = Pokemon(10)
        pokemon.addExp(1000)
        // test that move is proposed
        GlobalScope.launch {
            // test that move list is filled
            assertTrue(pokemon.proposeMove().isNotEmpty())
        }
    }

    // test for learnMove method
    @Test
    fun testLearnMove() {
        val pokemon = Pokemon(10)
        // test that move is learned
        val move1 = Move("Tackle", 35, 100, 35, 0, 0, "test", "test", "Test", "test")
        val move2 = Move("Tackle", 35, 100, 35, 0, 0, "test", "test", "Test", "test")
        val move3 = Move("Tackle", 35, 100, 35, 0, 0, "test", "test", "Test", "test")
        val move4 = Move("Tackle", 35, 100, 35, 0, 0, "test", "test", "Test", "test")
        val move5 = Move("Tackle", 35, 100, 35, 0, 0, "test", "test", "Test", "test")
        pokemon.learnMove(move1)
        // check that move is in move list
        assertTrue(pokemon.moveList.contains(move1))
        pokemon.learnMove(move2)
        pokemon.learnMove(move3)
        pokemon.learnMove(move4)
        pokemon.learnMove(move5, move2)
        // check that old move is properly removed
        assertFalse(pokemon.moveList.contains(move2))
    }
}