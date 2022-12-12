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
}