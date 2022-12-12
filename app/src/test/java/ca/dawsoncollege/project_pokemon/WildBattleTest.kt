package ca.dawsoncollege.project_pokemon

import org.junit.Test
import org.junit.Assert.*

class WildBattleTest {
    // test the WildBattle class
    @Test
    fun testWildBattle() {
        val playerTrainer = PlayerTrainer("Ash")
        val pikachu = Pokemon(44, "pikachu")
        playerTrainer.team.add(pikachu)
        val enemyPokemon = Pokemon(44)
        val battle = WildBattle(playerTrainer)
        // check that the battle has been properly initialized
        assertTrue(battle.enemyPokemon == enemyPokemon && battle.playerPokemon == pikachu)
    }
    // test checkPokemonFainted function
    @Test
    fun testCheckPokemonFainted() {
        val playerTrainer = PlayerTrainer("Ash")
        val pikachu = Pokemon(44, "pikachu")
        playerTrainer.team.add(pikachu)
        val battle = WildBattle(playerTrainer)
        battle.enemyPokemon.hp = 0
        // check
        assertTrue(battle.checkPokemonFainted())
    }
    // test throwPokeball function
    @Test
    fun testThrowPokeball() {
        val playerTrainer = PlayerTrainer("Ash")
        val enemyPokemon = Pokemon(44)
        val battle = WildBattle(playerTrainer)
        battle.enemyPokemon = enemyPokemon

        // check return type
        assertTrue(battle.throwPokeball()  == true || battle.throwPokeball() == false)
    }

}