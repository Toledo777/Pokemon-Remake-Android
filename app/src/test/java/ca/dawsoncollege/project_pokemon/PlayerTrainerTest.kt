package ca.dawsoncollege.project_pokemon

import org.junit.Test
import org.junit.Assert.*

class PlayerTrainerTest {

    // test set starter and nickname
    @Test
    fun testSetStarterAndNickname() {
        val trainer = PlayerTrainer("Steve")
        trainer.setStarter("squirtle", "turtle")
        assertTrue(trainer.team[0].name.equals("turtle"))

        val expectedStarterLevel  = 5
        assertEquals(expectedStarterLevel, trainer.team[0].level)
    }

    // test set starter and nickname
    @Test
    fun testSetStarterAndNickname2() {
        val trainer = PlayerTrainer("Steve")
        trainer.setStarter("charmander", "rock")
        assertTrue(trainer.team[0].name.equals("rock"))

        val expectedStarterLevel  = 5
        assertEquals(expectedStarterLevel, trainer.team[0].level)
    }

    // test set starter
    @Test
    fun testSetStarterAndSpeciesName() {
        val trainer = PlayerTrainer("Steve")
        trainer.setStarter("bulbasaur")
        assertTrue(trainer.team[0].name.equals("bulbasaur"))

        val expectedStarterLevel  = 5
        assertEquals(expectedStarterLevel, trainer.team[0].level)

    }

    // test pokemon center healing
    @Test
    fun testPokemonCenterStats() {
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

    // test pokemon center healing
    @Test
    fun testPokemonCenter2() {
        val trainer = PlayerTrainer("bob");
        val pokemon = Pokemon(10)
        trainer.team.add(pokemon)

        // set hp to 0
        trainer.team[0].hp = 2
        // set pp to 0
        trainer.team[0].moveList[0].PP = 1
        // call method to test
        trainer.pokemonCenterHeal()
        // check that all stats are restored to max
        assertEquals(pokemon.battleStat.maxHP, trainer.team[0].hp)
        assertEquals(pokemon.moveList[0].maxPP, trainer.team[0].moveList[0].PP)
    }

    // test catching a pokemon with no HP
    fun catchPokemonTestEnemyNoHp() {
        val trainer = PlayerTrainer("jeff");
        trainer.setStarter("squirtle");
        val wildPokemon = Pokemon(10)
        wildPokemon.hp = 0

        // catch
        val bool = trainer.catchPokemon(trainer.team[0], wildPokemon)
        assertTrue(bool)

        assertTrue(trainer.team.contains(wildPokemon))
    }

    // test catching when player has no hp
    @Test
    fun catchPokemonTestPlayerNoHp() {
        val trainer = PlayerTrainer("jeff");
        trainer.setStarter("squirtle");
        val wildPokemon = Pokemon(16)
        trainer.team[0].hp = 0

        // catch
        val bool = trainer.catchPokemon(trainer.team[0], wildPokemon)
        assertFalse(bool)

        assertFalse(trainer.team.contains(wildPokemon))
    }

    @Test
    fun testRandomEnemyLevel1() {
        val testLevel = 1
        val pokemon = Pokemon(testLevel)
        val trainer = PlayerTrainer("jeff");
        trainer.team.add(pokemon)
        val enemyLevel = trainer.getRandomEnemyLevel()

        assertEquals(testLevel, enemyLevel)
    }

    @Test
    fun testRandomEnemyLevel88() {
        val testLevel = 88
        val pokemon = Pokemon(testLevel)
        val pokemon2 = Pokemon(testLevel)
        val trainer = PlayerTrainer("jeff");
        trainer.team.add(pokemon)
        trainer.team.add(pokemon2)
        val enemyLevel = trainer.getRandomEnemyLevel()
        assertEquals(testLevel, enemyLevel)
    }
}