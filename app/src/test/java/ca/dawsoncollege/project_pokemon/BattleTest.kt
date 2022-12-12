package ca.dawsoncollege.project_pokemon

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.Assert.*

class BattleTest {
    private val playerTrainer = PlayerTrainer("Ash")
    private val enemyTrainer = EnemyTrainer("Gary")
    private val playerPokemon = Pokemon(44)
    init {
        playerTrainer.team.add(playerPokemon)
    }

    // test switchOutPlayerPkm
    @Test
    fun testSwitchOutPokemon() {
        val battle = TrainerBattle(playerTrainer, enemyTrainer)
        val nextPokemon = Pokemon(44)
        playerTrainer.team.add(nextPokemon)
        battle.switchOutPlayerPkm()
        assertEquals(nextPokemon, battle.playerPokemon)
    }

    // test potion
    @Test
    fun testPlayerUsePotion() {
        val setHp = 0
        val battle = TrainerBattle(playerTrainer, enemyTrainer)
        battle.playerPokemon.hp = setHp;
        battle.playerUsePotion()
        // check
        assertTrue(battle.playerPokemon.hp == battle.playerPokemon.battleStat.maxHP || battle.playerPokemon.hp == setHp + 20)
    }

}