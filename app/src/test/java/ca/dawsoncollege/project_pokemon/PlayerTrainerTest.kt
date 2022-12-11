package ca.dawsoncollege.project_pokemon

import org.junit.Test
import org.junit.Assert.*

class PlayerTrainerTest {

    @Test
    fun testPokemonCenter() {
        val trainer = PlayerTrainer("bob");
        val pokemon = Pokemon(10)
        trainer.team.add(pokemon)

        // set hp to 0
        trainer.team[0].hp = 0
        // set pp to 0
        trainer.team[0].moveList[0].PP = 0
        // call method to test
        trainer.pokemonCenterHeal()
        // check that all stats are restored to max
        assertEquals(pokemon.battleStat.maxHP, trainer.team[0].hp)
        assertEquals(pokemon.moveList[0].maxPP, trainer.team[0].moveList[0].PP)
    }
}