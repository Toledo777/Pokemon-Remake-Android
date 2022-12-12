package ca.dawsoncollege.project_pokemon

import org.junit.Test
import org.junit.Assert.*

class TrainerBattleTest {

// test checkPokemonFainted function
    @Test
    fun testCheckPokemonFainted() {
        val playerTrainer = PlayerTrainer("Ash")
        val enemyTrainer = EnemyTrainer("Gary")
        val playerPokemon = Pokemon(44)
        val enemyPokemon = Pokemon(44)
        playerTrainer.team.add(playerPokemon)
        enemyTrainer.team.add(enemyPokemon)
        val battle = TrainerBattle(playerTrainer, enemyTrainer)
        battle.enemyPokemon = enemyPokemon
        battle.enemyPokemon.hp = 0
        // check
        assertTrue(battle.checkPokemonFainted())
    }
    // test switchOutEnemyPkm function
    @Test
    fun testSwitchOutEnemyPkm() {
        val playerTrainer = PlayerTrainer("Ash")
        val enemyTrainer = EnemyTrainer("Gary")
        val playerPokemon = Pokemon(44)
        val enemyPokemon = Pokemon(44, "squirtle")
        val pikachu = Pokemon(44, "pikachu")
        playerTrainer.team.add(playerPokemon)
        enemyTrainer.team.add(enemyPokemon)
        enemyTrainer.team.add(pikachu)
        val battle = TrainerBattle(playerTrainer, enemyTrainer)
        battle.playerPokemon = playerPokemon
        battle.enemyPokemon = enemyPokemon
        battle.switchOutEnemyPkm()
        // check that switch function properly switches
        assertTrue(battle.enemyPokemon.name == "pikachu")
        // check that fainted pokemon is removed from team
        assertFalse(battle.enemyTrainer.team.contains(enemyPokemon))
    }

}